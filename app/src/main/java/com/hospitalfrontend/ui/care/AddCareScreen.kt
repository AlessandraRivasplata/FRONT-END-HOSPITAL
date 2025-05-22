@file:OptIn(ExperimentalMaterial3Api::class)

import android.media.MediaPlayer
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
import com.hospitalfrontend.ui.profile.InputField
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import com.hospitalfrontend.ui.profile.MedicalDrawerItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.hospitalfrontend.ui.sharedViewModel.DrawerNavigationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCaresScreen(
    navController: NavController,
    patientId: String?,
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity),
    createCareViewModel: CreateCareViewModel = viewModel(),
    drawerNavigationViewModel: DrawerNavigationViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom  d'Usuari"
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val currentDrawerRoute by drawerNavigationViewModel.currentDrawerRoute.collectAsState()

    var showVitalSigns by remember { mutableStateOf(true) }
    var showDrainages by remember { mutableStateOf(false) }
    var showHigiene by remember { mutableStateOf(false) }
    var showDieta by remember { mutableStateOf(false) }
    var showMobilizations by remember { mutableStateOf(false) }

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

    var systolicError by remember { mutableStateOf<String?>(null) }
    var diastolicError by remember { mutableStateOf<String?>(null) }
    var tempError by remember { mutableStateOf<String?>(null) }
    var respiratoryError by remember { mutableStateOf<String?>(null) }
    var pulseError by remember { mutableStateOf<String?>(null) }
    var oxygenError by remember { mutableStateOf<String?>(null) }
    var higieneError by remember { mutableStateOf<String?>(null) }
    var texturaError by remember { mutableStateOf<String?>(null) }
    var dietaTipoError by remember { mutableStateOf<String?>(null) }
    var autonomiaError by remember { mutableStateOf<String?>(null) }
    var protesisError by remember { mutableStateOf<String?>(null) }
    var sedestacionError by remember { mutableStateOf<String?>(null) }
    var deambulacionError by remember { mutableStateOf<String?>(null) }
    var deambulacionTypeError by remember { mutableStateOf<String?>(null) }
    var cambiosPosturalesError by remember { mutableStateOf<String?>(null) }
    var observacionesError by remember { mutableStateOf<String?>(null) }


    val mediaPlayerState = remember { mutableStateOf<MediaPlayer?>(null) }
    var showAlert by remember { mutableStateOf(false) } // Alarma de rangos fuera de lo normal
    var alertMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) } // Diálogo de confirmación para guardar

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

    fun playAlarmSound(mediaPlayerState: MutableState<MediaPlayer?>) {
        Log.d("AlarmSound", "Intentant reproduir el so sonida_alerta.mp3")
        mediaPlayerState.value?.stop()
        mediaPlayerState.value?.release()
        mediaPlayerState.value = null

        val mediaPlayer = MediaPlayer.create(context, R.raw.sonida_alerta)
        if (mediaPlayer == null) {
            Log.e("AlarmSound", "Error: No s'ha pogut crear el MediaPlayer. Comprova el fitxer a res/raw/sonida_alerta.mp3.")
            return
        }
        try {
            mediaPlayer.setVolume(1.0f, 1.0f)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            Log.d("AlarmSound", "So iniciat en bucle")
            mediaPlayerState.value = mediaPlayer
        } catch (e: Exception) {
            Log.e("AlarmSound", "Error en iniciar el so: ${e.message}")
            mediaPlayer.release()
        }
    }

    fun validateFields(): Boolean {
        var isValid = true

        systolicError = null
        diastolicError = null
        tempError = null
        respiratoryError = null
        pulseError = null
        oxygenError = null
        higieneError = null
        texturaError = null
        dietaTipoError = null
        autonomiaError = null
        protesisError = null
        sedestacionError = null
        deambulacionError = null
        deambulacionTypeError = null
        cambiosPosturalesError = null
        observacionesError = null

        if (tensionSistolica.text.isEmpty()) { systolicError = "Requerit"; isValid = false }
        if (tensionDiastolica.text.isEmpty()) { diastolicError = "Requerit"; isValid = false }
        if (frecuenciaRespiratoria.text.isEmpty()) { respiratoryError = "Requerit"; isValid = false }
        if (pulso.text.isEmpty()) { pulseError = "Requerit"; isValid = false }
        if (temperatura.text.isEmpty()) { tempError = "Requerit"; isValid = false }
        if (saturacionOxigeno.text.isEmpty()) { oxygenError = "Requerit"; isValid = false }
        if (tipusHigiene.isEmpty()) { higieneError = "Requerit"; isValid = false }
        if (selectedTextura.isEmpty()) { texturaError = "Requerit"; isValid = false }
        if (selectedTipusDieta.isEmpty()) { dietaTipoError = "Requerit"; isValid = false }
        if (autonomiaAlimentaria.isEmpty()) { autonomiaError = "Requerit"; isValid = false }
        if (portadorProtesis.isEmpty()) { protesisError = "Requerit"; isValid = false }
        if (sedestacion.text.isEmpty()) { sedestacionError = "Requerit"; isValid = false }
        if (selectedDeambulacion.isEmpty()) { deambulacionError = "Requerit"; isValid = false }
        else if (selectedDeambulacion == "Sí") {
            if (selectedDeambulacionType.isEmpty()) { deambulacionTypeError = "Requerit si Deambulació és Sí"; isValid = false }
        }
        if (cambiosPosturales.text.isEmpty()) { cambiosPosturalesError = "Requerit"; isValid = false }
        if (observaciones.text.isEmpty()) { observacionesError = "Requerit"; isValid = false }

        return isValid
    }

    // Diálogo de alerta por rangos fuera de lo normal
    if (showAlert) {
        AlertDialog(
            onDismissRequest = {
                mediaPlayerState.value?.stop()
                mediaPlayerState.value?.release()
                mediaPlayerState.value = null
                showAlert = false
                showConfirmationDialog = true // Mostrar el diálogo de confirmación después de la alerta
            },
            title = { Text("Alerta de Constants Vitals") },
            text = { Text(alertMessage + "\n\nEstàs segur que vols guardar aquests valors anòmals?") },
            confirmButton = {
                TextButton(onClick = {
                    mediaPlayerState.value?.stop()
                    mediaPlayerState.value?.release()
                    mediaPlayerState.value = null
                    showAlert = false
                    showConfirmationDialog = true // Mostrar el diálogo de confirmación si acepta la alerta
                }) {
                    Text("D'acord")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mediaPlayerState.value?.stop()
                    mediaPlayerState.value?.release()
                    mediaPlayerState.value = null
                    showAlert = false
                    // No se muestra el diálogo de confirmación si cancela la alerta
                }) {
                    Text("Cancel·lar")
                }
            }
        )
    }

    // Diálogo de confirmación para guardar (NUEVO)
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar Creació de Cura") },
            text = { Text("Estàs segur que vols crear aquesta cura?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                    // Lógica para guardar la cura
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
                        drainage_type = drenajeTipo.text.takeIf { it.isNotEmpty() },
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
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel·lar")
                }
            }
        )
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
                        val isFormValid = validateFields()
                        val alerts = mutableListOf<String>()

                        // Validaciones de rangos para alertas
                        tensionSistolica.text.toIntOrNull()?.let {
                            if (it > 140 || it < 90) { alerts.add("Tensió Sistòlica fora de rang: $it (Límit: 90-140)") }
                        }
                        tensionDiastolica.text.toIntOrNull()?.let {
                            if (it >= 90 || it < 50) { alerts.add("Tensió Diastòlica fora de rang: $it (Límit: 50-<90)") }
                        }
                        frecuenciaRespiratoria.text.toIntOrNull()?.let {
                            if (it > 20 || it < 12) { alerts.add("Freqüència Respiratòria fora de rang: $it (Límit: 12-20)") }
                        }
                        pulso.text.toIntOrNull()?.let {
                            if (it > 100 || it < 50) { alerts.add("Pols fora de rang: $it (Límit: 50-100)") }
                        }
                        temperatura.text.toDoubleOrNull()?.let {
                            if (it > 38.5 || it < 34.9) { alerts.add("Temperatura fora de rang: $it°C (Límit: 34.9-38.5)") }
                        }
                        saturacionOxigeno.text.toDoubleOrNull()?.let {
                            if (it < 94.0) { alerts.add("Saturació d'Oxigen fora de rang: $it% (Límit: ≥94%)") }
                        }

                        if (isFormValid) { // Primero, verifica si todos los campos obligatorios están llenos
                            if (alerts.isNotEmpty()) { // Si hay alertas de rango
                                alertMessage = alerts.joinToString("\n")
                                showAlert = true // Activa el diálogo de alerta (que luego activará el de confirmación si se acepta)
                                playAlarmSound(mediaPlayerState)
                            } else { // Si no hay alertas de rango y el formulario es válido
                                showConfirmationDialog = true // Directamente muestra el diálogo de confirmación
                            }
                        } else { // Si el formulario no es válido (faltan campos obligatorios)
                            scope.launch { snackbarHostState.showSnackbar("Si us plau, omple tots els camps obligatoris.") }
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
                                                .fillMaxWidth(),
                                            isError = higieneError != null
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
                                                        higieneError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (higieneError != null) {
                                        Text(
                                            text = higieneError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

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
                                                .fillMaxWidth(),
                                            isError = texturaError != null
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
                                                        texturaError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (texturaError != null) {
                                        Text(
                                            text = texturaError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
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
                                                .fillMaxWidth(),
                                            isError = dietaTipoError != null
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
                                                        dietaTipoError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (dietaTipoError != null) {
                                        Text(
                                            text = dietaTipoError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
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
                                                .fillMaxWidth(),
                                            isError = autonomiaError != null
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
                                                        autonomiaError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (autonomiaError != null) {
                                        Text(
                                            text = autonomiaError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
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
                                                .fillMaxWidth(),
                                            isError = protesisError != null
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
                                                        protesisError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (protesisError != null) {
                                        Text(
                                            text = protesisError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

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
                                        error = sedestacionError
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
                                                .fillMaxWidth(),
                                            isError = deambulacionError != null
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
                                                        deambulacionError = null
                                                        deambulacionTypeError = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                    if (deambulacionError != null) {
                                        Text(
                                            text = deambulacionError!!,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                        )
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
                                                    .fillMaxWidth(),
                                                isError = deambulacionTypeError != null
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
                                                            deambulacionTypeError = null
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                        if (deambulacionTypeError != null) {
                                            Text(
                                                text = deambulacionTypeError!!,
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                                            )
                                        }
                                    }
                                    InputField(
                                        value = cambiosPosturales,
                                        onValueChange = { cambiosPosturales = it },
                                        label = "Cavis Posturals",
                                        placeholder = "De allitat a incorporat...",
                                        error = cambiosPosturalesError
                                    )
                                }
                            }
                        }
                    }
                }

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
                                modifier = Modifier.height(100.dp),
                                error = observacionesError
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