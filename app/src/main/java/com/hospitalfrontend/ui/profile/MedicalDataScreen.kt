package com.hospitalfrontend.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.model.DiagnosisResponse
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    //val diagnosisState: MedicalDataUiState by medicalDataViewModel.medicalDataUiState.collectAsState()
    //val diagnosisData: DiagnosisResponse? by medicalDataViewModel.diagnosis.collectAsState()

    // Obtenemos el estado y los datos del ViewModel
    val diagnosisState by medicalDataViewModel.medicalDataUiState.collectAsState()
    val diagnosisData by medicalDataViewModel.diagnosis.collectAsState()
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom d'Usuari"

    // Hacer la consulta cuando se llega a la pantalla
    LaunchedEffect(patientId) {
        patientId?.toIntOrNull()?.let { id ->
            medicalDataViewModel.getDiagnosisByPatientId(id)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFE0F2F1)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu),
                        contentDescription = "Imatge del menú",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = nurseName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color(0xFF004D40)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    MedicalDrawerItem("Dades Personals") {
                        navController.navigate("personal_data/$patientId")
                    }
                    MedicalDrawerItem("Dades Mèdiques") {
                        navController.navigate("medical_data/$patientId")
                    }
                    MedicalDrawerItem("Registre de cures") {
                        navController.navigate("care_data/$patientId")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { navController.navigate("list_rooms") }
                            .padding(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Sortir",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sortir",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF004D40)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "DADES MÈDIQUES",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF00695C),
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Tornar enrere",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color(0xFF00695C)
                                )
                            }
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menú desplegable",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color(0xFF00695C)
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
                Image(
                    painter = painterResource(id = R.drawable.medical_data_icono),
                    contentDescription = "Imagen de Datos Médicos",
                    modifier = Modifier
                        .size(110.dp)
                        .clickable { /* Acción opcional */ }
                )
                Spacer(modifier = Modifier.height(20.dp))

                when (diagnosisState) {
                    is MedicalDataUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is MedicalDataUiState.Success -> {
                        diagnosisData?.let { data ->
                            // Grau de dependència con el mismo diseño que otros campos
                            InputField(
                                value = data.degreeOfDependence.toString(), // O mapea a "Dependent parcial" si es necesario
                                label = "Grau de Dependència",
                                enabled = false
                            )
                            InputField(
                                value = "${if (data.oxygenCarrier) "Sí" else "No"}",
                                label = "Portador d'O₂:",
                                enabled = false
                            )
                            data.oxygenCarrierObservations?.let {
                                InputField(
                                    value = it,
                                    label = "Observacions d'O₂",
                                    enabled = false
                                )
                            }
                            InputField(
                                value = "${if (data.diaperCarrier) "Sí" else "No"}",
                                label = "Portador de Bolquer",
                                enabled = false
                            )
                            data.diaperCarrierObservations?.let {
                                InputField(
                                    value = it,
                                    label = "Observacions Bolquer",
                                    enabled = false
                                )
                            }
                            InputField(
                                value = "${if (data.urinaryCatheter) "Sí" else "No"}",
                                label = "Sonda Vesical",
                                enabled = false
                            )
                            data.urinaryCatheterObservations?.let {
                                InputField(
                                    value = it,
                                    label = "Observacions  sonda vescial",
                                    enabled = false
                                )
                            }
                            InputField(
                                value = "${if (data.rectalCatheter) "Sí" else "No"}",
                                label = "Sonda Rectal",
                                enabled = false
                            )
                            data.rectalCatheterObservations?.let {
                                InputField(
                                    value = it,
                                    label = "Observacions sonda rectal",
                                    enabled = false
                                )
                            }
                            InputField(
                                value = "${if (data.nasogastricCatheter) "Sí" else "No"}",
                                label = "Sonda Nasogàstrica",
                                enabled = false
                            )
                            data.nasogastricCatheterObservations?.let {
                                InputField(
                                    value = it,
                                    label = "Observacions nasogàstrica",
                                    enabled = false
                                )
                            }
                            InputField(
                                value = data.diagnosisDate,
                                label = "Data del Diagnòstic",
                                enabled = false
                            )
                        }
                    }
                    is MedicalDataUiState.Error -> {
                        Text(
                            text = "Error en carregar les dades mèdiques",
                            color = Color.Red,
                            fontSize = 18.sp
                        )
                    }
                    else -> {
                        Text(
                            text = "Esperant consulta de dades mèdiques...",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MedicalDrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}

@Composable
fun InputField(value: String, label: String, enabled: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        ,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        enabled = enabled,
        readOnly = true
    )
}
