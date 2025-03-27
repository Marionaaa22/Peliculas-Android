package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.MovieDBMovies
import com.mariona.act_pelis_favoritas.models.respostaMovieDB
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.GET
import retrofit2.http.Query

interface movieDBEndpoints {

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @Query("api_key") api_key: String = "91dc26768f0840d58b4150143252503"
    ): Response<respostaMovieDB>

}