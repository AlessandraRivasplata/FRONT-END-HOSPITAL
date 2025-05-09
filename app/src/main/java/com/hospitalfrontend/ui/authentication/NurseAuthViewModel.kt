package com.hospitalfrontend.ui.authentication

import com.hospitalfrontend.model.Nurse
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NurseAuthViewModel : ViewModel() {

    private val _nurseName = MutableStateFlow("")
    private val _nurseUsername = MutableStateFlow("")
    private val _nursePassword = MutableStateFlow("")

    private val _nurses = MutableStateFlow<List<Nurse>>(listOf(

    ))

    // Public StateFlow to observe the list of nurses
    val nurses: StateFlow<List<Nurse>> = _nurses

    fun login(username: String, password: String): Nurse? {
        return _nurses.value.find { it.username == username && it.password == password }
    }

    fun register(name: String, username: String, password: String): Nurse? {
        if (_nurses.value.any { it.username == username }) {
            return null
        }

        val newNurse = Nurse(
            id = _nurses.value.size + 1,
            name = name,
            password = password,
            username = username,
            profileImage = null,
            nurseNumber = TODO()
        )

        _nurses.value = _nurses.value + newNurse // Add the new nurse to the list
        return newNurse
    }

    fun resetFields() {
        _nurseName.value = ""
        _nurseUsername.value = ""
        _nursePassword.value = ""
    }

    val nurseName: StateFlow<String> = _nurseName
    val nurseUsername: StateFlow<String> = _nurseUsername
    val nursePassword: StateFlow<String> = _nursePassword

    fun currentNurse(username: String, password: String): String {
        val nurse = nurses.value.find { it.username == username && it.password == password }
        val name = nurse?.name ?: ""
        setNurseName(name)
        return name
    }

    fun setNurseName(name: String) {
        _nurseName.value = name
    }

    fun setNurseUsername(username: String) {
        _nurseUsername.value = username
    }

    fun setNursePassword(password: String) {
        _nursePassword.value = password
    }
}