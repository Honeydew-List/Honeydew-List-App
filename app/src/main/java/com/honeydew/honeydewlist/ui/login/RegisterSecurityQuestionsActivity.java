package com.honeydew.honeydewlist.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.honeydew.honeydewlist.R;

import java.util.ArrayList;
import java.util.List;

public class RegisterSecurityQuestionsActivity extends AppCompatActivity {
    TextInputLayout menu;
    AutoCompleteTextView question1;
    ArrayList<String> arrayList_questions;
    ArrayAdapter<String> arrayAdapter_questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_security_questions);

        // TODO: Create security questions dropdown list
        menu = (TextInputLayout) findViewById(R.id.menu);
        question1 = (AutoCompleteTextView) findViewById(R.id.question1);

        arrayList_questions = new ArrayList<>();
        arrayList_questions.add(getString(R.string.option_1));
        arrayList_questions.add(getString(R.string.option_2));
        arrayList_questions.add(getString(R.string.option_3));
        arrayList_questions.add(getString(R.string.option_4));
        arrayList_questions.add(getString(R.string.option_5));
        arrayList_questions.add(getString(R.string.option_6));
        arrayList_questions.add(getString(R.string.option_7));
        arrayList_questions.add(getString(R.string.option_8));
        arrayList_questions.add(getString(R.string.option_9));
        arrayList_questions.add(getString(R.string.option_10));
        arrayList_questions.add(getString(R.string.option_11));
        arrayList_questions.add(getString(R.string.option_12));

        arrayAdapter_questions = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.list_item,
                arrayList_questions
        );
        question1.setAdapter(arrayAdapter_questions);

        // TODO: Get security questions and answers from user

        // TODO: Send account info to database on successful account creation

        // For back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    // For back button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        return super.onContextItemSelected(item);
    }
}