package com.hospitalfrontend.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.hospitalfrontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseLoginScreen(navController: NavController, nurseLoginViewModel: NurseLoginViewModel) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)), // Fondo más suave
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp), // Elevación más sutil
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp), // Padding más generoso
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_hospitex),
                    contentDescription = "LOGO HOSPITEX",
                    modifier = Modifier.size(200.dp) // Tamaño ajustado
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Título
                Text(
                    text = "Bienvenido!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50), // Color más elegante
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Username Field
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isError = false
                    },
                    label = { Text("Usuario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(70.dp), // Aumenta la altura del campo de texto
                    singleLine = true,
                    isError = isError,
                    shape = RoundedCornerShape(0.dp), // Bordes cuadrados
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color(0xFF4A90E2),
                        unfocusedBorderColor = Color(0xFFBDC3C7)
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp // Aumenta el tamaño de la fuente dentro del TextField
                    )
                )



                Spacer(modifier = Modifier.height(20.dp))

                // Login Button
                Button(
                    onClick = {
                        if (username.text.isEmpty()) {
                            isError = true
                        } else {
                            // Aquí va la lógica de autenticación
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Botón de mayor altura
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78C9E4)) // Botón azul
                ) {
                    Text(
                        text = "Entrar",
                        color = Color.White,
                        fontSize = 18.sp, // Texto más grande para el botón
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
