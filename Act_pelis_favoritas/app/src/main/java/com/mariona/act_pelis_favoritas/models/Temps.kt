package com.mariona.act_pelis_favoritas.models

import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import java.io.Serializable
import com.google.gson.annotations.SerializedName
import java.util.concurrent.locks.Condition

data class Temps(
    val location: Location,
    val current: Current
)

data class Current (
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Long,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("temp_c")
    val tempC: Double,

    @SerializedName("temp_f")
    val tempF: Double,

    @SerializedName("is_day")
    val isDay: Long,

    val condition: Condition,

    @SerializedName("wind_mph")
    val windMph: Double,

    @SerializedName("wind_kph")
    val windKph: Double,

    @SerializedName("wind_degree")
    val windDegree: Int,

    @SerializedName("wind_dir")
    val windDir: String,

    @SerializedName("pressure_mb")
    val pressureMb: Double,

    @SerializedName("pressure_in")
    val pressureIn: Double,

    @SerializedName("precip_mm")
    val precipMm: Double,

    @SerializedName("precip_in")
    val precipIn: Double,

    val humidity: Int,

    val cloud: Int,

    @SerializedName("feelslike_c")
    val feelslikeC: Double,

    @SerializedName("feelslike_f")
    val feelslikeF: Double,

    @SerializedName("windchill_c")
    val windchillC: Double,

    @SerializedName("windchill_f")
    val windchillF: Double,

    @SerializedName("heatindex_c")
    val heatindexC: Double,

    @SerializedName("heatindex_f")
    val heatindexF: Double,

    @SerializedName("dewpoint_c")
    val dewpointC: Double,

    @SerializedName("dewpoint_f")
    val dewpointF: Double,

    @SerializedName("vis_km")
    val visKm: Double,

    @SerializedName("vis_miles")
    val visMiles: Double,

    val uv: Double,

    @SerializedName("gust_mph")
    val gustMph: Double,

    @SerializedName("gust_kph")
    val gustKph: Double
): Serializable

data class Condition (
    val text: String,
    val icon: String,
    val code: Int

): Serializable

data class Location (
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,

    @SerializedName("tz_id")
    val tzId: String,

    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,

    val localtime: String
): Serializable