package com.hospitalfrontend.ui.nurseinfo.byname

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseAuthViewModel
import com.hospitalfrontend.ui.nurseinfo.all.NurseItem

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FindNurseScreen(
    navController: NavController,
    nurseAuthViewModel: NurseAuthViewModel,
    findNurseViewModel: FindNurseByNameViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }

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

        // Back Button
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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 225.dp),
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
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Search Nurse",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Enter nurse name", color = Color.White) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { findNurseViewModel.findNurseByName(searchText) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE73843)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Search")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = findNurseViewModel.findNurseUiState) {
                    is FindNurseUiState.Loading -> {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is FindNurseUiState.Success -> {
                        state.nurses.forEach { nurse ->
                            NurseItem(
                                id = nurse.id,
                                name = nurse.name,
                                username = nurse.username
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    is FindNurseUiState.Error -> {
                        Text(
                            "No nurses found or error occurred",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    is FindNurseUiState.Initial -> {
                        // Initial state, show nothing or instructions
                    }
                }
            }
        }
    }
}