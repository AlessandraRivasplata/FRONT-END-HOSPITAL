package com.hospitalfrontend.ui.nurseinfo.byname

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface FindNurseUiState {
    data class Success(val nurses: List<Nurse>) : FindNurseUiState
    object Error : FindNurseUiState
    object Loading : FindNurseUiState
    object Initial : FindNurseUiState
}

class FindNurseByNameViewModel : ViewModel() {
    var findNurseUiState: FindNurseUiState by mutableStateOf(FindNurseUiState.Initial)
        private set

    fun findNurseByName(name: String) {
        viewModelScope.launch {
            findNurseUiState = FindNurseUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getNursesByName(name)

                if (response.isSuccessful) {
                    response.body()?.let { nurses ->
                        findNurseUiState = FindNurseUiState.Success(nurses)
                    } ?: run {
                        findNurseUiState = FindNurseUiState.Error
                    }
                } else {
                    findNurseUiState = FindNurseUiState.Error
                }
            } catch (e: Exception) {
                Log.d("FindNurse", "Error: ${e.message}")
                findNurseUiState = FindNurseUiState.Error
            }
        }
    }
}