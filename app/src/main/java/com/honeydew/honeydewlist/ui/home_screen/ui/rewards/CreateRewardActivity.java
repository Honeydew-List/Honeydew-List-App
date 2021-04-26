package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.database.sqlite.SQLiteDatabaseLockedException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.data.Reward;
import com.honeydew.honeydewlist.data.Task;

import java.util.HashMap;
import java.util.Map;

public class CreateRewardActivity extends AppCompatActivity {
    private static final String TAG = "DB ERROR";
    private FirebaseFirestore db;
    private String userID;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout;
        toolBarLayout = findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        EditText name_value_edt, description_value_edt, reward_value_edt;
        FloatingActionButton fab;

        name_value_edt = findViewById(R.id.text_input_edit_text_reward_name);
        description_value_edt = findViewById(R.id.text_input_edit_text_reward_description);
        reward_value_edt = findViewById(R.id.text_input_edit_text_reward_cost);
        fab = findViewById(R.id.fab);

        // Get current user
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            userID = user.getUid();
            try {
                db.collection("users").document(userID).get()
                        .addOnSuccessListener(documentSnapshot -> username = documentSnapshot.getData().get("username").toString())
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




        fab.setOnClickListener(view -> {
            String RewardName, RewardDescription, MelonCost;

            RewardName = name_value_edt.getText().toString();
            RewardDescription = description_value_edt.getText().toString();
            MelonCost = reward_value_edt.getText().toString();

            int MelonCostInt;

            // validating the text fields if empty or not.
            if (TextUtils.isEmpty(RewardName)) {
                name_value_edt.setError("Please enter Reward Name");
            } else if (TextUtils.isEmpty(MelonCost)) {
                reward_value_edt.setError("Please enter Melon cost");
            } else {
                // calling method to add data to Firebase Firestore.
                try {
                    MelonCostInt = Integer.parseInt(MelonCost);
                    addDataToFirestore(RewardName, RewardDescription, MelonCostInt);
                    finish();
                } catch (NumberFormatException e) {
                    reward_value_edt.setError("Please enter only numbers for the reward");
                } catch (SQLiteDatabaseLockedException e) {
                    Log.e(TAG, "onCreateView: Database already in use", e);
                } catch (RuntimeException e) {
                    Log.e(TAG, "onCreate: Maybe document field is not the right type?", e);
                } catch (Exception e) {
                    Log.e(TAG, "onCreateView: Something happened", e);
                }
            }

        });
    }

    private void addDataToFirestore(String RewardName, String RewardDescription, int MelonCost) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbTasks = db.collection("users/" + userID + "/rewards");



        // adding our data to our courses object class.
        Reward reward = new Reward(RewardName,
                RewardDescription,
                username,
                userID,
                (long) MelonCost,
                false, "", "", "");

        // below method is use to add data to Firebase Firestore.
        dbTasks.add(reward).addOnSuccessListener(documentReference -> {
            // after the data addition is successful
            // we are displaying a success toast message.
            Map<String, Object> itemID = new HashMap<String, Object>() {{
                put("itemID", documentReference.getId());
            }};
            documentReference.update(itemID);
            Toast.makeText(getApplicationContext(),
                    "Your Task has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // this method is called when the data addition process is failed.
            // displaying a toast message when data addition is failed.
            Toast.makeText(getApplicationContext(),
                    "Fail to add course \n" + e,
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (db != null) {
            db.terminate();
        }
    }
}
