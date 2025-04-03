package com.example.scriba.data

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }

    suspend fun delete(note: NoteEntity) {
        noteDao.delete(note)
    }
}
