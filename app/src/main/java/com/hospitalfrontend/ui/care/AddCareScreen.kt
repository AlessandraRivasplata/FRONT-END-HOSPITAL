import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    createCareViewModel: CreateCareViewModel = viewModel() // ViewModel de creación de care
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val nurseName = nurseSharedViewModel.nurse?.name ?: "Nom d'Usuari"

    var showTensionFields by remember { mutableStateOf(false) }
    var showDrenajesFields by remember { mutableStateOf(false) }
    var showMobilizacionesFields by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(true) }

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
    var selectedDeambulacion by remember { mutableStateOf(TextFieldValue("")) }
    var cambiosPosturales by remember { mutableStateOf(TextFieldValue("")) }
    var tipusHigiene by remember { mutableStateOf(TextFieldValue("")) }
    var recordedAt by remember { mutableStateOf("") }

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
            content = { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.informe_medico),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(150.dp)
                                .clickable { }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Text("Constants Vitals", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)

                        Text(
                            "Pressió Arterial",
                            fontSize = 18.sp,
                            modifier = Modifier.clickable { showTensionFields = !showTensionFields }
                        )
                    }

                    if (showTensionFields) {
                        item { InputField(tensionSistolica, { tensionSistolica = it }, "Tensió Sistòlica", isEditing) }
                        item { InputField(tensionDiastolica, { tensionDiastolica = it }, "Tensió Diastòlica", isEditing) }
                    }

                    item { InputField(frecuenciaRespiratoria, { frecuenciaRespiratoria = it }, "Freqüència Respiratòria", isEditing) }
                    item { InputField(pulso, { pulso = it }, "Pols", isEditing) }
                    item { InputField(temperatura, { temperatura = it }, "Temperatura", isEditing) }
                    item { InputField(saturacionOxigeno, { saturacionOxigeno = it }, "Saturació d'Oxigen", isEditing) }

                    item {
                        Text(
                            "Drenatges",
                            fontSize = 18.sp,
                            modifier = Modifier.clickable { showDrenajesFields = !showDrenajesFields }
                        )
                    }

                    if (showDrenajesFields) {
                        item { InputField(drenajeTipo, { drenajeTipo = it }, "Tipus de Drenatge", isEditing) }
                        item { InputField(drenajeDebito, { drenajeDebito = it }, "Dèbit", isEditing) }
                    }

                    item {
                        Text(
                            "Mobilitzacions",
                            fontSize = 18.sp,
                            modifier = Modifier.clickable { showMobilizacionesFields = !showMobilizacionesFields }
                        )
                    }

                    if (showMobilizacionesFields) {
                        item { InputField(sedestacion, { sedestacion = it }, "Sedestació", isEditing) }
                        item { InputField(selectedDeambulacion, { selectedDeambulacion = it }, "Tipus de deambulació", isEditing) }
                        item { InputField(cambiosPosturales, { cambiosPosturales = it }, "Canvis posturals", isEditing) }
                        item { InputField(tipusHigiene, { tipusHigiene = it }, "Tipus d'higiene", isEditing) }
                    }

                    item { InputField(observaciones, { observaciones = it }, "Observacions", isEditing) }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        if (isEditing) {
                            Button(
                                onClick = {
                                    // Formato de fecha
                                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                                    recordedAt = formatter.format(Date())

                                    // Crear JSON
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
                                        hygine_type = tipusHigiene.text,
                                        sedation = sedestacion.text,
                                        ambulation = selectedDeambulacion.text,
                                        postural_changes = cambiosPosturales.text,
                                        recorded_at = recordedAt,
                                        note = observaciones.text
                                    )

                                    // Enviar el JSON al ViewModel para hacer la solicitud
                                    createCareViewModel.createCare(jsonData)

                                    isEditing = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        )
    }
}
