package com.example.scriba

import com.example.scriba.data.PreferencesManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesManager: PreferencesManager,
    onBack: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Mode Toggle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Dark Mode")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = darkMode,
                    onCheckedChange = { checked ->
                        scope.launch { preferencesManager.setDarkMode(checked) }
                    }
                )
            }
            // User Name Field
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("User Name") },
                modifier = Modifier.fillMaxWidth()
            )
            // User Email Field with validation
            OutlinedTextField(
                value = userEmail,
                onValueChange = { newValue ->
                    userEmail = newValue
                    emailError = if (newValue.isNotEmpty() && !emailRegex.matches(newValue)) {
                        "Invalid email address"
                    } else {
                        ""
                    }
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
            // Save Button with Snackbar feedback; remains on settings screen
            Button(
                onClick = {
                    scope.launch {
                        preferencesManager.setUserName(userName)
                        preferencesManager.setUserEmail(userEmail)
                        snackbarHostState.showSnackbar("Settings saved")
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = emailError.isEmpty()
            ) {
                Text("Save")
            }
        }
    }
}
