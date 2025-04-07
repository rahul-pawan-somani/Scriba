package com.example.scriba.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for storing NoteEntity objects.
 *
 * This abstract class provides the NoteDao for CRUD operations.
 *
 * @see NoteDao
 */
@Database(entities = [NoteEntity::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    /**
     * Abstract method to get the NoteDao.
     */
    abstract fun noteDao(): NoteDao

    companion object {
        // Volatile variable to ensure the INSTANCE is visible across threads.
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        /**
         * Returns a singleton instance of NoteDatabase.
         *
         * If the database doesn't exist, it creates a new instance in a thread-safe manner.
         *
         * @param context The application context.
         * @return A singleton instance of NoteDatabase.
         */
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                // Build the database with destructive migration fallback.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}