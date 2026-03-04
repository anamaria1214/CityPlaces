package com.example.demoapp.feature.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.R
import com.example.demoapp.core.utils.RequestResult
import kotlinx.coroutines.delay

@Composable
fun Login(
    onNavigateToUsers: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    // Estado para gestionar los snackbars
    val snackbarHostState = remember { SnackbarHostState() }
    // Observar el estado de loginResult
    val loginResult by viewModel.loginResult.collectAsState()

    // Efecto para mostrar el snackbar cuando hay resultado
    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            // Obtener el mensaje según el resultado
            val message = when (result) {
                is RequestResult.Success -> result.message
                is RequestResult.Failure -> result.errorMessage
            }
            snackbarHostState.showSnackbar(message) // Mostrar el snackbar con el mensaje

            // Navegar a la pantalla de usuarios si el login fue exitoso. Se puede agregar un delay para que el usuario alcance a ver el mensaje
            if (result is RequestResult.Success) {
                delay(1000) // 2 segundos
                onNavigateToUsers()
            }

            // Reseta el estado del loginResult en el ViewModel después de mostrar el mensaje
            viewModel.resetLoginResult()
        }
    }
    Scaffold (
        snackbarHost = {
            // Mostrar el SnackbarHost para gestionar los snackbars. Un SnackbarHost es un contenedor que muestra los snackbars.
            SnackbarHost(snackbarHostState) { data ->
                val isError = loginResult is RequestResult.Failure
                // Mostrar el Snackbar con el estilo adecuado según si es error o éxito
                Snackbar(
                    containerColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Text(data.visuals.message)
                }
            }
        }

    ) {
        paddingValues ->

    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(paddingValues)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_campaign_24),
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = Color(0xFF1E88E5)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D1B2A)
            )
            Text(
                text = "Log in to discover new places or manage touristic sites.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = {
                    viewModel.email.onChange(it)
                },
                label = { Text("Email Address") },
                isError = !viewModel.email.isValid, // Se muestra el borde rojo si hay error
                supportingText = viewModel.email.error?.let { error ->
                    { Text(text = error) }
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = android.R.drawable.ic_dialog_email),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = {
                    viewModel.password.onChange(it)
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        painterResource(id = android.R.drawable.ic_lock_idle_lock),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        Icon(
                            painter = painterResource(id = if (viewModel.passwordVisible) android.R.drawable.ic_menu_view else android.R.drawable.ic_partial_secure),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )

            TextButton(
                onClick = {
                },

                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password?", color = Color(0xFF1E88E5))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.login()
                },
                enabled = viewModel.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text("  Or continue with  ", color = Color.Gray, fontSize = 14.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SocialButton(iconRes = R.drawable.ios_logo)
                SocialButton(iconRes = android.R.drawable.ic_menu_send)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account? ", color = Color.Gray)
                TextButton(onClick = {

                }, contentPadding = PaddingValues(0.dp)) {
                    Text("Sign up", color = Color(0xFF1E88E5), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SocialButton(iconRes: Int) {
    OutlinedCard(
        modifier = Modifier.size(60.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
    }

}


fun validateEmail(email: String): String? {
    return when {
        email.isEmpty() -> "El email es obligatorio"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingresa un email válido"
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isEmpty() -> "La contraseña es obligatoria"
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
        else -> null
    }
}