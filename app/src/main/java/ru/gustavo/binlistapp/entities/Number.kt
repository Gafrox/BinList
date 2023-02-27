package ru.gustavo.binlistapp.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Number(
    @SerializedName("length")
    val length: Int = 0,
    @SerializedName("luhn")
    val luhn: Boolean = false
) : Serializable