package com.example.demoapp.feature.recover_password.verification

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demoapp.core.utils.RequestResult

private val BlueAccent = Color(0xFF2979FF)
private val LightBlueBackground = Color(0xFFE8F0FE)

@Composable
@Preview
fun VerificationScreen(viewModel: VerificationViewModel = viewModel()) {
    val code by viewModel.code.collectAsState()
    val verificationResult by viewModel.verificationResult.collectAsState()
    val resendResult by viewModel.resendResult.collectAsState()

    VerificationContent(
        code = code,
        isFormValid = viewModel.isFormValid,
        verificationResult = verificationResult,
        resendResult = resendResult,
        onCodeChange = {
            viewModel.onCodeChange(it)
            if (verificationResult != null || resendResult != null) viewModel.resetResults()
        },
        onResendCodeClick = viewModel::resendCode,
        onVerifyCodeClick = viewModel::verifyCode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VerificationContent(
    code: String,
    isFormValid: Boolean,
    verificationResult: RequestResult?,
    resendResult: RequestResult?,
    onCodeChange: (String) -> Unit,
    onResendCodeClick: () -> Unit,
    onVerifyCodeClick: () -> Unit
) {
    val codeLength = 6

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recover Password",
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

            // Título
            Text(
                text = "Verification",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Descripción
            Text(
                text = "Enter the 6-digit verification code sent to your inbox.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Label
            Text(
                text = "Verification Code",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 6 cajas de código OTP
            BasicTextField(
                value = code,
                onValueChange = onCodeChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        repeat(codeLength) { index ->
                            val char = code.getOrNull(index)
                            val isFocused = code.length == index

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .border(
                                        width = if (isFocused) 1.5.dp else 1.dp,
                                        color = if (isFocused) BlueAccent else Color(0xFFE0E0E0),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char?.toString() ?: "",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )

            verificationResult?.let { result ->
                Spacer(modifier = Modifier.height(10.dp))
                val message = when (result) {
                    is RequestResult.Success -> result.message
                    is RequestResult.Failure -> result.errorMessage
                }
                val messageColor = if (result is RequestResult.Success) Color(0xFF2E7D32) else Color.Red
                Text(text = message, color = messageColor, fontSize = 13.sp)
            }

            resendResult?.let { result ->
                Spacer(modifier = Modifier.height(8.dp))
                val message = when (result) {
                    is RequestResult.Success -> result.message
                    is RequestResult.Failure -> result.errorMessage
                }
                val messageColor = if (result is RequestResult.Success) Color(0xFF2E7D32) else Color.Red
                Text(text = message, color = messageColor, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resend code
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onResendCodeClick) {
                    Text(
                        text = "Resend code",
                        color = BlueAccent,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            // Empujar el botón y la card hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botón Verify Code
            Button(
                onClick = onVerifyCodeClick,
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
            ) {
                Text(
                    text = "Verify Code",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card informativa
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlueBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = BlueAccent,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Wait up to 2 minutes for the code.\nRemember to check your junk/spam folder.",
                        color = Color(0xFF455A64),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerificationContentPreview() {
    VerificationContent(
        code = "123",
        isFormValid = false,
        verificationResult = null,
        resendResult = null,
        onCodeChange = {},
        onResendCodeClick = {},
        onVerifyCodeClick = {}
    )
}
