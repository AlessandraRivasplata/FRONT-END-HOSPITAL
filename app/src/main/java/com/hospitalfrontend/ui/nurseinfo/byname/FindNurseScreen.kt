package com.hospitalfrontend.ui.nurseinfo.byname

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShap
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FindNurseScreen(
    navController: NavController,
    nurseAuthViewModel: NurseAuthViewModel
) {
    val nurses = nurseAuthViewModel.nurses.collectAsState(initial = emptyList())
    var input by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<String>>(emptyList()) }
    var error by remember { mutableStateOf(false) }

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
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
        }
        // Search Form Section
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
                // Search Header
                Text(
                    text = "Search Nurse",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Search Input Field
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Nombre o usuario") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Button
                Button(
                    onClick = {
                        val found = nurses.value.filter { nurse ->
                            nurse.name.contains(input, ignoreCase = true) ||
                                    nurse.username.contains(input, ignoreCase = true)
                        }.map { nurse -> "Id ${nurse.id}: name = ${nurse.name} | username = ${nurse.username}" }

                        if (found.isNotEmpty()) {
                            results = found
                            error = false
                        } else {
                            results = emptyList()
                            error = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Buscar", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Results
                if (results.isNotEmpty()) {
                    Text(
                        text = "Resultados:",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    results.forEach { result ->
                        val details = result.split("|")
                        val name = details[0].split("=")[1].trim()
                        val username = details[1].split("=")[1].trim()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.imagen_login_hospital),
                                contentDescription = "Nurse Image",
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "Name: $name",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Username: $username",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                // Display Error Message
                if (error) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No se encontraron coincidencias.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
