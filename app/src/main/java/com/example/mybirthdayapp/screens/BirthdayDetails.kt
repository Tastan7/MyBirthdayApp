package com.example.mybirthdayapp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mybirthdayapp.model.Birthday


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailsScreen(
    birthday: Birthday,
    onUpdateClicked: (Int, Birthday) -> Unit, // Update callback with birthday ID
    onBackPressed: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(birthday.name) }
    var birthYear by remember { mutableStateOf(birthday.birthYear.toString()) }
    var birthMonth by remember { mutableStateOf(birthday.birthMonth.toString()) }
    var birthDayOfMonth by remember { mutableStateOf(birthday.birthDayOfMonth.toString()) }
    var remarks by remember { mutableStateOf(birthday.remarks ?: "") }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Helper function to validate the input
    fun validateInputs(): Boolean {
        return when {
            birthYear.toIntOrNull() == null -> {
                errorMessage = "Birth Year must be a valid number."
                Log.e("BirthdayDetailsScreen", "Invalid Birth Year: $birthYear")
                false
            }
            birthMonth.toIntOrNull() == null -> {
                errorMessage = "Birth Month must be a valid number."
                Log.e("BirthdayDetailsScreen", "Invalid Birth Month: $birthMonth")
                false
            }
            birthDayOfMonth.toIntOrNull() == null -> {
                errorMessage = "Birth Day must be a valid number."
                Log.e("BirthdayDetailsScreen", "Invalid Birth Day: $birthDayOfMonth")
                false
            }
            name.isBlank() -> {
                errorMessage = "Name must be filled."
                Log.e("BirthdayDetailsScreen", "Missing Name")
                false
            }
            else -> {
                showError = false
                true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Birthday Details") },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the image using AsyncImage
            AsyncImage(
                model = birthday.pictureUrl ?: "",
                contentDescription = "Birthday Picture",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            if (showError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }
            // lave et map der kan binde en label til en værdi
            if (isEditing) {
                DetailEditableField(value = name, onValueChange = { name = it }, label = "Name")
                DetailEditableField(value = birthYear, onValueChange = { birthYear = it }, label = "Birth Year", keyboardType = KeyboardType.Number)
                DetailEditableField(value = birthMonth, onValueChange = { birthMonth = it }, label = "Birth Month", keyboardType = KeyboardType.Number)
                DetailEditableField(value = birthDayOfMonth, onValueChange = { birthDayOfMonth = it }, label = "Birth Day", keyboardType = KeyboardType.Number)
                DetailEditableField(value = remarks, onValueChange = { remarks = it }, label = "Remarks")

                Button(
                    onClick = {
                        if (validateInputs()) {
                            try {
                                val updatedBirthday = birthday.copy(
                                    name = name,
                                    birthYear = birthYear.toInt(),
                                    birthMonth = birthMonth.toInt(),
                                    birthDayOfMonth = birthDayOfMonth.toInt(),
                                    remarks = remarks,
                                    pictureUrl = birthday.pictureUrl ?: "" // Ensure pictureUrl is not null
                                )
                                onUpdateClicked(birthday.id, updatedBirthday)
                                isEditing = false
                            } catch (e: Exception) {
                                showError = true
                                errorMessage = "An error occurred: ${e.message}"
                                Log.e("BirthdayDetailsScreen", "Update failed", e)
                            }
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Apply Changes")
                }
            } else {
                Text(
                    text = birthday.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // lave dette programatisk så det ikke bare er hardcoded
                DetailRow(label = "Birth Year", value = birthYear)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Birth Month", value = birthMonth)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Birth Day", value = birthDayOfMonth)
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                DetailRow(label = "Remarks", value = remarks)
            }
        }
    }
}

@Composable
fun DetailEditableField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun BirthdayDetailsPreview() {
    BirthdayDetailsScreen(
        birthday = Birthday(
            id = 1,
            userId = "User1",
            name = "John Doe",
            birthYear = 1990,
            birthMonth = 1,
            birthDayOfMonth = 1,
            remarks = "Remarks",
            pictureUrl = "",
            age = 31
        ),
        onUpdateClicked = { _, _ -> },
        onBackPressed = {}
    )
}

