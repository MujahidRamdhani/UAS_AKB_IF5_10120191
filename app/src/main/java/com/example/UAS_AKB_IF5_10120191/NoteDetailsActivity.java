package com.example.UAS_AKB_IF5_10120191;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Nama : Ahmad Mujahid Ramdhani
// Kelas : IF-5
// Nim : 10120191
public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title, content, docId;
    boolean isEditMode = false;
    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode) {
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener(v -> saveNote());
        deleteNoteTextViewBtn.setOnClickListener(v -> deleteNoteFromFirebase());
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(System.currentTimeMillis()); // Use System.currentTimeMillis() for timestamp

        saveNoteToFirebase(note);
    }

    void saveNoteToFirebase(Note note) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        if (isEditMode) {
            databaseReference.child(docId).setValue(note)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Utility.showToast(NoteDetailsActivity.this, "Note updated successfully");
                            startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
                            finish();
                        } else {
                            Utility.showToast(NoteDetailsActivity.this, "Failed while updating note");
                        }
                    });
        } else {
            databaseReference.push().setValue(note)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                            startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
                            finish();
                        } else {
                            Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                        }
                    });
        }
    }

        void deleteNoteFromFirebase() {

      DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
            databaseReference.child(docId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            });
        }
}
