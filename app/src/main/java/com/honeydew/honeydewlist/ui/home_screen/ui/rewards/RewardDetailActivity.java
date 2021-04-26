package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class RewardDetailActivity extends AppCompatActivity {
    private static final String TAG = "DB ERROR";
    private String owner, ownerUUID, itemID, itemName, description, redeemer, redeemerUUID;
    String username, uuid;
    private long melonCost;
    private Boolean redeemedStatus;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        TextView cost_tv, owner_tv, description_tv, description_label_tv, redeemed_tv;
        FirebaseAuth auth;
        FirebaseUser user;

        // Set title
        Intent i = getIntent();
        itemName = i.getStringExtra("name");
        toolBarLayout.setTitle(itemName == null ? getTitle() : itemName);

        // Back button
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Initialize database
        db = FirebaseFirestore.getInstance();

        // Get data from intents
        description = i.getStringExtra("description");
        owner = i.getStringExtra("owner");
        ownerUUID = i.getStringExtra("ownerUUID");
        itemID = i.getStringExtra("itemID");
        melonCost = i.getLongExtra("points", -1);

        redeemerUUID = i.getStringExtra("completionDoerUUID");
        redeemer = i.getStringExtra("completionDoer");
        redeemedStatus = i.getBooleanExtra("completionStatus", false);

        // Find text views
        cost_tv = findViewById(R.id.cost);
        owner_tv = findViewById(R.id.owner);
        description_label_tv = findViewById(R.id.description_label);
        description_tv = findViewById(R.id.description);
        redeemed_tv = findViewById(R.id.redeemed);

        // Set text views
        cost_tv.setText(MessageFormat.format("{0}🍈", melonCost));
        owner_tv.setText(owner);
        description_tv.setText(description);
        if (TextUtils.isEmpty(description)) {
            description_label_tv.setVisibility(View.GONE);
            description_tv.setVisibility(View.GONE);
        }
        if (redeemedStatus) {
            redeemed_tv.setText(String.format("Redeemed by: %s", redeemer));
        } else {
            redeemed_tv.setText(R.string.NotRedeemed);
        }

        // Get current user id and username
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            uuid = user.getUid();
            try {
                db.collection("users").document(uuid).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            username = documentSnapshot.getData().get("username").toString();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(),
                                    "Could not find username from Firestore",
                                    Toast.LENGTH_SHORT).show();
                        });
            } catch (SQLiteDatabaseLockedException e) {
                Log.e(TAG, "onCreateView: Database already in use", e);
            } catch (RuntimeException e) {
                Log.e(TAG, "onCreate: Maybe document field is not the right type?", e);
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Something happened", e);
            }
        }

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