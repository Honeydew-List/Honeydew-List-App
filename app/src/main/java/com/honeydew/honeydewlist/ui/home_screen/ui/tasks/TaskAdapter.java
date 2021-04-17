package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Task;

import java.text.MessageFormat;

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

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView name, desc, points;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.list_item_name);
            desc = itemView.findViewById(R.id.list_item_description);
            points = itemView.findViewById(R.id.list_item_melon_count);
        }
    }
}