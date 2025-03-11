package com.mariona.act_pelis_favoritas.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie (
    val movies: List<MovieElement>,
    val conf: List<Conf>
): Serializable

data class Conf (
    val city: String
) : Serializable

data class MovieElement(
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    val favorite: Boolean,

    @SerializedName("genre_ids")
    val genreIDS: List<Long>,

    val id: Long,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    val overview: String,
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("release_date")
    val releaseDate: String,

    val title: String,
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Long,

    @SerializedName("my_score")
    val myScore: Int

): Serializable