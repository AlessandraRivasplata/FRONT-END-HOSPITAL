package com.hospitalfrontend.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.hospitalfrontend.R
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GlideImage(
            model = R.drawable.fondohomecorrected,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.fondo_cruz_home),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 125.dp)
                .size(125.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("find_nurse") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE73843).copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lupa),
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Search Nurse",
                        color = Color.White
                    )
                }
            }

            Button(
                onClick = { navController.navigate("all_nurses") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE73843).copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lista),
                        contentDescription = "List Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "View All Nurses",
                        color = Color.White
                    )
                }
            }
            Button(
                onClick = { navController.navigate("screen_nurse") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE73843).copy(alpha = 0.8f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.imagen_login_hospital),
                        contentDescription = "user icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                    Text(
                        text = "Perfil",
                        color = Color.White
                    )
                }
            }
        }
    }
}




