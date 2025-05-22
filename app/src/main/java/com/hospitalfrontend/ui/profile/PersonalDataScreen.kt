package com.hospitalfrontend.ui.profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.model.Patient
import com.hospitalfrontend.ui.profile.PatientDataViewModel
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    navController: NavController,
    patientId: String?,
    viewModel: PatientDataViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val patient by viewModel.patient.collectAsState()

    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom d'usuari"

    LaunchedEffect(patientId) {
        patientId?.toIntOrNull()?.let { id ->
            viewModel.getPatientById(id)
        }
    }

    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellido by remember { mutableStateOf(TextFieldValue("")) }
    var fechaNacimiento by remember { mutableStateOf(TextFieldValue("")) }
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var lengua by remember { mutableStateOf(TextFieldValue("")) }
    var antecedentes by remember { mutableStateOf(TextFieldValue("")) }
    var alergias by remember { mutableStateOf(TextFieldValue("")) }
    var familiarInfo by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(patient) {
        patient?.let {
            nombre = TextFieldValue(it.name)
            apellido = TextFieldValue(it.surname)
            fechaNacimiento = TextFieldValue(it.birthDate)
            direccion = TextFieldValue(it.address)
            lengua = TextFieldValue(it.language)
            antecedentes = TextFieldValue(it.medicalHistory)
            alergias = TextFieldValue(it.allergies)
            familiarInfo = TextFieldValue(it.familiarInfo)
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
                    MedicalDrawerItem("Dades Personals") {
                        scope.launch { drawerState.close() }
                        navController.navigate("personal_data/$patientId")
                    }
                    MedicalDrawerItem("Dades Mèdiques") {
                        scope.launch { drawerState.close() }
                        navController.navigate("medical_data/$patientId")
                    }
                    MedicalDrawerItem("Registre de cures") {
                        scope.launch { drawerState.close() }
                        navController.navigate("care_data/$patientId")
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
    )  {
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
                                text = "DADES PERSONALS",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00695C),
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center,
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
                    painter = painterResource(id = R.drawable.personaldata_icono),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { /* Acción para cambiar la imagen */ }
                )
                Spacer(modifier = Modifier.height(20.dp))
                InputField(value = nombre, onValueChange = { nombre = it }, label = "Nom", enabled = false)
                InputField(value = apellido, onValueChange = { apellido = it }, label = "Cognoms", enabled = false)
                InputField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = "Data de naixement", enabled = false)
                InputField(value = direccion, onValueChange = { direccion = it }, label = "Direcció", enabled = false)
                InputField(value = lengua, onValueChange = { lengua = it }, label = "Llengua", enabled = false)
                InputField(value = antecedentes, onValueChange = { antecedentes = it }, label = "Antecedents mèdics", enabled = false)
                InputField(value = alergias, onValueChange = { alergias = it }, label = "Al·lèrgies", enabled = false)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Informació Familiar", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                InputField(value = familiarInfo, onValueChange = { familiarInfo = it }, label = "Informació del familiar", enabled = false)
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun InputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                style = TextStyle(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        enabled = enabled
    )
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}