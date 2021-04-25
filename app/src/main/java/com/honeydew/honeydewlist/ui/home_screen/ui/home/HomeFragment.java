package com.honeydew.honeydewlist.ui.home_screen.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;
    private String username,melons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        // Temporary logout button
        Button logout;
        logout = root.findViewById(R.id.button);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        userID = user.getUid();
        inflater.inflate(R.menu.home_menu, menu);

        db.collection("users").document(userID).get()
                .addOnSuccessListener(documentSnapshot -> {
                    username = documentSnapshot.getData().get("username").toString();
                    menu.findItem(R.id.menuUsername).setTitle(getString(R.string.menuUser, username));
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(),
                        "Could not find username from Firestore",
                        Toast.LENGTH_SHORT).show());

        db.collection("users").document(userID).addSnapshotListener((value, error) -> {
            melons = value.getData().get("melon_count").toString();
            menu.findItem(R.id.melon_stats).setTitle(getString(R.string.melonText, melons));
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