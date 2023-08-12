package com.example.UAS_AKB_IF5_10120191;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Nama : Ahmad Mujahid Ramdhani
// Kelas : IF-5
// Nim : 10120191
public class NoteFragment extends Fragment {

    private View rootView;
    private FloatingActionButton addNoteBtn;
    private RecyclerView recyclerView;
    private ImageButton menuBtn;
    private NoteAdapter noteAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_note, container, false);

        addNoteBtn = rootView.findViewById(R.id.add_note_btn);
        recyclerView = rootView.findViewById(R.id.recyler_view);
        menuBtn = rootView.findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(getActivity(), NoteDetailsActivity.class)));
        menuBtn.setOnClickListener((v) -> showMenu());
        setupRecyclerView();

        return rootView;
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getTitle().equals("Logout")) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                requireActivity().finish();
                return true;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(databaseReference.orderByChild("timestamp"), Note.class)
                .build();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter = new NoteAdapter(options, requireContext());
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
}