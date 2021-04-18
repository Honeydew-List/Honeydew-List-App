package com.honeydew.honeydewlist.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honeydew.honeydewlist.R;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.honeydew.honeydewlist.ui.home_screen.HomeScreen;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG ="EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        EditText email_value;
        EditText username_value;
        EditText password_value, verify_password_value;
        Button next;

        email_value = (EditText) findViewById(R.id.email);
        username_value = (EditText) findViewById(R.id.username);
        password_value = (EditText) findViewById(R.id.password);
        verify_password_value = (EditText) findViewById(R.id.verify_password);
        next = (Button) findViewById(R.id.nextButton);

        username_value.setText(intent.getStringExtra("username"));
        password_value.setText(intent.getStringExtra("password"));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackBar;

                String Email, Username, Password, VerifyPassword;
                Email = email_value.getText().toString();
                Username = username_value.getText().toString();
                Password = password_value.getText().toString();
                VerifyPassword = verify_password_value.getText().toString();

                if (Email.matches("")){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Email must not be empty",
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
                } else if (Username.matches("")){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Username must not be empty",
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
                } else if (Password.matches("")){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Password must not be empty",
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
                } else if (VerifyPassword.matches("")){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Verify Password must not be empty",
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
                } else if (Password.length() < 7){
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Password must be longer than 7 characters",
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
                } else if (!Password.matches(VerifyPassword)) {
                    snackBar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Passwords Do Not Match",
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
                } else {
                    Intent downloadIntent = new Intent(
                            getApplicationContext(),
                            RegisterSecurityQuestionsActivity.class
                    );
                    downloadIntent.putExtra("email", Email);
                    downloadIntent.putExtra("username", Username);
                    downloadIntent.putExtra("password", Password);
                    startActivity(downloadIntent);
                }
            }
        });

        // For back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

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
}