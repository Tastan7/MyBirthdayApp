package com.example.mybirthdayapp.repository

import com.example.mybirthdayapp.model.Birthday
import retrofit2.Call
import retrofit2.http.*

interface BirthdaysService {
    
    // Fetch all birthdays
    @GET("birthdays")
    fun getAllBirthdays(): Call<List<Birthday>>

    // Get birthday by ID (This is optional only if used)
    @GET("birthdays/{birthdayId}")
    fun getBirthdayById(@Path("birthdayId") birthdayId: Int): Call<Birthday>

    // Add a new birthday
    @POST("birthdays")
    fun addBirthday(@Body birthday: Birthday): Call<Birthday>

    // Delete a birthday by ID
    @DELETE("birthdays/{id}")
    fun deleteBirthday(@Path("id") id: Int): Call<Birthday>

    // Update an existing birthday by ID
    @PUT("birthdays/{id}")
    fun updateBirthday(@Path("id") id: Int, @Body birthday: Birthday): Call<Birthday>

}