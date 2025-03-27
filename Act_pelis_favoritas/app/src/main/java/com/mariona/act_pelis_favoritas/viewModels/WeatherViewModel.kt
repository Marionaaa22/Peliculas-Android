package com.mariona.act_pelis_favoritas.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.mariona.act_pelis_favoritas.models.Conf
import com.mariona.act_pelis_favoritas.models.Temps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import com.mariona.act_pelis_favoritas.retrofit.Connection
import com.mariona.act_pelis_favoritas.retrofit.weatherEndpoints
import java.io.IOException

class WeatherViewModel(val cityName: String): ViewModel() {
    private val _weatherLoading = MutableLiveData(false)
    public val weatherLoading: LiveData<Boolean> get() = _weatherLoading

    private val _weather = MutableLiveData<Temps>()
    public val weather: LiveData<Temps> get() = _weather

    private val _error = MutableLiveData<String?>(null)
    public val error: LiveData<String?> get() = _error

    init {
        searchWeather(cityName)
    }

    public fun searchWeather(city: String) {
        viewModelScope.launch {
            _weatherLoading.value = true
            _error.value = null

            try {
                var resposta = Connection.weatherService.searchWeather("7acd40f4666e4bec915175109252403", city, "no")

                if (resposta.isSuccessful) {
                    _weather.value = resposta.body()
                } else {
                    _error.value = "ERROR CODE: " + resposta.code().toString()
                }
            }
            catch (e: IOException) {
                _error.value = "Error de red"
            }
            catch (e: Exception) {
                _error.value = "Error desconocido: ${e.localizedMessage}"
            }
            finally {
                _weatherLoading.value = false
            }
        }
    }

    fun updateConf(id: String, city: String) {
        var conf = Conf(id = id, city = city)
        viewModelScope.launch {
            Connection.service.updateConfWeather(id!!, conf)
            searchWeather(city)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class WeatherViewModelFactory(val cityName: String): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(cityName) as T
    }
}