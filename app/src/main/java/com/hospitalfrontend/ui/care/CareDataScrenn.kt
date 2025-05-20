package com.hospitalfrontend.ui.profile

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.care.CaresDataViewModel
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDataScreen(
    navController: NavController,
    patientId: String?,
    viewModel: CaresDataViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val caresViewModel: CaresDataViewModel = viewModel()
    val cares = caresViewModel.cares.collectAsState().value

    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nombre de Usuario"

    // Llamada a la API al entrar en la pantalla
    LaunchedEffect(patientId) {
        patientId?.toIntOrNull()?.let { id ->
            caresViewModel.getCaresByPatientId(id)
        }
    }

    LaunchedEffect(cares) {
        if (cares.isNotEmpty()) {
            Log.d("CareDataScreen", "Cares cargados: $cares")
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu),
                        contentDescription = "Imagen del menú",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(nurseName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    DrawerItem("Dades Mèdiques") { navController.navigate("medical_data/$patientId") }
                    DrawerItem("Dades Personals") { navController.navigate("personal_data/$patientId") }
                    DrawerItem("Historial de Cures") { navController.navigate("care_data/$patientId") }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Acción para salir */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Salir",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HISTORIAL",
                                fontSize = 20.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    navigationIcon = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.flecha_atras),
                                    contentDescription = "Volver",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_icono),
                                    contentDescription = "Menú",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    },
                    actions = {
                        // Reemplazo IconButton por Button verde estilo "DETALL"
                        Button(
                            onClick = { navController.navigate("add_care/$patientId") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00695C), // Verde igual al botón DETALL
                                contentColor = Color.White // Texto blanco
                            ),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(36.dp) // Tamaño ajustado para TopAppBar
                        ) {
                            Text(
                                text = "Crear",
                                fontSize = 14.sp // Tamaño de texto ajustado
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Resto del código sin cambios
                Image(
                    painter = painterResource(id = R.drawable.historial_icono),
                    contentDescription = "Imagen de cabecera",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )

                cares.reversed().forEach { care ->
                    CareDataCard(
                        careId = care.idCare,
                        patientId = patientId ?: "",
                        name = "${care.patient.surname.uppercase()}, ${care.patient.name.uppercase()}",
                        specialty = "Pressió: ${care.systolicBp}/${care.diastolicBp} mmHg | Pols: ${care.pulse} bpm",
                        status = "Sat. Oxigen: ${care.oxygenSaturation}%",
                        location = "Infermera: ${care.nurse.name}",
                        dateTime = care.recordedAt.replace("T", " ").substringBefore("."),
                        type = "Nota: ${care.note}",
                        navController = navController
                    )
                }
            }
        }
}
}


@Composable
fun CareDataCard(
    careId: Int,
    patientId: String,
    name: String,
    specialty: String,
    status: String,
    location: String,
    dateTime: String,
    type: String,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = specialty,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = status,
                        fontSize = 12.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.hospital_icono),
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = location,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.fecha_icono),
                        contentDescription = "Hora",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateTime,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = type,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = { navController.navigate("care_details/${careId}/${patientId}") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
            ) {
                Text(
                    text = "DETALL",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}
