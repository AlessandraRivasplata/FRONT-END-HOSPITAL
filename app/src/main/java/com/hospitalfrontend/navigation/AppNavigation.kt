package com.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hospitalfrontend.ui.home.HomeScreen
import com.hospitalfrontend.ui.nurseinfo.all.AllNursesScreen
import com.hospitalfrontend.ui.profile.MedicalDataScreen
import com.hospitalfrontend.ui.nurseinfo.byname.FindNurseScreen
import com.hospitalfrontend.ui.authentication.NurseLoginScreen
import com.hospitalfrontend.ui.authentication.NurseRegisterScreen
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel
import com.hospitalfrontend.ui.authentication.NurseRegisterViewModel
import com.hospitalfrontend.ui.nurseinfo.byId.FindNurseByIdScreen
import com.hospitalfrontend.ui.nurseinfo.byId.FindNurseByIdViewModel
import com.hospitalfrontend.ui.nurseinfo.screen.DeleteNurseViewModel
import com.hospitalfrontend.ui.nurseinfo.screen.NurseInfoScreen
import com.hospitalfrontend.ui.nurseinfo.screen.UpdateNurseScreen
import com.hospitalfrontend.ui.nurseinfo.screen.UpdateNurseViewModel
import com.hospitalfrontend.ui.rooms.ListRoomScreen
import com.hospitalfrontend.ui.profile.PersonalDataScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val nurseAuthViewModel: NurseAuthViewModel = viewModel()
    val nurseRegisterViewModel: NurseRegisterViewModel = viewModel()
    val nurseLoginViewModel: NurseLoginViewModel = viewModel()
    val deleteNurseViewModel: DeleteNurseViewModel = viewModel()
    val updateNurseViewModel: UpdateNurseViewModel = viewModel()
    val findNurseByIdViewModel: FindNurseByIdViewModel = viewModel()

    // para que se vea el login como primera pantalla cambiar el startDestination por login_nurse
    NavHost(navController = navController, startDestination = "medical_data") {
        //composable("personal_data") { PersonalDataScreen(navController = navController) }
        composable("medical_data") { MedicalDataScreen(navController = navController) }
        composable("home") { HomeScreen(navController) }
        composable("find_nurse") {
            FindNurseScreen(
                navController = navController,
                nurseAuthViewModel = nurseAuthViewModel,
                findNurseViewModel = viewModel()
            )
        }
        composable("login_nurse") {
            NurseLoginScreen(navController, nurseLoginViewModel)
        }
        composable("register_nurse") {
            NurseRegisterScreen(
                navController = navController,
                createNurseViewModel = nurseRegisterViewModel
            )
        }
        composable("screen_nurse") {
            NurseInfoScreen(
                navController,
                nurseAuthViewModel,
                nurseLoginViewModel,
                deleteNurseViewModel
            )
        }
        composable("update_nurse") {
            UpdateNurseScreen(
                navController,
                updateNurseViewModel,
                nurseLoginViewModel
            )
        }
        composable("findbyid_nurse/{id}") {
                backStackEntry -> val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            FindNurseByIdScreen(
                navController,
                findNurseByIdViewModel,
                id
            )
        }
        composable("list_rooms") {
            ListRoomScreen(navController)
        }
        composable("medical_data") {
            MedicalDataScreen(navController)
        }
    }
}
