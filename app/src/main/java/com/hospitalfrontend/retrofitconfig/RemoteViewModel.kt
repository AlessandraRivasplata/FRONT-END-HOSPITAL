package com.hospitalfrontend.retrofitconfig

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteViewModel : ViewModel() {
    var remoteMessageUiState by mutableStateOf<RemoteMessageUiState>(RemoteMessageUiState.Cargant)
        private set

    fun getAllNurses() {
        viewModelScope.launch {
            remoteMessageUiState = RemoteMessageUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://192.168.43.219:8080") // IP Jordi 192.168.43.219  || Resto del grupo 10.0.2.2
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endPoint = connexio.create(RemoteMessageInterface::class.java)
                val response = endPoint.getAllNurses()

                remoteMessageUiState = RemoteMessageUiState.Success(response.nurses)
            } catch (e: Exception) {
                Log.d("exemple", "RESPOSTA ERROR ${e.message} ${e.printStackTrace()}")
                remoteMessageUiState = RemoteMessageUiState.Error
            }
        }
    }

    fun findNurseByName(name: String) {
        viewModelScope.launch {
            remoteMessageUiState = RemoteMessageUiState.Cargant
            try {
                val connexio = Retrofit.Builder()
                    .baseUrl("http://192.168.43.219:8080") //192.168.43.219
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