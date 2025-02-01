package com.hospitalfrontend.ui.nurseinfo.byId

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hospitalfrontend.R

@Composable
fun FindNurseByIdScreen(
    navController: NavHostController,
    findNurseByIdViewModel: FindNurseByIdViewModel,
    id: Int
) {
    val nurse by findNurseByIdViewModel.nurse.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        findNurseByIdViewModel.findNurseById(id)
    }

    val uiState by remember { derivedStateOf { findNurseByIdViewModel.findNurseByIdUiState } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.TopStart),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.flecha_atras),
                contentDescription = "Flecha Atrás",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
    when (uiState) {
        is FindNurseByIdUiState.Loading -> CircularProgressIndicator(
            color = Color.White,
        )

        is FindNurseByIdUiState.Error -> Text(
            "Error al obtener los datos del usuario",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
        )

        is FindNurseByIdUiState.Success -> {
            nurse?.let {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Foto de perfil
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.imagen_login_hospital),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Nombre
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Username
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.username_icon),
                            contentDescription = "Username icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = it.username,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Contraseña con opción de mostrar/ocultar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.candado_password),
                            contentDescription = "Password icon",
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isPasswordVisible) it.password else "********",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(
                                id = if (isPasswordVisible) R.drawable.ojo_abierto else R.drawable.ojo_cerrado
                            ),
                            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isPasswordVisible = !isPasswordVisible },
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            } ?: Text(
                "No se encontró la enfermera",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        is FindNurseByIdUiState.Initial -> {}
    }
}

