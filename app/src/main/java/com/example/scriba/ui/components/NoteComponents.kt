package com.example.scriba.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import com.example.scriba.data.NoteEntity


@Composable
fun NotesDisplay(
    notes: List<NoteEntity>,
    isGrid: Boolean,
    onNoteClick: (NoteEntity) -> Unit,
    onPinToggle: (NoteEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isGrid) {
        NoteGrid(notes, onNoteClick, onPinToggle, modifier)
    } else {
        NoteList(notes, onNoteClick, onPinToggle, modifier)
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
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = { Text("Content", style = MaterialTheme.typography.bodyLarge) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE
        )
    }
}

fun shareNote(context: Context, note: NoteEntity) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this note:\n\nTitle: ${note.title}\nContent: ${note.content}"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Note via")
    context.startActivity(shareIntent)
}
