package com.example.demoapp.feature.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.core.ui.ErrorModal
import com.example.demoapp.core.utils.RequestResult

// Colores de la pantalla (extraídos de la imagen)
private val BlueAccent = Color(0xFF1E88E5)
private val LightGray = Color(0xFFF5F5F5)
private val DarkText = Color(0xFF1A1A2E)
private val SubtitleGray = Color(0xFF757575)
private val ErrorRed = Color(0xFFD32F2F)
private val DividerGray = Color(0xFFBDBDBD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val registerResult by viewModel.registerResult.collectAsState()
    val errorModal by viewModel.errorModal.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    // Modal de error genérico — se muestra sobre cualquier contenido
    ErrorModal(
        state = errorModal,
        onDismiss = { viewModel.clearError() }
    )

    // Lista de ciudades de ejemplo para el Dropdown
    val cities = listOf("New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Miami")
    var expandedCityDropdown by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Account",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Título principal
            Text(
                text = "Join the Community",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtítulo
            Text(
                text = "Help improve your city by sharing beautiful\nplaces and discovering new ones.",
                fontSize = 13.sp,
                color = SubtitleGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------- Full Name --------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.fullName.value,
                onValueChange = { viewModel.fullName.onChange(it) },
                label = { Text("Full Name") },
                placeholder = { Text("John Doe", color = Color(0xFFBDBDBD)) },
                isError = viewModel.fullName.error != null,
                supportingText = viewModel.fullName.error?.let { error ->
                    { Text(text = error, color = ErrorRed) }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = registerTextFieldColors()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // -------- City (Dropdown) --------
            ExposedDropdownMenuBox(
                expanded = expandedCityDropdown,
                onExpandedChange = { expandedCityDropdown = !expandedCityDropdown }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    value = viewModel.city.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("City") },
                    placeholder = { Text("Select your city", color = Color(0xFFBDBDBD)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCityDropdown)
                    },
                    isError = viewModel.city.error != null,
                    supportingText = viewModel.city.error?.let { error ->
                        { Text(text = error, color = ErrorRed) }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = registerTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedCityDropdown,
                    onDismissRequest = { expandedCityDropdown = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                viewModel.city.onChange(city)
                                expandedCityDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // -------- Address --------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.address.value,
                onValueChange = { viewModel.address.onChange(it) },
                label = { Text("Address") },
                placeholder = { Text("123 Street Name", color = Color(0xFFBDBDBD)) },
                isError = viewModel.address.error != null,
                supportingText = viewModel.address.error?.let { error ->
                    { Text(text = error, color = ErrorRed) }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = registerTextFieldColors()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // -------- Email --------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.email.value,
                onValueChange = { viewModel.email.onChange(it) },
                label = { Text("Email") },
                placeholder = { Text("john.doe@invalid", color = Color(0xFFBDBDBD)) },
                isError = viewModel.email.error != null,
                supportingText = viewModel.email.error?.let { error ->
                    { Text(text = error, color = ErrorRed) }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(8.dp),
                colors = registerTextFieldColors()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // -------- Password --------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.password.value,
                onValueChange = { viewModel.password.onChange(it) },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                // Reemplazamos el icono del ojo por un texto clicable (MOSTRAR/OCULTAR)
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "OCULTAR" else "MOSTRAR",
                        color = SubtitleGray,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .padding(end = 8.dp)
                    )
                },
                isError = viewModel.password.error != null,
                supportingText = viewModel.password.error?.let { error ->
                    { Text(text = error, color = ErrorRed) }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(8.dp),
                colors = registerTextFieldColors()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // -------- Terms & Conditions --------
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = viewModel.termsAccepted,
                        onCheckedChange = { viewModel.onTermsChange(it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BlueAccent,
                            uncheckedColor = if (viewModel.termsError != null) ErrorRed else DividerGray
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("I agree to the ")
                            withStyle(style = SpanStyle(color = BlueAccent)) {
                                append("Terms of Service")
                            }
                            append(" and ")
                            withStyle(style = SpanStyle(color = BlueAccent)) {
                                append("Privacy Policy")
                            }
                            append(".")
                        },
                        fontSize = 13.sp,
                        color = DarkText
                    )
                }
                if (viewModel.termsError != null) {
                    Text(
                        text = viewModel.termsError!!,
                        color = ErrorRed,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------- Resultado (éxito/error) --------
            registerResult?.let { result ->
                when (result) {
                    is RequestResult.Success -> {
                        Text(
                            text = result.message,
                            color = Color(0xFF388E3C),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    is RequestResult.Failure -> {
                        Text(
                            text = result.errorMessage,
                            color = ErrorRed,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // -------- Botón Create Account --------
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------- Already have an account? Log In --------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account? ",
                    fontSize = 13.sp,
                    color = SubtitleGray
                )
                Text(
                    text = "Log In",
                    fontSize = 13.sp,
                    color = BlueAccent,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------- OR SIGN UP WITH --------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerGray,
                    thickness = 1.dp
                )
                Text(
                    text = "  OR SIGN UP WITH  ",
                    fontSize = 11.sp,
                    color = SubtitleGray,
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerGray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------- Botones Google / iOS --------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Google
                OutlinedButton(
                    onClick = { /* TODO: Google Sign-In */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DividerGray),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    // Reemplaza con tu ícono de Google real:
                    // Image(painter = painterResource(id = R.drawable.ic_google), ...)
                    Text(text = "G", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                }

                // iOS / Apple
                OutlinedButton(
                    onClick = { /* TODO: Apple Sign-In */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DividerGray),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    // Reemplaza con tu ícono de Apple real:
                    // Image(painter = painterResource(id = R.drawable.ic_apple), ...)
                    Text(text = "iOS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun registerTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF1E88E5),
    unfocusedBorderColor = Color(0xFFBDBDBD),
    errorBorderColor = Color(0xFFD32F2F),
    focusedLabelColor = Color(0xFF1E88E5),
    unfocusedLabelColor = Color(0xFF757575),
    errorLabelColor = Color(0xFFD32F2F),
    cursorColor = Color(0xFF1E88E5),
    errorCursorColor = Color(0xFFD32F2F),
)