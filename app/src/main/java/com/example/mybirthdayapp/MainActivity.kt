package com.example.mybirthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mybirthdayapp.model.BirthdayViewModel
import com.example.mybirthdayapp.model.AuthenticationViewModel
import com.example.mybirthdayapp.model.ThemeViewModel
import com.example.mybirthdayapp.repository.AuthenticationRepository
import com.example.mybirthdayapp.screens.Authentication
import com.example.mybirthdayapp.screens.BirthdayAddScreen
import com.example.mybirthdayapp.screens.BirthdayDetailsScreen
import com.example.mybirthdayapp.screens.BirthdayListScreen
import com.example.mybirthdayapp.ui.theme.MyBirthdayAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel() // Create ThemeViewModel

            MyBirthdayAppTheme(darkTheme = themeViewModel.isDarkMode.value) {
                MainScreen(themeViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val birthdayViewModel: BirthdayViewModel = viewModel()
    val authRepository = AuthenticationRepository() // Create repository instance
    val authViewModel = AuthenticationViewModel(authRepository) // Create ViewModel instance with the repository

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Authentication.route // Change start destination as needed
    ) {
         // Authentication screen
         composable(NavRoutes.Authentication.route) {
             Authentication(
                 user = authViewModel.user,
                 message = authViewModel.message.value,
                 signIn = { email, password -> authViewModel.signIn(email, password) },
                 register = { email, password -> authViewModel.register(email, password) },
                 navigateToNextScreen = { navController.navigate(NavRoutes.BirthdayList.route) }
             )
         }

        // Birthday List screen
        composable(NavRoutes.BirthdayList.route) {
            BirthdayListScreen(
                themeViewModel = themeViewModel,
                birthdays = birthdayViewModel.birthdays.value,
                onBirthdaySelected = { birthday ->
                    birthdayViewModel.getBirthdays() // Refresh the data
                    navController.navigate(NavRoutes.BirthdayDetails.createRoute(birthday.id))
                },
                onBirthdayDeleted = { birthday -> birthdayViewModel.deleteBirthday(birthday.id) },
                onAddBirthdayClicked = { navController.navigate(NavRoutes.BirthdayAdd.route) },
                signOut = {
                    authViewModel.signOut()
                    navController.navigate(NavRoutes.Authentication.route) {
                        popUpTo(NavRoutes.BirthdayList.route) { inclusive = true }
                    }
                },
                sortByName = { ascending -> birthdayViewModel.sortBirthdaysByName(ascending) }, // Pass sortByName
                sortByBirthYear = { ascending -> birthdayViewModel.sortBirthdaysByBirthYear(ascending) }, // Pass sortByBirthYear
                sortByAge = { ascending -> birthdayViewModel.sortBirthdaysByAge(ascending) }, // Pass sortByAge
                filterByName = { name -> birthdayViewModel.filterBirthdays("Name", name) }, // Pass filterByName
                filterByAge = { age -> birthdayViewModel.filterBirthdays("Age", age.toString()) } // Pass filterByAge
            )
        }

        // Birthday Details screen
        composable(
            NavRoutes.BirthdayDetails.route,
            arguments = listOf(navArgument(name = "birthdayId") { type = NavType.IntType })
        ) { backStackEntry ->
            val birthdayId = backStackEntry.arguments?.getInt("birthdayId")
            val updatedBirthday = birthdayViewModel.birthdays.value.find { it.id == birthdayId }
            if (updatedBirthday != null) {
                BirthdayDetailsScreen(
                    birthday = updatedBirthday,
                    onUpdateClicked = { id, updatedBirthday -> birthdayViewModel.updateBirthday(id, updatedBirthday) },
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }

        // Birthday Add screen
        composable(NavRoutes.BirthdayAdd.route) {
            BirthdayAddScreen(
                onAddBirthday = { newBirthday ->
                    birthdayViewModel.addBirthday(newBirthday)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    val mockThemeViewModel = ThemeViewModel().apply {
        isDarkMode.value = false // Set a default value for the preview
    }
    MyBirthdayAppTheme(darkTheme = mockThemeViewModel.isDarkMode.value) {
        MainScreen(themeViewModel = mockThemeViewModel)
    }
}