package ru.gustavo.binlistapp.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Country(
    @SerializedName("numeric")
    val numeric: Int = 0,
    @SerializedName("alpha2")
    val alpha2: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("emoji")
    val emoji: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("latitude")
    val latitude: Int = 0,
    @SerializedName("longitude")
    val longitude: Int = 0,
) : Serializable