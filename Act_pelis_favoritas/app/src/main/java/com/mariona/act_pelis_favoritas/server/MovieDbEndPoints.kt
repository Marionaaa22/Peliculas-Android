package com.mariona.act_pelis_favoritas.server

import com.mariona.act_pelis_favoritas.model.Conf
import com.mariona.act_pelis_favoritas.model.MovieElement
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.*

interface MovieDbEndPoints {
    @GET("movies")
    suspend fun getMovies(): Response<List<MovieElement>>

    @DELETE("movies/{id}")
    suspend fun deleteFavoriteMovie(@Path("id") id: Long)

    @PUT("movies/{id}")
    suspend fun updateScore(
        @Path("id") id: Long, @Body movie: MovieElement
    ): Response<ResponseBody>

    @GET("movies?_sort=title")
    suspend fun listMoviesByTitleAsc(): Response<List<MovieElement>>

    @GET("movies?_sort=-title")
    suspend fun listMoviesByTitleDesc(): Response<List<MovieElement>>

    @GET("movies?_sort=my_score")
    suspend fun listMoviesByScoreAsc(): Response<List<MovieElement>>

    @GET("movies?_sort=-my_score")
    suspend fun listMoviesByScoreDesc(): Response<List<MovieElement>>

    @GET("conf")
    suspend fun getCiudad(): Response<List<Conf>>
}