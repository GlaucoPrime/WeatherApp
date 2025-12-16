package com.glauco.weatherapp.ui

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.ui.nav.Route

@Composable
fun ListPage(viewModel: MainViewModel) {
    val cityList = viewModel.cities
    val activity = LocalContext.current as? Activity
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        items(cityList, key = { it.name }) { city ->
            CityItem(
                city = city,
                weather = viewModel.weather(city.name),
                onClick = {
                    viewModel.city = city.name
                    viewModel.page = Route.Home
                },
                onClose = {
                    viewModel.remove(city)
                }
            )
        }
    }
}