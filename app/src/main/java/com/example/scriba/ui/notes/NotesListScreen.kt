package com.example.scriba.ui.notes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scriba.data.NoteEntity
import com.example.scriba.data.PreferencesManager
import com.example.scriba.ui.components.SearchBar
import com.example.scriba.ui.components.NotesDisplay
import com.example.scriba.viewmodel.NotesViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Add


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    navController: NavController,
    preferencesManager: PreferencesManager
) {
    val notes by viewModel.notes.observeAsState(initial = emptyList())
    val isGrid by preferencesManager.viewModeFlow.collectAsState(initial = true)
    var query by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show a Snackbar if an error occurs
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    // Filter and sort the notes
    val sortedNotes = notes.filter {
        it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true)
    }.sortedByDescending { it.isPinned }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Scriba") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_note") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            NotesDisplay(
                notes = sortedNotes,
                isGrid = isGrid,
                onNoteClick = { note -> navController.navigate("edit_note/${note.id}") },
                onPinToggle = { note ->
                    viewModel.updateNote(note.copy(isPinned = !note.isPinned))
                }
            )
        }
    }
}
