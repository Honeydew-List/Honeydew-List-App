package com.honeydew.honeydewlist.ui.home_screen.ui.friends;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
//import com.google.android.gms.friends.OnFailureListener;
//import com.google.android.gms.friends.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Friend;
import com.honeydew.honeydewlist.ui.home_screen.inteface.GetFriendCallback;
import com.honeydew.honeydewlist.ui.home_screen.inteface.GetUserCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFriendActivity extends AppCompatActivity {
    private static final String TAG = "DB ERROR";
    private FirebaseFirestore db;
    private String userID;
    private String username, email;
    String friendEmail, friendName;
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

        EditText email_value_edt;
        FloatingActionButton fab;

//        name_value_edt = (EditText) findViewById(R.id.text_input_edit_text_friend_name);
//        id_value_edt = (EditText) findViewById(R.id.text_input_edit_text_friend_id);
        email_value_edt = findViewById(R.id.text_input_edit_text_friend_email);

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

//            friendName = name_value_edt.getText().toString();
//            friendID = id_value_edt.getText().toString();
            friendEmail = email_value_edt.getText().toString();

            // Make database call to get friendID and friendName from database
            if(TextUtils.isEmpty(friendEmail)) {
                email_value_edt.setError("Please enter an email");
            } else {
                try {
                    readUserData(friendId -> {
                        // Look up friend username
                        if (friendId == null) {
                            Log.i(TAG, "onCreateView: Did not find email in database");
                            email_value_edt.setError("Could not find user");
                        } else {
                            try {
                                readFriendData(friendId, friendString -> {
                                    try {
                                        addDatatoFirestore(friendString, friendId, friendEmail);
                                        finish();
                                    } catch (Resources.NotFoundException e) {
                                        Log.e(TAG, "onCreateView: Did not find value in database", e);
                                        email_value_edt.setError("Not a valid email");
                                    }  catch (SQLiteDatabaseLockedException e) {
                                        email_value_edt.setError("Not a valid email");
                                        Log.e(TAG, "onCreateView: Database already in use", e);
                                    } catch (RuntimeException e) {
                                        email_value_edt.setError("Not a valid email");
                                        Log.e(TAG, "onCreate: RuntimeException", e);
                                    } catch (Exception e) {
                                        email_value_edt.setError("Not a valid email");
                                        Log.e(TAG, "onCreateView: Something happened", e);
                                    }
                                });
                            } catch (Resources.NotFoundException e) {
                                Log.e(TAG, "onCreateView: Did not find value in database", e);
                                email_value_edt.setError("Not a valid email");
                            }  catch (SQLiteDatabaseLockedException e) {
                                email_value_edt.setError("Not a valid email");
                                Log.e(TAG, "onCreateView: Database already in use", e);
                            } catch (RuntimeException e) {
                                email_value_edt.setError("Not a valid email");
                                Log.e(TAG, "onCreate: RuntimeException", e);
                            } catch (Exception e) {
                                email_value_edt.setError("Not a valid email");
                                Log.e(TAG, "onCreateView: Something happened", e);
                            }
                        }
                    });
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "onCreateView: Did not find value in database", e);
                    email_value_edt.setError("Not a valid email");
                }  catch (SQLiteDatabaseLockedException e) {
                    email_value_edt.setError("Not a valid email");
                    Log.e(TAG, "onCreateView: Database already in use", e);
                } catch (RuntimeException e) {
                    email_value_edt.setError("Not a valid email");
                    Log.e(TAG, "onCreate: RuntimeException", e);
                } catch (Exception e) {
                    email_value_edt.setError("Not a valid email");
                    Log.e(TAG, "onCreateView: Something happened", e);
                }
            }
        });
    }

    public void readUserData(GetUserCallback myCallback) {
        db.collection("data").document("emailToUid").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = "";
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            Object userId_obj = document.get(FieldPath.of(friendEmail));
                            if (userId_obj != null) {
                                userId = userId_obj.toString();
                            }
                        }
                        myCallback.onCallback(userId);
                    }
                });
    }

    public void readFriendData(String friendId, GetUserCallback myCallback) {
        db.collection("users").document(friendId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String username = "";
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            username = document.getString("username");
                        }
                        myCallback.onCallback(username);
                    }
                });
    }

    private void addDatatoFirestore(String friendName, String friendID, String friendEmail){

        CollectionReference dbFriends = db.collection("users/" + userID + "/friends");

        Friend friend = new Friend(friendName, friendID, friendEmail);

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

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db.terminate();
        }
    }
}

