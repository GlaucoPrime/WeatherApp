package com.glauco.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.glauco.weatherapp.ui.HomePage
import com.glauco.weatherapp.ui.ListPage
import com.glauco.weatherapp.ui.MapPage
import com.glauco.weatherapp.viewmodel.MainViewModel

@Composable
fun MainNavHost(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Route.Home) {
        composable<Route.Home> {
            HomePage(mainViewModel = viewModel)
        }
        composable<Route.List> {
            ListPage(viewModel = viewModel)
        }
        composable<Route.Map> {
            MapPage(mainViewModel = viewModel)
        }
    }
}