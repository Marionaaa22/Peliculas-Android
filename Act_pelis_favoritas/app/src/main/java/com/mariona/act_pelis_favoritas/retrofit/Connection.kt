package com.mariona.act_pelis_favoritas.retrofit

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object Connection {

    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlN2VlMmViYjBlZWZjMzg2OWRkYTk5OGJmNjYwZDBlNSIsIm5iZiI6MTc0MTM3MDAyNi40NzQsInN1YiI6IjY3Y2IzMmFhN2M5NjdlMDRkNTViODFkYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.exgP_rmpLGXCSx8RaYre9nszrjECdl_Aep4yjRzyxf4"

    private val authInterceptor = Interceptor { chain ->
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()
        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val okHttpClientMovieDB = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val builder = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val builderMovieDB = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClientMovieDB)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val builderWeather = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: Endpoints = builder.create(Endpoints::class.java)

    val movieDBService: movieDBEndpoints = builderMovieDB.create(movieDBEndpoints::class.java)

    val weatherService: weatherEndpoints = builderWeather.create(weatherEndpoints::class.java)
}




