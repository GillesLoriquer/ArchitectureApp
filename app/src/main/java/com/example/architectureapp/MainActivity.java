package com.example.architectureapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.architectureapp.database.Note;
import com.example.architectureapp.databinding.ActivityMainBinding;
import com.example.architectureapp.viewModel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // THIS IS EQUAL
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        binding.setNoteViewModel(mNoteViewModel);
        binding.setLifecycleOwner(this);

        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
                binding.textview.setText("Liste de notes : " + mNoteViewModel.getAllNotes().getValue().size());
            }
        });
    }
}