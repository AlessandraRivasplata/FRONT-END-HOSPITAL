package com.hospitalfrontend.ui.nurseinfo.all

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel

@Composable
fun AllNursesScreen(
    navController: NavController,
    nurseAuthViewModel: NurseAuthViewModel
) {
    val nurses = nurseAuthViewModel.nurses.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFB74D4D), Color.Black))),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "ALL NURSES",
            style = androidx.compose.material3.MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .shadow(elevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                nurses.value.forEach { nurse ->
                    NurseItem(id = nurse.id, name = nurse.name, username = nurse.username)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier
                .width(200.dp)
                .height(56.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF5C5C), Color(0xFFD74D4D))
                    )
                )
        ) {
            Text(
                text = "GO BACK",
                color = Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
    }
}

@Composable
fun NurseItem(id: Int, name: String, username: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFC62828), Color(0xFF000000))), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Brush.verticalGradient(colors = listOf(Color(0xFFFF5C5C), Color(0xFFD74D4D))), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagen_login_hospital),
                contentDescription = "Imagen de usuario",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "Id: $id", color = Color.White, fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
            Text(text = "Name: $name", color = Color.White, fontSize = 16.sp ,fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
            Text(text = "Username: $username", color = Color.White, fontSize = 16.sp,fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
        }
    }
}
