package com.example.scriba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the notes database.
 *
 * Provides methods to interact with the "notes" table.
 */
@Dao
interface NoteDao {

    /**
     * Retrieves all notes from the database.
     *
     * @return A Flow emitting the list of all notes.
     */
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    /**
     * Inserts a new note into the database.
     *
     * @param note The note to insert.
     * @return The row ID of the newly inserted note.
     */
    @Insert
    suspend fun insert(note: NoteEntity): Long

    /**
     * Deletes all notes from the database.
     *
     * @return The number of rows deleted.
     */
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes(): Int

    /**
     * Updates an existing note in the database.
     *
     * @param note The note with updated information.
     */
    @Update
    suspend fun update(note: NoteEntity)

    /**
     * Deletes a specific note from the database.
     *
     * @param note The note to delete.
     * @return The number of rows deleted.
     */
    @Delete
    suspend fun delete(note: NoteEntity): Int
}