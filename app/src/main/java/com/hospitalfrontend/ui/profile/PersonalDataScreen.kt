package com.hospitalfrontend.ui.profile
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) } // Estado para el modo edición
    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var apellido by remember { mutableStateOf(TextFieldValue("")) }
    var fechaNacimiento by remember { mutableStateOf(TextFieldValue("")) }
    var direccion by remember { mutableStateOf(TextFieldValue("")) }
    var lengua by remember { mutableStateOf(TextFieldValue("")) }
    var antecedentes by remember { mutableStateOf(TextFieldValue("")) }
    var alergias by remember { mutableStateOf(TextFieldValue("")) }
    var cuidadorNombre by remember { mutableStateOf(TextFieldValue("")) }
    var cuidadorTelefono by remember { mutableStateOf(TextFieldValue("")) }

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
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.flecha_atras),
                            contentDescription = "Volver",
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified // Esto evita que se aplique un color y mantiene el original del icono
                        )

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
                .verticalScroll(rememberScrollState()), // Permite hacer scroll
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de perfil
            Image(
                painter = painterResource(id = R.drawable.personaldata_icono), // Asegúrate de tener este recurso
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clickable { /* Acción para cambiar la imagen */ }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campos de entrada
            InputField(value = nombre, onValueChange = { nombre = it }, label = "Nombre", enabled = isEditing)
            InputField(value = apellido, onValueChange = { apellido = it }, label = "Apellidos", enabled = isEditing)
            InputField(value = fechaNacimiento, onValueChange = { fechaNacimiento = it }, label = "Fecha de nacimiento", enabled = isEditing)
            InputField(value = direccion, onValueChange = { direccion = it }, label = "Dirección", enabled = isEditing)
            InputField(value = lengua, onValueChange = { lengua = it }, label = "Lengua", enabled = isEditing)
            InputField(value = antecedentes, onValueChange = { antecedentes = it }, label = "Antecedentes médicos", enabled = isEditing)
            InputField(value = alergias, onValueChange = { alergias = it }, label = "Alergias", enabled = isEditing)

            Spacer(modifier = Modifier.height(10.dp))

            // Datos del cuidador
            Text(text = "Datos del Cuidador", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)

            InputField(value = cuidadorNombre, onValueChange = { cuidadorNombre = it }, label = "Nombre del cuidador", enabled = isEditing)
            InputField(value = cuidadorTelefono, onValueChange = { cuidadorTelefono = it }, label = "Teléfono del cuidador", enabled = isEditing)

            Spacer(modifier = Modifier.height(20.dp))

            // Botón Guardar
            if (isEditing) {
                Button(
                    onClick = { isEditing = false }, // Aquí podrías manejar la lógica para guardar los datos
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Guardar")
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        enabled = enabled
    )
}
