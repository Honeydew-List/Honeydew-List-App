package com.honeydew.honeydewlist.ui.home_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.honeydew.honeydewlist.R;

public class HomeScreen extends AppCompatActivity {
    String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Intent intent = getIntent();
        Username = intent.getStringExtra("username");

        TextView textView = (TextView) findViewById(R.id.welcome);
        textView.setText(Username);
    }
}