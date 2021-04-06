package com.honeydew.honeydewlist.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.honeydew.honeydewlist.ui.home_screen.HomeScreen;
import com.honeydew.honeydewlist.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username_value, password_value;
        Button login, register, forgot_password;

        username_value = (EditText) findViewById(R.id.username);
        password_value = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);
        forgot_password = (Button) findViewById(R.id.forgot_password_Button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username, Password;
                Username = username_value.getText().toString();
                Password = password_value.getText().toString();

                // TODO: Verify Username and Password match in database
                if (Username.matches("ABC") && Password.matches("123")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Login Successful",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent downloadIntent = new Intent(getApplicationContext(), HomeScreen.class);
                    downloadIntent.putExtra("username", Username);
                    startActivity(downloadIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username, Password;
                Username = username_value.getText().toString();
                Password = password_value.getText().toString();
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.putExtra("username", Username);
                i.putExtra("password", Password);
                startActivity(i);
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getApplicationContext(),
                        "Not yet implemented",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}