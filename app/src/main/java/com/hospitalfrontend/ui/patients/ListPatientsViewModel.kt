package com.hospitalfrontend.ui.patients

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Patient
import com.hospitalfrontend.model.PatientResponse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface PatientsUiState {
    object Idle : PatientsUiState
    object Success : PatientsUiState
    object Error : PatientsUiState
    object Loading : PatientsUiState
    object Initial : PatientsUiState
}

class ListPatientsViewModel : ViewModel() {
    private val _patientsUiState = MutableStateFlow<PatientsUiState>(PatientsUiState.Initial)
    val patientsUiState: StateFlow<PatientsUiState> = _patientsUiState

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    fun getAllPatientsByRoomNumber(roomNumber: Int) {
        viewModelScope.launch {
            _patientsUiState.value = PatientsUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://192.168.1.65:8080/") // Verifica que sea la URL correcta 10.118.0.51 192.168.1.35 192.168.1.65
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getAllPatientsByRoomNumber(roomNumber)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _patients.value = body.data
                        _patientsUiState.value = PatientsUiState.Success
                        Log.d("URL", "Request: ${endpoint.getAllPatientsByRoomNumber(roomNumber)}")
                        Log.d("Patients", "Patients loaded: ${body.data}")
                    } ?: run {
                        Log.d("Patients", "No data in response body")
                        _patientsUiState.value = PatientsUiState.Error
                    }
                } else {
                    Log.d("Patients", "Response unsuccessful: ${response.code()}")
                    _patientsUiState.value = PatientsUiState.Error
                }
            } catch (e: Exception) {
                _patientsUiState.value = PatientsUiState.Error
                Log.d("Patients", "Error: ${e.message}")
            }
        }
    }
}

