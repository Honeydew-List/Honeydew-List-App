package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

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
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    ListView tasksLV;
    ArrayList<Task> dataModalArrayList;
    FirebaseFirestore db;
    ProgressBar progressBar;
    private String userID;

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
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            // here we are calling a method
            // to load data in our list view.
            loadDetailListview();
        }
        return root;
    }

    private void loadDetailListview() {
        // TODO: Remove temp id and add friend picker
        // Temp userID for testing
        String userID = "ABC#0123";
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
            return true;
        }
        return super.onOptionsItemSelected(item);
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