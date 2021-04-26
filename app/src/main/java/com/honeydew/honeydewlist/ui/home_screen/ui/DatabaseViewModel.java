package com.honeydew.honeydewlist.ui.home_screen.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseViewModel extends ViewModel {
    private final MutableLiveData<FirebaseFirestore> db;

    public DatabaseViewModel() {
        db = new MutableLiveData<>();
    }

    public FirebaseFirestore getDb() {
        return db.getValue();
    }

    public void initializeDb() {
        if (db.getValue() != null)
            db.getValue().terminate();
        db.setValue(FirebaseFirestore.getInstance());
    }
}
