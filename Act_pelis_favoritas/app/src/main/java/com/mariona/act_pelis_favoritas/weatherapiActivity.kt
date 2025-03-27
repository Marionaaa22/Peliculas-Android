package com.mariona.act_pelis_favoritas

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.mariona.act_pelis_favoritas.models.Temps
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.mariona.act_pelis_favoritas.databinding.ActivityWeatherBinding
import com.mariona.act_pelis_favoritas.viewModels.WeatherViewModel
import com.mariona.act_pelis_favoritas.viewModels.WeatherViewModelFactory

class weatherapiActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var city: String
    private lateinit var cityID: String
    private lateinit var newCity: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar!!.hide()
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        newCity = binding.newCity
        city = intent.extras!!.getString("city").toString()
        cityID = intent.extras!!.getString("cityID").toString()
        val factory = WeatherViewModelFactory(city)
        viewModel = ViewModelProvider(this, factory).get(WeatherViewModel::class.java)

        viewModel.weatherLoading.observe(this) { cargando ->
            if (cargando) {
                binding.progressWeather.visibility = View.VISIBLE
            }
            else {
                binding.progressWeather.visibility = View.GONE
            }
        }

        viewModel.weather.observe(this) { weather ->
            binding.tvCiutat.text = weather.location.name
            binding.tvTemperatura.text = weather.current.tempC.toString() + "ºC"
            binding.tvLatitud.text = weather.location.lat.toString()
            binding.tvLongitud.text = weather.location.lon.toString()
            binding.tvHumitat.text = weather.current.humidity.toString() + "%"
            binding.tvUv.text = weather.current.uv.toString()
            binding.tvVent.text = weather.current.windKph.toString() + " km/h"
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOptions = RequestOptions()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .placeholder(circularProgressDrawable)

            Glide.with(binding.imgTemps)
                .load("https:" + weather.current.condition.icon)
                .apply(requestOptions)
                .into(binding.imgTemps)
        }

        viewModel.error.observe(this) {
            if (it != null) {
                val snackbar = Snackbar.make(view, it,
                    Snackbar.LENGTH_LONG).setAction("Action", null)
                snackbar.setActionTextColor(Color.WHITE)
                val snackbarView = snackbar.view
                snackbarView.setBackgroundColor(Color.RED)
                val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                textView.setTextColor(Color.WHITE)
                textView.textSize = 28f
                snackbar.show()
            }
        }
    }

    fun changeClick(view: View) {
        var newCityName = newCity.text.toString()
        viewModel.updateConf(cityID, newCityName)
    }

    fun close(view: View) {
        finish()
    }
}