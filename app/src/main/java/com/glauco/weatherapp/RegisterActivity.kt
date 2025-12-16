package com.glauco.weatherapp

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.db.fb.toFBUser
import com.glauco.weatherapp.model.User
import com.glauco.weatherapp.ui.theme.WeatherAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                RegisterPage()
            }
        }
    }
}

@Composable
fun RegisterPage(modifier: Modifier = Modifier) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    val activity = LocalContext.current as? Activity

    Column(
        modifier = modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro", fontSize = 24.sp)
        Spacer(modifier = Modifier.size(24.dp))
        OutlinedTextField(value = name, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth(0.9f), onValueChange = { name = it })
        OutlinedTextField(value = email, label = { Text("E-mail") }, modifier = Modifier.fillMaxWidth(0.9f), onValueChange = { email = it })
        OutlinedTextField(value = password, label = { Text("Senha") }, modifier = Modifier.fillMaxWidth(0.9f), onValueChange = { password = it }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = confirmPassword, label = { Text("Confirmar Senha") }, modifier = Modifier.fillMaxWidth(0.9f), onValueChange = { confirmPassword = it }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.size(24.dp))
        Button(
            onClick = {
                Firebase.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity!!) { task ->
                        if (task.isSuccessful) {
                            FBDatabase().register(User(name, email).toFBUser())
                            Toast.makeText(activity, "Registro OK!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(activity, "Registro FALHOU!", Toast.LENGTH_LONG).show()
                        }
                    }
            },
            enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword
        ) {
            Text("Registrar")
        }
        Button(onClick = {
            name = ""; email = ""; password = ""; confirmPassword = ""
        }) {
            Text("Limpar")
        }
    }
}