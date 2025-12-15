package com.glauco.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.glauco.weatherapp.R
import com.glauco.weatherapp.viewmodel.MainViewModel
import com.glauco.weatherapp.model.Weather
import com.glauco.weatherapp.model.Forecast
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomePage(viewModel: MainViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // CORREÇÃO: Resolve 'Operator ==', checa se a cidade é nula
        if (viewModel.city == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .wrapContentSize(Alignment.Center)
            ) {
                Text(
                    text = "Selecione uma cidade!",
                    fontWeight = FontWeight.Bold, color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center, fontSize = 28.sp
                )
            }
            return
        }

        // CORREÇÃO: Usa !! para forçar não-nulidade (resolve 'Argument type mismatch String?')
        val name = viewModel.city!!
        val weather = viewModel.weather(name)
        val forecast = viewModel.forecast(name)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                // TÍTULO DA CIDADE
                Text(text = name, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                // CLIMA ATUAL (INTEGRADO - substitui CurrentWeather)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = weather.imgUrl,
                        modifier = Modifier.size(120.dp),
                        error = painterResource(id = R.drawable.loading),
                        contentDescription = "Ícone do clima"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${weather.temp}°C",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                        Text(
                            text = weather.desc,
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                // TÍTULO DA PREVISÃO
                Text(
                    text = "Previsão para 10 dias",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // LISTA DE PREVISÕES (INTEGRADO - substitui ForecastItem)
            // CORREÇÃO: Usa ?: emptyList() (resolve 'Argument type mismatch List<Forecast>?')
            items(items = forecast ?: emptyList()) { item ->
                ForecastItemContent(item)
            }
        }
    }
}

// NOVO COMPONENTE: Conteúdo do item de previsão (anteriormente ForecastItem)
@Composable
fun ForecastItemContent(
    forecast: Forecast,
    modifier: Modifier = Modifier
) {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = formatter.parse(forecast.date)
    val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(date!!)
    val format = DecimalFormat("#.0")
    val tempMin = format.format(forecast.tempMin)
    val tempMax = format.format(forecast.tempMax)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable (onClick = {}),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // DIA DA SEMANA
            Text(
                text = dayName,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )

            // ÍCONE
            AsyncImage(
                model = forecast.imgUrl,
                modifier = Modifier.size(50.dp),
                error = painterResource(id = R.drawable.loading),
                contentDescription = "Ícone do clima"
            )

            Spacer(modifier = Modifier.width(16.dp))

            // MIN/MAX
            Text(
                text = "${tempMin}°C / ${tempMax}°C",
                fontSize = 16.sp
            )
        }
        Divider()
    }
}