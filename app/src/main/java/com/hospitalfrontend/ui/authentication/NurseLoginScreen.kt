package com.hospitalfrontend.ui.authentication

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseLoginScreen(
    navController: NavController,
    nurseLoginViewModel: NurseLoginViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    val nurseLoginUiState by nurseLoginViewModel.nurseLoginUiState.collectAsState()
    val nurse by nurseLoginViewModel.nurse.collectAsState() // Observamos el nurse actual

    LaunchedEffect(nurseLoginUiState) {
        if (nurseLoginUiState is NurseLoginUiState.Success && nurse != null) {
            nurseSharedViewModel.nurse = nurse // Guardamos en el shared
            navController.navigate("list_rooms")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_hospitex),
                    contentDescription = "LOGO HOSPITEX",
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Título
                Text(
                    text = "Benvingut!",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
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
                    label = { Text("Usuari") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(70.dp),
                    singleLine = true,
                    isError = isError,
                    shape = RoundedCornerShape(0.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color(0xFFF6F6F6),
                        focusedBorderColor = Color(0xFF4A90E2),
                        unfocusedBorderColor = Color(0xFFBDC3C7),
                        focusedTextColor = Color(0xFF4A90E2)
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Login Button
                Button(
                    onClick = {
                        val usernameValue = username.text

                        // Verificar si el campo está vacío o si el texto no es un número
                        if (usernameValue.isEmpty()) {
                            isError = true
                        } else if (!usernameValue.all { it.isDigit() }) {
                            // Si el valor no es un número
                            isError = true
                        } else {
                            val nurseNumber = usernameValue.toInt()
                            nurseLoginViewModel.login(nurseNumber)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78C9E4))
                ) {
                    if (nurseLoginUiState is NurseLoginUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Entrar",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Mensaje de error si el login falla
                if (nurseLoginUiState is NurseLoginUiState.Error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No existeix aquest usuari", color = Color.Red)
                }
            }
        }
    }
}
