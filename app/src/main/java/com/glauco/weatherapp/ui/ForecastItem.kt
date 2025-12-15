package com.glauco.weatherapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.R
import com.glauco.weatherapp.model.Forecast
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import java.text.DecimalFormat

@Composable
fun ForecastItem(
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    val format = DecimalFormat("#.0")
    val tempMin = format.format(forecast.tempMin)
    val tempMax = format.format(forecast.tempMax)

    Row (
        modifier = modifier.fillMaxWidth().padding (12.dp)
            .clickable (onClick = {}), // onClick vazio
        verticalAlignment = Alignment.CenterVertically
    ) {
        // SUBSTITUI O ICON PELO AsyncImage (Prática 09, Passo 5) [cite: 426, 427, 428, 429, 430]
        AsyncImage(
            model = forecast.imgUrl,
            modifier = Modifier.size(70.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )

        Spacer (modifier = Modifier.size (16.dp))

        Column {
            Text (modifier = Modifier, text = forecast.weather, fontSize = 24.sp)
            Row {
                Text (modifier = Modifier, text = forecast.date, fontSize = 20.sp)
                Spacer (modifier = Modifier.size(12.dp))
                Text (modifier = Modifier, text = "Min: $tempMin°C", fontSize = 16.sp)
                Spacer (modifier = Modifier.size(12.dp))
                Text (modifier = Modifier, text = "Max: $tempMax°C", fontSize = 16.sp)
            }
        }
    }
}