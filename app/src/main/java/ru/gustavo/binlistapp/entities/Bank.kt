package ru.gustavo.binlistapp.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Bank(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("city")
    val city: String?
) : Serializable