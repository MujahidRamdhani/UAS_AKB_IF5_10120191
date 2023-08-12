package com.example.UAS_AKB_IF5_10120191;


import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

// Nama : Ahmad Mujahid Ramdhani
// Kelas : IF-5
// Nim : 10120191
public class Utility {
    static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static DatabaseReference getDatabaseReferenceForNotes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("notes").child(currentUser.getUid()).child("my_notes");
    }

    static String timestampToString(long timestamp) {
        Date date = new Date(timestamp);
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

}
