package com.hospitalfrontend.ui.care

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.profile.DrawerItem
import com.hospitalfrontend.ui.profile.InputField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDetailScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Mostrar/Ocultar secciones
    var showTensionFields by remember { mutableStateOf(false) }
    var showDrenajesFields by remember { mutableStateOf(false) }
    var showHigieneFields by remember { mutableStateOf(false) }
    var showDietaFields by remember { mutableStateOf(false) }
    var showMobilizacionesFields by remember { mutableStateOf(false) }

    // Variables de campos
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

    // Mobilitzacions
    var sedestacion by remember { mutableStateOf(TextFieldValue("")) }
    var selectedDeambulacion by remember { mutableStateOf("Seleccionar") }
    var cambiosPosturales by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DETALLE DE CURAS",
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
                    .clickable { /* Acción para cambiar la imagen */ }
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Constantes Vitales", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)

            Text("Presión Arterial", fontSize = 18.sp, modifier = Modifier.clickable { showTensionFields = !showTensionFields })
            if (showTensionFields) {
                InputField(value = tensionSistolica, onValueChange = { tensionSistolica = it }, label = "Tensión Sistólica", enabled = isEditing)
                InputField(value = tensionDiastolica, onValueChange = { tensionDiastolica = it }, label = "Tensión Diastólica", enabled = isEditing)
            }
            InputField(value = frecuenciaRespiratoria, onValueChange = { frecuenciaRespiratoria = it }, label = "Frecuencia Respiratoria", enabled = isEditing)
            InputField(value = pulso, onValueChange = { pulso = it }, label = "Pulso", enabled = isEditing)
            InputField(value = temperatura, onValueChange = { temperatura = it }, label = "Temperatura", enabled = isEditing)
            InputField(value = saturacionOxigeno, onValueChange = { saturacionOxigeno = it }, label = "Saturación de Oxígeno", enabled = isEditing)

            Text("Drenajes", fontSize = 18.sp, modifier = Modifier.clickable { showDrenajesFields = !showDrenajesFields })
            if (showDrenajesFields) {
                InputField(value = drenajeTipo, onValueChange = { drenajeTipo = it }, label = "Tipo de Drenaje", enabled = isEditing)
                InputField(value = drenajeDebito, onValueChange = { drenajeDebito = it }, label = "Débito", enabled = isEditing)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección de Higiene
            Text("Higiene", fontSize = 18.sp, modifier = Modifier.clickable { showHigieneFields = !showHigieneFields })
            if (showHigieneFields) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Allitat")
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(checked = higieneAllitat, onCheckedChange = { higieneAllitat = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Parcial al llit")
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(checked = higieneParcialLlit, onCheckedChange = { higieneParcialLlit = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Dutxa amb ajuda")
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(checked = higieneDutxaAjuda, onCheckedChange = { higieneDutxaAjuda = it })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Autònom")
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(checked = higieneAutonom, onCheckedChange = { higieneAutonom = it })
                }
            }

            // Sección Dieta
            Text("Dieta", fontSize = 18.sp, modifier = Modifier.clickable { showDietaFields = !showDietaFields })
            if (showDietaFields) {
                DropdownMenuBox(
                    selectedOption = selectedTexture,
                    options = listOf("Absoluta", "Hídrica", "Líquida", "Túrmix", "Semitova", "Tova", "Fàcil masticació", "Basal"),
                    label = "Textures",
                    onOptionSelected = { selectedTexture = it }
                )
                DropdownMenuBox(
                    selectedOption = selectedDieta,
                    options = listOf("Diabètica", "Hipolipídica", "Hipocalòrica", "Hipercalòrica", "Hipoproteica", "Hiperproteica", "Astringent", "Baixa en residus", "Celíaca", "Rica en fibra", "Sense lactosa", "Sense fruits secs", "Sense ou", "Sense porc"),
                    label = "Tipus de dieta",
                    onOptionSelected = { selectedDieta = it }
                )
                DropdownMenuBox(
                    selectedOption = autonomia,
                    options = listOf("Autònom", "Ajuda"),
                    label = "Autònom o ajuda",
                    onOptionSelected = { autonomia = it }
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Portador de prótesi")
                    Spacer(modifier = Modifier.width(8.dp))
                    Checkbox(checked = portadorProtesi, onCheckedChange = { portadorProtesi = it })
                }
            }

            //Sección Mobilizaciones
            Text("Mobilizaciones", fontSize = 18.sp, modifier = Modifier.clickable { showMobilizacionesFields = !showMobilizacionesFields })
            if (showMobilizacionesFields) {
                InputField(sedestacion, { sedestacion = it }, "Sedestación", isEditing)
                DropdownMenuBox(
                    selectedOption = selectedDeambulacion,
                    options = listOf("Sin ayuda", "Bastón", "Caminador", "Ayuda física"),
                    label = "Tipos de deambulación",
                    onOptionSelected = { selectedDieta = it }
                )
                InputField(cambiosPosturales, { cambiosPosturales = it}, "Cambiios posturales", isEditing)

            }

            //Sección Observaciones
            InputField(value = observaciones, onValueChange = { observaciones = it }, label = "Observaciones", enabled = isEditing)

            Spacer(modifier = Modifier.height(20.dp))
            if (isEditing) {
                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Guardar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(selectedOption: String, options: List<String>, label: String, onOptionSelected: (String) -> Unit) {
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
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
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
