package com.mariona.act_pelis_favoritas.server

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object WeatherConnection {
    private val okHttpClient = HttpLoggingInterceptor().run{
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(this).build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: WeatherEndPoints = retrofit.create()
}