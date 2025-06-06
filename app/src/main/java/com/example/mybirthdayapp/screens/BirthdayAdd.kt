package com.example.mybirthdayapp.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mybirthdayapp.model.Birthday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayAddScreen(
    onAddBirthday: (Birthday) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var pictureUrl by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    fun validateInputs(): Boolean {
        return when {
            name.isBlank() -> {
                errorMessage = "Name cannot be empty."
                false
            }
            date.toIntOrNull() == null || date.toInt() !in 1..31 -> {
                errorMessage = "Date must be a valid number between 1 and 31."
                false
            }
            month.toIntOrNull() == null || month.toInt() !in 1..12 -> {
                errorMessage = "Month must be a valid number between 1 and 12."
                false
            }
            year.toIntOrNull() == null || year.toInt() < 1900 -> {
                errorMessage = "Year must be a valid number greater than 1900."
                false
            }
            else -> {
                errorMessage = ""
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        BirthdayEditableField(value = name, onValueChange = { name = it }, label = "Name")
        BirthdayEditableField(value = date, onValueChange = { date = it }, label = "Date", keyboardType = KeyboardType.Number)
        BirthdayEditableField(value = month, onValueChange = { month = it }, label = "Month", keyboardType = KeyboardType.Number)
        BirthdayEditableField(value = year, onValueChange = { year = it }, label = "Year", keyboardType = KeyboardType.Number)
        BirthdayEditableField(value = pictureUrl, onValueChange = { pictureUrl = it }, label = "Picture URL")
        BirthdayEditableField(value = remarks, onValueChange = { remarks = it }, label = "Remarks")

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onNavigateBack) {
                Text("Cancel")
            }
            Button(onClick = {
                if (validateInputs()) {
                    val birthday = Birthday(
                        id = 0,
                        userId = "userEman",
                        name = name,
                        birthYear = year.toInt(),
                        birthMonth = month.toInt(),
                        birthDayOfMonth = date.toInt(),
                        remarks = remarks,
                        pictureUrl = pictureUrl,
                        age = 0
                    )
                    onAddBirthday(birthday)
                }
            }) {
                Text("Add Birthday")
            }
        }
    }
}

@Composable
fun BirthdayEditableField(
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

@Preview
@Composable
fun BirthdayAddPreview() {
    BirthdayAddScreen(
        onAddBirthday = {},
        onNavigateBack = {}
    )
}