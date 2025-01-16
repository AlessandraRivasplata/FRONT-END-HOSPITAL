package com.hospitalfrontend.retrofitconfig

import com.hospitalfrontend.model.Nurse

sealed interface RemoteMessageUiState {
    data class Success(
        val remoteMessage: List<Nurse>
    ) : RemoteMessageUiState
    object Error : RemoteMessageUiState
    object Cargant : RemoteMessageUiState
}