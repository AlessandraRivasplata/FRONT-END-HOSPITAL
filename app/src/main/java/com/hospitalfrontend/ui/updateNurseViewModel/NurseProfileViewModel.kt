package com.hospitalfrontend.ui.updateNurseViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hospitalfrontend.retrofitconfig.RemoteMessageInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Define a sealed interface for the UI state
sealed interface NurseProfileUiState {
    data class Success(val profileData: Map<String, Any>) : NurseProfileUiState
    object Error : NurseProfileUiState
    object Loading : NurseProfileUiState
    object Initial : NurseProfileUiState
}

class NurseProfileViewModel : ViewModel() {

    private val _nurseProfileUiState = MutableStateFlow<NurseProfileUiState>(NurseProfileUiState.Initial)
    val nurseProfileUiState: StateFlow<NurseProfileUiState> = _nurseProfileUiState

    fun fetchNurseProfile(nurseId: Int) {
        viewModelScope.launch {
            _nurseProfileUiState.value = NurseProfileUiState.Loading
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080") // Ensure this matches your backend URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(RemoteMessageInterface::class.java)
                val response = api.getNurseProfile(nurseId)

                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _nurseProfileUiState.value = NurseProfileUiState.Success(data)
                        Log.d("NurseProfileVM", "Profile data loaded: $data")
                    } ?: run {
                        _nurseProfileUiState.value = NurseProfileUiState.Error
                        Log.d("NurseProfileVM", "Response body is null")
                    }
                } else {
                    _nurseProfileUiState.value = NurseProfileUiState.Error
                    Log.d("NurseProfileVM", "Error fetching profile: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _nurseProfileUiState.value = NurseProfileUiState.Error
                Log.e("NurseProfileVM", "Exception fetching profile: ${e.message}", e)
            }
        }
    }
}