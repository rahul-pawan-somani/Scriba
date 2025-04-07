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


class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    private val repository = NoteRepository(noteDao)

    val notes: LiveData<List<NoteEntity>> = repository.allNotes.asLiveData()

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insert(NoteEntity(title = title, content = content))
        }
    }

    fun updateNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun clearAllNotes() {
        viewModelScope.launch {
            repository.clearAllNotes()
        }
    }
}