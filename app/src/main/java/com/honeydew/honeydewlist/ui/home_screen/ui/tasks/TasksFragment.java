package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksFragment extends Fragment {

    public TasksFragment() {
        this.setHasOptionsMenu(true);
    };

    ListView coursesLV;
    ArrayList<Task> dataModalArrayList;
    FirebaseFirestore db;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        // below line is use to initialize our variables
        coursesLV = root.findViewById(R.id.idLVCourses);
        dataModalArrayList = new ArrayList<>();
        progressBar = root.findViewById(R.id.progressBar);

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // here we are calling a method
        // to load data in our list view.
        loadDatailListview();

        return root;
    }

    private void loadDatailListview() {
        // user is the selected friend
        final String user = "ABC#0123";
        // below line is use to get data from Firebase
        // firestore using collection in android.
        db.collection("users/" + user + "/tasks").get()
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
                            Task dataModal = d.toObject(Task.class);
                            dataModal.setItemID(d.getId());
                            Log.i("dataModal ID", "loadDatailListview: " + dataModal.getItemID());

                            // after getting data from Firebase we are
                            // storing that data in our array list
                            dataModalArrayList.add(dataModal);
                        }
                        // after that we are passing our array list to our adapter class.
                        TasksLVAdapter adapter = new TasksLVAdapter(requireContext(),
                                dataModalArrayList);

                        // after passing this array list to our adapter
                        // class we are setting our adapter to our list view.
                        coursesLV.setAdapter(adapter);
                    } else {
                        // if the snapshot is empty we are displaying a toast message.
                        Toast.makeText(requireContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    // we are displaying a toast message
                    // when we get any error from Firebase.
                    Toast.makeText(requireContext(), "Fail to load data..", Toast.LENGTH_SHORT).show();
                });
    }
}