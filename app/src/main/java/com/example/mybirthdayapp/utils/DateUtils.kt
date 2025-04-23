package com.example.mybirthdayapp.utils

import java.time.LocalDate
import java.time.Period

fun calculateAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
    val today = LocalDate.now()
    val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)
    return Period.between(birthDate, today).years
}