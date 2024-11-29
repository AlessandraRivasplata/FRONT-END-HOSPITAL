package com.hospitalfrontend.ui.find

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FindNurseScreen(navController: NavController) {
    val nurses = listOf(
        "Id 1: name = Paco Perez | username = pperez | password = paco123",
        "Id 2: name = Pepe Rodriguez | username = prodriguez | password = pepe123",
        "Id 3: name = Fran Gomez | username = fgomez | password = fran123",
        "Id 4: name = Paco Lopez | username = plopez | password = paco456",
        "Id 5: name = Paco Rodriguez | username = parodriguez | password = paco789"
    )

    var input by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<String>>(emptyList()) }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Find Nurse by Name or Username",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter name, surname, or username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Botón para buscar
        Button(
            onClick = {
                val found = nurses.filter { nurse ->
                    nurse.lowercase().contains(input.lowercase())
                }
                if (found.isNotEmpty()) {
                    results = found
                    error = false
                } else {
                    results = emptyList()
                    error = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Enter", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (results.isNotEmpty()) {
            Text(
                text = "Results:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            results.forEach { result ->
                Text(
                    text = result,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        if (error) {
            Text(
                text = "Error: No nurse found with the provided input.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}
