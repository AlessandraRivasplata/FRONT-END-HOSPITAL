package com.hospitalfrontend.ui.nurseinfo.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            horizontalArrangement = Arrangement.Start
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

        Spacer(modifier = Modifier.height(90.dp))

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

        Text(
            text = nurseName,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = nursePassword,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.navigate("login_nurse") {
                    popUpTo("home") { inclusive = true }
                }
                nurseAuthViewModel.resetFields()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE73843).copy(alpha = 0.8f)
            )
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
    }
}

