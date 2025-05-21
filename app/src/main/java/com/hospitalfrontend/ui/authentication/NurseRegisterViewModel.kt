package com.hospitalfrontend.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface CreateNurseUiState {
    object Idle : CreateNurseUiState
    object Success : CreateNurseUiState
    object Error : CreateNurseUiState
    object Loading : CreateNurseUiState
    object Initial : CreateNurseUiState
}

class NurseRegisterViewModel : ViewModel() {
    private val _createNurseUiState = MutableStateFlow<CreateNurseUiState>(CreateNurseUiState.Initial)
    val createNurseUiState: StateFlow<CreateNurseUiState> = _createNurseUiState

    fun createNurse(nurse: Nurse): String? {
        var message: String? = null
        viewModelScope.launch {
            _createNurseUiState.value = CreateNurseUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")//Sergi: http://10.0.22.114:8080
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.createNurse(nurse)

                if (response.isSuccessful) {
                    message = response.body()?.text
                    _createNurseUiState.value = CreateNurseUiState.Success
                } else {
                    message = "Error: ${response.errorBody()?.string()}"
                    _createNurseUiState.value = CreateNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.d("CreateNurse", "Error: ${e.message}")
                message = "Error: ${e.message}"
                _createNurseUiState.value = CreateNurseUiState.Error
            }
        }
        return message
    }
    fun resetRegisterState() {
        _createNurseUiState.value = CreateNurseUiState.Idle
    }
}
