package com.glauco.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.db.fb.toFBUser
import com.glauco.weatherapp.model.User
import com.glauco.weatherapp.ui.theme.WeatherAppTheme
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterPage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {

    var nome by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var repetirSenha by rememberSaveable { mutableStateOf("") }

    val activity = LocalContext.current as Activity

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crie sua conta",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.size(24.dp))

        OutlinedTextField(
            value = nome,
            label = { Text("Digite seu nome") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { nome = it },
            singleLine = true
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = email,
            label = { Text("Digite seu e-mail") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { email = it },
            singleLine = true
        )
        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = senha,
            label = { Text("Digite sua senha") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { senha = it },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = repetirSenha,
            label = { Text("Repita sua senha") },
            modifier = Modifier.fillMaxWidth(0.9f),
            onValueChange = { repetirSenha = it },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.size(24.dp))

        val isEnabled = nome.isNotEmpty() &&
                email.isNotEmpty() &&
                senha.isNotEmpty() &&
                repetirSenha.isNotEmpty() &&
                senha == repetirSenha

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {

                                FBDatabase().register(User(nome, email).toFBUser())
                                Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()

                            } else {
                                Toast.makeText(activity, "Registro FALHOU!", Toast.LENGTH_LONG).show()
                            }
                        }
                },
                enabled = isEnabled
            ) {
                Text("Registrar")
            }

            Button(onClick = {
                nome = ""
                email = ""
                senha = ""
                repetirSenha = ""
            }) {
                Text("Limpar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    WeatherAppTheme {
        RegisterPage()
    }
}