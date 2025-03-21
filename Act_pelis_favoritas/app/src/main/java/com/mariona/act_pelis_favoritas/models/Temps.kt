package com.mariona.act_pelis_favoritas.models

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class Temps(
    val location: Location,
    val current: Current
) : Serializable

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
) : Serializable

data class Current(
    val last_updated_epoch: Long,
    val last_updated: String,
    val temp_c: Double,
    val temp_f: Double,
    val is_day: Long,
    val condition: Condition,
    val wind_mph: Double,
    val wind_kph: Double,
    val wind_degree: Long,
    val wind_dir: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureIn: Double,
    val precip_mm: Double,
    val precip_in: Double,
    val humidity: Long,
    val cloud: Long,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val uv: Double,
    val gust_mph: Double,
    val gust_kph: Double
) : Serializable

data class Condition(
    val text: String,
    val icon: String,
    val code: Long
) : Serializable

