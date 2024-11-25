package com.example.tourtailor.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tourtailor.R
import com.example.tourtailor.ui.theme.AppTypography
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }


    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.register_header),
                style = AppTypography.displaySmall.copy(fontWeight = FontWeight.W900),
                modifier = Modifier.padding(bottom = 70.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.login_email_label)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),// Add rounded corners
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between TextFields
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.login_password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp)) // Add some space between TextFields
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.register_confirm_password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                isError = password != confirmPassword
            )
            if (password != confirmPassword) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Button(
                onClick = {
                    authViewModel.register(email, password) { success, error ->
                        if (success) {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            currentUser?.let { user ->
                                navController.navigate("login")
                            }
                        } else {
                            message = error ?: "Registration failed"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.register_register))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Already have an account?",
                    style = AppTypography.bodyMedium)
                TextButton(
                    onClick = { navController.navigate("login") }
                ) { Text(stringResource(R.string.register_login)) }
            }
            Text(text = message, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
