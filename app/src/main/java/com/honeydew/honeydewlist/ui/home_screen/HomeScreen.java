package com.honeydew.honeydewlist.ui.home_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.ui.home_screen.ui.home.HomeFragment;
import com.honeydew.honeydewlist.ui.home_screen.ui.options.OptionsFragment;
import com.honeydew.honeydewlist.ui.home_screen.ui.rewards.RewardsFragment;
import com.honeydew.honeydewlist.ui.home_screen.ui.tasks.TasksFragment;

public class HomeScreen extends AppCompatActivity {
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new TasksFragment();
        final Fragment fragment3 = new RewardsFragment();
        final Fragment fragment4 = new OptionsFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        int itemId = item.getItemId();
                        if (itemId == R.id.navigation_home) {
                            fragment = fragment1;
                        } else if (itemId == R.id.navigation_tasks) {
                            fragment = fragment2;
                        } else if (itemId == R.id.navigation_rewards) {
                            fragment = fragment3;
                        } else {
                            fragment = fragment4;
                        }
                        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onBackPressed() {
        // Press back button twice to exit app
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

}