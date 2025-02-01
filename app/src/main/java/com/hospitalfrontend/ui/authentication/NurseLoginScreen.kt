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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardType
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
fun NurseLoginScreen(navController: NavController, nurseLoginViewModel: NurseLoginViewModel) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val nurseLoginUiState by nurseLoginViewModel.nurseLoginUiState.collectAsState()

    LaunchedEffect(Unit) {
        nurseLoginViewModel.resetLoginState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Section
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

            // Background Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
            )
        }

        // Login Form Section
        Box(
            modifier = Modifier.fillMaxSize(),
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
                // Welcome Text
                Text(
                    text = "Bienvenido!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Username Field
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

                // Password Field
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

                // Login Button
                Button(
                    onClick = {
                        nurseLoginViewModel.login(username.text, password.text)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Entrar", color = Color.White)
                }

                // Navigate to Register Screen
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { navController.navigate("register_nurse") }
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "¿No tienes cuenta?",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                when (nurseLoginUiState) {
                    is NurseLoginUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is NurseLoginUiState.Success -> {
                        Text(text = "Login exitoso", color = Color.Green, modifier = Modifier.align(Alignment.CenterHorizontally))
                        LaunchedEffect(Unit) {
                            delay(2000)
                            navController.navigate("home")
                        }
                    }
                    is NurseLoginUiState.Error -> {
                        Text(text = "Error de login", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    else -> {}
                }
            }
        }
    }
}
