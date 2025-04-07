package com.example.scriba.data

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
    suspend fun insert(note: NoteEntity): Long = noteDao.insert(note)

    /**
     * Deletes a specific note from the database.
     *
     * @param note The note to be deleted.
     * @return The number of rows deleted.
     */
    suspend fun delete(note: NoteEntity): Int = noteDao.delete(note)

    /**
     * Updates an existing note in the database.
     *
     * @param note The note with updated information.
     */
    suspend fun updateNote(note: NoteEntity) = noteDao.update(note)

    /**
     * Deletes all notes from the database.
     *
     * @return The number of rows deleted.
     */
    suspend fun clearAllNotes() = noteDao.deleteAllNotes()
}