@file:OptIn(ExperimentalMaterial3Api::class)

package com.hospitalfrontend.ui.care

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.profile.MedicalDrawerItem
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import com.hospitalfrontend.viewmodel.CareDetailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDetailScreen(
    careId: Int,
    patientId: String?,
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity),
    navController: NavController,
) {
    val viewModel: CareDetailViewModel = viewModel()
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom d'Usuari"
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estats per seccions col·lapsables
    var showVitalSigns by remember { mutableStateOf(true) }
    var showDrainages by remember { mutableStateOf(false) }
    var showHigiene by remember { mutableStateOf(false) }
    var showDieta by remember { mutableStateOf(false) }
    var showMobilizations by remember { mutableStateOf(false) }

    // Estats per camps (només lectura)
    var tensionSistolica by remember { mutableStateOf(TextFieldValue("")) }
    var tensionDiastolica by remember { mutableStateOf(TextFieldValue("")) }
    var frecuenciaRespiratoria by remember { mutableStateOf(TextFieldValue("")) }
    var pulso by remember { mutableStateOf(TextFieldValue("")) }
    var temperatura by remember { mutableStateOf(TextFieldValue("")) }
    var saturacionOxigeno by remember { mutableStateOf(TextFieldValue("")) }
    var drenajeTipo by remember { mutableStateOf(TextFieldValue("")) }
    var drenajeDebito by remember { mutableStateOf(TextFieldValue("")) }
    var observaciones by remember { mutableStateOf(TextFieldValue("")) }
    var sedestacion by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDeambulacion by remember { mutableStateOf("") }
    var selectedDeambulacionType by remember { mutableStateOf("") }
    var cambiosPosturales by remember { mutableStateOf(TextFieldValue("")) }
    var tipusHigiene by remember { mutableStateOf("") }
    var selectedTextura by remember { mutableStateOf("") }
    var selectedTipusDieta by remember { mutableStateOf(setOf<String>()) }
    var autonomiaAlimentaria by remember { mutableStateOf("") }
    var portadorProtesis by remember { mutableStateOf("") }

    // Carregar dades inicials
    val care by viewModel.care.collectAsState()
    LaunchedEffect(care) {
        viewModel.getCareById(careId)
        care?.let {
            tensionSistolica = TextFieldValue(it.systolicBp?.toString() ?: "")
            tensionDiastolica = TextFieldValue(it.diastolicBp?.toString() ?: "")
            frecuenciaRespiratoria = TextFieldValue(it.respiratoryRate?.toString() ?: "")
            pulso = TextFieldValue(it.pulse?.toString() ?: "")
            temperatura = TextFieldValue(it.bodyTemperature?.toString() ?: "")
            saturacionOxigeno = TextFieldValue(it.oxygenSaturation?.toString() ?: "")
            drenajeTipo = TextFieldValue(it.drainageType.orEmpty())
            drenajeDebito = TextFieldValue(it.drainageDebit?.toString() ?: "")
            observaciones = TextFieldValue(it.note.orEmpty())
            sedestacion = TextFieldValue(it.sedation.orEmpty())
            cambiosPosturales = TextFieldValue(it.posturalChanges.orEmpty())
            selectedDeambulacion = it.ambulation?.substringBefore(" (") ?: ""
            selectedDeambulacionType = if (it.ambulation?.contains("(") == true) {
                it.ambulation.substringAfter("(").substringBefore(")")
            } else ""
            tipusHigiene = it.hygieneType.orEmpty() // Corregit de hygineType a hygieneType
            selectedTextura = it.dietTexture.orEmpty()
            selectedTipusDieta = it.dietType?.split(",")?.map { it.trim() }?.toSet() ?: setOf()
            autonomiaAlimentaria = it.dietAutonomy.orEmpty()
            portadorProtesis = it.prosthesis?.toString() ?: ""
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
                        // navController.navigate("personal_data/$patientId")
                    }
                    MedicalDrawerItem("Dades Mèdiques") {
                        scope.launch { drawerState.close() }
                        // navController.navigate("medical_data/$patientId")
                    }
                    MedicalDrawerItem("Registre de cures") {
                        scope.launch { drawerState.close() }
                        // navController.navigate("care_data/$patientId")
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
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "DETALL DE CURES",
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
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.informe_medico),
                        contentDescription = "Icono de cuidados",
                        modifier = Modifier
                            .size(150.dp)
                            .padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Secció Constants Vitals
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showVitalSigns = !showVitalSigns }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Constants Vitals",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (showVitalSigns) R.drawable.arrow_up else R.drawable.arrow_down
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (showVitalSigns) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    ReadOnlyTextField(tensionSistolica, "Pressió Sistòlica")
                                    ReadOnlyTextField(tensionDiastolica, "Pressió Diastòlica")
                                    ReadOnlyTextField(frecuenciaRespiratoria, "Freqüència Respiratòria")
                                    ReadOnlyTextField(pulso, "Pols")
                                    ReadOnlyTextField(temperatura, "Temperatura")
                                    ReadOnlyTextField(saturacionOxigeno, "Saturació d'Oxigen")
                                }
                            }
                        }
                    }
                }

                // Secció Drenatges
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDrainages = !showDrainages }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Drenatges",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (showDrainages) R.drawable.arrow_up else R.drawable.arrow_down
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (showDrainages) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    ReadOnlyTextField(drenajeTipo, "Tipus de drenatge")
                                    ReadOnlyTextField(drenajeDebito, "Débit")
                                }
                            }
                        }
                    }
                }

                // Secció Higiene
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showHigiene = !showHigiene }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Higiene",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (showHigiene) R.drawable.arrow_up else R.drawable.arrow_down
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (showHigiene) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    ReadOnlyDropdownField(tipusHigiene, "Tipus d'Higiene")
                                }
                            }
                        }
                    }
                }

                // Secció Dieta
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDieta = !showDieta }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Dieta",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (showDieta) R.drawable.arrow_up else R.drawable.arrow_down
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (showDieta) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    Text(
                                        "Textures",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    ReadOnlyDropdownField(selectedTextura, "Textura")
                                    Text(
                                        "Tipus de dieta",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    ReadOnlyDropdownField(selectedTipusDieta.joinToString(", "), "Tipus de dieta")
                                    Text(
                                        "Autonomia alimentària",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    ReadOnlyDropdownField(autonomiaAlimentaria, "Autonomia alimentària")
                                    Text(
                                        "Portador de pròtesis",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    ReadOnlyDropdownField(portadorProtesis, "Portador de pròtesis")
                                }
                            }
                        }
                    }
                }

                // Secció Movilitzacions
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showMobilizations = !showMobilizations }
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Movilitzacions",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (showMobilizations) R.drawable.arrow_up else R.drawable.arrow_down
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            if (showMobilizations) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                    ReadOnlyTextField(sedestacion, "Sedestació")
                                    Text(
                                        "Deambulació",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    ReadOnlyDropdownField(selectedDeambulacion, "Deambulació")
                                    if (selectedDeambulacion == "Sí") {
                                        ReadOnlyDropdownField(selectedDeambulacionType, "Tipus de deambulació")
                                    }
                                    ReadOnlyTextField(cambiosPosturales, "Canvis Posturals")
                                }
                            }
                        }
                    }
                }

                // Observacions
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            ReadOnlyTextField(
                                value = observaciones,
                                label = "Observacions",
                                singleLine = false,
                                modifier = Modifier.height(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadOnlyTextField(
    value: TextFieldValue,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {}, // No permet canvis
            label = { Text(label) },
            readOnly = true,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )
    }
}

@Composable
fun ReadOnlyDropdownField(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = {}, // No permet canvis
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray
            )
        )
    }
}
