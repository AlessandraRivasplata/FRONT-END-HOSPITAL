package com.hospitalfrontend.ui.authentication

import com.hospitalfrontend.model.Nurse
import androidx.lifecycle.ViewModel

class NurseAuthViewModel : ViewModel() {

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

    fun listNurses() {
        nurses.forEach { println(it) }
    }
}
