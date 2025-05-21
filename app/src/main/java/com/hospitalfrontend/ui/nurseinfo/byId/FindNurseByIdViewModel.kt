package com.hospitalfrontend.ui.nurseinfo.byId

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface FindNurseByIdUiState {
    object Success : FindNurseByIdUiState
    object Error : FindNurseByIdUiState
    object Loading : FindNurseByIdUiState
    object Initial : FindNurseByIdUiState
}

class FindNurseByIdViewModel : ViewModel() {
    var findNurseByIdUiState: FindNurseByIdUiState by mutableStateOf(FindNurseByIdUiState.Initial)
        private set

    private val _nurse = MutableStateFlow<Nurse?>(null)
    val nurse: StateFlow<Nurse?> = _nurse

    fun findNurseById(id: Int) {
        viewModelScope.launch {
            findNurseByIdUiState = FindNurseByIdUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getNurseById(id)

                if (response.isSuccessful) {
                        _nurse.value = response.body()
                        Log.d("FindById", "Nursebyid: ${_nurse.value}")
                        findNurseByIdUiState = FindNurseByIdUiState.Success
                } else {
                    findNurseByIdUiState = FindNurseByIdUiState.Error
                }
            } catch (e: Exception) {
                Log.d("FindNurseById", "Error: ${e.message}")
                findNurseByIdUiState = FindNurseByIdUiState.Error
            }
        }
    }
}
