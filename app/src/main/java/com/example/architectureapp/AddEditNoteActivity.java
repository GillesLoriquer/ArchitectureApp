package com.example.architectureapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.architectureapp.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.architectureapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.architectureapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.architectureapp.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Affiche le bouton back dans l'action bar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        Intent intent = getIntent();
        if (intent.hasExtra(AddEditNoteActivity.EXTRA_ID)) {
            // Set le titre dans l'action bar
            setTitle("Edit note");

            editTextTitle.setText(intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, -1));
        } else {
            // Set le titre dans l'action bar
            setTitle("Save note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please fill title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESCRIPTION, description);
        intent.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
        if (id != -1) intent.putExtra(EXTRA_ID, id);

        setResult(RESULT_OK, intent);
        finish();
    }
}
