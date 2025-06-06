package com.hospitalfrontend.ui.profile

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hospitalfrontend.ui.care.CaresDataViewModel
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import kotlinx.coroutines.launch
import com.hospitalfrontend.ui.sharedViewModel.DrawerNavigationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDataScreen(
    navController: NavController,
    patientId: String?,
    viewModel: CaresDataViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity),
    drawerNavigationViewModel: DrawerNavigationViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val caresViewModel: CaresDataViewModel = viewModel()
    val cares = caresViewModel.cares.collectAsState().value

    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nombre de Usuario"
    val currentDrawerRoute by drawerNavigationViewModel.currentDrawerRoute.collectAsState()

    LaunchedEffect(patientId) {
        patientId?.toIntOrNull()?.let { id ->
            caresViewModel.getCaresByPatientId(id)
        }
        drawerNavigationViewModel.setCurrentDrawerRoute("care_data/$patientId")
    }

    LaunchedEffect(cares) {
        if (cares.isNotEmpty()) {
            Log.d("CareDataScreen", "Cares cargados: ${cares.size}")
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
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("nurse_profile")
                            }
                    )
                    Text(
                        text = "Edit Profile",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("nurse_profile")
                            }
                            .padding(bottom = 4.dp)
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
                    MedicalDrawerItem(
                        text = "Dades Personals",
                        route = "personal_data/$patientId",
                        isSelected = currentDrawerRoute == "personal_data/$patientId"
                    ) { route ->
                        scope.launch { drawerState.close() }
                        drawerNavigationViewModel.setCurrentDrawerRoute(route)
                        navController.navigate(route)
                    }
                    MedicalDrawerItem(
                        text = "Dades Mèdiques",
                        route = "medical_data/$patientId",
                        isSelected = currentDrawerRoute == "medical_data/$patientId"
                    ) { route ->
                        scope.launch { drawerState.close() }
                        drawerNavigationViewModel.setCurrentDrawerRoute(route)
                        navController.navigate(route)
                    }
                    MedicalDrawerItem(
                        text = "Registre de cures",
                        route = "care_data/$patientId",
                        isSelected = currentDrawerRoute == "care_data/$patientId"
                    ) { route ->
                        scope.launch { drawerState.close() }
                        drawerNavigationViewModel.setCurrentDrawerRoute(route)
                        navController.navigate(route)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("list_rooms")
                            }
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
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE0F7FA).copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HISTORIAL",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00695C),
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center
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
                    },

                    actions = {
                        Button(
                            onClick = { navController.navigate("add_care/$patientId") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00695C),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .height(36.dp)
                        ) {
                            Text(
                                text = "Crear",
                                fontSize = 14.sp
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