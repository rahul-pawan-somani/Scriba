package com.example.scriba.ui.notes

import com.example.scriba.data.PreferencesManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


/**
 * Composable that displays the settings screen.
 *
 * This screen allows the user to adjust appearance settings, update profile details,
 * and perform actions such as clearing all notes.
 *
 * @param preferencesManager Manager for reading and writing user preferences.
 * @param onBack Callback to navigate back from the settings screen.
 * @param onClearNotes Callback for clearing all notes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBack: () -> Unit,
    onClearNotes: () -> Unit // Callback for clearing all notes
) {
    // Collect stored preferences as state.
    val darkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)
    val storedUserName by preferencesManager.userNameFlow.collectAsState(initial = "")
    val storedUserEmail by preferencesManager.userEmailFlow.collectAsState(initial = "")
    val storedIsGrid by preferencesManager.viewModeFlow.collectAsState(initial = true)

    // Local state for editing profile and view mode.
    var userName by remember { mutableStateOf(storedUserName) }
    var userEmail by remember { mutableStateOf(storedUserEmail) }
    var isGrid by remember { mutableStateOf(storedIsGrid) }
    var emailError by remember { mutableStateOf("") }

    // Update local state when stored values change.
    LaunchedEffect(storedUserName) { userName = storedUserName }
    LaunchedEffect(storedUserEmail) { userEmail = storedUserEmail }
    LaunchedEffect(storedIsGrid) { isGrid = storedIsGrid }

    // Define a regular expression for validating email addresses.
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    // Remember a coroutine scope and snackbar host state for asynchronous tasks.
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // TopAppBar with title and navigation icon.
            CenterAlignedTopAppBar(
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
        // Main content using a LazyColumn for vertical scrolling.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            item {
                Text(text = "Appearance", style = MaterialTheme.typography.titleMedium)
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Dark Mode")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { checked ->
                            // Update dark mode preference.
                            scope.launch { preferencesManager.setDarkMode(checked) }
                        }
                    )
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "View Mode")
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        RadioButton(
                            selected = isGrid,
                            onClick = {
                                isGrid = true
                                scope.launch { preferencesManager.setGridView(true) }
                            }
                        )
                        Text("Grid", modifier = Modifier.align(Alignment.CenterVertically))
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = !isGrid,
                            onClick = {
                                isGrid = false
                                scope.launch { preferencesManager.setGridView(false) }
                            }
                        )
                        Text("List", modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }
            }
            item { HorizontalDivider() }

            // Profile Section
            item {
                Text(text = "Profile", style = MaterialTheme.typography.titleMedium)
            }
            item {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("User Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = userEmail,
                    onValueChange = { newValue ->
                        userEmail = newValue
                        // Validate email format.
                        emailError = if (newValue.isNotEmpty() && !emailRegex.matches(newValue)) {
                            "Invalid email address"
                        } else ""
                    },
                    label = { Text("Email Address") },
                    isError = emailError.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (emailError.isNotEmpty()) {
                item {
                    Text(
                        text = emailError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // Save profile settings and show confirmation.
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
            }
            item { HorizontalDivider() }

            // Actions Section
            item {
                Text(text = "Actions", style = MaterialTheme.typography.titleMedium)
            }
            item {
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
                            // Reset preferences to default values.
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
                        Text("Reset Settings")
                    }
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
                        Text(
                            text = buildAnnotatedString {
                                append("Are you sure you want to")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(" DELETE ")
                                }
                                append("all notes?")
                            }
                        )
                        Text("(This action cannot be undone)", fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Confirm deletion of all notes.
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