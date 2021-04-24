package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;
import com.honeydew.honeydewlist.ui.home_screen.ui.rewards.RewardsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksFragment extends Fragment {
    private TasksViewModel tasksViewModel;
    ListView tasksLV;
    ArrayList<Task> dataModalArrayList;
    FirebaseFirestore db;
    ProgressBar progressBar;
    private String userID;
    private int checkedItem;
    private ArrayList<String> friendIds;
    ArrayList<String> friends;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        setHasOptionsMenu(true);
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

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
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        friendIds = new ArrayList<>();
        if (user != null) {
            userID = user.getUid();
            friendIds.add(user.getUid());
            // here we are calling a method
            // to load data in our list view.
            tasksViewModel.setCheckedItem(new MutableLiveData<>(0));

            checkedItem = tasksViewModel.getCheckedItem().getValue();

            tasksViewModel.getCheckedItem().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer i) {
                    checkedItem = i;
                    loadDetailListview(i);
                }
            });
        }

        return root;
    }

    private void loadDetailListview(Integer i) {
        // TODO: Remove temp id and add friend picker
        // Temp userID for testing
        userID = friendIds.get(i);
        // user is the selected friend

        // after that we are passing our array list to our adapter class.
        TasksLVAdapter adapter = new TasksLVAdapter(requireContext(), dataModalArrayList);

        // after passing this array list to our adapter
        // class we are setting our adapter to our list view.
        tasksLV.setAdapter(adapter);

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
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(requireContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
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
        inflater.inflate(R.menu.app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {
            // navigate to add task screen
            Intent i = new Intent(getContext(), CreateTaskActivity.class);
            startActivity(i);

            return true;
        } else if (itemId == R.id.action_filter) {
            // navigate to screen to choose which friends to show tasks from
            Toast.makeText(
                    getContext(),
                    "Not yet implemented",
                    Toast.LENGTH_SHORT
            ).show();

            showFriendDialog();
//            loadDetailListview();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFriendDialog() {
        friendIds.clear();
        friends = new ArrayList<>();

        // Add current user to first item
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            friendIds.add(user.getUid());
            friends.add("Me");
        } else {
            return;
        }

        // TODO: Get friends from Firestore
        friendIds.add("GVO4PUFOy4VqvRZXoeGigyVQwg12");
        friendIds.add("ABC#0123");
        friends.add("richardxd");
        friends.add("ABC");


        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());

        builder.setTitle("Pick a friend");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                Log.d("friendDialog", "negative button clicked");
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Load friend from index chosen
                Log.d("friendDialog", "positive button clicked");
                checkedItem = which;
                tasksViewModel.setCheckedItem(new MutableLiveData<>(which));
                dialog.dismiss();
            }
        });

        // Single-choice items (initialized with checked item)
        builder.setSingleChoiceItems(friends.toArray(new CharSequence[0]), checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // item selected logic, don't anything until "Ok" is hit
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.terminate();
        }
    }
}