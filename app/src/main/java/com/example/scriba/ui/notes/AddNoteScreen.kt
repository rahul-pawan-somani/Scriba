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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.automirrored.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    onSave: (NoteEntity) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
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
                },
                actions = {
                    IconButton(onClick = {
                        val tempNote = NoteEntity(
                            id = 0,
                            title = title,
                            content = content,
                            isPinned = false
                        )
                        shareNote(context, tempNote)
                    }) {
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
