package com.hospitalfrontend.ui.care

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Care
import com.hospitalfrontend.model.CareResponse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface CaresUiState {
    object Idle : CaresUiState
    object Success : CaresUiState
    object Error : CaresUiState
    object Loading : CaresUiState
    object Initial : CaresUiState
}

class CaresDataViewModel : ViewModel() {
    private val _caresUiState = MutableStateFlow<CaresUiState>(CaresUiState.Initial)
    val caresUiState: StateFlow<CaresUiState> = _caresUiState

    private val _cares = MutableStateFlow<List<Care>>(emptyList())
    val cares: StateFlow<List<Care>> = _cares

    fun getCaresByPatientId(id: Int) {
        viewModelScope.launch {
            _caresUiState.value = CaresUiState.Loading
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/") // Ajusta la URL si cambia 192.168.1.65 10.118.0.51 10.118.3.202
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = retrofit.create(RemoteMessageInterface::class.java)
                val response = endpoint.getCaresByPatientId(id)

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _cares.value = body.data
                        _caresUiState.value = CaresUiState.Success
                        Log.d("Cares", "Loaded: ${body.data}")
                    } ?: run {
                        Log.d("Cares", "Empty body")
                        _caresUiState.value = CaresUiState.Error
                    }
                } else {
                    Log.d("Cares", "Unsuccessful response: ${response.code()}")
                    _caresUiState.value = CaresUiState.Error
                }
            } catch (e: Exception) {
                Log.d("Cares", "Error: ${e.message}")
                _caresUiState.value = CaresUiState.Error
            }
        }
    }
}
