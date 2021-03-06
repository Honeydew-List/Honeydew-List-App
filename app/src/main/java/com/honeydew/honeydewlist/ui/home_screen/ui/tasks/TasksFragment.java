package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;
import com.honeydew.honeydewlist.ui.home_screen.inteface.GetFriendCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksFragment extends Fragment {

    private static final String TAG = "DB ERROR";
    private ListView tasksLV;
    private ArrayList<Task> dataModalArrayList;
    private TasksLVAdapter adapter;
    private CollectionReference friendsRef;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private ArrayList<String> foundFriendIds;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;
    private String username,melons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        setHasOptionsMenu(true);

        // below line is use to initialize our variables
        tasksLV = root.findViewById(R.id.idLVTasks);
        dataModalArrayList = new ArrayList<>();
        progressBar = root.findViewById(R.id.progressBar);

        LayoutInflater layoutInflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) layoutInflater.inflate(R.layout.lv_footer, tasksLV, false);
        tasksLV.addFooterView(footer);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // Get current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        foundFriendIds = new ArrayList<>();
        if (user != null) {
            userID = user.getUid();
            foundFriendIds.add(user.getUid());
            friendsRef = db.collection("users/" + userID + "/friends");




            // after that we are passing our array list to our adapter class.
            adapter = new TasksLVAdapter(requireContext(), dataModalArrayList, db);

            // after passing this array list to our adapter
            // class we are setting our adapter to our list view.
            tasksLV.setAdapter(adapter);


            // here we are calling a method
            // to load data in our list view.
            // Call for each friend

//            new MyAsyncTask().execute();
//            getFriends();
            readFriendData(friendIds -> {
                foundFriendIds.addAll(friendIds);
                // Load the listview
                for (int i = 0; i < foundFriendIds.size(); i++) {
                    try {
                        loadDetailListview(foundFriendIds.get(i));
                    } catch (SQLiteDatabaseLockedException e) {
                        Log.e(TAG, "onCreateView: Database already in use", e);
                    } catch (RuntimeException e) {
                        Log.e(TAG, "onCreate: RuntimeException", e);
                    } catch (Exception e) {
                        Log.e(TAG, "onCreateView: Something happened", e);
                    }

                }
            });


        }

        return root;
    }

    private void loadDetailListview(String userID) {

        // below line is use to get data from Firebase
        // firestore using collection in android.
        db.collection("users/" + userID + "/tasks").get()
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
                            Task dataModel = d.toObject(Task.class);
                            assert dataModel != null;
                            dataModel.setItemID(d.getId());
                            Log.i("dataModel ID", "loadDetailListview: " + dataModel.getItemID());

                            // after getting data from Firebase we are
                            // storing that data in our array list
                            dataModalArrayList.add(dataModel);
                            adapter.notifyDataSetChanged();
                        }
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
            // navigate to add task screen
            if (db != null) {
                db.terminate();
            }
            Intent i = new Intent(getContext(), CreateTaskActivity.class);
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

    public void readFriendData(GetFriendCallback myCallback) {
        friendsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> friendList = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String userID = document.getString("uuid");
                    friendList.add(userID);
                }
                myCallback.onCallback(friendList);
            }
        });
    }
}