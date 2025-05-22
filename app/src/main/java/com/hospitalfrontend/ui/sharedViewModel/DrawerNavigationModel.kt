package com.hospitalfrontend.ui.sharedViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerNavigationViewModel : ViewModel() {
    private val _currentDrawerRoute = MutableStateFlow<String?>(null)
    val currentDrawerRoute: StateFlow<String?> = _currentDrawerRoute

    fun setCurrentDrawerRoute(route: String?) {
        _currentDrawerRoute.value = route
    }
}