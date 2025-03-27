package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.Conf
import com.mariona.act_pelis_favoritas.models.Movies
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface Endpoints {
    @GET("movies")
    suspend fun listMovies(
        @Query("_sort") sort: String
    ): Response<List<Movies>>

    @POST("movies")
    suspend fun newMovie(@Body movie: Movies): Response<ResponseBody>

    @DELETE("movies/{id}")
    suspend fun deleteMovie(@Path("id") id: Long)

    @PUT("movies/{id}")
    suspend fun updateMovie(
        @Path("id") id: Long, @Body movie: Movies?
    )

    @GET("conf")
    suspend fun confWeather(): Response<List<Conf>>

    @POST("conf/{id}")
    suspend fun updateConfWeather(@Path("id") id: String, @Body conf: Conf)


}