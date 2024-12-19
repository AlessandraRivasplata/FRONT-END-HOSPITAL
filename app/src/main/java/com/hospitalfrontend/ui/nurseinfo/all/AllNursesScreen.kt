package com.hospitalfrontend.ui.nurseinfo.all

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel

@Composable
fun AllNursesScreen(
    navController: NavController,
    nurseAuthViewModel: NurseAuthViewModel
) {
    // Observe the list of nurses from the ViewModel
    val nurses = nurseAuthViewModel.nurses.collectAsState(initial = emptyList())

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

        // Dynamically iterate over the list of nurses
        nurses.value.forEach { nurse ->
            NurseItem(id = nurse.id, name = nurse.name, username = nurse.username)
            Spacer(modifier = Modifier.height(8.dp))
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
