package com.hospitalfrontend.ui.sharedViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.hospitalfrontend.model.Nurse

class NurseSharedViewModel : ViewModel() {
    var nurse by mutableStateOf<Nurse?>(null)
}
