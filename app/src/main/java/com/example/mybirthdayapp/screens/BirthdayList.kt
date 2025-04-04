package com.example.mybirthdayapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mybirthdayapp.model.Birthday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayListScreen(
    birthdays: List<Birthday> = listOf(),
    onBirthdaySelected: (Birthday) -> Unit = {},
    onBirthdayDeleted: (Birthday) -> Unit = {},
    signOut: () -> Unit = {},
    sortByName: (Boolean) -> Unit = {},
    sortByBirthYear: (Boolean) -> Unit = {},
    sortByBirthMonth: (Boolean) -> Unit = {},
    filterByName: (String) -> Unit = {},
    filterByBirthYear: (Int) -> Unit = {},
    filterByBirthMonth: (Int) -> Unit = {},
    onAddBirthdayClicked: () -> Unit = {}
) {
    var filterText by remember { mutableStateOf("") }
    var filterType by remember { mutableStateOf("Name") }
    var isNameAscending by remember { mutableStateOf(true) }
    var isBirthYearAscending by remember { mutableStateOf(true) }
    var isBirthMonthAscending by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) } // State for logout confirmation dialog

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Birthday List") },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) { // Show logout dialog on click
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
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Birthday")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {

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
                        if (filterText.isEmpty()) {
                            filterByName("") // Reset name filter
                            filterByBirthYear(0) // Reset birth year filter
                            filterByBirthMonth(0) // Reset birth month filter
                        } else if (filterType == "Name") {
                            filterByName(filterText) // Apply name filter
                        } else if (filterType == "Birth Year") {
                            val birthYear = filterText.toIntOrNull()
                            if (birthYear != null) {
                                filterByBirthYear(birthYear) // Apply birth year filter
                            } else {
                                filterByBirthYear(0) // Reset birth year filter if input is invalid
                                errorMessage = "Please enter a valid number for Birth Year." // Set error message
                            }
                        } else if (filterType == "Birth Month") {
                            val birthMonth = filterText.toIntOrNull()
                            if (birthMonth != null) {
                                filterByBirthMonth(birthMonth) // Apply birth month filter
                            } else {
                                filterByBirthMonth(0) // Reset birth month filter if input is invalid
                                errorMessage = "Please enter a valid number for Birth Month." // Set error message
                            }
                        }
                    },
                    label = { Text("Filter by $filterType") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = if (filterType == "Name") KeyboardType.Text else KeyboardType.Number
                    )
                )

                Button(
                    onClick = {
                        // Toggle filter type between Name, Birth Year, and Birth Month
                        filterType = when (filterType) {
                            "Name" -> "Birth Year"
                            "Birth Year" -> "Birth Month"
                            else -> "Name"
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(filterType)
                }
            }

            // Error Message Display
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sorting Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                Button(
                    onClick = {
                        sortByBirthMonth(isBirthMonthAscending) // Trigger sorting by birth month
                        isBirthMonthAscending = !isBirthMonthAscending // Toggle sorting order
                },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text= if (isBirthMonthAscending) "Month \u25B2" else "Month \u25BC",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)

                    )
                }
            }

            // List of Birthdays
            LazyColumn(
                contentPadding = innerPadding,
                modifier = Modifier.fillMaxSize()
            ) {
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
                Text(text = "Birth Year: ${birthday.birthYear}") // Display birth year
                Text(text = "Birth Month: ${birthday.birthMonth}") // Display birth month
            }
            IconButton(onClick = { onBirthdayDeleted(birthday) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

// generate me a preview with 5 birthdays
@Preview
@Composable
fun BirthdayListScreenPreview(){
    BirthdayListScreen(
        birthdays = listOf(
            Birthday(1, "1", "John Doe", 1990, 1, 1, "Remarks", "https://example.com/picture.jpg", 31),
            Birthday(2, "1", "Jane Doe", 1995, 2, 2, "Remarks", "https://example.com/picture.jpg", 26),
            Birthday(3, "1", "Alice Doe", 2000, 3, 3, "Remarks", "https://example.com/picture.jpg", 21),
            Birthday(4, "1", "Bob Doe", 2005, 4, 4, "Remarks", "https://example.com/picture.jpg", 16),
            Birthday(5, "1", "Eve Doe", 2010, 5, 5, "Remarks", "https://example.com/picture.jpg", 11)
        )
    )
}



