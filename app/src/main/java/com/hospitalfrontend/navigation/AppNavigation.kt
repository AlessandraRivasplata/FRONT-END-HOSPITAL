package com.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hospitalfrontend.ui.home.HomeScreen
import com.hospitalfrontend.ui.nurseinfo.all.AllNursesScreen
import com.hospitalfrontend.ui.nurseinfo.byname.FindNurseScreen
import com.hospitalfrontend.ui.authentication.NurseLoginScreen
import com.hospitalfrontend.ui.authentication.NurseRegisterScreen
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    val nurseAuthViewModel: NurseAuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login_nurse") {
        composable("home") { HomeScreen(navController) }
        composable("all_nurses") { AllNursesScreen(navController) }
        composable("find_nurse") { FindNurseScreen(navController) }
        composable("login_nurse") { NurseLoginScreen(navController, nurseAuthViewModel) }
        composable("register_nurse") { NurseRegisterScreen(navController, nurseAuthViewModel) }
    }
}

