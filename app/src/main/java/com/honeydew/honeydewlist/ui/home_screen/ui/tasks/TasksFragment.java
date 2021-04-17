package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

public class TasksFragment extends Fragment {

    public TasksFragment() {};

    private TaskAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = root.findViewById(R.id.firestore_task_list);
        setUpRecyclerView();
        return root;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document("ABC#0123").collection("tasks");

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions
                .Builder<Task>().setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}