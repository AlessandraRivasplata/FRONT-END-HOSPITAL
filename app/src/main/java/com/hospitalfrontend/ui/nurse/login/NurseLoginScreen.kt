package com.hospitalfrontend.ui.nurse.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NurseLoginScreen(navController: NavController) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }
    var isLoginSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Título
        Text(
            text = "Login de Enfermero",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        )

        // Campo de usuario
        TextField(
            value = username,
            onValueChange = {
                username = it
                isError = false // Limpia el estado de error al cambiar el valor
            },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        TextField(
            value = password,
            onValueChange = {
                password = it
                isError = false // Limpia el estado de error al cambiar el valor
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = isError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de validación
        Button(
            onClick = {
                if (validateCredentials(username.text, password.text)) {
                    isLoginSuccess = true
                    isError = false
                } else {
                    isError = true // Marca error si las credenciales son incorrectas
                    isLoginSuccess = false
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesión", color = Color.White)
        }

        // Mensaje de error
        if (isError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Usuario o contraseña incorrectos. Intenta nuevamente.",
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Mensaje de éxito
        if (isLoginSuccess) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Login correcto",
                color = Color.Green,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para volver
        Button(
            onClick = { navController.popBackStack() }, // Vuelve hacia atrás
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}

// Función de validación de credenciales (dummy)
private fun validateCredentials(username: String, password: String): Boolean {
    val validCredentials = mapOf(
        "pperez" to "paco123",
        "prodriguez" to "pepe123",
        "fgomez" to "fran123"
    )
    return validCredentials[username] == password
}


