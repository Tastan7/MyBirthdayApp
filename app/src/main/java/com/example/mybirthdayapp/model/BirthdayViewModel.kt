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

    //TODO: Make sure the code below is correct for later use
//
//    // Sorting methods
//    fun sortBirthdaysByName(ascending: Boolean) {
//        repository.sortBirthdaysByName(ascending)
//    }
//
//    fun sortBirthdaysByBirthYear(ascending: Boolean) {
//        repository.sortBirthdaysByBirthYear(ascending)
//    }
//
//    fun sortBirthdaysByBirthMonth(ascending: Boolean) {
//        repository.sortBirthdaysByBirthMonth(ascending)
//    }
//
//    // Filtering methods
//
//    fun filterBirthdaysByName(nameFragment: String) {
//        repository.filterBirthdaysByName(nameFragment)
//    }
//
//    fun filterBirthdaysByBirthYear(birthYear: Int) {
//        repository.filterBirthdaysByBirthYear(birthYear)
//    }
//
//    fun filterBirthdaysByBirthMonth(birthMonth: Int) {
//        repository.filterBirthdaysByBirthMonth(birthMonth)
//    }
//
//    fun filterBirthdaysByAge(minAge: Int) {
//        repository.filterBirthdaysByAge(minAge)
//    }

}
