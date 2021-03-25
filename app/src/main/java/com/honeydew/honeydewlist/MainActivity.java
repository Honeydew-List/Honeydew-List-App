package com.honeydew.honeydewlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EditText username_value, password_value;
        Button login;

        username_value = (EditText) findViewById(R.id.username);
        password_value = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginButton);

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

                }
            }
        });

    }
}