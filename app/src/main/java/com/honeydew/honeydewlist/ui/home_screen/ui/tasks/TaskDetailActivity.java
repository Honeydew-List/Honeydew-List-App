package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.honeydew.honeydewlist.R;

import java.text.MessageFormat;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Declare variables
        String taskName, taskDescription, taskOwner, taskOwnerUUID, taskID;
        Long melonReward;
        Boolean completionStatus, verifiedStatus;
        TextView reward_tv, owner_tv, description_tv;

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
        reward_tv = findViewById(R.id.reward);
        owner_tv = findViewById(R.id.owner);
        description_tv = findViewById(R.id.description);

        // Set text views to data from intent extras
        reward_tv.setText(MessageFormat.format("{0}🍈", melonReward));
        owner_tv.setText(String.format("%s", taskOwner));
        description_tv.setText(String.format("Description:\n%s", taskDescription));
    }
}