import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.profile.DrawerItem
import com.hospitalfrontend.ui.profile.InputField
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCaresScreen(
    navController: NavController,
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
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
                    DrawerItem("Inici") { navController.navigate("main") }
                    DrawerItem("Llista de cures") { navController.navigate("care_list") }
                    DrawerItem("Dades del pacient") { navController.navigate("patient_data") }
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
                                text = "AFEGIR CURES",
                                fontSize = 20.sp,
                                color = Color.Black
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
                                    painter = painterResource(id = R.drawable.flecha_atras),
                                    contentDescription = "Tornar",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_icono),
                                    contentDescription = "Obrir menú",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.informe_medico),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(150.dp)
                            .clickable { }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Constants Vitals",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        "Pressió Arterial",
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { showTensionFields = !showTensionFields }
                    )
                    if (showTensionFields) {
                        InputField(tensionSistolica, { tensionSistolica = it }, "Tensió Sistòlica", isEditing)
                        InputField(tensionDiastolica, { tensionDiastolica = it }, "Tensió Diastòlica", isEditing)
                    }

                    InputField(frecuenciaRespiratoria, { frecuenciaRespiratoria = it }, "Freqüència Respiratòria", isEditing)
                    InputField(pulso, { pulso = it }, "Pols", isEditing)
                    InputField(temperatura, { temperatura = it }, "Temperatura", isEditing)
                    InputField(saturacionOxigeno, { saturacionOxigeno = it }, "Saturació d'Oxigen", isEditing)

                    Text(
                        "Drenatges",
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { showDrenajesFields = !showDrenajesFields }
                    )
                    if (showDrenajesFields) {
                        InputField(drenajeTipo, { drenajeTipo = it }, "Tipus de Drenatge", isEditing)
                        InputField(drenajeDebito, { drenajeDebito = it }, "Dèbit", isEditing)
                    }

                    Text(
                        "Mobilitzacions",
                        fontSize = 18.sp,
                        modifier = Modifier.clickable { showMobilizacionesFields = !showMobilizacionesFields }
                    )
                    if (showMobilizacionesFields) {
                        InputField(sedestacion, { sedestacion = it }, "Sedestació", isEditing)
                        InputField(selectedDeambulacion, { selectedDeambulacion = it }, "Tipus de deambulació", isEditing)
                        InputField(cambiosPosturales, { cambiosPosturales = it }, "Canvis posturals", isEditing)
                    }

                    InputField(observaciones, { observaciones = it }, "Observacions", isEditing)

                    Spacer(modifier = Modifier.height(20.dp))
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
        )
    }
}
