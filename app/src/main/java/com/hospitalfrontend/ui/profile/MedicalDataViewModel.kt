package com.hospitalfrontend.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.DiagnosisResponse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface MedicalDataUiState {
    object Idle : MedicalDataUiState
    object Success : MedicalDataUiState
    object Error : MedicalDataUiState
    object Loading : MedicalDataUiState
    object Initial : MedicalDataUiState
}

class MedicalDataViewModel : ViewModel() {

    private val _medicalDataUiState = MutableStateFlow<MedicalDataUiState>(MedicalDataUiState.Initial)
    val medicalDataUiState: StateFlow<MedicalDataUiState> = _medicalDataUiState

    private val _diagnosis = MutableStateFlow<DiagnosisResponse?>(null)
    val diagnosis: StateFlow<DiagnosisResponse?> = _diagnosis

    fun getDiagnosisByPatientId(id: Int) {
        viewModelScope.launch {
            _medicalDataUiState.value = MedicalDataUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://192.168.1.65:8080/") //10.118.0.51  192.168.1.65
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getDiagnosisByPatientId(id)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _diagnosis.value = body
                        _medicalDataUiState.value = MedicalDataUiState.Success
                        Log.d("Diagnosis", "Diagnosis loaded: $body")
                    } ?: run {
                        Log.d("Diagnosis", "No data in response body")
                        _medicalDataUiState.value = MedicalDataUiState.Error
                    }
                } else {
                    Log.d("Diagnosis", "Response unsuccessful: ${response.code()}")
                    _medicalDataUiState.value = MedicalDataUiState.Error
                }
            } catch (e: Exception) {
                _medicalDataUiState.value = MedicalDataUiState.Error
                Log.d("Diagnosis", "Error: ${e.message}")
            }
        }
    }
}
