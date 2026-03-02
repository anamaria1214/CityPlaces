package com.example.demoapp.core.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ValidatedField(
    initialValue: String,
    private val validator: (String) -> String?
) {
    var value by mutableStateOf(initialValue)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    val isValid: Boolean
        get() = error == null && value.isNotBlank()

    fun onChange(newValue: String) {
        value = newValue
        error = validator(newValue)
    }

    fun reset() {
        value = ""
        error = null
    }
}

