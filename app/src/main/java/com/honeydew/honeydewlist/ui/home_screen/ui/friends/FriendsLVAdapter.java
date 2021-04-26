package com.honeydew.honeydewlist.ui.home_screen.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Friend;


public class FriendsLVAdapter extends ArrayAdapter<Friend>{

    public FriendsLVAdapter(@NonNull Context context, ArrayList<Friend> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView,
                        @NonNull final ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.friend_lv_item, parent,
                    false);
        }

        Friend dataModal = getItem(position);

        TextView userEmail = listitemView.findViewById(R.id.list_item_userEmail);
        TextView userName = listitemView.findViewById(R.id.list_item_username);
        userEmail.setText(dataModal.getEmail());
        userName.setText(dataModal.getOwner());

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        return listitemView;
    }
}
