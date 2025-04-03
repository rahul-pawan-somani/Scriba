package com.example.scriba.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Delete
    suspend fun delete(note: NoteEntity): Int
}

