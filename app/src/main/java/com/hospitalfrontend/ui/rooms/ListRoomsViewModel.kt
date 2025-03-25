package com.hospitalfrontend.ui.rooms

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Room
import com.hospitalfrontend.model.RoomResponse
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface RoomsUiState {
    object Idle : RoomsUiState
    object Success : RoomsUiState
    object Error : RoomsUiState
    object Loading : RoomsUiState
    object Initial : RoomsUiState
}

class ListRoomsViewModel : ViewModel() {
    private val _roomsUiState = MutableStateFlow<RoomsUiState>(RoomsUiState.Initial)
    val roomsUiState: StateFlow<RoomsUiState> = _roomsUiState

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    fun getAllRooms() {
        viewModelScope.launch {
            _roomsUiState.value = RoomsUiState.Loading
            try {
                val connection = Retrofit.Builder()
                    .baseUrl("http://10.118.0.51:8080/") // Asegúrate de que la URL de base sea correcta
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val endpoint = connection.create(RemoteMessageInterface::class.java)
                val response = endpoint.getAllRooms()

                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        _rooms.value = body.data
                        _roomsUiState.value = RoomsUiState.Success
                        Log.d("Rooms", "Rooms loaded: ${body.data}")
                    } ?: run {
                        Log.d("Rooms", "No data in response body")
                        _roomsUiState.value = RoomsUiState.Error
                    }
                } else {
                    Log.d("Rooms", "Response unsuccessful: ${response.code()}")
                    _roomsUiState.value = RoomsUiState.Error
                }
            } catch (e: Exception) {
                _roomsUiState.value = RoomsUiState.Error
                Log.d("Rooms", "Error: ${e.message}")
            }
        }
    }
}

