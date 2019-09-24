package com.example.architectureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private static final int EDIT_NOTE_REQUEST_CODE = 2;

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
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        binding.buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
            }
        });

        // OBSERVERS
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        // SWIPE TO DELETE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mNoteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Snackbar.make(binding.getRoot(), "Note deleted", Snackbar.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        // RECYCLERVIEW ITEM CLICK
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());

                startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Note note = new Note(
                    bundle.getString(AddEditNoteActivity.EXTRA_TITLE),
                    bundle.getString(AddEditNoteActivity.EXTRA_DESCRIPTION),
                    bundle.getInt(AddEditNoteActivity.EXTRA_PRIORITY)
            );
            mNoteViewModel.insert(note);
            Snackbar.make(binding.getRoot(), "Note saved", Snackbar.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int id = bundle.getInt(AddEditNoteActivity.EXTRA_ID);
            if (id == -1) {
                Snackbar.make(binding.getRoot(), "Note can't be updated", Snackbar.LENGTH_SHORT).show();
                return;
            }
            Note note = new Note(
                    bundle.getString(AddEditNoteActivity.EXTRA_TITLE),
                    bundle.getString(AddEditNoteActivity.EXTRA_DESCRIPTION),
                    bundle.getInt(AddEditNoteActivity.EXTRA_PRIORITY)
            );
            note.setId(bundle.getInt(AddEditNoteActivity.EXTRA_ID));
            mNoteViewModel.update(note);
            Snackbar.make(binding.getRoot(), "Note has been updated", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(binding.getRoot(), "Note has not been saved", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            deleteAllNotes();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        mNoteViewModel.deleteAllNotes();
        Snackbar.make(binding.getRoot(), "All notes deleted", Snackbar.LENGTH_SHORT).show();
    }
}