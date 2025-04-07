package com.example.scriba.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a note.
 *
 * This entity is used with Room to store note details in the "notes" table.
 *
 * @property id Auto-generated primary key for the note.
 * @property title The title of the note.
 * @property content The content/body of the note.
 * @property isPinned Boolean flag indicating if the note is pinned.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Unique identifier, auto-generated.
    val title: String, // Title of the note.
    val content: String, // Main content of the note.
    val isPinned: Boolean = false // Indicates if the note is pinned.
)