package com.hospitalfrontend.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hospitalfrontend.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NurseRegisterScreen(navController: NavController, nurseAuthViewModel: NurseAuthViewModel) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }
    var isRegisterSuccess by remember { mutableStateOf(false) }
    var isScreenLocked by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE73843)),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 1f)),
                                    startY = size.height - 250f,
                                    endY = size.height
                                )
                            )
                        }
                ) {
                    GlideImage(
                        model = R.drawable.gif,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .width(100.dp)
                        .padding(top = 100.dp)
                        .background(color = Color.White, shape = MaterialTheme.shapes.medium)
                )
                Image(
                    painter = painterResource(id = R.drawable.cruz_home),
                    contentDescription = "Cruz Home",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 100.dp)
                )
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
                        if (name.text.isNotEmpty() && username.text.isNotEmpty() && password.text.isNotEmpty()) {
                            if (nurseAuthViewModel.register(name.text, username.text, password.text) != null) {
                                isRegisterSuccess = true
                                isError = false
                            } else {
                                errorMessage = "El username ya existe"
                                isError = true
                                isRegisterSuccess = false
                            }
                        } else {
                            errorMessage = "Todos los campos son obligatorios"
                            isError = true
                            isRegisterSuccess = false
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

                if (isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                if (isRegisterSuccess) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cuenta creada con éxito.",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    LaunchedEffect(key1 = isRegisterSuccess) {
                        isScreenLocked = true
                        delay(3000)
                        navController.navigate("login_nurse") {
                            popUpTo("login_nurse") { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
