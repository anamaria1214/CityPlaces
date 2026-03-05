package com.example.demoapp.feature.recover_password.reset_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.RequestResult
import com.example.demoapp.core.utils.ValidatedField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResetPasswordViewModel : ViewModel() {

    private val _resetPasswordResult = MutableStateFlow<RequestResult?>(null)
    val resetPasswordResult: StateFlow<RequestResult?> = _resetPasswordResult.asStateFlow()

    val newPassword = ValidatedField("") { value ->
        when {
            value.isBlank() -> "La contraseña es obligatoria"
            value.length < 8 -> "La contraseña debe tener al menos 8 caracteres"
            !value.any(Char::isDigit) || !value.any { !it.isLetterOrDigit() } ->
                "Debe incluir números y símbolos"
            else -> null
        }
    }

    val confirmPassword = ValidatedField("") { value ->
        when {
            value.isBlank() -> "Confirma la contraseña"
            value != newPassword.value -> "Las contraseñas no coinciden"
            else -> null
        }
    }

    var newPasswordVisible by mutableStateOf(false)
        private set

    var confirmPasswordVisible by mutableStateOf(false)
        private set

    val hasMinLength: Boolean
        get() = newPassword.value.length >= 8

    val hasNumbersAndSymbols: Boolean
        get() = newPassword.value.any(Char::isDigit) && newPassword.value.any { !it.isLetterOrDigit() }

    val isFormValid: Boolean
        get() = newPassword.isValid && confirmPassword.isValid

    fun onNewPasswordChange(value: String) {
        newPassword.onChange(value)
        // Revalida confirmacion al cambiar la contraseña principal.
        if (confirmPassword.value.isNotEmpty() || confirmPassword.error != null) {
            confirmPassword.onChange(confirmPassword.value)
        }
    }

    fun onConfirmPasswordChange(value: String) {
        confirmPassword.onChange(value)
    }

    fun toggleNewPasswordVisibility() {
        newPasswordVisible = !newPasswordVisible
    }

    fun toggleConfirmPasswordVisibility() {
        confirmPasswordVisible = !confirmPasswordVisible
    }

    fun resetPassword() {
        newPassword.onChange(newPassword.value)
        confirmPassword.onChange(confirmPassword.value)

        _resetPasswordResult.value = if (isFormValid) {
            RequestResult.Success("Contraseña actualizada exitosamente")
        } else {
            RequestResult.Failure("Revisa los campos antes de continuar")
        }
    }

    fun resetResult() {
        _resetPasswordResult.value = null
    }

    fun resetForm() {
        newPassword.reset()
        confirmPassword.reset()
        newPasswordVisible = false
        confirmPasswordVisible = false
        _resetPasswordResult.value = null
    }
}

