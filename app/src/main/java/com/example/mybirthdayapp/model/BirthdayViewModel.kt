package com.example.mybirthdayapp.model

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import com.example.mybirthdayapp.repository.BirthdaysRepository

class BirthdayViewModel : ViewModel() {

    private val repository = BirthdaysRepository()

    // Expose repository data as State to be observed in the UI
    val birthdays: State<List<Birthday>> = repository.birthdays
    val errorMessage: State<String> = repository.errorMessage
    val isLoadingBirthdays: State<Boolean> = repository.isLoadingBirthdays

    init {
        getBirthdays()
    }

    // Fetch all birthdays from the repository
    fun getBirthdays() {
        repository.getBirthdays()
    }

    // Add a birthday via the repository
    fun addBirthday(birthday: Birthday) {
        repository.addBirthday(birthday)
    }

    // Update a birthday via the repository
    fun updateBirthday(birthdayId: Int, birthday: Birthday) {
        repository.updateBirthday(birthdayId, birthday)
    }

       // Delete a birthday via the repository
    fun deleteBirthday(birthdayId: Int) {
        repository.deleteBirthday(birthdayId)
    }

    // Sorting by name
    fun sortBirthdaysByName(ascending: Boolean) {
        repository.sortBirthdaysByName(ascending)
    }

    // Sorting by birth year
    fun sortBirthdaysByBirthYear(ascending: Boolean) {
        repository.sortBirthdaysByBirthYear(ascending)
    }

    // Sorting by age
    fun sortBirthdaysByAge(ascending: Boolean) {
        repository.sortBirthdaysByAge(ascending)
    }
}
