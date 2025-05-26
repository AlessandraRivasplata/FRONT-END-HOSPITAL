package com.hospitalfrontend.ui.grafic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.VitalSignsDTO
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface VitalSignsUiState {
    object Idle : VitalSignsUiState
    object Success : VitalSignsUiState
    object Error : VitalSignsUiState
    object Loading : VitalSignsUiState
    object Initial : VitalSignsUiState
}

class VitalSignsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<VitalSignsUiState>(VitalSignsUiState.Initial)
    val uiState: StateFlow<VitalSignsUiState> = _uiState

    private val _vitalSigns = MutableStateFlow<List<VitalSignsDTO>?>(null)
    val vitalSigns: StateFlow<List<VitalSignsDTO>?> = _vitalSigns

    fun getVitalSignsByPatientId(patientId: Int) {
        viewModelScope.launch {
            _uiState.value = VitalSignsUiState.Loading
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.43.219:8080/") // Usa la IP o localhost adecuada
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(RemoteMessageInterface::class.java)
                val response = service.getVitalSignsByPatientId(patientId)

                if (response.isSuccessful) {
                    response.body()?.let { vitals ->
                        _vitalSigns.value = vitals
                        _uiState.value = VitalSignsUiState.Success
                        Log.d("VitalSigns", "Vital signs loaded: $vitals")
                    } ?: run {
                        Log.d("VitalSigns", "No data in response body")
                        _uiState.value = VitalSignsUiState.Error
                    }
                } else {
                    Log.d("VitalSigns", "Response unsuccessful: ${response.code()}")
                    _uiState.value = VitalSignsUiState.Error
                }
            } catch (e: Exception) {
                Log.d("VitalSigns", "Error: ${e.message}")
                _uiState.value = VitalSignsUiState.Error
            }
        }
    }
}
