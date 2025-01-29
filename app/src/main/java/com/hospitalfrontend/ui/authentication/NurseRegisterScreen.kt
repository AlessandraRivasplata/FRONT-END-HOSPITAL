package com.hospitalfrontend.ui.authentication

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.model.Nurse
import kotlinx.coroutines.delay

@Composable
fun NurseRegisterScreen(
    navController: NavController,
    createNurseViewModel: NurseRegisterViewModel // Aquí recibimos el NurseRegisterViewModel
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isScreenLocked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Estado local para el UI
    val createNurseUiState by createNurseViewModel.createNurseUiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE73843)),
                contentAlignment = Alignment.TopCenter
            ) {
                // Aquí iría la imagen de fondo, si es necesario
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .background(
                        color = Color.DarkGray.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        isError = false
                    },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError,
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isError = false
                    },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError,
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isError = false
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (isPasswordVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado
                                ),
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                            val nurse = Nurse(id = 0, name = name, username = username, password = password) // Asegúrate de enviar un id
                            createNurseViewModel.createNurse(nurse) // Usamos el ViewModel para crear la enfermera
                        } else {
                            errorMessage = "Todos los campos son obligatorios"
                            isError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isScreenLocked
                ) {
                    Text(text = "Crear Cuenta", color = Color.White)
                }

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable(
                            enabled = !isScreenLocked,
                            onClick = { navController.navigate("login_nurse") }
                        )
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "¿Ya tienes cuenta? Iniciar sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }

                // Observa el estado del ViewModel para mostrar mensajes de éxito o error
                when (createNurseUiState) {
                    is CreateNurseUiState.Success -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cuenta creada con éxito.",
                            color = Color.Green,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        LaunchedEffect(key1 = createNurseUiState) {
                            isScreenLocked = true
                            delay(3000)
                            navController.navigate("login_nurse") {
                                popUpTo("login_nurse") { inclusive = true }
                            }
                        }
                    }
                    is CreateNurseUiState.Error -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Hubo un error al crear la cuenta.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    is CreateNurseUiState.Loading -> {
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    else -> {}
                }
            }
        }
    }
}
