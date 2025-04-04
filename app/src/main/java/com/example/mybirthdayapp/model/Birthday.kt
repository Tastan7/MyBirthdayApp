package com.example.mybirthdayapp.model

data class Birthday(
    val id: Int = 0,
    val userId: String = "string",
    val name: String = "string",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDayOfMonth: Int = 0,
    val remarks: String? = "string",
    val pictureUrl: String? = "string",
    val age: Int = 0
) {
    override fun toString(): String {
        return "$id Owner: $userId, Name: $name, Birth Year: $birthYear, Birth Month: $birthMonth, Birth Day: $birthDayOfMonth, Remarks: $remarks, PictureUrl: $pictureUrl, Age: $age"
    }
}