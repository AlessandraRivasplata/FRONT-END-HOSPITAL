package com.hospitalfrontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.model.Care
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed interface CareDetailUiState {
    object Idle : CareDetailUiState
    object Success : CareDetailUiState
    object Error : CareDetailUiState
    object Loading : CareDetailUiState
    object Initial : CareDetailUiState
}

class CareDetailViewModel : ViewModel() {

    private val _careDetailUiState = MutableStateFlow<CareDetailUiState>(CareDetailUiState.Initial)
    val careDetailUiState: StateFlow<CareDetailUiState> = _careDetailUiState

    private val _care = MutableStateFlow<Care?>(null)
    val care: StateFlow<Care?> = _care

    fun getCareById(id: Int) {
        viewModelScope.launch {
            _careDetailUiState.value = CareDetailUiState.Loading
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.118.3.202:8080/") // Ajusta la IP si cambia 10.118.3.202
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(RemoteMessageInterface::class.java)
                val response = api.getCareById(id)

                if (response.isSuccessful) {
                    response.body()?.let { careData ->
                        _care.value = careData
                        _careDetailUiState.value = CareDetailUiState.Success
                        Log.d("CareDetail", "Care loaded: $careData")
                    } ?: run {
                        Log.d("CareDetail", "Empty body in response")
                        _careDetailUiState.value = CareDetailUiState.Error
                    }
                } else {
                    Log.d("CareDetail", "Response error: ${response.code()}")
                    _careDetailUiState.value = CareDetailUiState.Error
                }
            } catch (e: Exception) {
                Log.e("CareDetail", "Exception: ${e.message}")
                _careDetailUiState.value = CareDetailUiState.Error
            }
        }
    }
}
