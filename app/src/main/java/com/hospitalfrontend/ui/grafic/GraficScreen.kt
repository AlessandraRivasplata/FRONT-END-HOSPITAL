import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hospitalfrontend.ui.grafic.VitalSignsViewModel
import kotlin.math.roundToInt

data class Scale<T>(
    val values: List<T>,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraficScreen(
    navController: NavHostController,
    patientId: Int,
    viewModel: VitalSignsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val vitalSigns by viewModel.vitalSigns.collectAsState()

    LaunchedEffect(patientId) {
        viewModel.getVitalSignsByPatientId(patientId)
    }

    // Formatear fechas
    val timeLabels = vitalSigns?.map { formatRecordedAt(it.recordedAt.toString()) } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gráfico Constantes Vitales",
                        fontSize = 18.sp,
                        color = Color(0xFF00695C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tornar enrere",
                            modifier = Modifier.size(25.dp),
                            tint = Color(0xFF00695C)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        val scrollState = rememberScrollState()
        val canvasWidth = 2000.dp
        val canvasHeight = 600.dp
        val scaleSteps = 10

        val scales = listOf(
            Scale((50 downTo 5 step 5).take(scaleSteps), Color.Black),
            Scale((270 downTo 70 step 20).take(scaleSteps), Color(0xFF00897B)),
            Scale((140 downTo 60 step 8).take(scaleSteps), Color(0xFF1565C0)),
            Scale(generateDecimalScale(39.0, 34.0, scaleSteps), Color(0xFFD32F2F))
        )

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .horizontalScroll(scrollState)
        ) {
            Row {
                // Escalas verticales
                Row(
                    modifier = Modifier
                        .height(canvasHeight)
                        .background(Color.White)
                ) {
                    scales.forEach { scale ->
                        Column(
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            scale.values.forEach { value ->
                                Text(
                                    text = when (value) {
                                        is Float -> String.format("%.1f", value)
                                        else -> value.toString()
                                    },
                                    fontSize = 10.sp,
                                    color = scale.color
                                )
                            }
                        }
                    }
                }

                // Columna que contiene el canvas y las fechas debajo
                Column {
                    // Canvas principal
                    Box {
                        Canvas(
                            modifier = Modifier
                                .width(canvasWidth)
                                .height(canvasHeight)
                                .background(Color.White)
                        ) {
                            drawAlignedGrid(scaleSteps, Color.LightGray)
                        }
                    }

                    // Fila de fechas justo debajo del canvas (gráfico)
                    Row(
                        modifier = Modifier
                            .width(canvasWidth)
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        timeLabels.forEach { label ->
                            Box(
                                modifier = Modifier.width(40.dp),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Text(text = label, fontSize = 10.sp, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Función para formatear las fechas desde ISO 8601 a "HH:mm dd-MM-yyyy"
@SuppressLint("NewApi")
fun formatRecordedAt(dateString: String): String {
    val inputFormatter = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val outputFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
    return try {
        val parsed = java.time.ZonedDateTime.parse(dateString, inputFormatter)
        parsed.format(outputFormatter)
    } catch (e: Exception) {
        dateString
    }
}

fun generateDecimalScale(max: Double, min: Double, steps: Int): List<Float> {
    val stepSize = (max - min) / (steps - 1)
    return List(steps) { i -> (max - i * stepSize).toFloat() }
}

fun DrawScope.drawAlignedGrid(horizontalLines: Int, lineColor: Color) {
    val width = size.width
    val height = size.height
    val stepY = height / (horizontalLines - 1)

    // Líneas horizontales
    for (i in 0 until horizontalLines) {
        val y = i * stepY
        drawLine(
            color = lineColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 1f
        )
    }

    // Líneas verticales cada 40.dp aprox
    val gridSizePx = 40.dp.toPx()
    var x = 0f
    while (x <= width) {
        drawLine(
            color = lineColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f
        )
        x += gridSizePx
    }
}



