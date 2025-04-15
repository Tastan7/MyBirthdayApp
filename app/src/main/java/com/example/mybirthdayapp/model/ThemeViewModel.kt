package com.example.mybirthdayapp.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    val isDarkMode = mutableStateOf(false)

    fun toggleTheme() {
        isDarkMode.value = !isDarkMode.value
    }
}