@file:OptIn(ExperimentalMaterial3Api::class)

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom  d'Usuari"
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Estados para secciones colapsables
    var showVitalSigns by remember { mutableStateOf(true) }
    var showDrainages by remember { mutableStateOf(false) }
    var showHigiene by remember { mutableStateOf(false) }
    var showDieta by remember { mutableStateOf(false) }
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
    var respiratoryError by remember { mutableStateOf<String?>(null) }
    var pulseError by remember { mutableStateOf<String?>(null) }
    var oxygenError by remember { mutableStateOf<String?>(null) }

    // Estado para la alerta
    val mediaPlayerState = remember { mutableStateOf<MediaPlayer?>(null) }
    var showAlert by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf("") }

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

    // Función para reproducir sonido de alarma
    fun playAlarmSound(mediaPlayerState: MutableState<MediaPlayer?>) {
        Log.d("AlarmSound", "Intentant reproduir el so sonida_alerta.mp3")
        // Atura i allibera qualsevol MediaPlayer existent per evitar fuites
        mediaPlayerState.value?.stop()
        mediaPlayerState.value?.release()
        mediaPlayerState.value = null

        val mediaPlayer = MediaPlayer.create(context, R.raw.sonida_alerta)
        if (mediaPlayer == null) {
            Log.e("AlarmSound", "Error: No s'ha pogut crear el MediaPlayer. Comprova el fitxer a res/raw/sonida_alerta.mp3.")
            return
        }
        try {
            mediaPlayer.setVolume(1.0f, 1.0f) // Volum al màxim
            mediaPlayer.isLooping = true // Activa el bucle
            mediaPlayer.start()
            Log.d("AlarmSound", "So iniciat en bucle")
            mediaPlayerState.value = mediaPlayer // Guarda la referència
        } catch (e: Exception) {
            Log.e("AlarmSound", "Error en iniciar el so: ${e.message}")
            mediaPlayer.release()
        }
    }

    // Diálogo de alerta
    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                // Atura el so quan es tanqui el diàleg
                mediaPlayerState.value?.stop()
                mediaPlayerState.value?.release()
                mediaPlayerState.value = null
                showAlert = false
            },
            title = { Text("Alerta de Constants Vitals") },
            text = { Text(alertMessage) },
            confirmButton = {
                TextButton(onClick = {
                    // Atura el so quan es premi "D'acord"
                    mediaPlayerState.value?.stop()
                    mediaPlayerState.value?.release()
                    mediaPlayerState.value = null
                    showAlert = false
                }) {
                    Text("D'acord")
                }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState, //
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFE0F2F1) //
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, //
                    modifier = Modifier
                        .fillMaxSize() //
                        .padding(16.dp) //
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu), //
                        contentDescription = "Imatge del menú", //
                        modifier = Modifier
                            .size(120.dp) //
                            .padding(top = 8.dp) //
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("nurse_profile")
                            }
                    )
                    Spacer(modifier = Modifier.height(8.dp)) //
                    Text(
                        text = nurseName, //
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold, //
                            fontSize = 20.sp //
                        ),
                        color = Color(0xFF004D40) //
                    )
                    Spacer(modifier = Modifier.height(16.dp)) //
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp) //
                    Spacer(modifier = Modifier.height(8.dp)) //
                    MedicalDrawerItem("Dades Personals") { //
                        scope.launch { drawerState.close() }
                        navController.navigate("personal_data/$patientId")
                    }
                    MedicalDrawerItem("Dades Mèdiques") { //
                        scope.launch { drawerState.close() }
                        navController.navigate("medical_data/$patientId")
                    }
                    MedicalDrawerItem("Registre de cures") { //
                        scope.launch { drawerState.close() }
                        navController.navigate("care_data/$patientId")
                    }
                    Spacer(modifier = Modifier.weight(1f)) //
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp) //
                    Spacer(modifier = Modifier.height(8.dp)) //
                    Row(
                        verticalAlignment = Alignment.CenterVertically, //
                        modifier = Modifier
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("list_rooms")
                            } //
                            .padding(12.dp) //
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono), //
                            contentDescription = "Sortir", //
                            modifier = Modifier.size(24.dp), //
                            tint = Color.Unspecified //
                        )
                        Spacer(modifier = Modifier.width(8.dp)) //
                        Text(
                            text = "Sortir", //
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium), //
                            color = Color(0xFF004D40) //
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
                            text = "AFEGIR CURES",
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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Validacions de rangs bàsics
                        systolicError =
                            if (tensionSistolica.text.isNotEmpty() && tensionSistolica.text.toIntOrNull() !in 50..250) {
                                "Rang no vàlid (50-250)"
                            } else null
                        diastolicError =
                            if (tensionDiastolica.text.isNotEmpty() && tensionDiastolica.text.toIntOrNull() !in 30..150) {
                                "Rang no vàlid (30-150)"
                            } else null
                        tempError = if (temperatura.text.isNotEmpty()) {
                            val tempValue = temperatura.text.toDoubleOrNull()
                            if (tempValue != null && tempValue !in 34.0..42.0) {
                                "Rang no vàlid (34-42)"
                            } else null
                        } else null
                        respiratoryError =
                            if (frecuenciaRespiratoria.text.isNotEmpty() && frecuenciaRespiratoria.text.toIntOrNull() !in 0..100) {
                                "Rang no vàlid (0-100)"
                            } else null
                        pulseError =
                            if (pulso.text.isNotEmpty() && pulso.text.toIntOrNull() !in 0..200) {
                                "Rang no vàlid (0-200)"
                            } else null
                        oxygenError = if (saturacionOxigeno.text.isNotEmpty()) {
                            val oxygenValue = saturacionOxigeno.text.toDoubleOrNull()
                            if (oxygenValue != null && oxygenValue !in 0.0..100.0) {
                                "Rang no vàlid (0-100)"
                            } else null
                        } else null

                        // Validacions d'alarmes
                        val alerts = mutableListOf<String>()
                        tensionSistolica.text.toIntOrNull()?.let {
                            if (it > 140 || it < 90) {
                                alerts.add("Tensió Sistòlica fora de rang: $it (Límit: 90-140)")
                            }
                        }
                        tensionDiastolica.text.toIntOrNull()?.let {
                            if (it >= 90 || it < 50) {
                                alerts.add("Tensió Diastòlica fora de rang: $it (Límit: 50-<90)")
                            }
                        }
                        frecuenciaRespiratoria.text.toIntOrNull()?.let {
                            if (it > 20 || it < 12) {
                                alerts.add("Freqüència Respiratòria fora de rang: $it (Límit: 12-20)")
                            }
                        }
                        pulso.text.toIntOrNull()?.let {
                            if (it > 100 || it < 50) {
                                alerts.add("Pols fora de rang: $it (Límit: 50-100)")
                            }
                        }
                        temperatura.text.toDoubleOrNull()?.let {
                            if (it > 38.5 || it < 34.9) {
                                alerts.add("Temperatura fora de rang: $it°C (Límit: 34.9-38.5)")
                            }
                        }
                        saturacionOxigeno.text.toDoubleOrNull()?.let {
                            if (it < 94.0) {
                                alerts.add("Saturació d'Oxigen fora de rang: $it% (Límit: ≥94%)")
                            }
                        }

                        if (alerts.isNotEmpty()) {
                            alertMessage = alerts.joinToString("\n")
                            showAlert = true
                            playAlarmSound(mediaPlayerState) // Passa la variable mediaPlayerState
                        }

                        // Continuar amb el desat si no hi ha errors de validació
                        if (systolicError == null && diastolicError == null && tempError == null &&
                            respiratoryError == null && pulseError == null && oxygenError == null
                        ) {
                            val formatter =
                                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
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
                                snackbarHostState.showSnackbar("La cura s'ha desat correctament")
                                navController.popBackStack()
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.guardar_icono),
                        contentDescription = "Desar",
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
                                            onValueChange = { if (it.text.all { char -> char.isDigit() }) {
                                                tensionSistolica = it
                                            } },
                                            label = "Pressió Sistòlica",
                                            placeholder = "90-140",
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            error = systolicError
                                        )
                                    InputField(
                                        value = tensionDiastolica,
                                        onValueChange = { if (it.text.all { char -> char.isDigit() }) {
                                            tensionDiastolica = it
                                        } },
                                        label = "Pressió Diastòlica",
                                        placeholder = "50-90",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        error = diastolicError
                                    )
                                    InputField(
                                        value = frecuenciaRespiratoria,
                                        onValueChange = { if (it.text.all { char -> char.isDigit() }) {
                                            frecuenciaRespiratoria = it
                                        } },
                                        label = "Freqüència Respiratòria",
                                        placeholder = "12-20",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        error = respiratoryError
                                    )
                                    InputField(
                                        value = pulso,
                                        onValueChange = { if (it.text.all { char -> char.isDigit() }) {
                                            pulso = it
                                        } },
                                        label = "Pols",
                                        placeholder = "50-100",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        error = pulseError
                                    )
                                    InputField(
                                        value = temperatura,
                                        onValueChange = { temperatura = it },
                                        label = "Temperatura",
                                        placeholder = "34.9-38.5",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        error = tempError
                                    )
                                    InputField(
                                        value = saturacionOxigeno,
                                        onValueChange = { saturacionOxigeno = it },
                                        label = "Saturació d'Oxigen",
                                        placeholder = "≥94",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        error = oxygenError
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
                                        label = "Tipus de drenatge",
                                        placeholder = "rutinari, passiu...",
                                    )
                                    InputField(
                                        value = drenajeDebito,
                                        onValueChange = { if (it.text.all { char -> char.isDigit() }) {
                                            drenajeDebito = it
                                        } },
                                        label = "Débit",
                                        placeholder = "50",
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
                                    InputField(
                                        value = sedestacion,
                                        onValueChange = { sedestacion = it },
                                        label = "Sedestació",
                                        placeholder = "Allitat, Incorporat, Cadira de rodes...",
                                    )
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
                                    InputField(
                                        value = cambiosPosturales,
                                        onValueChange = { cambiosPosturales = it },
                                        label = "Cavis Posturals",
                                        placeholder = "De allitat a incorporat...",
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
                                label = "Observacions",
                                placeholder = "Cura realizada sin problemas",
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
fun InputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null,
    singleLine: Boolean = true,
    placeholder: String? = null
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = { placeholder?.let { Text(it, color = Color.Gray.copy(alpha = 0.5f)) } },
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
