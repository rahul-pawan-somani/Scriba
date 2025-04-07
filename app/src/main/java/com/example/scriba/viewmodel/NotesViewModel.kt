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
import android.util.Log
import androidx.lifecycle.MutableLiveData


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

    // LiveData to expose error messages.
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /**
     * Adds a new note with the given title and content.
     *
     * @param title The title of the new note.
     * @param content The content of the new note.
     */
    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                // Insert a new note into the repository.
                val result = repository.insert(NoteEntity(title = title, content = content))
                if (result == -1L) {
                    _errorMessage.value = "Failed to add note."
                }
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Error adding note", e)
                _errorMessage.value = "Error adding note: ${e.message}"
            }
        }
    }


    /**
     * Updates an existing note.
     *
     * @param note The note with updated data.
     */
    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            try {
                repository.updateNote(note)
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Error updating note", e)
                _errorMessage.value = "Error updating note: ${e.message}"
            }
        }
    }

    /**
     * Deletes a specific note.
     *
     * @param note The note to be deleted.
     */
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            try {
                val rowsDeleted = repository.delete(note)
                if (rowsDeleted == 0) {
                    _errorMessage.value = "Failed to delete note."
                }
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Error deleting note", e)
                _errorMessage.value = "Error deleting note: ${e.message}"
            }
        }
    }

    /**
     * Clears all notes from the database.
     */
    fun clearAllNotes() {
        viewModelScope.launch {
            try {
                repository.clearAllNotes()
            } catch (e: Exception) {
                Log.e("NotesViewModel", "Error clearing notes", e)
                _errorMessage.value = "Error clearing notes: ${e.message}"
            }
        }
    }

    /**
     * Clears the current error message.
     */
    fun clearError() {
        _errorMessage.value = null
    }
}