package com.example.scriba

import com.example.scriba.viewmodel.NotesViewModel
import com.example.scriba.data.NoteEntity
import com.example.scriba.data.PreferencesManager
import com.example.scriba.ui.theme.ScribaTheme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*


const val ROUTE_NOTES = "note_list"
const val ROUTE_ADD = "add_note"

// Sample notes for preview purposes (NoteEntity includes an isPinned field)
val sampleNotes = listOf(
    NoteEntity(1, "Meeting Notes", "Discuss project timeline and goals.", isPinned = false),
    NoteEntity(2, "Shopping List", "Milk, Eggs, Bread, Coffee.", isPinned = true),
    NoteEntity(3, "Ideas", "Note-taking app with voice input and tagging.", isPinned = false)
)

class MainActivity : ComponentActivity() {
    private val viewModel: NotesViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencesManager = PreferencesManager(applicationContext)

        setContent {
            val darkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)
            ScribaTheme(darkTheme = darkMode) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = ROUTE_NOTES) {
                    composable(ROUTE_NOTES) {
                        val notes by viewModel.notes.observeAsState(initial = emptyList())
                        val isGrid by preferencesManager.viewModeFlow.collectAsState(initial = true)
                        var query by remember { mutableStateOf("") }
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
                                FloatingActionButton(onClick = { navController.navigate(ROUTE_ADD) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                                }
                            }
                        ) { innerPadding ->
                            Column(
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .fillMaxSize()
                            ) {
                                SearchBar(query = query, onQueryChange = { query = it }, modifier = Modifier.fillMaxWidth())
                                Spacer(modifier = Modifier.height(8.dp))
                                NotesDisplay(
                                    notes = sortedNotes,
                                    isGrid = isGrid,
                                    onNoteClick = { note -> navController.navigate("edit_note/${note.id}") },
                                    onPinToggle = { note -> viewModel.updateNote(note.copy(isPinned = !note.isPinned)) }
                                )
                            }
                        }
                    }
                    composable(ROUTE_ADD) {
                        AddNoteScreen(
                            onSave = { note ->
                                viewModel.addNote(note.title, note.content)
                                navController.popBackStack()
                            },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                    composable("edit_note/{noteId}") { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")?.toIntOrNull() ?: -1
                        val notesList by viewModel.notes.observeAsState(initial = emptyList())
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
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
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

@Composable
fun NotesDisplay(
    notes: List<NoteEntity>,
    isGrid: Boolean,
    onNoteClick: (NoteEntity) -> Unit,
    onPinToggle: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isGrid) {
        NoteGrid(notes = notes, onNoteClick = onNoteClick, onPinToggle = onPinToggle, modifier = modifier)
    } else {
        NoteList(notes = notes, onNoteClick = onNoteClick, onPinToggle = onPinToggle, modifier = modifier)
    }
}

@Composable
fun NoteGrid(
    notes: List<NoteEntity>,
    onNoteClick: (NoteEntity) -> Unit,
    onPinToggle: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(notes) { note ->
            NoteCard(note = note, onClick = { onNoteClick(note) }, onPinToggle = onPinToggle)
        }
    }
}

@Composable
fun NoteList(
    notes: List<NoteEntity>,
    onNoteClick: (NoteEntity) -> Unit,
    onPinToggle: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(notes) { note ->
            NoteCard(note = note, onClick = { onNoteClick(note) }, onPinToggle = onPinToggle)
        }
    }
}

@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onPinToggle: (NoteEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = { onPinToggle(note) }) {
                    Icon(
                        imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = if (note.isPinned) "Unpin note" else "Pin note"
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search notes...") },
        singleLine = true,
        modifier = modifier
    )
}

@Composable
fun AutoNoteForm(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = { Text("Title", style = MaterialTheme.typography.headlineMedium) },
            textStyle = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary
            )
        )
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = { Text("Content", style = MaterialTheme.typography.bodyLarge) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onSave: (NoteEntity) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (title.isNotBlank() || content.isNotBlank()) {
                            val newNote = NoteEntity(
                                id = 0,
                                title = title,
                                content = content,
                                isPinned = false
                            )
                            onSave(newNote)
                        } else {
                            onCancel()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AutoNoteForm(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    currentNote: NoteEntity,
    onSave: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit
) {
    var title by remember { mutableStateOf(currentNote.title) }
    var content by remember { mutableStateOf(currentNote.content) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (title.isBlank() && content.isBlank()) {
                            onDelete(currentNote)
                        } else {
                            onSave(currentNote.copy(title = title, content = content))
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AutoNoteForm(
            title = title,
            content = content,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteListPreview() {
    ScribaTheme {
        NoteGrid(notes = sampleNotes, onNoteClick = {}, onPinToggle = {})
    }
}
