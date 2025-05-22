package com.hospitalfrontend.navigation

import AddCaresScreen
import NurseLoginScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hospitalfrontend.ui.home.HomeScreen
import com.hospitalfrontend.ui.profile.MedicalDataScreen

import com.hospitalfrontend.ui.authentication.NurseRegisterScreen
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel
import com.hospitalfrontend.ui.authentication.NurseRegisterViewModel
import com.hospitalfrontend.ui.care.CareDetailScreen
import com.hospitalfrontend.ui.nurseinfo.byId.FindNurseByIdViewModel
import com.hospitalfrontend.ui.nurseinfo.screen.DeleteNurseViewModel
import com.hospitalfrontend.ui.nurseinfo.screen.NurseInfoScreen
import com.hospitalfrontend.ui.nurseinfo.screen.UpdateNurseScreen
import com.hospitalfrontend.ui.nurseinfo.screen.UpdateNurseViewModel
import com.hospitalfrontend.ui.nurseprofile.NurseProfileScreen
import com.hospitalfrontend.ui.nurseprofile.NurseProfileViewModel
import com.hospitalfrontend.ui.patients.ListPatients
import com.hospitalfrontend.ui.profile.PersonalDataScreen
import com.hospitalfrontend.ui.profile.CareDataScreen
import com.hospitalfrontend.ui.rooms.ListRoomScreen
import com.hospitalfrontend.ui.nurseinfo.byname.FindNurseScreen
import com.hospitalfrontend.ui.sharedViewModel.DrawerNavigationViewModel


@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val nurseAuthViewModel: NurseAuthViewModel = viewModel()
    val nurseRegisterViewModel: NurseRegisterViewModel = viewModel()
    val nurseLoginViewModel: NurseLoginViewModel = viewModel()
    val deleteNurseViewModel: DeleteNurseViewModel = viewModel()
    val updateNurseViewModel: UpdateNurseViewModel = viewModel()
    val findNurseByIdViewModel: FindNurseByIdViewModel = viewModel()
    val nurseProfileViewModel: NurseProfileViewModel = viewModel()
    val drawerNavigationViewModel: DrawerNavigationViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login_nurse") {

        composable("personal_data/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
            PersonalDataScreen(navController = navController, patientId = patientId, drawerNavigationViewModel = drawerNavigationViewModel)
        }
        composable("medical_data/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
            MedicalDataScreen(navController = navController, patientId = patientId, drawerNavigationViewModel = drawerNavigationViewModel)
        }
        composable("care_data/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
            CareDataScreen(navController = navController, patientId = patientId, drawerNavigationViewModel = drawerNavigationViewModel)
        }
        composable("add_care/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
            AddCaresScreen(navController = navController, patientId = patientId, drawerNavigationViewModel = drawerNavigationViewModel)
        }

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
        composable("list_rooms") {
            ListRoomScreen(navController)
        }

        composable("care_details/{careId}/{patientId}") { backStackEntry ->
            val careId = backStackEntry.arguments?.getString("careId")?.toIntOrNull()
            val patientId = backStackEntry.arguments?.getString("patientId")?.toIntOrNull()

            if (careId != null && patientId != null) {
                CareDetailScreen(
                    careId = careId,
                    patientId = patientId.toString(),
                    navController = navController,
                    drawerNavigationViewModel = drawerNavigationViewModel
                )
            }
        }

        composable("list_patients/{roomNumber}") { backStackEntry ->
            val roomNumber = backStackEntry.arguments?.getString("roomNumber")
            ListPatients(navController, roomNumber)
        }

        composable("nurse_profile") {
            NurseProfileScreen(
                navController = navController,
                nurseLoginViewModel = nurseLoginViewModel,
                nurseProfileViewModel = nurseProfileViewModel
            )
        }
    }
}