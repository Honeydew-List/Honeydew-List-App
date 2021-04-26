package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;

import java.util.HashMap;
import java.util.Map;

public class RewardDetailActivity extends AppCompatActivity {
    private static final String TAG = "DB ERROR";
    private String owner, ownerUUID, itemID, itemName, description, redeemer, redeemerUUID;
    private long points;
    private Boolean redeemed;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        // Set title
        Intent i = getIntent();
        itemName = i.getStringExtra("name");
        toolBarLayout.setTitle(itemName == null ? getTitle() : itemName);

        // Back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Initialize database
        db = FirebaseFirestore.getInstance();

        // Buy button, only works for non owners and if user has enough melons
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // updateFirestore();
                } catch (SQLiteDatabaseLockedException e) {
                    Log.e(TAG, "onCreateView: Database already in use", e);
                } catch (RuntimeException e) {
                    Log.e(TAG, "onCreate: RuntimeException", e);
                } catch (Exception e) {
                    Log.e(TAG, "onCreateView: Something happened", e);
                }
                finish();
            }

            private void updateFirestore() {
                Map<String, Object> stringObjectMap = new HashMap<String, Object>() {{
                    put("redeemed", true);
                }};
                db.collection("users/" + i.getStringExtra("ownerUUID") + "/rewards").
                        document(i.getStringExtra("itemID")).update(stringObjectMap);
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