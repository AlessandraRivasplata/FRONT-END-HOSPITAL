package com.hospitalfrontend.ui.nurseinfo.all

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ButtonDefaults
import com.hospitalfrontend.R // Asegúrate de que el paquete sea correcto para tus recursos

@Composable
fun AllNursesScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "All Nurses",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val nurses = listOf(
            Triple(1, "Paco Perez", "pperez"),
            Triple(2, "Pepe Rodriguez", "prodriguez"),
            Triple(3, "Fran Gomez", "fgomez")
        )

        nurses.forEach { (id, name, username) ->
            NurseItem(id, name, username)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() }, // Vuelve hacia atrás
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver", color = Color.White)
        }
    }
}

@Composable
fun NurseItem(id: Int, name: String, username: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagen_login_hospital),
            contentDescription = "Imagen de usuario",
            modifier = Modifier
                .size(64.dp)
                .padding(end = 8.dp)
        )
        Column {
            Text(text = "Id: $id")
            Text(text = "Name: $name")
            Text(text = "Username: $username")
        }
    }
}
