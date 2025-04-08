package com.example.scriba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scriba.data.PreferencesManager
import com.example.scriba.ui.theme.ScribaTheme
import com.example.scriba.viewmodel.NotesViewModel
import com.example.scriba.ui.notes.NotesListScreen
import com.example.scriba.ui.notes.AddNoteScreen
import com.example.scriba.ui.notes.EditNoteScreen
import com.example.scriba.ui.notes.SettingsScreen


const val ROUTE_NOTES = "note_list"
const val ROUTE_ADD = "add_note"

class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencesManager = PreferencesManager(applicationContext)

        setContent {
            val darkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)
            ScribaTheme(darkTheme = darkMode) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = ROUTE_NOTES) {
                    // Note listing screen
                    composable(ROUTE_NOTES) {
                        NotesListScreen(
                            viewModel = viewModel,
                            navController = navController,
                            preferencesManager = preferencesManager
                        )
                    }
                    // Add note screen
                    composable(ROUTE_ADD) {
                        AddNoteScreen(
                            onSave = { note ->
                                viewModel.addNote(note.title, note.content)
                                navController.popBackStack()
                            },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                    // Edit note screen (retrieves noteId and passes the matching note)
                    composable("edit_note/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: -1
                        val notesList = viewModel.notes.value ?: emptyList()
                        val noteToEdit = notesList.find { it.id == noteId }
                        if (noteToEdit != null) {
                            EditNoteScreen(
                                currentNote = noteToEdit,
                                onSave = { updatedNote ->
                                    viewModel.updateNote(updatedNote)
                                    navController.popBackStack()
                                },
                                onDelete = { note ->
                                    viewModel.deleteNote(note)
                                    navController.popBackStack()
                                }
                            )
                        } else {
                            // Here you could show a loading indicator or error message
                        }
                    }
                    // Settings screen
                    composable("settings") {
                        SettingsScreen(
                            preferencesManager = preferencesManager,
                            onBack = { navController.popBackStack() },
                            onClearNotes = { viewModel.clearAllNotes() }
                        )
                    }
                }
            }
        }
    }
}
