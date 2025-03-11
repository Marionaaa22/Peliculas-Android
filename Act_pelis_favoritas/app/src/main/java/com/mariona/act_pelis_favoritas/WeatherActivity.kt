package com.mariona.act_pelis_favoritas

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mariona.act_pelis_favoritas.databinding.ActivityWeatherBinding
import com.mariona.act_pelis_favoritas.model.Temps
import com.bumptech.glide.Glide

class WeatherActivity : AppCompatActivity() {
    private val weatherViewModel: TempsViewModel by viewModels { TempsViewModelFactory() }
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherViewModel.temps.observe(this) { weather ->
            if (weather != null) {
                insertarDatosActivity(weather)
            } else {
                Log.d("WeatherActivity", "Weather data is null")
            }
        }

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun insertarDatosActivity(temps: Temps) {
        Log.d("WeatherActivity", "Insertando datos en la actividad: $temps")
        binding.tvLatitud.text = temps.location.lat.toString()
        binding.tvLongitud.text = temps.location.lon.toString()
        binding.tvVent.text = temps.current.wind_dir
        binding.tvHumitat.text = "${temps.current.humidity}%"
        binding.tvUv.text = temps.current.uv.toString()
        binding.tvLocalizacion.text = temps.location.name
        binding.tvTemperatura.text = "${temps.current.temp_c} °C"
        binding.tvDescripcio.text = temps.current.condition.text
        binding.tvCiutat.text = temps.location.country

        Glide.with(this)
            .load("https:" + temps.current.condition.icon)
            .into(binding.imgTemps)
    }
}