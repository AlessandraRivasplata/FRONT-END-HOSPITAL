package com.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hospitalfrontend.ui.home.HomeScreen
import com.hospitalfrontend.ui.nurse.all.AllNursesScreen
import com.hospitalfrontend.ui.nurse.byname.FindNurseScreen // Importa FindNurseScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("all_nurses") { AllNursesScreen(navController) }
        composable("find_nurse") { FindNurseScreen(navController) }
    }
}
