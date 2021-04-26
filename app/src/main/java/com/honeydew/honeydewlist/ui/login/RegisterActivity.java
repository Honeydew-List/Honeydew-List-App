package com.honeydew.honeydewlist.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honeydew.honeydewlist.R;

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
        username_value = (EditText) findViewById(R.id.login_email);
        password_value = (EditText) findViewById(R.id.password);
        verify_password_value = (EditText) findViewById(R.id.verify_password);
        next = (Button) findViewById(R.id.nextButton);

        email_value.setText(intent.getStringExtra("email"));
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
                    email_value.setError("Email cannot be empty");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email_value.setError("Enter a valid email");
                } else if (Username.matches("")){
                    username_value.setError("Username cannot be empty");
                } else if (Password.matches("")){
                    password_value.setError("Password cannot be empty");
                } else if (VerifyPassword.matches("")){
                    verify_password_value.setError("Password cannot be empty");
                } else if (Password.length() < 7){
                    password_value.setError("Password must at least be 8 characters");
                } else if (!Password.matches(VerifyPassword)) {
                    verify_password_value.setError("Passwords do not match");
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

    private void reload() { }

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