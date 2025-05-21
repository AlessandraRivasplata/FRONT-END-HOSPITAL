package com.hospitalfrontend.ui.nurseinfo.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface DeleteNurseUiState {
    object Success : DeleteNurseUiState
    object Error : DeleteNurseUiState
    object Loading : DeleteNurseUiState
    object Initial : DeleteNurseUiState
}

class DeleteNurseViewModel : ViewModel() {
    private val _deleteNurseUiState = MutableStateFlow<DeleteNurseUiState>(DeleteNurseUiState.Initial)

    fun deleteNurse(id: Int) {
        viewModelScope.launch {
            _deleteNurseUiState.value = DeleteNurseUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.deleteNurseById(id)

                if (response.isSuccessful) {
                    Log.d("DeleteNurse", "Nurse deleted successfully")
                    _deleteNurseUiState.value = DeleteNurseUiState.Success
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.d("DeleteNurse", "Error: $errorBody")
                    _deleteNurseUiState.value = DeleteNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.d("DeleteNurse", "ErRoR: ${e.message}")
                _deleteNurseUiState.value = DeleteNurseUiState.Error
            }
        }
    }
}
