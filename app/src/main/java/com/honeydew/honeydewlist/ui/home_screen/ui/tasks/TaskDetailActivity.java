package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;

import java.text.MessageFormat;

public class TaskDetailActivity extends AppCompatActivity {
    private static final String TAG = "Firestore";
    private FirebaseFirestore db;
    private Boolean completionStatus, verifiedStatus;
    private String taskName, taskDescription, taskOwner, taskOwnerUUID, taskID;
    private long melonReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Declare variables
        TextView status_tv, reward_tv, owner_tv, description_tv;
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();

        // Set app bar title
        taskName = i.getStringExtra("name");
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(taskName == null ? getTitle() : taskName);

        // Add back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Add FAB button click listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> finish());

        // Get data from intent extras
        taskDescription = i.getStringExtra("description");
        taskOwner = i.getStringExtra("owner");
        taskOwnerUUID = i.getStringExtra("ownerUUID");
        taskID = i.getStringExtra("itemID");
        melonReward = i.getLongExtra("points", -1);

        // Note: Check status against database when "Verify Completion" button is pressed
        completionStatus = i.getBooleanExtra("completionStatus", false);
        verifiedStatus = i.getBooleanExtra("verifiedStatus", false);

        // Find text views
        status_tv = findViewById(R.id.status);
        reward_tv = findViewById(R.id.reward);
        owner_tv = findViewById(R.id.owner);
        description_tv = findViewById(R.id.description);

        // Set text views to data from intent extras
        if (completionStatus) {
            if (verifiedStatus) {
                status_tv.setText(R.string.VerifiedStatusText);
            } else {
                status_tv.setText(R.string.CompletedStatusText);
            }
        } else {
            status_tv.setText(R.string.NotCompletedText);
        }

        reward_tv.setText(MessageFormat.format("{0}🍈", melonReward));
        owner_tv.setText(taskOwner);
        description_tv.setText(taskDescription);

        db.collection("users").document(taskOwnerUUID).collection("tasks").document(taskID).addSnapshotListener((value, error) -> {
            try {
                completionStatus = value.getBoolean("completionStatus");
                verifiedStatus = value.getBoolean("verifiedStatus");
                taskDescription = value.getData().get("description").toString();
                taskOwner = value.getData().get("owner").toString();
                taskOwnerUUID = value.getData().get("uuid").toString();
                melonReward = value.getLong("points");

                // Update text views
                if (completionStatus) {
                    if (verifiedStatus) {
                        status_tv.setText(R.string.VerifiedStatusText);
                    } else {
                        status_tv.setText(R.string.CompletedStatusText);
                    }
                } else {
                    status_tv.setText(R.string.NotCompletedText);
                }

                reward_tv.setText(MessageFormat.format("{0}🍈", melonReward));
                owner_tv.setText(taskOwner);
                description_tv.setText(taskDescription);
            } catch (RuntimeException e) {
                Log.e(TAG, "onCreate: Maybe document field is not the right type?", e);
            } catch (Exception e) {
                Log.e(TAG, "onCreate: Something happened", e);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db.terminate();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (db != null) {
            db.terminate();
        }
    }
}