package com.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hospitalfrontend.ui.home.HomeScreen
import com.hospitalfrontend.ui.nurseinfo.all.AllNursesScreen
import com.hospitalfrontend.ui.nurseinfo.byname.FindNurseScreen
import com.hospitalfrontend.ui.authentication.NurseLoginScreen
import com.hospitalfrontend.ui.authentication.NurseRegisterScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login_nurse") {
        composable("home") { HomeScreen(navController) }
        composable("all_nurses") { AllNursesScreen(navController) }
        composable("find_nurse") { FindNurseScreen(navController) }
        composable("login_nurse") { NurseLoginScreen(navController) }
        composable("register_nurse") { NurseRegisterScreen(navController) }
    }
}
