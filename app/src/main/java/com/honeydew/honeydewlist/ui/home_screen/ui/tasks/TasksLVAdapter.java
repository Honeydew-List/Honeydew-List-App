package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;

import com.google.android.material.snackbar.Snackbar;
import com.honeydew.honeydewlist.R;

import com.honeydew.honeydewlist.data.Task;

// Credit to https://www.geeksforgeeks.org/how-to-create-dynamic-listview-in-android-using-firebase-firestore/
public class TasksLVAdapter extends ArrayAdapter<Task> {
    // constructor for our list view adapter.
    public TasksLVAdapter(@NonNull Context context, ArrayList<Task> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView,
                        @NonNull final ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.task_lv_item, parent,
                    false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Task dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView name = listitemView.findViewById(R.id.list_item_name);
        TextView description = listitemView.findViewById(R.id.list_item_description);
        TextView points = listitemView.findViewById(R.id.list_item_melon_count);
        TextView owner = listitemView.findViewById(R.id.list_item_owner);
        CheckBox completionStatus = listitemView.findViewById(R.id.list_item_check_box);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        name.setText(dataModal.getName());
        description.setText(dataModal.getDescription());
        points.setText(MessageFormat.format("{0}", "Melons: " + dataModal.getPoints()));
        owner.setText(String.format("%s %s",
                getContext().getResources().getString(R.string.ownerLabel), dataModal.getOwner()));
        completionStatus.setChecked(dataModal.getCompletionStatus());

        // below line is use to add item click listener
        // for our item of list view.
        listitemView.setOnClickListener(v -> {
            // on the item click on our list view.
            // we are displaying a toast message.
//            Toast.makeText(v.getContext(), "Item clicked is : " + dataModal.getName(),
//                    Toast.LENGTH_SHORT).show();
            final Snackbar snackBar = Snackbar.make(
                    v,
                    "Item clicked is : " + dataModal.getName(),
                    Snackbar.LENGTH_SHORT
            );
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();
                }
            });
            snackBar.show();
            // Use the itemID to load the task details from firestore
            Log.i("TasksLVAdapter", "getView: " + dataModal.getName());
            Intent i = new Intent(v.getContext(), TaskDetailActivity.class);
            i.putExtra("owner", dataModal.getOwner());
            i.putExtra("ownerUUID", dataModal.getUUID());
            i.putExtra("itemID", dataModal.getItemID());
            getContext().startActivity(i);
        });
        return listitemView;
    }
}
