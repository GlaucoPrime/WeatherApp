package com.glauco.weatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.R
import com.glauco.weatherapp.model.Weather
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(viewModel: MainViewModel) {
    val context = LocalContext.current
    val camPosState = rememberCameraPositionState()
    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    Column {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            onMapClick = {
                viewModel.addCity(it)
            },
            properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            cameraPositionState = camPosState
        ) {
            viewModel.cities.forEach {
                if (it.location != null) {
                    val weather = viewModel.weather(it.name)
                    // Carrega a imagem ou usa o loading
                    val image = weather.bitmap ?: ContextCompat.getDrawable(context, R.drawable.loading)!!.toBitmap()

                    // Redimensionando a imagem
                    val marker = BitmapDescriptorFactory.fromBitmap(image.scale(120, 120))

                    val desc = if (weather == Weather.LOADING) "Carregando clima..." else weather.desc

                    Marker(
                        state = MarkerState(position = it.location),
                        icon = marker,
                        title = it.name,
                        snippet = desc
                    )
                }
            }
        }
    }
}