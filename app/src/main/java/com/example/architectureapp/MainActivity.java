package com.example.architectureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureapp.adapter.NoteAdapter;
import com.example.architectureapp.database.Note;
import com.example.architectureapp.databinding.ActivityMainBinding;
import com.example.architectureapp.viewModel.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_REQUEST_CODE = 1;

    private ActivityMainBinding binding;
    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // BINDING + VIEWMODEL
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // THIS IS EQUAL
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        binding.setNoteViewModel(mNoteViewModel);
        binding.setLifecycleOwner(this);

        // RECYCLERVIEW + ADAPTER
        final NoteAdapter adapter = new NoteAdapter();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        binding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
            }
        });

        // OBSERVERS
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Note note = new Note(
                    bundle.getString(AddNoteActivity.EXTRA_TITLE),
                    bundle.getString(AddNoteActivity.EXTRA_DESCRIPTION),
                    bundle.getInt(AddNoteActivity.EXTRA_PRIORITY)
            );
            mNoteViewModel.insert(note);
            Snackbar.make(binding.getRoot(), "Note saved", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(binding.getRoot(), "Note has not been saved", Snackbar.LENGTH_SHORT).show();
        }
    }
}