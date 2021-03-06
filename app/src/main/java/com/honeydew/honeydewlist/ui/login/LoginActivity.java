package com.honeydew.honeydewlist.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honeydew.honeydewlist.ui.home_screen.HomeScreen;
import com.honeydew.honeydewlist.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Auth";
    private ProgressBar progressbar;
    private EditText email_value, password_value;
    private Button login, register, forgot_password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        email_value = (EditText) findViewById(R.id.login_email);
        password_value = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);
        forgot_password = (Button) findViewById(R.id.forgot_password_Button);
        progressbar = findViewById(R.id.progressBar);

        login.setOnClickListener(v -> {
            loginUserAccount();
        });

        register.setOnClickListener(v -> {
            String Email, Password;
            Email = email_value.getText().toString();
            Password = password_value.getText().toString();
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            i.putExtra("email", Email);
            i.putExtra("password", Password);
            startActivity(i);
        });

        forgot_password.setOnClickListener(v -> Toast.makeText(
                getApplicationContext(),
                "Not yet implemented",
                Toast.LENGTH_SHORT
        ).show());
    }

    private void loginUserAccount() {
        // show the visibility of progress bar to show loading
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = email_value.getText().toString();
        password = password_value.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            email_value.setError("Email cannot be empty");
            progressbar.setVisibility(View.GONE);
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_value.setError("Enter a valid email");
            progressbar.setVisibility(View.GONE);
            return;
        } else if (password.length() < 7) {
            password_value.setError("Password must at least be 8 characters");
            progressbar.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        progressbar.setVisibility(View.GONE);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Log.i(TAG, "signInWithEmail:failure" + task.getException().getMessage());
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Incorrect Email or Password",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Incorrect Username or Password",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void reload() {
        Intent i = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(i);
        finish();
    }
}