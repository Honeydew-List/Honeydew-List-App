package com.honeydew.honeydewlist.ui.home_screen.ui.options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.honeydew.honeydewlist.R;

public class OptionsFragment extends Fragment {

    private OptionsViewModel optionsViewModel;

    public OptionsFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionsViewModel =
                new ViewModelProvider(this).get(OptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_options, container, false);
        final TextView textView = root.findViewById(R.id.text_options);
        optionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}