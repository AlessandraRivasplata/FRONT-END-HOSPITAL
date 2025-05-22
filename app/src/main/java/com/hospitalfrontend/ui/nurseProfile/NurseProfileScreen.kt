package com.hospitalfrontend.ui.nurseprofile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel
import com.hospitalfrontend.ui.profile.InputField // Assuming InputField is accessible
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseProfileScreen(
    navController: NavController,
    nurseLoginViewModel: NurseLoginViewModel = viewModel(LocalContext.current as ComponentActivity), // Get from Activity scope if shared
    nurseProfileViewModel: NurseProfileViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity) // To get nurseName for drawer
) {
    val loggedInNurseState by nurseLoginViewModel.nurse.collectAsState()
    val profileUiState by nurseProfileViewModel.nurseProfileUiState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentLoggedInNurseName = nurseSharedViewModel.nurse?.name ?: "Infermer/a"


    LaunchedEffect(loggedInNurseState) {
        loggedInNurseState?.id?.let { nurseId ->
            nurseProfileViewModel.fetchNurseProfile(nurseId)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color(0xFFE0F2F1) // Consistent drawer color
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.medico_menu), // Nurse icon for drawer
                        contentDescription = "Imatge del menú",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(top = 8.dp)
                            .clickable {
                                // Optionally, make this image also navigate or perform an action
                                // For now, it's just visual like in other drawers
                                scope.launch { drawerState.close() }
                                // If you want the image itself to navigate to profile again (though you are on it)
                                // navController.navigate("nurse_profile")
                            }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentLoggedInNurseName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color(0xFF004D40)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Add nurse-specific drawer items here if needed, e.g., settings
                    // For now, mainly "Sortir" (Back to rooms list)

                    Spacer(modifier = Modifier.weight(1f))
                    Divider(color = Color(0xFFB2DFDB), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                scope.launch { drawerState.close() }
                                navController.navigate("list_rooms") {
                                    popUpTo("list_rooms") { inclusive = true } // Clear back stack to rooms
                                }
                            }
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icono),
                            contentDescription = "Tornar a Habitacions",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified // Use original icon color
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tornar a Habitacions",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF004D40)
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE0F7FA).copy(alpha = 0.3f)), // Subtle cyan background
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PERFIL INFERMER/A",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00695C), // Teal color for harmony
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center
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
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Tornar enrere",
                                    modifier = Modifier.size(25.dp),
                                    tint = Color(0xFF00695C)
                                )
                            }
                        }
                    },
                    actions = {
                        // You can add actions like an Edit button here if needed in the future
                        // For example:
                        // TextButton(onClick = { /* navController.navigate("edit_nurse_profile") */ }) {
                        //     Text("Editar", color = Color.Black, fontSize = 18.sp)
                        // }
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
                Image(
                    painter = painterResource(id = R.drawable.enfermera_login), // Main profile image
                    contentDescription = "Imatge de Perfil",
                    modifier = Modifier
                        .size(160.dp)
                        .padding(bottom = 24.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.Crop
                )

                when (val state = profileUiState) {
                    is NurseProfileUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
                    }
                    is NurseProfileUiState.Success -> {
                        val profileData = state.profileData
                        InputField(
                            value = TextFieldValue(profileData["id"]?.toString() ?: "N/A"),
                            onValueChange = {},
                            label = "ID Infermer/a",
                            enabled = false
                        )
                        InputField(
                            value = TextFieldValue(profileData["name"]?.toString() ?: "N/A"),
                            onValueChange = {},
                            label = "Nom",
                            enabled = false
                        )
                        InputField(
                            value = TextFieldValue(profileData["username"]?.toString() ?: "N/A"),
                            onValueChange = {},
                            label = "Nom d'usuari",
                            enabled = false
                        )
                        InputField(
                            value = TextFieldValue(profileData["email"]?.toString() ?: "N/A"),
                            onValueChange = {},
                            label = "Correu Electrònic",
                            enabled = false
                        )
                        InputField(
                            value = TextFieldValue(profileData["nurseNumber"]?.toString() ?: "N/A"),
                            onValueChange = {},
                            label = "Número d'Infermer/a",
                            enabled = false
                        )
                    }
                    is NurseProfileUiState.Error -> {
                        Text(
                            "Error al carregar el perfil.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 32.dp)
                        )
                    }
                    is NurseProfileUiState.Initial -> {
                        if (loggedInNurseState == null) {
                            Text(
                                "Si us plau, inicia sessió per veure el perfil.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        } else {
                            Text(
                                "Carregant perfil...",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        nurseLoginViewModel.clearNurse()
                        navController.navigate("login_nurse") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D40)),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Tancar Sessió")
                }
            }
        }
    }
}

