package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.Movie
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.*

interface MovieDbEndPoints {
    @GET("movies")
    suspend fun listMovies(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<Movie>>

    @POST("movies")
    suspend fun newMovie(@Body movie: Movie): Response<ResponseBody>

    @DELETE("movies/{id}")
    suspend fun delateMovie(@Path("id") id: Long)

    @PUT("movies/{id}")
    suspend fun updateMovie(
        @Path("id") id: Long, @Body movie: Movie?
    )

}