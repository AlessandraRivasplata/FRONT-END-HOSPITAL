package com.hospitalfrontend.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalDataScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Variables para el grado de dependencia
    var autonomoAVD by remember { mutableStateOf(false) }
    var dependienteParcial by remember { mutableStateOf(false) }
    var dependienteTotal by remember { mutableStateOf(false) }

    // Portador de O2
    var portadorO2 by remember { mutableStateOf(false) }
    var tipoO2 by remember { mutableStateOf(TextFieldValue("")) }

    // Portador de pañal
    var portadorPañal by remember { mutableStateOf(false) }
    var numeroCambios by remember { mutableStateOf(TextFieldValue("")) }
    var estadoPiel by remember { mutableStateOf(TextFieldValue("")) }

    // Sonda vesical
    var sondaVesical by remember { mutableStateOf(false) }
    var tipoSondaVesical by remember { mutableStateOf(TextFieldValue("")) }
    var debitoSondaVesical by remember { mutableStateOf(TextFieldValue("")) }

    // Sonda rectal
    var sondaRectal by remember { mutableStateOf(false) }
    var debitoSondaRectal by remember { mutableStateOf(TextFieldValue("")) }

    // Sonda nasogástrica
    var sondaNasogastrica by remember { mutableStateOf(false) }
    var tipoSondaNasogastrica by remember { mutableStateOf("Aspiració") } // Valor por defecto
    var observacionesNasogastrica by remember { mutableStateOf(TextFieldValue("")) }
    val opcionesNasogastrica = listOf("Aspiració", "Decúbit")

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu),
                        contentDescription = "Imagen del menú",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nombre de Usuario", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    DrawerItem("Datos Médicos") { navController.navigate("medical_data") }
                    DrawerItem("Datos Personales") { navController.navigate("personal_data") }
                    DrawerItem("Datos de Cuidado") { navController.navigate("care_data") }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Acción para salir */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Salir",
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
                                text = "MEDICAL DATA",
                                fontSize = 20.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleLarge
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
                // Imagen arriba
                Image(
                    painter = painterResource(id = R.drawable.medical_data_icono), // Cambia por la imagen que quieras
                    contentDescription = "Imagen de cabecera",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )

                // Grado de dependencia
                Text("Grau de dependència:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = autonomoAVD, onCheckedChange = { autonomoAVD = it }, enabled = isEditing)
                    Text("Autònom AVD")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = dependienteParcial, onCheckedChange = { dependienteParcial = it }, enabled = isEditing)
                    Text("Dependent parcial")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = dependienteTotal, onCheckedChange = { dependienteTotal = it }, enabled = isEditing)
                    Text("Dependent total")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Portador de O2
                Text("Portador d'O₂:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = portadorO2, onCheckedChange = { portadorO2 = it }, enabled = isEditing)
                    Text("Sí")
                }
                if (portadorO2 && isEditing) {
                    InputField(value = tipoO2, onValueChange = { tipoO2 = it }, label = "Tipus", enabled = isEditing)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !portadorO2, onCheckedChange = { portadorO2 = !it }, enabled = isEditing)
                    Text("No")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Portador de pañal
                Text("Portador de bolquer:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = portadorPañal, onCheckedChange = { portadorPañal = it }, enabled = isEditing)
                    Text("Sí")
                }
                if (portadorPañal && isEditing) {
                    InputField(value = numeroCambios, onValueChange = { numeroCambios = it }, label = "Nombre de canvis", enabled = isEditing)
                    InputField(value = estadoPiel, onValueChange = { estadoPiel = it }, label = "Estat de la pell", enabled = isEditing)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !portadorPañal, onCheckedChange = { portadorPañal = !it }, enabled = isEditing)
                    Text("No")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Sonda vesical
                Text("Sonda vesical:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = sondaVesical, onCheckedChange = { sondaVesical = it }, enabled = isEditing)
                    Text("Sí")
                }
                if (sondaVesical && isEditing) {
                    InputField(value = tipoSondaVesical, onValueChange = { tipoSondaVesical = it }, label = "Tipus", enabled = isEditing)
                    InputField(value = debitoSondaVesical, onValueChange = { debitoSondaVesical = it }, label = "Dèbit", enabled = isEditing)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !sondaVesical, onCheckedChange = { sondaVesical = !it }, enabled = isEditing)
                    Text("No")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Sonda rectal
                Text("Sonda rectal:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = sondaRectal, onCheckedChange = { sondaRectal = it }, enabled = isEditing)
                    Text("Sí")
                }
                if (sondaRectal && isEditing) {
                    InputField(value = debitoSondaRectal, onValueChange = { debitoSondaRectal = it }, label = "Dèbit", enabled = isEditing)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !sondaRectal, onCheckedChange = { sondaRectal = !it }, enabled = isEditing)
                    Text("No")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Sonda nasogástrica
                Text("Sonda nasogàstrica:", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = sondaNasogastrica, onCheckedChange = { sondaNasogastrica = it }, enabled = isEditing)
                    Text("Sí")
                }
                if (sondaNasogastrica && isEditing) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = tipoSondaNasogastrica,
                            onValueChange = {},
                            label = { Text("Tipus") },
                            readOnly = true,
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
                            opcionesNasogastrica.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        tipoSondaNasogastrica = opcion
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    InputField(value = observacionesNasogastrica, onValueChange = { observacionesNasogastrica = it }, label = "Observacions", enabled = isEditing)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = !sondaNasogastrica, onCheckedChange = { sondaNasogastrica = !it }, enabled = isEditing)
                    Text("No")
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Botón Guardar
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
}