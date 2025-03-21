package com.hospitalfrontend.ui.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellido by remember { mutableStateOf(TextFieldValue("")) }
    var fechaNacimiento by remember { mutableStateOf(TextFieldValue("")) }
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var lengua by remember { mutableStateOf(TextFieldValue("")) }
    var antecedentes by remember { mutableStateOf(TextFieldValue("")) }
    var alergias by remember { mutableStateOf(TextFieldValue("")) }
    var cuidadorNombre by remember { mutableStateOf(TextFieldValue("")) }
    var cuidadorTelefono by remember { mutableStateOf(TextFieldValue("")) }

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
                                text = "DATOS PERSONALES",
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
                Image(
                    painter = painterResource(id = R.drawable.personaldata_icono),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clickable { /* Acción para cambiar la imagen */ }
                )
                Spacer(modifier = Modifier.height(20.dp))
                InputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre", enabled = isEditing)
                InputField(value = apellido, onValueChange = { apellido = it }, label = "Apellidos", enabled = isEditing)
                InputField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = "Fecha de nacimiento", enabled = isEditing)
                InputField(value = direccion, onValueChange = { direccion = it }, label = "Dirección", enabled = isEditing)
                InputField(value = lengua, onValueChange = { lengua = it }, label = "Lengua", enabled = isEditing)
                InputField(value = antecedentes, onValueChange = { antecedentes = it }, label = "Antecedentes médicos", enabled = isEditing)
                InputField(value = alergias, onValueChange = { alergias = it }, label = "Alergias", enabled = isEditing)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Datos del Cuidador", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                InputField(value = cuidadorNombre, onValueChange = { cuidadorNombre = it }, label = "Nombre del cuidador", enabled = isEditing)
                InputField(value = cuidadorTelefono, onValueChange = { cuidadorTelefono = it }, label = "Teléfono del cuidador", enabled = isEditing)
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
}

@Composable
fun InputField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String, enabled: Boolean) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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