package com.example.mybirthdayapp.model

data class Birthday(
    val id: Int,
    val userId:String,
    val name: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDayOfMonth: Int,
    val remarks: String?,
    val pictureUrl: String?,
    val age: Int

){
    constructor(
        userId: String,
        name: String,
        birthYear: Int,
        birthMonth: Int,
        birthDayOfMonth: Int,
        remarks: String,
        pictureUrl: String,
        age: Int
    ) : this(-1, userId, name, birthYear, birthMonth, birthDayOfMonth, remarks, pictureUrl, age)

    override fun toString(): String {
        return "$id Owner: $userId, Name: $name, Birth Year: $birthYear, Birth Month: $birthMonth, Birth Day: $birthDayOfMonth, Remarks: $remarks, PictureUrl: $pictureUrl, Age: $age"
    }
}