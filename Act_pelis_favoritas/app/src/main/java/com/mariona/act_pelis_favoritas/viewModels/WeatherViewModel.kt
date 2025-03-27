package com.mariona.act_pelis_favoritas.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.mariona.act_pelis_favoritas.models.Conf
import com.mariona.act_pelis_favoritas.models.Temps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import com.mariona.act_pelis_favoritas.retrofit.weatherEndpoints

class WeatherViewModel : ViewModel() {
    private val _weatherLoading = MutableLiveData(false)
    public val weatherLoading: LiveData<Boolean> get() = _weatherLoading

    private val _weather = MutableLiveData<Temps>()
    public val weather: LiveData<Temps> get() = _weather

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            _weatherLoading.value = true
            _error.value = null
            try {
                val response = weatherEndpoints.service.searchWeather(Conf.API_KEY, Conf.CITY, "no")
                if (response.isSuccessful) {
                    _weather.value = response.body()
                } else {
                    _error.value = "ERROR CODE: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Unknown error: ${e.message}"
            } finally {
                _weatherLoading.value = false
            }
        }
    }

}