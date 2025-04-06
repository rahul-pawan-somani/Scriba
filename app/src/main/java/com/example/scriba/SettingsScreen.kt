package com.example.scriba

import com.example.scriba.data.PreferencesManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBack: () -> Unit,
    onClearNotes: () -> Unit // Callback for clearing all notes
) {
    // Collect stored preferences as state
    val darkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)
    val storedUserName by preferencesManager.userNameFlow.collectAsState(initial = "")
    val storedUserEmail by preferencesManager.userEmailFlow.collectAsState(initial = "")

    // Local state for editing, initialize with stored values
    var userName by remember { mutableStateOf(storedUserName) }
    var userEmail by remember { mutableStateOf(storedUserEmail) }
    // Local state for email validation error
    var emailError by remember { mutableStateOf("") }

    // Update local state when the stored values change
    LaunchedEffect(storedUserName) { userName = storedUserName }
    LaunchedEffect(storedUserEmail) { userEmail = storedUserEmail }

    // Define a simple email regex for validation
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    val scope = rememberCoroutineScope()
    // Create a SnackbarHostState to display feedback messages
    val snackbarHostState = remember { SnackbarHostState() }
    // State to control display of the delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // === Appearance Section ===
            Text(text = "Appearance", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dark Mode")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = darkMode,
                    onCheckedChange = { checked ->
                        scope.launch {
                            preferencesManager.setDarkMode(checked)
                        }
                    }
                )
            }
            HorizontalDivider()

            // === Profile Section ===
            Text(text = "Profile", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = userEmail,
                onValueChange = { newValue ->
                    userEmail = newValue
                    emailError = if (newValue.isNotEmpty() && !emailRegex.matches(newValue)) {
                        "Invalid email address"
                    } else ""
                },
                label = { Text("Email Address") },
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // Save button placed in the Profile section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            preferencesManager.setUserName(userName)
                            preferencesManager.setUserEmail(userEmail)
                            snackbarHostState.showSnackbar("Settings saved")
                        }
                    },
                    enabled = emailError.isEmpty()
                ) {
                    Text("Save")
                }
            }
            HorizontalDivider()

            // === Actions Section ===
            Text(text = "Actions", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { showDeleteDialog = true }
                ) {
                    Text("Clear All Notes")
                }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            preferencesManager.setDarkMode(false)
                            preferencesManager.setUserName("")
                            preferencesManager.setUserEmail("")
                        }
                        userName = ""
                        userEmail = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Settings reset to defaults")
                        }
                    }
                ) {
                    Text("Reset")
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirm Delete") },
                text = {
                    Column {
                        Text("(This action cannot be undone)", fontWeight = FontWeight.Bold)
                        Text(
                            text = buildAnnotatedString {
                                append("Are you sure you want to")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(" DELETE ")
                                }
                                append("your all notes?")
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                onClearNotes()
                                snackbarHostState.showSnackbar("All notes cleared")
                            }
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
