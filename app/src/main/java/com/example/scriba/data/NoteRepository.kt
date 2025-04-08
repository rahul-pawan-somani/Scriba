package com.example.scriba.data

import android.util.Log
import kotlinx.coroutines.flow.Flow


/**
 * Repository class that abstracts access to note data.
 *
 * This class mediates between the data source (NoteDao) and the rest of the application.
 *
 * @property noteDao Data Access Object for note operations.
 */
class NoteRepository(private val noteDao: NoteDao) {

    /**
     * Flow of all notes from the database.
     */
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to be inserted.
     * @return The new row ID of the inserted note.
     */
    suspend fun insert(note: NoteEntity): Long {
        return try {
            noteDao.insert(note)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error inserting note", e)
            -1L
        }
    }

    /**
     * Deletes a specific note from the database.
     *
     * @param note The note to be deleted.
     * @return The number of rows deleted.
     */
    suspend fun delete(note: NoteEntity): Int {
        return try {
            noteDao.delete(note)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error deleting note", e)
            0
        }
    }

    /**
     * Updates an existing note in the database.
     *
     * @param note The note with updated information.
     */
    suspend fun updateNote(note: NoteEntity) {
        try {
            noteDao.update(note)
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error updating note", e)
        }
    }

    /**
     * Deletes all notes from the database.
     */
    suspend fun clearAllNotes() {
        try {
            noteDao.deleteAllNotes()
        } catch (e: Exception) {
            Log.e("NoteRepository", "Error clearing notes", e)
        }
    }
}
