package com.hospitalfrontend.ui.care

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.profile.InputField
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
    LaunchedEffect(careId) {
        viewModel.getCareById(careId)
    }

    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom d'Usuari"
    var isEditing by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showTensionFields by remember { mutableStateOf(false) }
    var showDrenajesFields by remember { mutableStateOf(false) }
    var showHigieneFields by remember { mutableStateOf(false) }
    var showDietaFields by remember { mutableStateOf(false) }
    var showMobilizacionesFields by remember { mutableStateOf(false) }

    var tensionSistolica by remember { mutableStateOf(TextFieldValue("")) }
    var tensionDiastolica by remember { mutableStateOf(TextFieldValue("")) }
    var frecuenciaRespiratoria by remember { mutableStateOf(TextFieldValue("")) }
    var pulso by remember { mutableStateOf(TextFieldValue("")) }
    var temperatura by remember { mutableStateOf(TextFieldValue("")) }
    var saturacionOxigeno by remember { mutableStateOf(TextFieldValue("")) }
    var drenajeTipo by remember { mutableStateOf(TextFieldValue("")) }
    var drenajeDebito by remember { mutableStateOf(TextFieldValue("")) }

    var higieneAllitat by remember { mutableStateOf(false) }
    var higieneParcialLlit by remember { mutableStateOf(false) }
    var higieneDutxaAjuda by remember { mutableStateOf(false) }
    var higieneAutonom by remember { mutableStateOf(false) }

    var selectedTexture by remember { mutableStateOf("Seleccionar") }
    var selectedDieta by remember { mutableStateOf("Seleccionar") }
    var autonomia by remember { mutableStateOf("Seleccionar") }
    var portadorProtesi by remember { mutableStateOf(false) }

    var observaciones by remember { mutableStateOf(TextFieldValue("")) }
    var sedestacion by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDeambulacion by remember { mutableStateOf(TextFieldValue("")) }
    var cambiosPosturales by remember { mutableStateOf(TextFieldValue("")) }

    val care by viewModel.care.collectAsState()

    LaunchedEffect(care) {
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
            selectedDeambulacion = TextFieldValue(it.ambulation.orEmpty())
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
                        contentDescription = "Imatge del menú",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(nurseName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    MedicalDrawerItem("Dades Mèdiques") { navController.navigate("medical_data/$patientId") }
                    MedicalDrawerItem("Dades Personals") { navController.navigate("personal_data/$patientId") }
                    MedicalDrawerItem("Historial de Cures") { navController.navigate("care_data/$patientId") }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Acción para salir */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Sortir",
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
                                text = "DETALL DE CURES",
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        Row(
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
                        TextButton(onClick = { isEditing = !isEditing }) {
                            Text(
                                text = if (isEditing) "Cancelar" else "Editar",
                                color = Color.Black,
                                fontSize = 18.sp
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
                    painter = painterResource(id = R.drawable.care_icon),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clickable { }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text("Constants Vitals", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)

                Text("Pressió Arterial", fontSize = 18.sp, modifier = Modifier.clickable { showTensionFields = !showTensionFields })
                if (showTensionFields) {
                    InputField(tensionSistolica, { tensionSistolica = it }, "Tensió Sistòlica", isEditing)
                    InputField(tensionDiastolica, { tensionDiastolica = it }, "Tensió Diastòlica", isEditing)
                }

                InputField(frecuenciaRespiratoria, { frecuenciaRespiratoria = it }, "Freqüència Respiratòria", isEditing)
                InputField(pulso, { pulso = it }, "Pols", isEditing)
                InputField(temperatura, { temperatura = it }, "Temperatura", isEditing)
                InputField(saturacionOxigeno, { saturacionOxigeno = it }, "Saturació d'Oxigen", isEditing)

                Text("Drenatges", fontSize = 18.sp, modifier = Modifier.clickable { showDrenajesFields = !showDrenajesFields })
                if (showDrenajesFields) {
                    InputField(drenajeTipo, { drenajeTipo = it }, "Tipus de Drenatge", isEditing)
                    InputField(drenajeDebito, { drenajeDebito = it }, "Dèbit", isEditing)
                }

                Text("Mobilitzacions", fontSize = 18.sp, modifier = Modifier.clickable { showMobilizacionesFields = !showMobilizacionesFields })
                if (showMobilizacionesFields) {
                    InputField(sedestacion, { sedestacion = it }, "Sedestació", isEditing)
                    InputField(selectedDeambulacion, { selectedDeambulacion = it }, "Tipus de deambulació", isEditing)
                    InputField(cambiosPosturales, { cambiosPosturales = it }, "Canvis posturals", isEditing)
                }

                InputField(observaciones, { observaciones = it }, "Observacions", isEditing)

                if (isEditing) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    selectedOption: String,
    options: List<String>,
    label: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption) }

    Box(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedText = option
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}