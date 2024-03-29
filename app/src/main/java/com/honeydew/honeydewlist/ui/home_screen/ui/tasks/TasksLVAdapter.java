package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;

import com.honeydew.honeydewlist.data.Task;

// Credit to https://www.geeksforgeeks.org/how-to-create-dynamic-listview-in-android-using-firebase-firestore/
public class TasksLVAdapter extends ArrayAdapter<Task> {
    private static final String TAG = "DB ERROR";
    private FirebaseFirestore db;
    // constructor for our list view adapter.
    public TasksLVAdapter(@NonNull Context context, ArrayList<Task> dataModalArrayList, FirebaseFirestore db) {
        super(context, 0, dataModalArrayList);
        this.db = db;
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
//        CheckBox completionStatus = listitemView.findViewById(R.id.list_item_check_box);
        MaterialCardView card = listitemView.findViewById(R.id.card_view);


        // If description is empty, don't show the text view
        if (TextUtils.isEmpty(dataModal.getDescription()))
            description.setVisibility(View.GONE);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        name.setText(dataModal.getName());
        description.setText(dataModal.getDescription());
        points.setText(MessageFormat.format("{0}🍈", dataModal.getPoints()));
        owner.setText(String.format("%s %s",
                getContext().getResources().getString(R.string.ownerLabel), dataModal.getOwner()));
//        completionStatus.setChecked(dataModal.getCompletionStatus());
        // Only mark as verified if it is completed
        if (dataModal.getCompletionStatus())
            card.setChecked(dataModal.getVerifiedStatus());

//        completionStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    updateFirestore(v);
//                } catch (IllegalStateException e) {
//                    Log.w(TAG, "onClick: db is terminated, reinitializing now", e);
//                    db = FirebaseFirestore.getInstance();
//                    updateFirestore(v);
//                } catch (SQLiteDatabaseLockedException e) {
//                    Log.e(TAG, "onCreateView: Database already in use", e);
//                } catch (RuntimeException e) {
//                    Log.e(TAG, "onCreate: RuntimeException", e);
//                } catch (Exception e) {
//                    Log.e(TAG, "onCreateView: Something happened", e);
//                }
//
//
//            }
//            private void updateFirestore(View v){
//                Map<String, Object> stringObjectMap = new HashMap<String, Object>() {{
//                    put("completionStatus", completionStatus.isChecked());
//                }};
//                db.collection("users/" + dataModal.getUUID() + "/tasks").
//                        document(dataModal.getItemID()).update(stringObjectMap);
//            }
//
//        });

        // below line is use to add item click listener
        // for our item of list view.
        card.setOnClickListener(v -> {
            // Commented out because we are using a check box instead
            // card.setChecked(!card.isChecked());

            if (db != null) {
                db.terminate();
            }
            // Use the itemID to load the task details from firestore
            Log.i("TasksLVAdapter", "getView: " + dataModal.getName());
            Intent i = new Intent(v.getContext(), TaskDetailActivity.class);
            i.putExtra("owner", dataModal.getOwner());
            i.putExtra("ownerUUID", dataModal.getUUID());
            i.putExtra("itemID", dataModal.getItemID());
            i.putExtra("name", dataModal.getName());
            i.putExtra("description", dataModal.getDescription());
            i.putExtra("points", dataModal.getPoints());
            i.putExtra("completionStatus", dataModal.getCompletionStatus());
            i.putExtra("completionDoer", dataModal.getCompletionDoer());
            i.putExtra("completionDoerUUID", dataModal.getCompletionDoerUUID());
            i.putExtra("verifiedStatus", dataModal.getVerifiedStatus());
            getContext().startActivity(i);
        });
        return listitemView;
    }
}
