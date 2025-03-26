package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.Temps
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherEndpoints {
    @GET("current.json")
    suspend fun searchWeather(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String
    ): Response<Temps>
}