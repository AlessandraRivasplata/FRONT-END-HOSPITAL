package com.hospitalfrontend.ui.rooms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.model.Room

@Composable
fun ListRoomScreen(navController: NavController, listRoomsViewModel: ListRoomsViewModel = viewModel()) {
    val roomsUiState by listRoomsViewModel.roomsUiState.collectAsState()
    val rooms by listRoomsViewModel.rooms.collectAsState()

    LaunchedEffect(Unit) {
        listRoomsViewModel.getAllRooms()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Image(
            painter = painterResource(id = R.drawable.hospital_bed_2775552),
            contentDescription = "LOGO ROOM",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(max = 700.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Habitaciones",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    when (roomsUiState) {
                        is RoomsUiState.Loading -> {
                            CircularProgressIndicator()
                        }
                        is RoomsUiState.Success -> {
                            LazyColumn(
                                contentPadding = PaddingValues(vertical = 8.dp),
                                modifier = Modifier.heightIn(max = 550.dp)
                            ) {
                                items(rooms) { room ->
                                    RoomItem(room = room, onClick = {
                                        // Navegación o lógica al seleccionar la habitación
                                    })
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                        is RoomsUiState.Error -> {
                            Text(
                                text = "Error al cargar las habitaciones",
                                color = Color.Red,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: Room, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF78C9E4))
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Habitación ${room.roomNumber}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Piso ${room.floor}",
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}