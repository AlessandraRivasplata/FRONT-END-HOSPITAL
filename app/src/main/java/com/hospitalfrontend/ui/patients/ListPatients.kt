package com.hospitalfrontend.ui.patients

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPatients(navController: NavController, roomNumber: String?, viewModel: ListPatientsViewModel = viewModel()) {
    val patientsUiState by viewModel.patientsUiState.collectAsState()
    val patients by viewModel.patients.collectAsState()

    LaunchedEffect(roomNumber) {
        roomNumber?.toIntOrNull()?.let { viewModel.getAllPatientsByRoomNumber(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)), // Same background color as ListRoomScreen
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            painter = painterResource(id = R.drawable.hospital_bed_2775552), // Reuse the same image as ListRoomScreen
            contentDescription = "LOGO PATIENTS",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(max = 700.dp),
                shape = RoundedCornerShape(16.dp), // Same rounded corners as ListRoomScreen
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp), // Same elevation
                colors = CardDefaults.cardColors(containerColor = Color.White) // Same card background
            ) {
                Column(
                    modifier = Modifier.padding(24.dp), // Consistent padding
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "HABITACIÓ $roomNumber",
                                    fontSize = 28.sp, // Larger font size like ListRoomScreen
                                    fontWeight = FontWeight.Bold, // Bold like ListRoomScreen
                                    color = Color(0xFF2C3E50) // Same title color
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Tornar",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Black
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White) // Match card background
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    when (patientsUiState) {
                        is PatientsUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is PatientsUiState.Success -> {
                            LazyColumn(
                                contentPadding = PaddingValues(vertical = 8.dp),
                                modifier = Modifier.heightIn(max = 550.dp) // Match ListRoomScreen's LazyColumn height
                            ) {
                                items(patients) { patient ->
                                    PatientCard(
                                        navController = navController,
                                        patientId = patient.idPatient.toString(),
                                        patientName = patient.name,
                                        room = "Room $roomNumber"
                                    )
                                    Spacer(modifier = Modifier.height(8.dp)) // Same spacing as RoomItem
                                }
                            }
                        }
                        is PatientsUiState.Error -> {
                            Text(
                                text = "Error en carregar els pacients",
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun PatientCard(navController: NavController, patientId: String, patientName: String, room: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                Log.d("PatientDebug", "Patient ID: $patientId")
                navController.navigate("personal_data/$patientId")
            },
        shape = RoundedCornerShape(12.dp), // Same shape as RoomItem
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Same elevation as RoomItem
        colors = CardDefaults.cardColors(containerColor = Color(0xFF78C9E4)) // Same color as RoomItem
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp) // Same padding as RoomItem
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = patientName,
                fontSize = 20.sp, // Same font size as RoomItem
                fontWeight = FontWeight.Bold, // Same weight as RoomItem
                color = Color.White // Same text color as RoomItem
            )
            Text(
                text = room,
                fontSize = 18.sp, // Slightly smaller like RoomItem's floor text
                color = Color.Gray, // Same secondary text color
                fontWeight = FontWeight.Bold
            )
        }
    }
}