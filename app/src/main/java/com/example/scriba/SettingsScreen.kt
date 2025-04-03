package com.example.scriba

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scriba.data.PreferencesManager
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

    // Update local state when the stored values change
    LaunchedEffect(storedUserName) {
        userName = storedUserName
    }
    LaunchedEffect(storedUserEmail) {
        userEmail = storedUserEmail
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
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
                        scope.launch {
                            preferencesManager.setDarkMode(checked)
                        }
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
            // User Email Field
            OutlinedTextField(
                value = userEmail,
                onValueChange = { userEmail = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth()
            )
            // Save Button for User Details with redirection on save
            Button(
                onClick = {
                    scope.launch {
                        preferencesManager.setUserName(userName)
                        preferencesManager.setUserEmail(userEmail)
                        // After saving, navigate back to the main page.
                        onBack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}
