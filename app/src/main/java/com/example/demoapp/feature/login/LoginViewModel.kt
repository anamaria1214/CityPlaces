package com.example.demoapp.feature.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.ErrorModalState
import com.example.demoapp.core.utils.ValidatedField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // -------- Modal de error genérico --------
    private val _errorModal = MutableStateFlow<ErrorModalState?>(null)
    val errorModal: StateFlow<ErrorModalState?> = _errorModal.asStateFlow()

    fun showError(title: String = "Error", message: String) {
        _errorModal.value = ErrorModalState(title = title, message = message)
    }

    fun clearError() {
        _errorModal.value = null
    }

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

}