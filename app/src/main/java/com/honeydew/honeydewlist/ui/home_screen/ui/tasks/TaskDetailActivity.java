package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.honeydew.honeydewlist.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String taskName, taskDescription, taskOwner, taskOwnerUUID, taskID, melonReward;

        Intent i = getIntent();
        taskName = i.getStringExtra("name");
        taskDescription = i.getStringExtra("description");
        taskOwner = i.getStringExtra("owner");
        taskOwnerUUID = i.getStringExtra("ownerUUID");
        taskID = i.getStringExtra("itemID");
        melonReward = i.getStringExtra("points");

        // Check status against database when "Verify Completion" button is pressed
        Boolean completionStatus = i.getBooleanExtra("completionStatus", false);
        Boolean verifiedStatus = i.getBooleanExtra("verifiedStatus", false);


        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(taskName == null ? getTitle() : taskName);

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


    }
}