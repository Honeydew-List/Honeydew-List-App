package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Reward;
import com.honeydew.honeydewlist.data.Task;
import com.honeydew.honeydewlist.ui.home_screen.inteface.GetFriendCallback;
import com.honeydew.honeydewlist.ui.home_screen.ui.tasks.CreateTaskActivity;
import com.honeydew.honeydewlist.ui.home_screen.ui.tasks.TasksLVAdapter;

import java.util.ArrayList;
import java.util.List;

public class RewardsFragment extends Fragment {

    private ListView rewardsLV;
    private ArrayList<Reward> dataModalArrayList;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ArrayList<String> foundFriendIds;
    private CollectionReference friendsRef;
    private RewardsLVAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        setHasOptionsMenu(true);

        // below line is use to initialize our variables
        rewardsLV = root.findViewById(R.id.idLVRewards);
        dataModalArrayList = new ArrayList<>();
        progressBar = root.findViewById(R.id.progressBar);

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.lv_footer, rewardsLV, false);
        rewardsLV.addFooterView(footer);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // Get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        foundFriendIds = new ArrayList<>();

        if (user != null) {
            String userID = user.getUid();
            foundFriendIds.add(user.getUid());
            friendsRef = db.collection("users/" + userID + "/friends");




            // after that we are passing our array list to our adapter class.
            adapter = new RewardsLVAdapter(requireContext(), dataModalArrayList, db);

            // after passing this array list to our adapter
            // class we are setting our adapter to our list view.
            rewardsLV.setAdapter(adapter);


            // here we are calling a method
            // to load data in our list view.
            readFriendData(friendIds -> {
                foundFriendIds.addAll(friendIds);
                // Load the listview
                for (int i = 0; i < foundFriendIds.size(); i++) {
                    try {
                        loadDetailListview(foundFriendIds.get(i));
                    } catch (SQLiteDatabaseLockedException e) {
                        Log.e("DB ERROR", "onCreateView: Database already in use", e);
                    } catch (Exception e) {
                        Log.e("DB ERROR", "onCreateView: Something happened", e);
                    }
                }
            });
        }

        return root;
    }

    private void loadDetailListview(String userID) {

        // below line is use to get data from Firebase
        // firestore using collection in android.
        db.collection("users/" + userID + "/rewards").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    // after getting the data we are calling on success method
                    // and inside this method we are checking if the received
                    // query snapshot is empty or not.
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // if the snapshot is not empty we are hiding
                        // our progress bar and adding our data in a list.
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            // after getting this list we are passing
                            // that list to our object class.
                            Reward dataModel = d.toObject(Reward.class);
                            assert dataModel != null;
                            dataModel.setItemID(d.getId());
                            Log.i("dataModel ID", "loadDetailListview: " + dataModel.getItemID());

                            // after getting data from Firebase we are
                            // storing that data in our array list
                            dataModalArrayList.add(dataModel);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(requireContext(), "No data found in Database for " + userID, Toast.LENGTH_SHORT).show();
                        Log.i("Firebase", "loadDetailListview: No data found in Database");
                    }
                }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            // we are displaying a toast message
            // when we get any error from Firebase.
            Toast.makeText(requireContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
            Log.d("Firestore Error", "loadDetailListview: " + e.getMessage());
        });
    }

    public void readFriendData(GetFriendCallback myCallback) {
        friendsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> friendList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userID = document.getId().trim();
                    friendList.add(userID);
                }
                myCallback.onCallback(friendList);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {
            // navigate to add task screen
            if (db != null) {
                db.terminate();
            }
            Intent i = new Intent(getContext(), CreateRewardActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db.terminate();
        }
    }
}