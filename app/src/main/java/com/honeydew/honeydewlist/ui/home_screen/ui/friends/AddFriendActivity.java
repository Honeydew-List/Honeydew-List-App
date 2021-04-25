package com.honeydew.honeydewlist.ui.home_screen.ui.friends;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
//import com.google.android.gms.friends.OnFailureListener;
//import com.google.android.gms.friends.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Friend;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddFriendActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String userID;
    private String username;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout;
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EditText name_value_edt, id_value_edt;
        FloatingActionButton fab;

        name_value_edt = (EditText) findViewById(R.id.text_input_edit_text_friend_name);
        id_value_edt = (EditText) findViewById(R.id.text_input_edit_text_friend_id);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        db = FirebaseFirestore.getInstance();
        if (user != null) {
            userID = user.getUid();
            db.collection("users").document(userID).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        username = documentSnapshot.getData().get("username").toString();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(),
                                "Could not find username from Firestore",
                                Toast.LENGTH_SHORT).show();
                    });
        }

        fab.setOnClickListener(view -> {
            String friendName, friendID;

            friendName = name_value_edt.getText().toString();
            friendID = id_value_edt.getText().toString();

            if(TextUtils.isEmpty(friendName)) {
                name_value_edt.setError("Please enter friend username");
            } else if(TextUtils.isEmpty(friendID)){
                id_value_edt.setError("Please enter friend userID");
            }
            else {
                try {
                    addDatatoFirestore(friendName, friendID);
                } catch (Resources.NotFoundException e) {
                    id_value_edt.setError("Not a valid userID");
                }
            }

            finish();

        });
    }

    private void addDatatoFirestore(String friendName, String friendID){

        CollectionReference dbFriends = db.collection("users/" + userID + "/friends");

        Friend friend = new Friend(friendName, friendID);

        dbFriends.add(friend).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(),
                    "Your Friend has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(),
                    "Fail to add friend \n" + e,
                    Toast.LENGTH_SHORT).show();
        });
    }
}

