package com.example.demoapp.feature.recover_password.forgot_password

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.RequestResult
import com.example.demoapp.core.utils.ValidatedField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel : ViewModel() {

    private val _forgotPasswordResult = MutableStateFlow<RequestResult?>(null)
    val forgotPasswordResult: StateFlow<RequestResult?> = _forgotPasswordResult.asStateFlow()

    val email = ValidatedField("") { value ->
        when {
            value.isEmpty() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    val isFormValid: Boolean
        get() = email.isValid

    fun sendCode() {
        email.onChange(email.value)

        if (isFormValid) {
            _forgotPasswordResult.value = RequestResult.Success("Código enviado al correo")
        } else {
            _forgotPasswordResult.value = RequestResult.Failure(email.error ?: "Verifica el correo ingresado")
        }
    }

    fun resetForm() {
        email.reset()
    }

    fun resetForgotPasswordResult() {
        _forgotPasswordResult.value = null
    }
}