package com.glauco.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.glauco.weatherapp.db.fb.FBDatabase
import com.glauco.weatherapp.db.fb.FBCity
import com.glauco.weatherapp.db.fb.FBUser
import com.glauco.weatherapp.db.fb.toFBCity
import com.glauco.weatherapp.model.City
import com.glauco.weatherapp.model.User
import com.google.android.gms.maps.model.LatLng
import java.lang.IllegalArgumentException

private fun getInitialCities() = List(20) { i ->
    City(name = "Cidade $i", weather = "Carregando clima...", location = LatLng(0.0 + (i * 0.01), 0.0 + (i * 0.01)))
}.toMutableStateList()

private fun isStaticCity(city: City): Boolean {
    return city.name.startsWith("Cidade ") && city.weather == "Carregando clima..."
}

class MainViewModel(private val db: FBDatabase) : ViewModel(),
    FBDatabase.Listener {

    private val _cities = mutableStateListOf<City>()
    val cities: List<City>
        get() = _cities.toList()

    private val _user = mutableStateOf<User?> (null)
    val user: User?
        get() = _user.value

    init {
        db.setListener(this)
        _cities.addAll(getInitialCities())
    }

    fun remove(city: City) {
        if (isStaticCity(city)) {
            _cities.remove(city)
        } else {
            db.remove(city.toFBCity())
        }
    }

    fun add(name: String, location: LatLng? = null) {
        db.add(City(name, location = location).toFBCity())
    }

    override fun onUserLoaded(user: FBUser) {
        _user.value = user.toUser()
    }

    override fun onUserSignOut() {
        _user.value = null
        _cities.clear()
        _cities.addAll(getInitialCities())
    }

    override fun onCityAdded(city: FBCity) {
        val newCity = city.toCity()
        if (newCity !in _cities) {
            _cities.add(newCity)
        }
    }

    override fun onCityUpdated(city: FBCity) {
        val updatedCity = city.toCity()
        val index = _cities.indexOfFirst { it.name == updatedCity.name }
        if (index != -1) {
            _cities[index] = updatedCity
        }
    }

    override fun onCityRemoved(city: FBCity) {
        _cities.remove(city.toCity())
    }
}