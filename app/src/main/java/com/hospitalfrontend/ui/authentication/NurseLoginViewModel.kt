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
    object Idle : NurseLoginUiState
    object Success : NurseLoginUiState
    object Error : NurseLoginUiState
    object Loading : NurseLoginUiState
    object Initial : NurseLoginUiState
}

class NurseLoginViewModel : ViewModel() {
    private val _nurseLoginUiState = MutableStateFlow<NurseLoginUiState>(NurseLoginUiState.Initial)
    val nurseLoginUiState: StateFlow<NurseLoginUiState> = _nurseLoginUiState

    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse

    fun login(nurseNumber: Int) {
        viewModelScope.launch {
            _nurseLoginUiState.value = NurseLoginUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.118.0.51:8080") //ip del servidor springboot
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.login(nurseNumber)

                if (response.isSuccessful) {
                    _nurse.value = response.body() // Variable where the nurse's info is stored
                    Log.d("NurseLogin", "Nurse logged in: ${_nurse.value}")
                    _nurseLoginUiState.value = NurseLoginUiState.Success
                } else {
                    _nurseLoginUiState.value = NurseLoginUiState.Error
                }
            } catch (e: Exception) {
                Log.d("NurseLogin", "Error: ${e.message}")
                _nurseLoginUiState.value = NurseLoginUiState.Error
            }
        }
    }

    fun clearNurse() {
        _nurse.value = null
    }

    fun resetLoginState() {
        _nurseLoginUiState.value = NurseLoginUiState.Idle
    }
}
