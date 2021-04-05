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
        Button login, register;

        username_value = (EditText) findViewById(R.id.username);
        password_value = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);
        register = (Button) findViewById(R.id.registerButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username, Password;
                Username = username_value.getText().toString();
                Password = password_value.getText().toString();

                if (Username.matches("ABC") && Password.matches("123")) {
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
                String Username;
                Username = username_value.getText().toString();
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                i.putExtra("username", Username);
                startActivity(i);
            }
        });
    }
}