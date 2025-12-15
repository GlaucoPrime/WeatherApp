package com.glauco.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.api.WeatherService // IMPORT OBRIGATÓRIO
import com.glauco.weatherapp.ui.nav.BottomNavBar
import com.glauco.weatherapp.ui.nav.BottomNavItem
import com.glauco.weatherapp.ui.nav.MainNavHost
import com.glauco.weatherapp.ui.nav.Route
import com.glauco.weatherapp.ui.CityDialog
import com.glauco.weatherapp.ui.theme.WeatherAppTheme
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.viewmodel.MainViewModelFactory
import com.google.firebase.auth.auth
import com.google.firebase.ktx.Firebase
import android.Manifest
import androidx.compose.material3.Text
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val fbDB = remember { FBDatabase() }
            // NOVO: Passa 'this' (Contexto da Activity) para o WeatherService
            val weatherService = remember { WeatherService(this) }
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModelFactory(fbDB, weatherService)
            )
            WeatherAppTheme {

                val fbDB = remember { FBDatabase() }
                // Correção: Passando 'this' para o construtor do WeatherService (Prática 09)
                val weatherService = remember { WeatherService(this) }
                // Correção: Passando o 'weatherService' para o factory
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(fbDB, weatherService)
                )

                val navController = rememberNavController()
                var showDialog by remember { mutableStateOf(false) }
                val currentRoute = navController.currentBackStackEntryAsState()
                val showButton = currentRoute.value?.destination?.hasRoute(Route.List::class) == true
                val launcher = rememberLauncherForActivityResult(contract =
                    ActivityResultContracts.RequestPermission(), onResult = {} )

                // NOVO: Navegação por ViewModel (Prática 08)
                LaunchedEffect(viewModel.page) {
                    navController.navigate(viewModel.page) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }

                if (showDialog) CityDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { city ->
                        if (city.isNotBlank()) viewModel.addCity(city) // addCity agora recebe apenas 'city' (string)
                        showDialog = false
                    })

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                val name = viewModel.user?.name ?: "[carregando...]"
                                Text("Bem-vindo/a! $name")
                            },
                            actions = {
                                IconButton(onClick = {
                                    FirebaseAuth.getInstance().signOut()
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = "Logout"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        val items = listOf(
                            BottomNavItem.HomeButton,
                            BottomNavItem.ListButton,
                            BottomNavItem.MapButton,
                        )
                        // CORRIGIDO: BottomNavBar agora recebe (viewModel, items)
                        BottomNavBar(viewModel, items)
                    },
                    floatingActionButton = {
                        if (showButton) {
                            FloatingActionButton(onClick = { showDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar")
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        MainNavHost(navController, viewModel)
                    }
                }
            }
        }
    }
}