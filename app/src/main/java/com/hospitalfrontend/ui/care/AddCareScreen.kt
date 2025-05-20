@file:OptIn(ExperimentalMaterial3Api::class)

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.model.CreateCare
import com.hospitalfrontend.model.NurseId
import com.hospitalfrontend.model.PatientId
import com.hospitalfrontend.ui.care.CreateCareViewModel
import com.hospitalfrontend.ui.profile.DrawerItem
import com.hospitalfrontend.ui.profile.InputField
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import com.hospitalfrontend.ui.profile.MedicalDrawerItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCaresScreen(
    navController: NavController,
    patientId: String?,
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity),
    createCareViewModel: CreateCareViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nombre de Usuario"
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para secciones colapsables
    var showVitalSigns by remember { mutableStateOf(true) }
    var showDrainages by remember { mutableStateOf(false) }
    var showHigiene by remember { mutableStateOf(false) }
    var showDieta by remember { mutableStateOf(false) }
    var showProtesis by remember { mutableStateOf(false) }
    var showMobilizations by remember { mutableStateOf(false) }

    // Estados para campos con validación
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

    // Estados para errores de validación
    var systolicError by remember { mutableStateOf<String?>(null) }
    var diastolicError by remember { mutableStateOf<String?>(null) }
    var tempError by remember { mutableStateOf<String?>(null) }

    // Opciones para desplegables
    val higieneOptions = listOf("Aïllat", "Parcial al llit", "Dutxa amb ajuda", "Dutxa sense ajuda", "Autònom")
    val texturaOptions = listOf("Absoluta", "Hídrica (quantitat diària)", "Líquida", "Túrmix", "Semitova", "Tova", "Fàcil masticació", "Basal")
    val tipusDietaOptions = listOf(
        "Diabètica", "Hipolipídica", "Hipocalòrica", "Hipercalòrica", "Hipoproteica",
        "Hiperproteica", "Astringent", "Baixa en residus", "Celíaca", "Rica en fibra",
        "Sense lactosa", "Sense fruits secs", "Sense ou", "Sense porc"
    )
    val autonomiaOptions = listOf("Autònom", "Amb ajuda")
    val protesisOptions = listOf("Sí", "No")
    val deambulacionOptions = listOf("Sí", "No")
    val deambulacionTypeOptions = listOf("Amb bastó", "Amb caminador", "Amb ajuda física")

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
                    MedicalDrawerItem("Dades Personals") { navController.navigate("personal_data/$patientId") }
                    MedicalDrawerItem("Dades Mèdiques") { navController.navigate("medical_data/$patientId") }
                    MedicalDrawerItem("Registre de cures") { navController.navigate("care_data/$patientId") }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { navController.navigate("list_rooms") }) {
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
                            Text("AFEGIR CURES", fontSize = 20.sp, color = Color.Black)
                        }
                    },
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Validaciones antes de guardar
                        systolicError = if (tensionSistolica.text.isNotEmpty() && tensionSistolica.text.toIntOrNull() !in 50..250) {
                            "Rang no vàlid (50-250)"
                        } else null
                        diastolicError = if (tensionDiastolica.text.isNotEmpty() && tensionDiastolica.text.toIntOrNull() !in 30..150) {
                            "Rang no vàlid (30-150)"
                        } else null
                        tempError = if (temperatura.text.isNotEmpty()) {
                            val tempValue = temperatura.text.toDoubleOrNull()
                            if (tempValue != null && tempValue !in 34.0..42.0) {
                                "Rang no vàlid (34-42)"
                            } else null
                        } else null
                        if (systolicError == null && diastolicError == null && tempError == null) {
                            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                            val recordedAt = formatter.format(Date())

                            val jsonData = CreateCare(
                                patient = PatientId(patientId?.toIntOrNull()),
                                nurse = NurseId(nurseSharedViewModel.nurse?.id),
                                systolic_bp = tensionSistolica.text.toIntOrNull(),
                                diastolic_bp = tensionDiastolica.text.toIntOrNull(),
                                respiratory_rate = frecuenciaRespiratoria.text.toIntOrNull(),
                                pulse = pulso.text.toIntOrNull(),
                                body_temperature = temperatura.text.toDoubleOrNull(),
                                oxygen_saturation = saturacionOxigeno.text.toDoubleOrNull(),
                                drainage_type = drenajeTipo.text,
                                drainage_debit = drenajeDebito.text.toIntOrNull(),
                                hygine_type = tipusHigiene,
                                diet_texture = selectedTextura,
                                diet_type = selectedTipusDieta.joinToString(","),
                                diet_autonomy = autonomiaAlimentaria,
                                prosthesis = portadorProtesis,
                                sedation = sedestacion.text,
                                ambulation = if (selectedDeambulacion == "Sí") "$selectedDeambulacion ($selectedDeambulacionType)" else selectedDeambulacion,
                                postural_changes = cambiosPosturales.text,
                                recorded_at = recordedAt,
                                note = observaciones.text
                            )

                            scope.launch {
                                createCareViewModel.createCare(jsonData)
                                snackbarHostState.showSnackbar("Les cures s'han desat correctament")
                                navController.popBackStack()
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.guardar_icono),
                        contentDescription = "Guardar",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Unspecified
                    )
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
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

                // Sección Constantes Vitales
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
                                    InputField(
                                        value = tensionSistolica,
                                        onValueChange = { tensionSistolica = it },
                                        label = "Pressió Sistòlica",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        error = systolicError
                                    )
                                    InputField(
                                        value = tensionDiastolica,
                                        onValueChange = { tensionDiastolica = it },
                                        label = "Presión Diastólica",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        error = diastolicError
                                    )
                                    InputField(
                                        value = frecuenciaRespiratoria,
                                        onValueChange = { frecuenciaRespiratoria = it },
                                        label = "Freqüència Respiratòria",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    InputField(
                                        value = pulso,
                                        onValueChange = { pulso = it },
                                        label = "Pols",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                    InputField(
                                        value = temperatura,
                                        onValueChange = { temperatura = it },
                                        label = "Temperatura",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        error = tempError
                                    )
                                    InputField(
                                        value = saturacionOxigeno,
                                        onValueChange = { saturacionOxigeno = it },
                                        label = "Saturació d'Oxigen",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                                    )
                                }
                            }
                        }
                    }
                }

                // Sección Drenajes
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
                                    InputField(
                                        value = drenajeTipo,
                                        onValueChange = { drenajeTipo = it },
                                        label = "Tipus de drenatge"
                                    )
                                    InputField(
                                        value = drenajeDebito,
                                        onValueChange = { drenajeDebito = it },
                                        label = "Débit",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }
                    }
                }

                // Sección Higiene
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
                                    var expanded by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expanded,
                                        onExpandedChange = { expanded = !expanded }
                                    ) {
                                        TextField(
                                            value = tipusHigiene,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Tipus d'Higiene") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expanded,
                                            onDismissRequest = { expanded = false }
                                        ) {
                                            higieneOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        tipusHigiene = option
                                                        expanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Sección Dieta
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
                                    // Subsección Texturas
                                    Text(
                                        "Textures",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    var expandedTextura by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedTextura,
                                        onExpandedChange = { expandedTextura = !expandedTextura }
                                    ) {
                                        TextField(
                                            value = selectedTextura,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Textura") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTextura)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedTextura,
                                            onDismissRequest = { expandedTextura = false }
                                        ) {
                                            texturaOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        selectedTextura = option
                                                        expandedTextura = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Subsección Tipus de dieta
                                    Text(
                                        "Tipus de dieta",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    var expandedTipusDieta by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedTipusDieta,
                                        onExpandedChange = { expandedTipusDieta = !expandedTipusDieta }
                                    ) {
                                        TextField(
                                            value = selectedTipusDieta.joinToString(", "),
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Tipus de dieta") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipusDieta)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedTipusDieta,
                                            onDismissRequest = { expandedTipusDieta = false }
                                        ) {
                                            tipusDietaOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        selectedTipusDieta = if (option in selectedTipusDieta) {
                                                            selectedTipusDieta - option
                                                        } else {
                                                            selectedTipusDieta + option
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Subsección Autonomia alimentària
                                    Text(
                                        "Autonomia alimentària",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    var expandedAutonomia by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedAutonomia,
                                        onExpandedChange = { expandedAutonomia = !expandedAutonomia }
                                    ) {
                                        TextField(
                                            value = autonomiaAlimentaria,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Autonomia alimentària") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAutonomia)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedAutonomia,
                                            onDismissRequest = { expandedAutonomia = false }
                                        ) {
                                            autonomiaOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        autonomiaAlimentaria = option
                                                        expandedAutonomia = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Subsección Portador de pròtesis
                                    Text(
                                        "Portador de pròtesis",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    var expandedProtesis by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedProtesis,
                                        onExpandedChange = { expandedProtesis = !expandedProtesis }
                                    ) {
                                        TextField(
                                            value = portadorProtesis,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Portador de pròtesis") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProtesis)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedProtesis,
                                            onDismissRequest = { expandedProtesis = false }
                                        ) {
                                            protesisOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        portadorProtesis = option
                                                        expandedProtesis = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                // Sección Movilizaciones
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
                                    // Subsección Sedestación
                                    InputField(
                                        value = sedestacion,
                                        onValueChange = { sedestacion = it },
                                        label = "Sedestación"
                                    )

                                    // Subsección Deambulació
                                    Text(
                                        "Deambulació",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                    var expandedDeambulacion by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(
                                        expanded = expandedDeambulacion,
                                        onExpandedChange = { expandedDeambulacion = !expandedDeambulacion }
                                    ) {
                                        TextField(
                                            value = selectedDeambulacion,
                                            onValueChange = {},
                                            readOnly = true,
                                            label = { Text("Deambulació") },
                                            trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDeambulacion)
                                            },
                                            modifier = Modifier
                                                .menuAnchor()
                                                .fillMaxWidth()
                                        )
                                        ExposedDropdownMenu(
                                            expanded = expandedDeambulacion,
                                            onDismissRequest = { expandedDeambulacion = false }
                                        ) {
                                            deambulacionOptions.forEach { option ->
                                                DropdownMenuItem(
                                                    text = { Text(option) },
                                                    onClick = {
                                                        selectedDeambulacion = option
                                                        if (option != "Sí") selectedDeambulacionType = ""
                                                        expandedDeambulacion = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    // Subsección Deambulació Tipo (si Sí)
                                    if (selectedDeambulacion == "Sí") {
                                        var expandedDeambulacionType by remember { mutableStateOf(false) }
                                        ExposedDropdownMenuBox(
                                            expanded = expandedDeambulacionType,
                                            onExpandedChange = { expandedDeambulacionType = !expandedDeambulacionType }
                                        ) {
                                            TextField(
                                                value = selectedDeambulacionType,
                                                onValueChange = {},
                                                readOnly = true,
                                                label = { Text("Tipus de deambulació") },
                                                trailingIcon = {
                                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDeambulacionType)
                                                },
                                                modifier = Modifier
                                                    .menuAnchor()
                                                    .fillMaxWidth()
                                            )
                                            ExposedDropdownMenu(
                                                expanded = expandedDeambulacionType,
                                                onDismissRequest = { expandedDeambulacionType = false }
                                            ) {
                                                deambulacionTypeOptions.forEach { option ->
                                                    DropdownMenuItem(
                                                        text = { Text(option) },
                                                        onClick = {
                                                            selectedDeambulacionType = option
                                                            expandedDeambulacionType = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Subsección Cambios Posturales
                                    InputField(
                                        value = cambiosPosturales,
                                        onValueChange = { cambiosPosturales = it },
                                        label = "Cambios Posturales"
                                    )
                                }
                            }
                        }
                    }
                }

                // Observaciones
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            InputField(
                                value = observaciones,
                                onValueChange = { observaciones = it },
                                label = "Observaciones",
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

// Componente reutilizable para campos de entrada con soporte para errores
@Composable
fun InputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null,
    singleLine: Boolean = true
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}