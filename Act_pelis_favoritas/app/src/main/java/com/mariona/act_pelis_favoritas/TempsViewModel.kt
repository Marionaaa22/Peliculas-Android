package com.mariona.act_pelis_favoritas

import android.util.Log
import androidx.lifecycle.*
import com.mariona.act_pelis_favoritas.model.Conf
import com.mariona.act_pelis_favoritas.model.Temps
import com.mariona.act_pelis_favoritas.server.MovieDbConnection
import com.mariona.act_pelis_favoritas.server.WeatherConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope

class TempsViewModel : ViewModel() {
    val api = "6fbdce1b97534ec6adb163935240702"
    private val _loading = MutableLiveData(false)
    private val _temps = MutableLiveData<Temps>()
    val temps: LiveData<Temps> get() = _temps
    private val _errorApiRest = MutableLiveData<String?>()
    private val _city = MutableLiveData<List<Conf>?>()
    private val _error = MutableLiveData<String>()

    init {
        getCity()
    }

    private fun getCity() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _errorApiRest.value = null

                val response = withContext(Dispatchers.IO) {
                    MovieDbConnection.movieDbService.getCiudad()
                }

                if (response.isSuccessful) {
                    val cities = response.body()
                    _city.value = cities
                    cities?.firstOrNull()?.let { city ->
                        getTemps(city.city)
                    }
                } else {
                    _errorApiRest.value = "Error en obtindre la ciutat"
                }

            } catch (e: Exception) {
                _error.value = "Error en cargar el clima: ${e.message}"
            }
            _loading.value = false
        }
    }

    private fun getTemps(city: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                _errorApiRest.value = null

                val response = withContext(Dispatchers.IO) {
                    WeatherConnection.service.getTemps(api, city)
                }

                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    if (weatherResponse != null) {
                        val weather = Temps(
                            location = weatherResponse.location,
                            current = weatherResponse.current
                        )
                        _temps.value = weather
                        Log.d("TempsViewModel", "Dades del clima carregades: $weather")
                    } else {
                        _errorApiRest.value = "No s'ha trobat cap clima"
                    }
                } else {
                    _errorApiRest.value = "Error en cargar el clima"
                }
            } catch (e: Exception) {
                _error.value = "Error en cargar el clima: ${e.message}"
            }
            _loading.value = false
        }
    }
}

class TempsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TempsViewModel() as T
    }
}
