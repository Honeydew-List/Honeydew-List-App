package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

import java.text.MessageFormat;

public class TasksFragment extends Fragment {

    public TasksFragment() {};

    private TaskAdapter adapter;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerView;
    private CheckBox checkBox;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        recyclerView = view.findViewById(R.id.firestore_task_list);
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document("ABC#0123").collection("tasks");

        FirestoreRecyclerOptions<Task> options = new FirestoreRecyclerOptions
                .Builder<Task>().setQuery(query, Task.class)
                .build();

        adapter = new TaskAdapter(options);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
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

class TaskAdapter extends FirestoreRecyclerAdapter<Task, TaskAdapter.TaskViewHolder> {

    public TaskAdapter(@NonNull FirestoreRecyclerOptions<Task> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder holder, int position, @NonNull Task model) {
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());
        holder.points.setText(MessageFormat.format("{0}", model.getPoints()));
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_task_single,
                viewGroup, false);
        return new TaskViewHolder(v);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView name, desc, points;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.list_item_name);
            desc = itemView.findViewById(R.id.list_item_description);
            points = itemView.findViewById(R.id.list_item_melon_count);
        }
    }
}