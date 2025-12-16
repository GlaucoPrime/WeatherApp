package com.glauco.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.ui.HomePage
import com.glauco.weatherapp.ui.ListPage
import com.glauco.weatherapp.ui.MapPage

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController, startDestination = Route.Home) {
        composable<Route.Home> { HomePage(viewModel) }
        composable<Route.List> { ListPage(viewModel) }
        composable<Route.Map> { MapPage(viewModel) }
    }
}