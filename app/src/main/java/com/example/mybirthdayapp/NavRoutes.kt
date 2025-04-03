package com.example.mybirthdayapp

sealed class NavRoutes(val route: String) {
    // data object Authentication : NavRoutes("authentication")
    data object BirthdayList : NavRoutes("birthday_list")
    data object BirthdayDetails : NavRoutes("birthday_details/{birthdayId}") {
        fun createRoute(birthdayId: Int) = "birthday_details/$birthdayId"
    }
    data object BirthdayAdd : NavRoutes("add_birthday")
}