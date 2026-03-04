package com.example.demoapp.feature.register

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

class RegisterViewModel : ViewModel() {

    val fullName = ValidatedField("") { value ->
        when {
            value.isBlank() -> "El nombre completo es obligatorio"
            value.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
            else -> null
        }
    }

    val city = ValidatedField("") { value ->
        when {
            value.isBlank() -> "La ciudad es obligatoria"
            else -> null
        }
    }

    val address = ValidatedField("") { value ->
        when {
            value.isBlank() -> "La dirección es obligatoria"
            else -> null
        }
    }

    val email = ValidatedField("") { value ->
        when {
            value.isBlank() -> "El email es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Ingresa un email válido"
            else -> null
        }
    }

    val password = ValidatedField("") { value ->
        when {
            value.isBlank() -> "La contraseña es obligatoria"
            !PASSWORD_REGEX.matches(value) ->
                "Debe tener al menos 8 caracteres, una mayúscula, un número y un símbolo"
            else -> null
        }
    }

    var termsAccepted by mutableStateOf(false)
        private set

    var showTermsError by mutableStateOf(false)
        private set

    val termsError: String?
        get() = if (showTermsError && !termsAccepted) "Debes aceptar los términos y condiciones" else null

    val isFormValid: Boolean
        get() = fullName.isValid
                && city.isValid
                && address.isValid
                && email.isValid
                && password.isValid
                && termsAccepted

    private val _registerResult = MutableStateFlow<RequestResult?>(null)
    val registerResult: StateFlow<RequestResult?> = _registerResult.asStateFlow()


    fun onTermsChange(accepted: Boolean) {
        termsAccepted = accepted
        showTermsError = true
    }

    fun register() {
        // Forzar validación de todos los campos al hacer submit
        fullName.onChange(fullName.value)
        city.onChange(city.value)
        address.onChange(address.value)
        email.onChange(email.value)
        password.onChange(password.value)
        showTermsError = true

        if (isFormValid) {
            _registerResult.value = RequestResult.Success("Cuenta creada exitosamente")
        } else {
            _registerResult.value = RequestResult.Failure("Por favor corrige los errores del formulario")
        }
    }

    fun resetForm() {
        fullName.reset()
        city.reset()
        address.reset()
        email.reset()
        password.reset()
        termsAccepted = false
        showTermsError = false
        _registerResult.value = null
    }

    companion object {
        // Al menos 8 chars, 1 mayúscula, 1 número, 1 símbolo especial
        private val PASSWORD_REGEX = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
    }
}