package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;

import java.util.HashMap;
import java.util.Map;

public class RewardDetailActivity extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        toolBarLayout.setTitle(intent.getStringExtra("name"));

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        db = FirebaseFirestore.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // updateFirestore();
                finish();
            }

            private void updateFirestore() {
                Map<String, Object> stringObjectMap = new HashMap<String, Object>() {{
                    put("redeemed", true);
                }};
                db.collection("users/" + intent.getStringExtra("ownerUUID") + "/rewards").
                        document(intent.getStringExtra("itemID")).update(stringObjectMap);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) {
            db.terminate();
        }
    }
}