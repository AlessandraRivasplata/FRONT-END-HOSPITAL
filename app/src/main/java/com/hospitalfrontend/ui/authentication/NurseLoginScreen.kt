import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hospitalfrontend.R
import com.hospitalfrontend.ui.authentication.NurseLoginUiState
import com.hospitalfrontend.ui.authentication.NurseLoginViewModel
import com.hospitalfrontend.ui.sharedViewModel.NurseSharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NurseLoginScreen(
    navController: NavController,
    nurseLoginViewModel: NurseLoginViewModel = viewModel(),
    nurseSharedViewModel: NurseSharedViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var isError by remember { mutableStateOf(false) }

    val nurseLoginUiState by nurseLoginViewModel.nurseLoginUiState.collectAsState()
    val nurse by nurseLoginViewModel.nurse.collectAsState()

    LaunchedEffect(nurseLoginUiState) {
        if (nurseLoginUiState is NurseLoginUiState.Success && nurse != null) {
            nurseSharedViewModel.nurse = nurse
            navController.navigate("list_rooms")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // ← Fondo blanco
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // ← Contenido centrado
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_hospitex),
                contentDescription = "Logo Hospitex",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Benvingut/da",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
            Text(
                text = "Identifica't com a infermer/a",
                fontSize = 16.sp,
                color = Color(0xFF34495E),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    isError = false
                },
                label = { Text("TCAI") },

                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.enfermera), // Añade este ícono a tus recursos
                        contentDescription = "User Icon",
                        tint = Color.Unspecified
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(64.dp),
                singleLine = true,
                isError = isError,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = Color(0xFF4A90E2),
                    unfocusedBorderColor = Color(0xFFBDC3C7),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFF4A90E2)
                ),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val usernameValue = username.text
                    isError = usernameValue.isEmpty() || !usernameValue.all { it.isDigit() }
                    if (!isError) {
                        val nurseNumber = usernameValue.toInt()
                        nurseLoginViewModel.login(nurseNumber)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8E6CF))
            ) {
                if (nurseLoginUiState is NurseLoginUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "INICIAR",
                        color = Color(0xFF2C3E50),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedVisibility(visible = isError || nurseLoginUiState is NurseLoginUiState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isError) "Introdueix un ID vàlid" else "No existeix aquest usuari",
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
