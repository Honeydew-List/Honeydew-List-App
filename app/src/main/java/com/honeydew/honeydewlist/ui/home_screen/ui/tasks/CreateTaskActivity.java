package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.honeydew.honeydewlist.data.Task;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout;
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EditText name_value_edt, description_value_edt, reward_value_edt;
        FloatingActionButton fab;

        name_value_edt = (EditText) findViewById(R.id.text_input_edit_text_task_name);
        description_value_edt = (EditText) findViewById(R.id.text_input_edit_text_task_description);
        reward_value_edt = (EditText) findViewById(R.id.text_input_edit_text_task_points);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Get current user
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

//        db.collection("users").document(userID).get().addOnSuccessListener(
//            documentSnapshot -> username = documentSnapshot.getString("username")
//        ).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
//                "Could not find username from Firestore",
//                Toast.LENGTH_SHORT).show());


        fab.setOnClickListener(view -> {
            String TaskName, TaskDescription, TaskReward;

            TaskName = name_value_edt.getText().toString();
            TaskDescription = description_value_edt.getText().toString();
            TaskReward = reward_value_edt.getText().toString();

            int TaskRewardInt;

            // validating the text fields if empty or not.
            if (TextUtils.isEmpty(TaskName)) {
                name_value_edt.setError("Please enter Course Name");
            } else if (TextUtils.isEmpty(TaskReward)) {
                reward_value_edt.setError("Please enter Course Duration");
            } else {
                // calling method to add data to Firebase Firestore.
                try {
                    TaskRewardInt = Integer.parseInt(TaskReward);
                    addDataToFirestore(TaskName, TaskDescription, TaskRewardInt);
                } catch (NumberFormatException e) {
                    reward_value_edt.setError("Please enter only numbers for the reward");
                }
            }

            finish();
        });
    }

    private void addDataToFirestore(String TaskName, String TaskDescription, int TaskReward) {
        // Temp userID for testing
        userID = "ABC#0123";
        username = "ABC";

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbTasks = db.collection("users/" + userID + "/tasks");

        // adding our data to our courses object class.
        Task task = new Task(TaskName,
                TaskDescription,
                username,
                userID,
                (long) TaskReward,
                false);

        // below method is use to add data to Firebase Firestore.
        dbTasks.add(task).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(getApplicationContext(),
                        "Your Task has been added to Firebase Firestore",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(getApplicationContext(),
                        "Fail to add course \n" + e,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}