package com.hospitalfrontend.ui.authentication

import com.hospitalfrontend.model.Nurse
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NurseAuthViewModel : ViewModel() {
    private val _nurseName = MutableStateFlow<String>("")
    private val _nurseUsername = MutableStateFlow<String>("")
    private val _nursePassword = MutableStateFlow<String>("")

    private val nurses = mutableListOf(
        Nurse(1, "Paco Perez", "paco123", "pperez", null),
        Nurse(2, "Fran Rodriguez", "fran123", "frodriguez", null),
        Nurse(3, "Pepe Gomez", "pepe123", "pgomez", null)
    )

    fun login(username: String, password: String): Nurse? {
        return nurses.find { it.username == username && it.password == password }
    }

    fun register(name: String, username: String, password: String): Nurse? {
        if (nurses.any { it.username == username }) {
            return null
        }

        val newNurse = Nurse(
            id = nurses.size + 1,
            name = name,
            password = password,
            username = username,
            profileImage = null
        )
        nurses.add(newNurse)
        return newNurse
    }
    fun currentNurse(username: String, password: String): String {
        val nurse = nurses.find { it.username == username && it.password == password }
        val name = nurse?.name ?: ""
        setNurseName(name)
        return name
    }
    val nurseName: StateFlow<String> = _nurseName
    val nurseUsername: StateFlow<String> = _nurseUsername
    val nursePassword: StateFlow<String> = _nursePassword

    fun setNurseName(name: String) {
        _nurseName.value = name
    }
    fun setNurseUsername(username: String) {
        _nurseUsername.value = username
    }
    fun setNursePassword(password: String) {
        _nursePassword.value = password
    }
    fun resetFields() {
        setNurseName("")  // Limpia nurseName
        // Resetea otras variables del ViewModel, como username o password
        setNurseUsername("")
        setNursePassword("")
    }
}
