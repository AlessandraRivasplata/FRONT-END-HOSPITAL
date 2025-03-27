package com.hospitalfrontend.ui.nurseinfo.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateNurseScreen(
    navController: NavHostController,
    updateNurseViewModel: UpdateNurseViewModel,
    nurseLoginViewModel: NurseLoginViewModel
) {
    val nurse by nurseLoginViewModel.nurse.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Crear estados mutables para cada campo
    var name by remember { mutableStateOf(nurse?.name ?: "No se pudo obtener el nombre") }
    var username by remember { mutableStateOf(nurse?.username ?: "No se pudo obtener el usuario") }
    var password by remember { mutableStateOf(nurse?.password ?: "No se pudo obtener la contraseña") }

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

        // Espacio para separar elementos
        Spacer(modifier = Modifier.height(30.dp))

        // Texto "Editar perfil"
        Text(
            text = "Editar perfil",
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 56.dp)
        )

        // Texto "Información personal"
        Text(
            text = "Información personal",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 140.dp)
        )

        // Usamos Column para apilar las Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp)
        ) {
            // Card "Nombre completo"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp), // Espacio entre tarjetas
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF262626))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Nombre completo",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = name,
                        onValueChange = { newValue -> name = newValue },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFF262626),
                            focusedIndicatorColor = Color(0xFFE73843),
                        ),
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true
                    )

                }
            }

            // Card "Nombre de usuario"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp), // Espacio entre tarjetas
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF262626))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Nombre de usuario",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = username,
                        onValueChange = { newValue -> username = newValue },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFF262626),
                            focusedIndicatorColor = Color(0xFFE73843)
                        ),
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true
                    )
                }
            }

            // Card "Contraseña"
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF262626))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Contraseña",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = password,
                        onValueChange = { newValue -> password = newValue },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xFF262626),
                            focusedIndicatorColor = Color(0xFFE73843)
                        ),
                        textStyle = TextStyle(color = Color.White),
                        singleLine = true,
                    )
                }
            }

            Spacer(modifier = Modifier.height(75.dp))

            Button(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lapizupdatenurse),
                        contentDescription = "Update cuenta icono",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Actualizar Datos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
/*
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        updateNurseViewModel.updateNurse(nurse?.id ?: 0, name, username, password)
                        nurseLoginViewModel.setName(name)
                        nurseLoginViewModel.setUsername(username)
                        nurseLoginViewModel.setPassword(password)
                        navController.navigate("home")
                    }
                ) {
                    Text("Confirmar cambios")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Confirmar cambios") },
            text = { Text("¿Estás seguro de que quieres actualizar estos datos?") }
        )
    }
    */
}
