package com.honeydew.honeydewlist.ui.home_screen.ui.friends;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Friend;
import com.honeydew.honeydewlist.ui.home_screen.inteface.GetFriendCallback;
import com.honeydew.honeydewlist.ui.home_screen.ui.friends.AddFriendActivity;
import com.honeydew.honeydewlist.ui.home_screen.ui.friends.FriendsLVAdapter;
import com.honeydew.honeydewlist.data.Friend;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsFragment extends Fragment {

    ListView friendsLV;
    ArrayList<Friend> dataModalArrayList;
    FirebaseFirestore db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    private String username,melons;

    private String userID;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        setHasOptionsMenu(true);
        friendsLV = root.findViewById(R.id.idLVFriends);
        dataModalArrayList = new ArrayList<>();

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.lv_footer, friendsLV, false);
        friendsLV.addFooterView(footer);

        db = FirebaseFirestore.getInstance();
        if (user != null) {
            userID = user.getUid();
            // here we are calling a method
            // to load data in our list view.
            loadDetailListview();
        }

        return root;
    }

    private void loadDetailListview() {


        FriendsLVAdapter adapter = new FriendsLVAdapter(requireContext(), dataModalArrayList);

        friendsLV.setAdapter(adapter);

        db.collection("users/" + userID + "/friends").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // if the snapshot is not empty we are hiding
                        // our progress bar and adding our data in a list.
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            // after getting this list we are passing
                            // that list to our object class.
                            Friend dataModel = d.toObject(Friend.class);
                            assert dataModel != null;
                            // dataModel.setItemID(d.getId());
                            //Log.i("dataModel ID", "loadDetailListview: " + dataModel.getItemID());

                            // after getting data from Firebase we are
                            // storing that data in our array list
                            dataModalArrayList.add(dataModel);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(requireContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                        Log.i("Firebase", "loadDetailListview: No data found in Database");
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
            Log.d("Firestore Error", "loadDetailListview: " + e.getMessage());
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        userID = user.getUid();
        inflater.inflate(R.menu.app_bar_menu, menu);

        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    username = documentSnapshot.getData().get("username").toString();
                    menu.findItem(R.id.menuUsername).setTitle(getString(R.string.menuUser, username));
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(),
                        "Could not find username from Firestore",
                        Toast.LENGTH_SHORT).show());

        db.collection("users").document(userID).addSnapshotListener((value, error) -> {
            melons = value.getData().get("melon_count").toString();
            menu.findItem(R.id.melon_stats).setTitle(getString(R.string.melonText, melons));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {
            // navigate to add friend screen
            if (db != null) {
                db.terminate();
            }
            Intent i = new Intent(getContext(), AddFriendActivity.class);
            startActivity(i);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.terminate();
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        if (db != null) {
            db.terminate();
        }
    }
}

