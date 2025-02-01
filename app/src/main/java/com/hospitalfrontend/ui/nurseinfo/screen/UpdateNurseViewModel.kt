package com.hospitalfrontend.ui.nurseinfo.screen


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

sealed interface UpdateNurseUiState {
    object Success : UpdateNurseUiState
    object Error : UpdateNurseUiState
    object Loading : UpdateNurseUiState
    object Initial : UpdateNurseUiState
}

class UpdateNurseViewModel : ViewModel() {
    private val _updateNurseUiState = MutableStateFlow<UpdateNurseUiState>(UpdateNurseUiState.Initial)
    val updateNurseUiState: StateFlow<UpdateNurseUiState> = _updateNurseUiState

    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse

    fun updateNurse(id: Int, name: String, username: String, password: String) {
        viewModelScope.launch {
            _updateNurseUiState.value = UpdateNurseUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://192.168.43.219:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.updateNurse(id, name, username, password)

                if (response.isSuccessful) {
                    Log.d("UpdateNurse", "Nurse updated: ${_nurse.value}")
                    _updateNurseUiState.value = UpdateNurseUiState.Success
                } else {
                    _updateNurseUiState.value = UpdateNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.d("UpdateNurse", "Error: ${e.message}")
                _updateNurseUiState.value = UpdateNurseUiState.Error
            }
        }
    }
}

