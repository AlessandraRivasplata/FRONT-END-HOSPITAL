package com.hospitalfrontend.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.navigation.NavController
import com.hospitalfrontend.R
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalDataScreen(
    navController: NavController,
    patientId: String?,
    medicalDataViewModel: MedicalDataViewModel = viewModel(), // Obtener la instancia del ViewModel
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    // Estado para el drawer
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Obtenemos el estado y los datos del ViewModel
    val diagnosisState by medicalDataViewModel.medicalDataUiState.collectAsState()
    val diagnosisData by medicalDataViewModel.diagnosis.collectAsState()

    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nombre de Usuario"

    // Hacer la consulta cuando se llega a la pantalla
    LaunchedEffect(patientId) {
        patientId?.toIntOrNull()?.let { id ->
            medicalDataViewModel.getDiagnosisByPatientId(id)
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
                    DrawerItem("Datos Médicos") { navController.navigate("medical_data/$patientId") }
                    DrawerItem("Datos Personales") { navController.navigate("personal_data/$patientId") }
                    DrawerItem("Datos de Cuidado") { navController.navigate("care_data/$patientId") }
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
                                text = "Datos Médicos",
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
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_icono),
                                    contentDescription = "Menú",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
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
                // Mostrar los datos
                when (diagnosisState) {
                    is MedicalDataUiState.Loading -> {
                        Text("Cargando datos médicos...")
                    }
                    is MedicalDataUiState.Success -> {
                        diagnosisData?.let { data ->
                            Text("Grado de Dependencia: ${data.degreeOfDependence}")
                            Text("Portador de Oxígeno: ${if (data.oxygenCarrier) "Sí" else "No"}")
                            data.oxygenCarrierObservations?.let {
                                Text("Observaciones sobre Oxígeno: $it")
                            }
                            Text("Portador de Pañal: ${if (data.diaperCarrier) "Sí" else "No"}")
                            data.diaperCarrierObservations?.let {
                                Text("Observaciones sobre Pañal: $it")
                            }
                            Text("Catéter Urinario: ${if (data.urinaryCatheter) "Sí" else "No"}")
                            data.urinaryCatheterObservations?.let {
                                Text("Observaciones sobre Catéter Urinario: $it")
                            }
                            Text("Catéter Rectal: ${if (data.rectalCatheter) "Sí" else "No"}")
                            data.rectalCatheterObservations?.let {
                                Text("Observaciones sobre Catéter Rectal: $it")
                            }
                            Text("Catéter Nasogástrico: ${if (data.nasogastricCatheter) "Sí" else "No"}")
                            data.nasogastricCatheterObservations?.let {
                                Text("Observaciones sobre Catéter Nasogástrico: $it")
                            }
                            Text("Fecha del Diagnóstico: ${data.diagnosisDate}")
                        }
                    }
                    is MedicalDataUiState.Error -> {
                        Text("Error al cargar los datos médicos.")
                    }
                    else -> {
                        // Estado inicial o inactivo
                        Text("Esperando consulta de datos médicos...")
                    }
                }
            }
        }
    }
}

