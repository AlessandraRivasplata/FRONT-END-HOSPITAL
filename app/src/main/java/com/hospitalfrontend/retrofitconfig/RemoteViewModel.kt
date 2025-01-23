package com.hospitalfrontend.retrofitconfig

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Nurse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteViewModel : ViewModel() {
    private val _remoteMessageUiState = MutableStateFlow<RemoteMessageUiState>(RemoteMessageUiState.Cargant)
    val remoteMessageUiState: StateFlow<RemoteMessageUiState> = _remoteMessageUiState

    fun getAllNurses() {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteMessageUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endPoint = connexio.create(RemoteMessageInterface::class.java)
                val response = endPoint.getAllNurses()

                _remoteMessageUiState.value = RemoteMessageUiState.Success(response.nurses)
            } catch (e: Exception) {
                Log.d("exemple", "RESPOSTA ERROR ${e.message} ${e.printStackTrace()}")
                _remoteMessageUiState.value = RemoteMessageUiState.Error
            }
        }
    }

    fun createNurse(nurse: Nurse) {
        viewModelScope.launch {
            _remoteMessageUiState.value = RemoteMessageUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endPoint = connexio.create(RemoteMessageInterface::class.java)
                val response = endPoint.createNurse(nurse)

                _remoteMessageUiState.value = RemoteMessageUiState.Success(response.nurses)
            } catch (e: Exception) {
                Log.d("exemple", "RESPOSTA ERROR ${e.message} ${e.printStackTrace()}")
                _remoteMessageUiState.value = RemoteMessageUiState.Error
            }
        }
    }

    fun findNurseByName(name: String) {
        viewModelScope.launch {
            remoteMessageUiState = RemoteMessageUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080") //192.168.43.219
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endPoint = connexio.create(RemoteMessageInterface::class.java)
                val response = endPoint.getNursesByName(name)

                if (response.isSuccessful) {
                    response.body()?.let { nurses ->
                        remoteMessageUiState = RemoteMessageUiState.Success(nurses)
                    } ?: run {
                        remoteMessageUiState = RemoteMessageUiState.Error
                    }
                } else {
                    remoteMessageUiState = RemoteMessageUiState.Error
                }
            } catch (e: Exception) {
                Log.d("exemple", "RESPOSTA ERROR ${e.message} ${e.printStackTrace()}")
                remoteMessageUiState = RemoteMessageUiState.Error
            }
        }
    }
}