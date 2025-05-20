package com.hospitalfrontend.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.PatientResponseById
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface PatientUiState {
    object Idle : PatientUiState
    object Success : PatientUiState
    object Error : PatientUiState
    object Loading : PatientUiState
    object Initial : PatientUiState
}

class PatientDataViewModel : ViewModel() {
    private val _patientUiState = MutableStateFlow<PatientUiState>(PatientUiState.Initial)
    val patientUiState: StateFlow<PatientUiState> = _patientUiState

    private val _patient = MutableStateFlow<PatientResponseById?>(null)
    val patient: StateFlow<PatientResponseById?> = _patient

    fun getPatientById(id: Int) {
        viewModelScope.launch {
            _patientUiState.value = PatientUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") // Alessandra ip 10.0.2.2:8081 192.168.43.219
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getPatientById(id)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _patient.value = body
                        _patientUiState.value = PatientUiState.Success
                        Log.d("Patient", "Patient loaded: $body")
                    } ?: run {
                        Log.d("Patient", "No data in response body")
                        _patientUiState.value = PatientUiState.Error
                    }
                } else {
                    Log.d("Patient", "Response unsuccessful: ${response.code()}")
                    _patientUiState.value = PatientUiState.Error
                }
            } catch (e: Exception) {
                _patientUiState.value = PatientUiState.Error
                Log.d("Patient", "Error: ${e.message}")
            }
        }
    }
}
