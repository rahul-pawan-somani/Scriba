package com.example.scriba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scriba.data.NoteDatabase
import com.example.scriba.data.NoteEntity
import com.example.scriba.data.NoteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for managing note data.
 *
 * Provides functions to add, update, delete, and clear notes,
 * and exposes a LiveData stream of note lists.
 *
 * @param application The Application context.
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {
    // Initialize the DAO and Repository
    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    private val repository = NoteRepository(noteDao)

    // LiveData stream of notes from the repository.
    val notes: LiveData<List<NoteEntity>> = repository.allNotes.asLiveData()

    /**
     * Adds a new note with the given title and content.
     *
     * @param title The title of the new note.
     * @param content The content of the new note.
     */
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            // Insert a new note into the repository.
            repository.insert(NoteEntity(title = title, content = content))
        }
    }

    /**
     * Updates an existing note.
     *
     * @param note The note with updated data.
     */
    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    /**
     * Deletes a specific note.
     *
     * @param note The note to be deleted.
     */
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    /**
     * Clears all notes from the database.
     */
    fun clearAllNotes() {
        viewModelScope.launch {
            repository.clearAllNotes()
        }
    }
}
