package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        TextView reward_tv, owner_tv, description_tv;
        Chip complete_chip, verify_chip;
        FirebaseAuth auth;
        FirebaseUser user;
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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view -> finish());

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
        reward_tv = findViewById(R.id.reward);
        owner_tv = findViewById(R.id.owner);
        description_tv = findViewById(R.id.description);
        complete_chip = findViewById(R.id.complete_chip);
        verify_chip = findViewById(R.id.verify_chip);

        // Set chips to data from intent extras
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (completionStatus) {
            complete_chip.setChecked(true);
            // Set checkable
            if (user != null) {
                if (user.getUid().equals(taskOwnerUUID)) {
                    // Owner, can only set verified
                    verify_chip.setCheckable(true);
                }
            } else {
                // If user is not logged in, don't allow input
                complete_chip.setCheckable(false);
                verify_chip.setCheckable(false);
            }

            // Set checked
            verify_chip.setChecked(verifiedStatus);
        } else {
            complete_chip.setChecked(false);
            verify_chip.setChecked(false);
        }

        reward_tv.setText(MessageFormat.format("{0}🍈", melonReward));
        owner_tv.setText(taskOwner);
        description_tv.setText(taskDescription);
        try {
            db.collection("users").document(taskOwnerUUID).collection("tasks").document(taskID).addSnapshotListener((value, error) -> {
                if (value != null) {
                    completionStatus = value.getBoolean("completionStatus");
                    verifiedStatus = value.getBoolean("verifiedStatus");
                    taskDescription = value.getData().get("description").toString();
                    melonReward = value.getLong("points");

                    // Update chips
                    if (completionStatus) {
                        complete_chip.setChecked(true);
                        // Set checkable
                        if (user != null) {
                            if (user.getUid().equals(taskOwnerUUID)) {
                                // Owner, can only set verified
                                verify_chip.setCheckable(true);
                            }
                        } else {
                            // If user is not logged in, don't allow input
                            complete_chip.setCheckable(false);
                            verify_chip.setCheckable(false);
                        }

                        // Set checked
                        verify_chip.setChecked(verifiedStatus);
                    } else {
                        complete_chip.setChecked(false);
                        verify_chip.setChecked(false);
                    }

                    reward_tv.setText(MessageFormat.format("{0}🍈", melonReward));
                    description_tv.setText(taskDescription);
                }
            });
        } catch (SQLiteDatabaseLockedException e) {
            Log.e(TAG, "onCreateView: Database already in use", e);
        } catch (RuntimeException e) {
            Log.e(TAG, "onCreate: RuntimeException", e);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Something happened", e);
        }



        complete_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Don't let owner mark it as completed
                if (user != null) {
                    if (user.getUid().equals(taskOwnerUUID)) {
                        // NonOwner, can only set completed, ignore input
                        complete_chip.setChecked(!isChecked);
                        Snackbar snackBar = Snackbar.make(
                                findViewById(android.R.id.content),
                                "The owner of a task cannot mark it as completed",
                                Snackbar.LENGTH_SHORT
                        );
                        snackBar.setAction("Dismiss", v12 -> {
                            // Call your action method here
                            snackBar.dismiss();
                        });
                        snackBar.show();
                    } else {
                        // Sync with firestore here
                    }
                } else {
                    // If user is not logged in, don't allow input
                    complete_chip.setCheckable(false);
                    verify_chip.setCheckable(false);
                }
            }
        });

        verify_chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Don't let non owner mark it as verified
                if (user != null) {
                    if (!user.getUid().equals(taskOwnerUUID)) {
                        // Owner can only set verified, ignore input
                        verify_chip.setChecked(!isChecked);
                        Snackbar snackBar = Snackbar.make(
                                findViewById(android.R.id.content),
                                "Only the owner of a task can mark it as verified",
                                Snackbar.LENGTH_SHORT
                        );
                        snackBar.setAction("Dismiss", v12 -> {
                            // Call your action method here
                            snackBar.dismiss();
                        });
                        snackBar.show();
                    } else {
                        // Sync with firestore here
                    }
                } else {
                    // If user is not logged in, don't allow input
                    complete_chip.setCheckable(false);
                    verify_chip.setCheckable(false);
                }
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