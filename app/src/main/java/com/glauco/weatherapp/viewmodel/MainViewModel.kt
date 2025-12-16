package com.glauco.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.glauco.weatherapp.api.WeatherService
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.db.fb.FBCity
import com.glauco.weatherapp.db.fb.FBUser
import com.glauco.weatherapp.db.fb.toFBCity
import com.glauco.weatherapp.model.City
import com.glauco.weatherapp.model.Forecast
import com.glauco.weatherapp.model.User
import com.glauco.weatherapp.model.Weather
import com.glauco.weatherapp.ui.nav.Route
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val db: FBDatabase,
    private val service: WeatherService
) : ViewModel(), FBDatabase.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    val cities: List<City>
        get() = _cities.values.toList().sortedBy { it.name }

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    private val _weather = mutableStateMapOf<String, Weather>()
    private val forecast = mutableStateMapOf<String, List<Forecast>?>()

    private var _city = mutableStateOf<String?>(null)
    var city: String?
        get() = _city.value
        set(tmp) { _city.value = tmp }

    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    init {
        db.setListener(this)
    }

    fun weather(name: String): Weather {
        val w = _weather.getOrPut(name) {
            loadWeather(name)
            Weather.LOADING
        }
        return w
    }

    private fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            apiWeather?.let {
                val w = it.toWeather()
                _weather[name] = w
                loadBitmap(name, w.imgUrl)
            }
        }
    }

    private fun loadBitmap(name: String, imgUrl: String) {
        service.getBitmap(imgUrl) { bitmap ->
            _weather[name]?.let {
                _weather[name] = it.copy(bitmap = bitmap)
            }
        }
    }

    fun forecast(name: String): List<Forecast>? {
        return forecast.getOrPut(name) {
            loadForecast(name)
            emptyList()
        }
    }

    private fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            apiForecast?.let {
                forecast[name] = it.toForecast()
            }
        }
    }

    fun remove(city: City) {
        db.remove(city.toFBCity())
    }

    fun addCity(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                db.add(City(name = name, location = LatLng(lat, lng)).toFBCity())
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

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
    }

    override fun onCityAdded(city: FBCity) {
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityUpdated(city: FBCity) {
        _cities.remove(city.name)
        _cities[city.name!!] = city.toCity()
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.name)
    }
}