package com.honeydew.honeydewlist.ui.home_screen.ui.rewards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.honeydew.honeydewlist.R;

public class RewardsFragment extends Fragment {

    private RewardsViewModel rewardsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rewardsViewModel =
                new ViewModelProvider(this).get(RewardsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rewards, container, false);
        setHasOptionsMenu(true);
        final TextView textView = root.findViewById(R.id.text_rewards);
        rewardsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_item) {
            // navigate to add task screen
            Toast.makeText(
                    getContext(),
                    "Not yet implemented",
                    Toast.LENGTH_SHORT
            ).show();
            return true;
        } else if (itemId == R.id.action_filter) {
            // navigate to screen to choose which friends to show tasks from
            Toast.makeText(
                    getContext(),
                    "Not yet implemented",
                    Toast.LENGTH_SHORT
            ).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}