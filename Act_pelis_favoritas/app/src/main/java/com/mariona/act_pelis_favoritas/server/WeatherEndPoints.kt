package com.mariona.act_pelis_favoritas.server

import com.mariona.act_pelis_favoritas.models.Temps
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherEndPoints {

    @GET("/v1/current.json")
    suspend fun getTemps(@Query("key") apiKey: String, @Query("q") city: String
    ): Response<Temps>
}
