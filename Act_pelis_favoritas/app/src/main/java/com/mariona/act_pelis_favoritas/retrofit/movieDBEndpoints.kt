package com.mariona.act_pelis_favoritas.retrofit

import com.mariona.act_pelis_favoritas.models.respostaMovieDB
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface movieDBEndpoints {

    @GET("/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("lenguage") lenguage: String,
        @Query("_sort") sort: String,
        @Query("page") page: Int
    ): Response<respostaMovieDB>
}