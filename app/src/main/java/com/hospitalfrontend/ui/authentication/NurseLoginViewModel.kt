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

sealed interface NurseLoginUiState {
    object Success : NurseLoginUiState
    object Error : NurseLoginUiState
    object Loading : NurseLoginUiState
    object Initial : NurseLoginUiState
}

class NurseLoginViewModel : ViewModel() {
    private val _nurseLoginUiState = MutableStateFlow<NurseLoginUiState>(NurseLoginUiState.Initial)
    val nurseLoginUiState: StateFlow<NurseLoginUiState> = _nurseLoginUiState

    fun login(username: String, password: String): String? {
        var message: String? = null
        viewModelScope.launch {
            _nurseLoginUiState.value = NurseLoginUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8081")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.login(username, password)

                if (response.isSuccessful) {
                    message = "Login exitoso"
                    _nurseLoginUiState.value = NurseLoginUiState.Success
                } else {
                    message = "Error: ${response.errorBody()?.string()}"
                    _nurseLoginUiState.value = NurseLoginUiState.Error
                }
            } catch (e: Exception) {
                Log.d("NurseLogin", "Error: ${e.message}")
                message = "Error: ${e.message}"
                _nurseLoginUiState.value = NurseLoginUiState.Error
            }
        }
        return message
    }
}