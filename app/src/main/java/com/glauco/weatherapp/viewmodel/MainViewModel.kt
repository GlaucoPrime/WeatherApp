package com.glauco.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.db.fb.FBUser
import com.glauco.weatherapp.db.fb.FBCity
import com.glauco.weatherapp.db.fb.toFBCity
import com.glauco.weatherapp.model.City
import com.glauco.weatherapp.model.User
import com.glauco.weatherapp.model.Weather
import com.glauco.weatherapp.model.Forecast
import com.glauco.weatherapp.api.WeatherService
import com.glauco.weatherapp.ui.nav.Route
import com.google.android.gms.maps.model.LatLng

class MainViewModel(val db: FBDatabase, private val service : WeatherService) :
    ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    private val _user = mutableStateOf<User?> (null)
    private val _weather = mutableStateMapOf<String, Weather>()
    private val _forecast = mutableStateMapOf<String, List<Forecast>?>()
    private var _city = mutableStateOf<String?>(null)
    private var _page = mutableStateOf<Route> (Route.Home)


    val user: User?
        get() = _user.value

    val cities: List<City>
        get() = _cities.values.toList().sortedBy { it.name }

    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }

    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    init {
        db.setListener(this)
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name=name, location=LatLng(lat, lng)).toFBCity())
            }
        }
    }

    fun addCity(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                db.add(City(name = name, location = location).toFBCity())
            }
        }
    }

    fun weather(name: String) = _weather.getOrPut(name) {
        loadWeather(name)
        Weather.LOADING
    }

    fun forecast(name: String) = _forecast.getOrPut(name) {
        loadForecast(name)
        emptyList()
    }
    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                _weather[name] = apiWeather.toWeather()
                loadBitmap(name)
            }
        }
    }

    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                _forecast[name] = apiForecast.toForecast()
            }
        }
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
    }

    override fun onCityAdded(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityUpdated(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.name)
    }
    fun loadBitmap(name: String) {
        _weather[name]?.let { weather ->
            service.getBitmap(weather.imgUrl) { bitmap ->
                _weather[name] = weather.copy(bitmap = bitmap)
            }
        }
    }
}
