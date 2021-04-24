package com.honeydew.honeydewlist.ui.home_screen.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.honeydew.honeydewlist.R;
import com.honeydew.honeydewlist.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;
    private String userID;
    private String username,melons;
    private TextView userHome,Melons;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Temporary logout button
        Button logout;
        logout = (Button) root.findViewById(R.id.button);

        userHome = (TextView) root.findViewById(R.id.userHome);
        Melons = (TextView) root.findViewById(R.id.melonHome);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            userID = user.getUid();
            db.collection("users").document(userID).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        username = documentSnapshot.getData().get("username").toString();
                        userHome.setText(username);
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(),
                            "Could not find username from Firestore",
                            Toast.LENGTH_SHORT).show());

            db.collection("users").document(userID).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        melons = documentSnapshot.getData().get("melon_count").toString();
                        Melons.setText(getString(R.string.melonText, melons));
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(),
                            "Could not find melon count from Firestore",
                            Toast.LENGTH_SHORT).show());

        }

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
            getActivity().finish();
        });
        return root;
    }
}