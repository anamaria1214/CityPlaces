package com.example.demoapp.core.utils

/**
 * Representa el estado del modal de error genérico.
 * @param title   Título del modal (personalizable por pantalla).
 * @param message Mensaje descriptivo del error.
 */
data class ErrorModalState(
    val title: String = "Error",
    val message: String = ""
)

