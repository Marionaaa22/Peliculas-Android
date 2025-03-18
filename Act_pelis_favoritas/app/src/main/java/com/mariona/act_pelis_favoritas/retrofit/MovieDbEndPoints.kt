package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.Movies
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface MovieDbEndPoints {
    @GET("movies")
    suspend fun listMovies(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<Movies>>

    @POST("movies")
    suspend fun newMovie(@Body movie: Movies): Response<ResponseBody>

    @DELETE("movies/{id}")
    suspend fun deleteMovie(@Path("id") id: Long)

    @PUT("movies/{id}")
    suspend fun updateMovie(
        @Path("id") id: Long, @Body movie: Movies?
    )

}