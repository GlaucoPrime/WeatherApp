package com.glauco.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // CORREÇÃO: Resolve 'Unresolved reference composable'
import com.glauco.weatherapp.ui.HomePage
import com.glauco.weatherapp.ui.ListPage
import com.glauco.weatherapp.ui.MapPage
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.ui.LoginScreen // CORREÇÃO: Assume 'LoginScreen' é o Composable de Login

@Composable
fun MainNavHost(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login // Resolve 'Unresolved reference Login'
    ) {
        composable(Route.Login) { LoginScreen(viewModel = viewModel) }
        composable(Route.Home) { HomePage(viewModel = viewModel) }
        composable(Route.List) { ListPage(viewModel = viewModel) }
        // CORREÇÃO: Padroniza o parâmetro para 'viewModel' em todas as telas
        composable(Route.Map) { MapPage(viewModel = viewModel) }
    }
}