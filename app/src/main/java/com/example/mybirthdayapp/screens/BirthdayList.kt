package com.example.mybirthdayapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybirthdayapp.model.Birthday
import com.example.mybirthdayapp.model.ThemeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayListScreen(
    themeViewModel: ThemeViewModel,
    birthdays: List<Birthday> = listOf(),
    onBirthdaySelected: (Birthday) -> Unit = {},
    onBirthdayDeleted: (Birthday) -> Unit = {},
    signOut: () -> Unit = {},
    sortByName: (Boolean) -> Unit = {},
    sortByBirthYear: (Boolean) -> Unit = {},
    sortByAge: (Boolean) -> Unit = {},
    filterByName: (String) -> Unit = {},
    filterByAge: (Int) -> Unit = {},
    onAddBirthdayClicked: () -> Unit = {}
) {
    var filterText by remember { mutableStateOf("") }
    var filterType by remember { mutableStateOf("Name") }
    var isNameAscending by remember { mutableStateOf(true) }
    var isBirthYearAscending by remember { mutableStateOf(true) }
    var isAgeAscending by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) } // State for logout confirmation dialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Birthday List") },
                actions = {
                    IconButton(onClick = { themeViewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (themeViewModel.isDarkMode.value) {
                                Icons.Default.Brightness7 // Sun icon for light mode
                            } else {
                                Icons.Default.Brightness4 // Moon icon for dark mode
                            },
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBirthdayClicked,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Birthday")
            }
        },
        floatingActionButtonPosition = FabPosition.Center // center the FAB

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

            // Filter Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = filterText,
                    onValueChange = {
                        filterText = it
                        errorMessage = "" // Clear error message on input change
                        if (filterText.isEmpty()) {
                            // Reset to original list
                            filterByName("")
                        } else if (filterType == "Name") {
                            filterByName(filterText) // Automatically filter by name
                        } else if (filterType == "Age") {
                            val age = filterText.toIntOrNull()
                            if (age != null) {
                                filterByAge(age) // Automatically filter by age
                            } else {
                                errorMessage = "Please enter a valid number for Age."
                            }
                        }
                    },
                    label = { Text("Filter by $filterType") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = if (filterType == "Name") KeyboardType.Text else KeyboardType.Number
                    ),
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        filterType = if (filterType == "Name") "Age" else "Name"
                        filterText = "" // Clear filter text when switching
                        errorMessage = "" // Clear error message
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Switch to ${if (filterType == "Name") "Age" else "Name"}")
                }
            }

            // Error Message Display
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Sorting Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Sort by Name
                Button(
                    onClick = {
                        sortByName(isNameAscending) // Trigger sorting by name
                        isNameAscending = !isNameAscending // Toggle sorting order
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isNameAscending) "Name \u25B2" else "Name \u25BC",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }

                // Sort by Age
                Button(
                    onClick = {
                        sortByAge(isAgeAscending) // Trigger sorting by age
                        isAgeAscending = !isAgeAscending // Toggle sorting order
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isAgeAscending) "Age \u25B2" else "Age \u25BC",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }

                // Sort by Year
                Button(
                    onClick = {
                        sortByBirthYear(isBirthYearAscending) // Trigger sorting by birth year
                        isBirthYearAscending = !isBirthYearAscending // Toggle sorting order
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (isBirthYearAscending) "Year \u25B2" else "Year \u25BC",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }
            }

            // Birthday List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(birthdays) { birthday ->
                    BirthdayListItem(
                        birthday = birthday,
                        onBirthdaySelected = onBirthdaySelected,
                        onBirthdayDeleted = onBirthdayDeleted
                    )
                }
            }

            // Handle empty birthday list scenario
            if (birthdays.isEmpty()) {
                Text("No birthdays available. Please add some!", color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
        }

        // Confirmation Dialog for Logout
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout Confirmation") },
                text = { Text("Are you sure you want to log out?") },
                confirmButton = {
                    Button(onClick = {
                        signOut() // Call the sign-out function
                        showLogoutDialog = false // Dismiss dialog
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogoutDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun BirthdayListItem(
    birthday: Birthday,
    onBirthdaySelected: (Birthday) -> Unit = {},
    onBirthdayDeleted: (Birthday) -> Unit = {}
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Confirmation") },
            text = { Text("Are you sure you want to delete this birthday?") },
            confirmButton = {
                Button(onClick = {
                    onBirthdayDeleted(birthday)
                    showDeleteConfirmation = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteConfirmation = false }) {
                    Text("No")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onBirthdaySelected(birthday) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = birthday.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Age: ${birthday.age}")
                Text(text = "Year: ${birthday.birthYear}")
            }
            IconButton(
                onClick = { showDeleteConfirmation = true },
                modifier = Modifier
                    .size(48.dp) // Increase the size of the icon
                    .align(Alignment.CenterVertically) // Center the icon vertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(40.dp) // Ensure the icon itself is resized
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BirthdayListScreenPreview() {
    // Create a mock ThemeViewModel
    val mockThemeViewModel = ThemeViewModel().apply {
        isDarkMode.value = false // Set a default value for the preview
    }

    BirthdayListScreen(
        themeViewModel = mockThemeViewModel,
        birthdays = listOf(
            Birthday(1, "1", "John Doe", 1990, 1, 1, "Remarks", "https://example.com/picture.jpg", 31),
            Birthday(2, "1", "Jane Doe", 1995, 2, 2, "Remarks", "https://example.com/picture.jpg", 26),
            Birthday(3, "1", "Alice Doe", 2000, 3, 3, "Remarks", "https://example.com/picture.jpg", 21),
            Birthday(4, "1", "Bob Doe", 2005, 4, 4, "Remarks", "https://example.com/picture.jpg", 16),
            Birthday(5, "1", "Eve Doe", 2010, 5, 5, "Remarks", "https://example.com/picture.jpg", 11)
        )
    )
}



