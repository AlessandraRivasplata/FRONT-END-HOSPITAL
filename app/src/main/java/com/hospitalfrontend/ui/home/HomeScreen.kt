package com.hospitalfrontend.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hospitalfrontend.R

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hospital Management",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.cruz_home),
            contentDescription = "Cruz Home",
            modifier = Modifier
                .size(400.dp)
                .padding(bottom = 24.dp)
        )
        Button(
            onClick = { /* Desarrollo para el boton de login */ },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("find_nurse") }, // Navegar a FindNurseScreen
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Search Nurse by Name", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("all_nurses") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "View All Nurses", color = Color.White)
        }
    }
}

