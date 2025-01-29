package com.hospitalfrontend.ui.nurseinfo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel

@Composable
fun NurseInfoScreen(navController: NavHostController, nurseAuthViewModel: NurseAuthViewModel) {
    val nurseName by nurseAuthViewModel.nurseName.collectAsState()
    val nurseUsername by nurseAuthViewModel.nurseUsername.collectAsState()
    val nursePassword by nurseAuthViewModel.nursePassword.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Botón de volver atras
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE73843), shape = CircleShape)
                    .padding(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.flecha_atras),
                    contentDescription = "Flecha Atrás",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Botón de configuración
            Button(
                onClick = {
                    //Reedirigir a pantalla de config de usuario
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE73843), shape = CircleShape)
                    .padding(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.configicon),
                    contentDescription = "Icono de Configuración",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(90.dp))

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

        // Nombre
        Text(
            text = nurseName,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Username
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.username_icon),
                    contentDescription = "Username icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nurseUsername,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contraseña con opción de mostrar/ocultar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.candado_password),
                    contentDescription = "Password icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPasswordVisible) nursePassword else "********",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
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
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de logout
        Button(
            onClick = {
                navController.navigate("login_nurse") {
                    popUpTo("home") { inclusive = true }
                }
                nurseAuthViewModel.resetFields()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logoutimage),
                    contentDescription = "Logout icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Text(
                    text = "Logout",
                    color = Color.White
                )
            }
        }

        // Botón de actualizar datos de usuario
        Button(
            onClick = {
                // Acción para actualizar datos de usuario
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Actualizar Datos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }

        // Botón de borrar cuenta
        Button(
            onClick = {
                // Acción para borrar cuenta
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.skulldeletenurse),
                    contentDescription = "Borrar cuenta icono",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Borrar Cuenta",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}
