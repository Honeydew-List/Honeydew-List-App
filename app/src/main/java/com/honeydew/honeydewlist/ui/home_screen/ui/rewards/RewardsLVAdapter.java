package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Reward;
import com.honeydew.honeydewlist.ui.home_screen.ui.tasks.TaskDetailActivity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RewardsLVAdapter extends ArrayAdapter<Reward> {
    // constructor for our list view adapter.
    public RewardsLVAdapter(@NonNull Context context, ArrayList<Reward> dataModalArrayList) {
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
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.reward_lv_item, parent,
                    false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Reward dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView name = listitemView.findViewById(R.id.list_item_name);
        TextView description = listitemView.findViewById(R.id.list_item_description);
        TextView points = listitemView.findViewById(R.id.list_item_melon_count);
        TextView owner = listitemView.findViewById(R.id.list_item_owner);
        CheckBox completionStatus = listitemView.findViewById(R.id.list_item_check_box);
        MaterialCardView card = listitemView.findViewById(R.id.card_view);


        // If description is empty, don't show the text view
        if (TextUtils.isEmpty(dataModal.getDescription()))
            description.setVisibility(View.GONE);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        name.setText(dataModal.getName());
        description.setText(dataModal.getDescription());
        points.setText(MessageFormat.format("{0}", "Melons: " + dataModal.getCost()));
        owner.setText(String.format("%s %s",
                getContext().getResources().getString(R.string.ownerLabel), dataModal.getOwner()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // below line is use to add item click listener
        // for our item of list view.
        card.setOnClickListener(v -> {
            // Commented out because we are using a check box instead
            // card.setChecked(!card.isChecked());

            // Use the itemID to load the task details from firestore
            Log.i("RewardsLVAdapter", "getView: " + dataModal.getName());
            Intent i = new Intent(v.getContext(), TaskDetailActivity.class);
            i.putExtra("owner", dataModal.getOwner());
            i.putExtra("ownerUUID", dataModal.getUUID());
            i.putExtra("itemID", dataModal.getItemID());
            getContext().startActivity(i);
        });
        return listitemView;
    }
}
