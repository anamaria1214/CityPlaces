package com.example.demoapp.feature.recover_password.verification

import androidx.lifecycle.ViewModel
import com.example.demoapp.core.utils.RequestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VerificationViewModel : ViewModel() {

    private val codeLength = 6

    private val _verificationResult = MutableStateFlow<RequestResult?>(null)
    val verificationResult: StateFlow<RequestResult?> = _verificationResult.asStateFlow()

    private val _resendResult = MutableStateFlow<RequestResult?>(null)
    val resendResult: StateFlow<RequestResult?> = _resendResult.asStateFlow()

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code.asStateFlow()

    val isFormValid: Boolean
        get() = _code.value.length == codeLength

    fun onCodeChange(newValue: String) {
        if (newValue.length <= codeLength && newValue.all(Char::isDigit)) {
            _code.value = newValue
        }
    }

    fun verifyCode() {
        _verificationResult.value = if (isFormValid && _code.value == "123456") {
            RequestResult.Success("Codigo verificado correctamente")
        } else {
            RequestResult.Failure("Codigo invalido. Verifica e intenta de nuevo")
        }
    }

    fun resendCode() {
        _resendResult.value = RequestResult.Success("Se envio un nuevo codigo")
    }

    fun resetResults() {
        _verificationResult.value = null
        _resendResult.value = null
    }

    fun resetForm() {
        _code.value = ""
        resetResults()
    }
}

