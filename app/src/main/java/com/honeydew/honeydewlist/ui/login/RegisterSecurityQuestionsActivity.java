package com.honeydew.honeydewlist.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.ui.home_screen.HomeScreen;

import java.util.ArrayList;
import java.util.List;

public class RegisterSecurityQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_security_questions);

        // Get account info from previous page
        String Email, Username, Password;
        Intent intent = getIntent();
        Email = intent.getStringExtra("email");
        Username = intent.getStringExtra("username");
        Password = intent.getStringExtra("password");

        TextInputLayout menu1, menu2, menu3;
        AutoCompleteTextView question1, question2, question3;
        ArrayList<String> arrayList_questions;
        ArrayAdapter<String> arrayAdapter_questions;
        EditText answer1_value, answer2_value, answer3_value;
        Button finish;

        answer1_value = (EditText) findViewById(R.id.answer1);
        answer2_value = (EditText) findViewById(R.id.answer2);
        answer3_value = (EditText) findViewById(R.id.answer3);
        finish = (Button) findViewById(R.id.finishButton);

        // Create security questions dropdown list
        question1 = (AutoCompleteTextView) findViewById(R.id.question1);
        question2 = (AutoCompleteTextView) findViewById(R.id.question2);
        question3 = (AutoCompleteTextView) findViewById(R.id.question3);
        menu1 = (TextInputLayout) findViewById(R.id.menu1);
        menu2 = (TextInputLayout) findViewById(R.id.menu2);
        menu3 = (TextInputLayout) findViewById(R.id.menu3);

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
        question2.setAdapter(arrayAdapter_questions);
        question3.setAdapter(arrayAdapter_questions);

        // Get security questions and answers from user
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackBar;

                String Question1, Question2, Question3;
                String Answer1, Answer2, Answer3;
                Question1 = question1.getText().toString();
                Question2 = question2.getText().toString();
                Question3 = question3.getText().toString();
                Answer1 = answer1_value.getText().toString();
                Answer2 = answer2_value.getText().toString();
                Answer3 = answer3_value.getText().toString();

                if (Question1.matches("" )
                        || Question2.matches("" )
                        || Question3.matches("" )){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Question cannot be be empty",
                            Snackbar.LENGTH_SHORT
                    );
                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                } else if (Question1.matches(Question2) || Question1.matches(Question3)
                        || Question2.matches(Question3)) {
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Questions cannot be used more than once",
                            Snackbar.LENGTH_SHORT
                    );
                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();
                        }
                    });
                } else if (Answer1.matches("") || Answer2.matches("")
                        || Answer3.matches("")) {
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Please answer all questions",
                            Snackbar.LENGTH_SHORT
                    );
                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your action method here
                            snackBar.dismiss();
                        }
                    });
                } else {
                    // TODO: Send Email, Username, Password,
                    //  Security Questions and their answers to database

                    Intent i = new Intent(
                            getApplicationContext(),
                            HomeScreen.class
                    );
                    i.putExtra("email", Email);
                    i.putExtra("username", Username);
                    i.putExtra("password", Password);
                    i.putExtra("question1", Question1);
                    i.putExtra("question2", Question2);
                    i.putExtra("question3", Question3);
                    i.putExtra("answer1", Answer1);
                    i.putExtra("answer2", Answer2);
                    i.putExtra("answer3", Answer3);
                    startActivity(i);
                }
            }
        });

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

    // For dropdown menu
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