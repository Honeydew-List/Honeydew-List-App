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
import com.google.firebase.firestore.FieldValue;
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
    private long currentMelonCount;
    private Snackbar snackBar;
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
                            Map<String, Object> data = documentSnapshot.getData();
                            Object username_obj;
                            if (data != null) {
                                username_obj = data.get("username");
                                if (username_obj != null)
                                    username = username_obj.toString();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),
                                "Could not find username from Firestore",
                                Toast.LENGTH_SHORT).show());
            } catch (SQLiteDatabaseLockedException e) {
                Log.e(TAG, "onCreateView: Database already in use", e);
            } catch (RuntimeException e) {
                Log.e(TAG, "onCreate: Maybe document field is not the right type?", e);
            } catch (Exception e) {
                Log.e(TAG, "onCreateView: Something happened", e);
            }
        }

        try {
            db.collection("users").document(ownerUUID).collection("rewards").document(itemID).addSnapshotListener((value, error) -> {
                if (value != null) {
                    Map<String, Object> data  = value.getData();
                    redeemedStatus = value.getBoolean("redeemed");
                    if (data != null) {
                        Object desc_obj, red_obj, red_uuid_obj;
                        desc_obj = data.get("description");
                        red_obj = data.get("redeemer");
                        red_uuid_obj = data.get("redeemerUUID");

                        if (desc_obj != null) description = desc_obj.toString();
                        if (red_obj != null) redeemer = red_obj.toString();
                        if (red_uuid_obj != null) redeemerUUID = red_uuid_obj.toString();
                    }
                    try {
                        //noinspection ConstantConditions
                        melonCost = value.getLong("points");
                    } catch (RuntimeException e) {
                        Log.e(TAG, "onCreate: RuntimeException", e);
                    }
                    cost_tv.setText(MessageFormat.format("{0}🍈", melonCost));
                    description_tv.setText(description);
                    if (redeemedStatus) {
                        redeemed_tv.setText(String.format("Redeemed by: %s", redeemer));
                    } else {
                        redeemed_tv.setText(R.string.NotRedeemed);
                    }
                }
            });
        } catch (SQLiteDatabaseLockedException e) {
            Log.e(TAG, "onCreateView: Database already in use", e);
        } catch (RuntimeException e) {
            Log.e(TAG, "onCreate: RuntimeException", e);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Something happened", e);
        }

        // Buy button, only works for non owners and if user has enough melons
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Only allow it to be redeemed if it is not redeemed yet
                    if (redeemedStatus) {
                        snackBar = Snackbar.make(
                                findViewById(android.R.id.content),
                                "This reward has alreay been redeemed",
                                Snackbar.LENGTH_SHORT
                        );
                        snackBar.setAction("Dismiss", v12 -> {
                            // Call your action method here
                            snackBar.dismiss();
                        });
                        snackBar.show();
                    } else if (ownerUUID.equals(uuid)) {
                        snackBar = Snackbar.make(
                                findViewById(android.R.id.content),
                                "You cannot redeem your own reward",
                                Snackbar.LENGTH_SHORT
                        );
                        snackBar.setAction("Dismiss", v12 -> {
                            // Call your action method here
                            snackBar.dismiss();
                        });
                        snackBar.show();
                    } else {
                        updateFirestore(username, uuid, melonCost);
                    }
                } catch (SQLiteDatabaseLockedException e) {
                    Log.e(TAG, "onCreateView: Database already in use", e);
                } catch (RuntimeException e) {
                    Log.e(TAG, "onCreate: RuntimeException", e);
                } catch (Exception e) {
                    Log.e(TAG, "onCreateView: Something happened", e);
                }
                // finish();
            }

            private void updateFirestore(String redeemer, String redeemerUUID, long melonCost) {
                Map<String, Object> redeemedMap = new HashMap<String, Object>() {{
                    put("redeemed", true);
                    put("redeemer", redeemer);
                    put("redeemerUUID", redeemerUUID);
                }};
                db.collection("users/" + ownerUUID + "/rewards").
                        document(itemID).update(redeemedMap);

                Map<String, Object> costMap = new HashMap<String, Object>() {{
                    put("melon_count", FieldValue.increment(-1 * melonCost));
                }};
                db.collection("users").
                        document(redeemerUUID).update(costMap);
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