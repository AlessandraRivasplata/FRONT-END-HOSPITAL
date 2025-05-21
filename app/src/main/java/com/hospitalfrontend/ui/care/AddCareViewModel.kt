package com.hospitalfrontend.ui.care

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.CreateCare
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface CreateCareUiState {
    object Idle : CreateCareUiState
    object Success : CreateCareUiState
    object Error : CreateCareUiState
    object Loading : CreateCareUiState
}

class CreateCareViewModel : ViewModel() {

    private val _createCareUiState = MutableStateFlow<CreateCareUiState>(CreateCareUiState.Idle)
    val createCareUiState: StateFlow<CreateCareUiState> = _createCareUiState

    fun createCare(createCare: CreateCare) {
        viewModelScope.launch {
            _createCareUiState.value = CreateCareUiState.Loading
            try {
                // Configuración de Retrofit para hacer la petición
                val retrofit = Retrofit.Builder()

                    .baseUrl("http://10.0.2.2:8080/") // Cambia la IP si es necesario //192.168.43.219:8080

                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = retrofit.create(RemoteMessageInterface::class.java)
                val response = endpoint.createCare(createCare)

                if (response.isSuccessful) {
                    Log.d("CreateCare", "Cura creada correctamente")
                    _createCareUiState.value = CreateCareUiState.Success
                } else {
                    Log.d("CreateCare", "Fallo al crear la cura: ${response.code()}")
                    _createCareUiState.value = CreateCareUiState.Error
                }

            } catch (e: Exception) {
                Log.e("CreateCare", "Error: ${e.message}")
                _createCareUiState.value = CreateCareUiState.Error
            }
        }
    }
}
