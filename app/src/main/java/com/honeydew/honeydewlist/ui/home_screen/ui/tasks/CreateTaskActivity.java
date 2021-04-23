package com.honeydew.honeydewlist.ui.home_screen.ui.tasks;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;

import com.honeydew.honeydewlist.R;

import java.util.Objects;

public class CreateTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EditText name_value, description_value, reward_value;
        FloatingActionButton fab;

        name_value = (EditText) findViewById(R.id.text_input_edit_text_task_name);
        description_value = (EditText) findViewById(R.id.text_input_edit_text_task_description);
        reward_value = (EditText) findViewById(R.id.text_input_edit_text_task_points);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(view -> {
            String Name, Description;
            int Reward;

            Name = name_value.getText().toString();
            Description = description_value.getText().toString();
            Reward = Integer.parseInt(reward_value.getText().toString());

            // TODO: Add data to database

            finish();
        });
    }
}