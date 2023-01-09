package com.example.android.contactsapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.contactsapp.data.viewmodel.MainViewModel

/**
 * Navigation composable to host the flow.
 */
@Composable
fun Navigation(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.FirstScreen.route) {
        composable(route = Screen.FirstScreen.route) {
            FirstScreen(navController, mainViewModel)
        }
        composable(
            route = Screen.SecondScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "default"
                    nullable = true
                }
            )
        ) {
            SecondScreen(navController, it.arguments?.getString("id"), mainViewModel)
        }
    }
}