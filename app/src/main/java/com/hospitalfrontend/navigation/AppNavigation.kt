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
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel

import com.hospitalfrontend.ui.authentication.NurseRegisterViewModel
import com.hospitalfrontend.ui.nurseinfo.screen.NurseInfoScreen


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val nurseAuthViewModel: NurseAuthViewModel = viewModel()
    val nurseRegisterViewModel: NurseRegisterViewModel = viewModel()
    val nurseLoginViewModel: NurseLoginViewModel = viewModel()


    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("all_nurses") { AllNursesScreen(navController, nurseAuthViewModel, remoteViewModel = viewModel() ) }
        composable("find_nurse") { FindNurseScreen(navController = navController, nurseAuthViewModel = nurseAuthViewModel, findNurseViewModel = viewModel()
        )
        }
        composable("login_nurse") { NurseLoginScreen(navController, nurseLoginViewModel) }
        composable("register_nurse") { NurseRegisterScreen(navController = navController, createNurseViewModel = nurseRegisterViewModel)
        }
        composable("screen_nurse") { NurseInfoScreen(navController, nurseAuthViewModel) }
    }
}