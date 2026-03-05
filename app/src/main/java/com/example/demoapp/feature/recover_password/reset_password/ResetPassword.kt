package com.example.demoapp.feature.recover_password.reset_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.core.utils.RequestResult

private val BlueAccent = Color(0xFF2979FF)
private val GreenCheck = Color(0xFF4CAF50)

@Composable
@Preview
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = viewModel()) {
    val requestResult by viewModel.resetPasswordResult.collectAsState()

    ResetPasswordContent(
        newPassword = viewModel.newPassword.value,
        confirmPassword = viewModel.confirmPassword.value,
        newPasswordError = viewModel.newPassword.error,
        confirmPasswordError = viewModel.confirmPassword.error,
        newPasswordVisible = viewModel.newPasswordVisible,
        confirmPasswordVisible = viewModel.confirmPasswordVisible,
        hasMinLength = viewModel.hasMinLength,
        hasNumbersAndSymbols = viewModel.hasNumbersAndSymbols,
        isFormValid = viewModel.isFormValid,
        requestResult = requestResult,
        onNewPasswordChange = {
            viewModel.onNewPasswordChange(it)
            if (requestResult != null) viewModel.resetResult()
        },
        onConfirmPasswordChange = {
            viewModel.onConfirmPasswordChange(it)
            if (requestResult != null) viewModel.resetResult()
        },
        onToggleNewPasswordVisibility = viewModel::toggleNewPasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::toggleConfirmPasswordVisibility,
        onUpdatePasswordClick = viewModel::resetPassword
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResetPasswordContent(
    newPassword: String,
    confirmPassword: String,
    newPasswordError: String?,
    confirmPasswordError: String?,
    newPasswordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    hasMinLength: Boolean,
    hasNumbersAndSymbols: Boolean,
    isFormValid: Boolean,
    requestResult: RequestResult?,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onToggleNewPasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onUpdatePasswordClick: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reset Password",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: navigate back */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
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
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create New Password",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Create a new, strong password for your account.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = onNewPasswordChange,
                label = { Text("New Password", color = Color.Gray) },
                placeholder = { Text("Enter your new password", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onToggleNewPasswordVisibility) {
                        Icon(
                            painter = painterResource(
                                id = if (newPasswordVisible) android.R.drawable.ic_menu_view else android.R.drawable.ic_secure
                            ),
                            contentDescription = if (newPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = newPasswordError != null,
                supportingText = newPasswordError?.let { error -> { Text(text = error, color = Color.Red) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = BlueAccent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirm New Password", color = Color.Gray) },
                placeholder = { Text("Confirm new password", color = Color(0xFFBDBDBD)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onToggleConfirmPasswordVisibility) {
                        Icon(
                            painter = painterResource(
                                id = if (confirmPasswordVisible) android.R.drawable.ic_menu_view else android.R.drawable.ic_secure
                            ),
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Gray
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let { error -> { Text(text = error, color = Color.Red) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = BlueAccent
                )
            )

            requestResult?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                val message = when (result) {
                    is RequestResult.Success -> result.message
                    is RequestResult.Failure -> result.errorMessage
                }
                val messageColor = if (result is RequestResult.Success) Color(0xFF2E7D32) else Color.Red
                Text(text = message, color = messageColor, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            PasswordRequirement(
                text = "At least 8 characters",
                isMet = hasMinLength
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordRequirement(
                text = "Include numbers and symbols",
                isMet = hasNumbersAndSymbols
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onUpdatePasswordClick,
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Text(
                    text = "Update Password",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PasswordRequirement(
    text: String,
    isMet: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(
                id = if (isMet) android.R.drawable.radiobutton_on_background
                else android.R.drawable.radiobutton_off_background
            ),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = if (isMet) GreenCheck else Color.Gray
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = if (isMet) Color.Black else Color.Gray,
            fontSize = 13.sp
        )
    }
}
