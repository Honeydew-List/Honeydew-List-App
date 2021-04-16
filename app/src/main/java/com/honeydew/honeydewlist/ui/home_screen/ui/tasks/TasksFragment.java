package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

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
import androidx.recyclerview.widget.RecyclerView;


import com.honeydew.honeydewlist.R;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
//        final TextView textView = root.findViewById(R.id.text_tasks);
//        tasksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        mFirestoreList = root.findViewById(R.id.firestore_task_list);
        return root;
    }
}