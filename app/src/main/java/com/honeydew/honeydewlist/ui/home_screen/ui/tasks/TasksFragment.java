package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

public class TasksFragment extends Fragment {

    @SuppressWarnings("rawtypes")
    private FirestoreRecyclerAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasksViewModel tasksViewModel = new ViewModelProvider(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
//        final TextView textView = root.findViewById(R.id.text_tasks);
//        tasksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView mFirestoreList = root.findViewById(R.id.firestore_task_list);

        // TODO: Do this for every friend and add it to the task list
        // Query
        Query query = firebaseFirestore.collection("users")
                .document("ABC#0123").collection("tasks");
        // RecyclerOptions
        FirestoreRecyclerOptions<TasksModel> tasks = new FirestoreRecyclerOptions
                .Builder<TasksModel>().setQuery(query, TasksModel.class)
                .build();


        // View Holder
        adapter = new FirestoreRecyclerAdapter<TasksModel, TasksViewHolder>(tasks) {

            @NonNull
            @Override
            public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task_single, parent, false);
                return new TasksViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull TasksViewHolder holder, int position, @NonNull TasksModel model) {
                Task task = model.getTask();
                holder.list_name.setText(task.getName());
                holder.list_desc.setText(task.getDescription());
                holder.list_melon_count.setText(task.getPoints().toString());
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);
        return root;
    }

    private static class TasksViewHolder extends RecyclerView.ViewHolder{

        private TextView list_name;
        private TextView list_desc;
        private TextView list_melon_count;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_item_name);
            list_name = itemView.findViewById(R.id.list_item_description);
            list_name = itemView.findViewById(R.id.list_item_melon_count);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }
}