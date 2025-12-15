package com.glauco.weatherapp.api

import com.glauco.weatherapp.model.Forecast

data class APIWeatherForecast (
    var location: APILocation? = null,
    var current: APICurrentWeather? = null,
    var forecast: APIForecast? = null
) {
    fun toForecast(): List<Forecast>? {
        return forecast?.forecastday?.map { apiForecastDay ->
            Forecast(
                date = apiForecastDay.date ?: "00-00-0000",
                weather = apiForecastDay.day?.condition?.text ?: "Erro ao carregar!",
                tempMin = apiForecastDay.day?.mintemp_c ?: -1.0,
                tempMax = apiForecastDay.day?.maxtemp_c ?: -1.0,
                imgUrl = ("https:" + apiForecastDay.day?.condition?.icon)
            )
        }
    }
}