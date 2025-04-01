package com.hospitalfrontend.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hospitalfrontend.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDataScreen(navController: NavController) {
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu),
                        contentDescription = "Imagen del menú",
                        modifier = Modifier.size(150.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nombre de Usuario", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    DrawerItem("Datos Médicos") { navController.navigate("medical_data") }
                    DrawerItem("Datos Personales") { navController.navigate("personal_data") }
                    DrawerItem("Datos de Cuidado") { navController.navigate("care_data") }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Acción para salir */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Salir",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "HISTORIAL",
                                fontSize = 20.sp,
                                color = Color.Black,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    navigationIcon = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.flecha_atras),
                                    contentDescription = "Volver",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.menu_icono),
                                    contentDescription = "Menú",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Acción para crear */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.crear2_hospital),
                                contentDescription = "Crear",
                                modifier = Modifier.size(50.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen arriba
                Image(
                    painter = painterResource(id = R.drawable.historial_icono),
                    contentDescription = "Imagen de cabecera",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp)
                )

                // Tarjetas de datos de cuidado
                CareDataCard(
                    name = "MELIAN GUZMÁN, EDILBERTO MANUEL",
                    specialty = "MEDICINA GENERAL",
                    status = "Finalizada",
                    location = "Clínica Diagonal",
                    dateTime = "30 sep. 2024 04:15",
                    type = "URGENCIA MEDICINA INTERNA"
                )

                Spacer(modifier = Modifier.height(16.dp))

                CareDataCard(
                    name = "SETO TORRENT, NURIA",
                    specialty = "DERMATOLOGIA",
                    status = "Finalizada",
                    location = "Clínica Diagonal",
                    dateTime = "02 sep. 2024 11:30",
                    type = "URGENCIA MEDICINA INTERNA"
                )
            }
        }
    }
}

@Composable
fun CareDataCard(
    name: String,
    specialty: String,
    status: String,
    location: String,
    dateTime: String,
    type: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = specialty,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = status,
                        fontSize = 12.sp,
                        color = Color.Blue,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.hospital_icono), // Cambia por el ícono que desees
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = location,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.fecha_icono),
                        contentDescription = "Hora",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateTime,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = type,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Button(
                onClick = { /* Acción para ver detalles */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
            ) {
                Text(
                    text = "DETALLE",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}
