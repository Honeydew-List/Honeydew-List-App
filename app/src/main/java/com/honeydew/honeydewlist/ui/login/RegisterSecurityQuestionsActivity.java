package com.honeydew.honeydewlist.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.ui.home_screen.HomeScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterSecurityQuestionsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    private static final String TAG ="EmailPassword";
    private String Question1, Question2, Question3;
    private String Answer1, Answer2, Answer3;
    private String Email, Username, Password;
    private int melons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_security_questions);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressBar);

        // Get account info from previous page
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
        finish.setOnClickListener(v -> {
            final Snackbar snackBar;

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
                        "Questions cannot be be empty",
                        Snackbar.LENGTH_SHORT
                );
                snackBar.setAction("Dismiss", v12 -> {
                    // Call your action method here
                    snackBar.dismiss();
                });
                snackBar.show();
            } else if (Question1.equalsIgnoreCase(Question2) || Question1.equalsIgnoreCase(Question3)
                    || Question2.equalsIgnoreCase(Question3)) {
                snackBar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Questions cannot be used more than once",
                        Snackbar.LENGTH_SHORT
                );
                snackBar.setAction("Dismiss", v1 -> {
                    // Call your action method here
                    snackBar.dismiss();
                });
                snackBar.show();
            } else if (Answer1.matches("")) {
                answer1_value.setError("Answer cannot be empty");
            } else if (Answer2.matches("")) {
                answer2_value.setError("Answer cannot be empty");
            } else if (Answer3.matches("")) {
                answer3_value.setError("Answer cannot be empty");
            } else {
                // Send Email, Username,
                // Security Questions and their answers to database
                // Create account and put fields into user map
                createAccount(Email,Password);
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

    private void createAccount(String email, String password) {
        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            progressbar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progressbar.setVisibility(View.GONE);
                            updateUI(null);
                            
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(RegisterSecurityQuestionsActivity.this,
                                        "Error: Invalid Email Format",
                                        Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                Toast.makeText(RegisterSecurityQuestionsActivity.this,
                                        "Error: Account already exists",
                                        Toast.LENGTH_LONG).show();
                            } catch(Exception e) {
                                Toast.makeText(RegisterSecurityQuestionsActivity.this,
                                        "Error: Account creation failed",
                                        Toast.LENGTH_LONG).show();
                                Log.e(TAG, e.getMessage());
                            }

                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String,Object> userData = new HashMap<>();
            final String uuid = user.getUid();
            userData.put("email", Email);
            userData.put("username", Username);
            userData.put("uuid", uuid);
            userData.put("sec_question1", Question1);
            userData.put("sec_question2", Question2);
            userData.put("sec_question3", Question3);
            userData.put("sec_answer1", Answer1);
            userData.put("sec_answer2", Answer2);
            userData.put("sec_answer3", Answer3);
            userData.put("melon_count", melons);

            // Upload user map to database
            db.collection("users").document(uuid)
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written.");
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error adding document", e);
                    });


            // Upload user to user list
            db.collection("data").document("emailToUid")
                    .update(FieldPath.of(Email), uuid)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot emailToUid successfully updated!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error updating document emailToUid", e));

            Toast.makeText(
                    getApplicationContext(),
                    "Account Creation Successful",
                    Toast.LENGTH_SHORT
            ).show();

            Intent i = new Intent(
                    getApplicationContext(),
                    HomeScreen.class
            );
            finish();
            startActivity(i);
        } else {
            // Go back to register screen
            finish();
        }
    }

    private void reload() { }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}