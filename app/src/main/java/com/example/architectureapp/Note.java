package com.example.architectureapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "priority")
    private String priority;

    /** CONSTRUCTOR */
    public Note(String title, String description, String priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    /** SETTERS */
    public void setId(int id) {
        this.id = id;
    }

    /** GETTERS */
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}
