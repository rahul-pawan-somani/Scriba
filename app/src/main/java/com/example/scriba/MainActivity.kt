package com.example.scriba

import com.example.scriba.viewmodel.NotesViewModel
import com.example.scriba.viewmodel.Note  // Import the Note model from your viewmodel package
import com.example.scriba.ui.theme.ScribaTheme


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*

const val ROUTE_NOTES = "note_list"
const val ROUTE_ADD = "add_note"

// Sample notes for preview purposes
val sampleNotes = listOf(
    Note(1, "Meeting Notes", "Discuss project timeline and goals."),
    Note(2, "Shopping List", "Milk, Eggs, Bread, Coffee."),
    Note(3, "Ideas", "Note-taking app with voice input and tagging.")
)

class MainActivity : ComponentActivity() {
    // Use the ViewModel from the viewmodel package
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScribaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = ROUTE_NOTES) {
                    composable(ROUTE_NOTES) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            floatingActionButton = {
                                FloatingActionButton(onClick = {
                                    navController.navigate(ROUTE_ADD)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add Note"
                                    )
                                }
                            }
                        ) { innerPadding ->
                            // Observe the LiveData from your ViewModel.
                            val notes by viewModel.notes.observeAsState(initial = emptyList())
                            NoteList(notes = notes, modifier = Modifier.padding(innerPadding))
                        }
                    }
                    composable(ROUTE_ADD) {
                        AddNoteScreen(
                            onSave = { note ->
                                viewModel.addNote(note.title, note.content)
                                navController.popBackStack()
                            },
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NoteList(notes: List<Note>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(notes) { note ->
            NoteCard(note)
        }
    }
}

@Composable
fun NoteCard(note: Note) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(onSave: (Note) -> Unit, onCancel: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Note") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Button(onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        // Create a new note. ID will be assigned in the ViewModel.
                        val newNote = Note(id = 0, title = title, content = content)
                        onSave(newNote)
                    }
                }) {
                    Text("Save")
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    ScribaTheme {
        NoteList(notes = sampleNotes)
    }
}
