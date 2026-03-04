package com.example.demoapp.feature.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.RequestResult
import com.example.demoapp.core.utils.ValidatedField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableStateFlow<RequestResult?>(null)
    val loginResult: StateFlow<RequestResult?> = _loginResult.asStateFlow()
    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    val password = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "La contraseña es obligatoria"
            value.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
    }

    var passwordVisible by mutableStateOf<Boolean>(false)

    val isFormValid: Boolean
        get() = email.isValid
                && password.isValid

    fun resetForm() {
        email.reset()
        password.reset()
    }

    fun login() {
        if (isFormValid) {
            // Simulación de un proceso de login con datos estáticos
            _loginResult.value = if (email.value == "carlos@email.com" && password.value == "123456") {
                RequestResult.Success("Login exitoso")
            } else {
                RequestResult.Failure("Credenciales inválidas")
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

}