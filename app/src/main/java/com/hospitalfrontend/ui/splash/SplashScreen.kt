package com.hospitalfrontend.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hospitalfrontend.MainActivity
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.rooms.ListRoomsViewModel
import com.hospitalfrontend.ui.rooms.RoomsUiState
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {

    private val viewModel: ListRoomsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContent {
            val uiState by viewModel.roomsUiState.collectAsState()

            SplashScreenContent(
                uiState = uiState,
                onRetry = { viewModel.getAllRooms() }
            )

            LaunchedEffect(uiState) {
                if (uiState == RoomsUiState.Initial) {
                    viewModel.getAllRooms()
                } else if (uiState == RoomsUiState.Success) {
                    delay(1000)
                    startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreenContent(
    uiState: RoomsUiState,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.wife_visiting_her_ill_husband),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo circular centrado
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_hospitex),
                contentDescription = null,
                modifier = Modifier.size(135.dp)
            )
        }

        // Círculo de carga
        if (uiState == RoomsUiState.Loading) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // Botón de reintentar
        if (uiState == RoomsUiState.Error) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
        }
    }
}
