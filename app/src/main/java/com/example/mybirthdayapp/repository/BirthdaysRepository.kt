package com.example.mybirthdayapp.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.mybirthdayapp.model.Birthday
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BirthdaysRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    private val birthdaysService: BirthdaysService

    // State to hold the current list of birthdays
    val birthdays: MutableState<List<Birthday>> = mutableStateOf(listOf())
    val isLoadingBirthdays = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        birthdaysService = retrofit.create(BirthdaysService::class.java)
    }

    // Fetch all birthdays
    fun getBirthdays() {
        Log.d("BirthdaysRepository", "Fetching birthdays from API")
        isLoadingBirthdays.value = true
        birthdaysService.getAllBirthdays().enqueue(object : Callback<List<Birthday>> {
            override fun onResponse(call: Call<List<Birthday>>, response: Response<List<Birthday>>) {
                isLoadingBirthdays.value = false
                if (response.isSuccessful) {
                    birthdays.value = response.body() ?: emptyList()
                    errorMessage.value = ""
                    Log.d("BirthdaysRepository", "Fetched birthdays: ${birthdays.value}")
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BirthdaysRepository", message)
                }
            }

            override fun onFailure(call: Call<List<Birthday>>, t: Throwable) {
                isLoadingBirthdays.value = false
                val message = t.message ?: "Unknown error. Check your connection."
                errorMessage.value = message
                Log.e("BirthdaysRepository", message)
            }
        })
    }

    // Add a new birthday
    fun addBirthday(birthday: Birthday) {
        Log.d("BirthdaysRepository", "Attempting to add birthday: $birthday")
        birthdaysService.addBirthday(birthday).enqueue(object : Callback<Birthday> {
            override fun onResponse(call: Call<Birthday>, response: Response<Birthday>) {
                if (response.isSuccessful) {
                    getBirthdays()
                    Log.d("BirthdaysRepository", "Added birthday: $birthday")
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BirthdaysRepository", message)
                }
            }

            override fun onFailure(call: Call<Birthday>, t: Throwable) {
                val message = t.message ?: "Unknown error. Check your connection."
                errorMessage.value = message
                Log.e("BirthdaysRepository", message)
            }
        })
    }

    // Delete a birthday by ID
    fun deleteBirthday(id: Int) {
        Log.d("BirthdaysRepository", "Attempting to delete birthday with ID: $id")
        birthdaysService.deleteBirthday(id).enqueue(object : Callback<Birthday> {
            override fun onResponse(call: Call<Birthday>, response: Response<Birthday>) {
                if (response.isSuccessful) {
                    getBirthdays()
                    Log.d("BirthdaysRepository", "Deleted birthday with ID: $id")
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BirthdaysRepository", message)
                }
            }

            override fun onFailure(call: Call<Birthday>, t: Throwable) {
                val message = t.message ?: "Unknown error. Check your connection."
                errorMessage.value = message
                Log.e("BirthdaysRepository", message)
            }
        })
    }

    // Update an existing birthday by ID
    fun updateBirthday(id: Int, birthday: Birthday) {
        Log.d("BirthdaysRepository", "Attempting to update birthday with ID: $id")
        birthdaysService.updateBirthday(id, birthday).enqueue(object : Callback<Birthday> {
            override fun onResponse(call: Call<Birthday>, response: Response<Birthday>) {
                if (response.isSuccessful) {
                    getBirthdays()
                    Log.d("BirthdaysRepository", "Updated birthday with ID: $id")
                } else {
                    val message = "Error ${response.code()}: ${response.message()}"
                    errorMessage.value = message
                    Log.e("BirthdaysRepository", message)
                }
            }

            override fun onFailure(call: Call<Birthday>, t: Throwable) {
                val message = t.message ?: "Unknown error. Check your connection."
                errorMessage.value = message
                Log.e("BirthdaysRepository", message)
            }
        })
    }

    // TODO: Sorting and filtering functions (Depends on the requirements)
    // TODO: Implement sorting and filtering functions
}