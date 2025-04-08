package com.example.scriba.ui.notes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import com.example.scriba.data.NoteEntity
import com.example.scriba.ui.components.AutoNoteForm
import com.example.scriba.ui.components.shareNote
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    currentNote: NoteEntity,
    onSave: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit
) {
    var title by remember { mutableStateOf(currentNote.title) }
    var content by remember { mutableStateOf(currentNote.content) }
    val context = LocalContext.current

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
                },
                actions = {
                    IconButton(onClick = { shareNote(context, currentNote) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Note"
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
