package com.glauco.weatherapp.api

import com.glauco.weatherapp.model.Weather

data class APICurrentWeather (
    var location: APILocation? = null,
    var current: APIWeather? = null
) {
    fun toWeather(): Weather { // Função de conversão
        return Weather (
            date = current?.last_updated?:"...",
            desc = current?.condition?.text?:"...",
            temp = current?.temp_c?:-1.0,
            imgUrl = "https:" + current?.condition?.icon // Adiciona "https:"
        )
    }
}