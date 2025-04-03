package com.example.scriba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Define your Note data model here (or move this to a separate file if you prefer)
data class Note(
    val id: Int,
    val title: String,
    val content: String
)

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    // Use MutableLiveData to hold the list of notes
    private val _notes = MutableLiveData<List<Note>>(listOf(
        Note(1, "Meeting Notes", "Discuss project timeline and goals."),
        Note(2, "Shopping List", "Milk, Eggs, Bread, Coffee."),
        Note(3, "Ideas", "Note-taking app with voice input and tagging.")
    ))
    // Expose the notes as LiveData
    val notes: LiveData<List<Note>> = _notes

    fun addNote(title: String, content: String) {
        val currentList = _notes.value.orEmpty()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        _notes.value = currentList + Note(newId, title, content)
    }
}
