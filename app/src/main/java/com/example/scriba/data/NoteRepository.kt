package com.example.scriba.data

import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun insert(note: NoteEntity): Long = noteDao.insert(note)
    suspend fun delete(note: NoteEntity): Int = noteDao.delete(note)
    suspend fun clearAllNotes() {
        noteDao.deleteAllNotes()
    }
}
