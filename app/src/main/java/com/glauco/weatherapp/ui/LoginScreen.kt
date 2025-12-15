package com.glauco.weatherapp.ui

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.RegisterActivity // Você precisa deste import para o botão Registrar-se
import com.glauco.weatherapp.ui.nav.Route // Import para a nova rota de navegação
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(viewModel: MainViewModel) { // RENOMEADO e ACEITA VIEWMODEL

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val activity = LocalContext.current as Activity 
    val isEnabled = email.isNotEmpty() && password.isNotEmpty()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bem-vindo/a!",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = email,
            label = { Text("Digite seu e-mail") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { email = it },
            singleLine = true
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = password,
            label = { Text("Digite sua senha") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.size(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                // CORREÇÃO DE NAVEGAÇÃO: Muda a rota no ViewModel
                                viewModel.page = Route.Home 
                                
                                Toast.makeText(activity, "Login OK!", Toast.LENGTH_LONG).show()
                                
                                // Se a LoginActivity ainda estiver na pilha e for a tela inicial, 
                                // você pode finalizá-la aqui para que o botão voltar não volte para ela.
                                // activity.finish() 
                                
                            } else {
                                Toast.makeText(activity, "Login FALHOU!", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = isEnabled
            ) {
                Text("Login")
            }

            Button(onClick = {
                email = ""
                password = ""
            }) {
                Text("Limpar")
            }

            Button(onClick = {
                // Mantém a navegação via Intent para o Registro (Activity separada)
                activity.startActivity(
                    Intent(activity, RegisterActivity::class.java)
                )
            }) {
                Text("Registrar-se")
            }
        }
    }
}