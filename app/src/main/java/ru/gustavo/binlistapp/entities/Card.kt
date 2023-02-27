package ru.gustavo.binlistapp.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Card(
    @SerializedName("number")
    val number: Number?,
    @SerializedName("scheme")
    val scheme: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("prepaid")
    val prepaid: Boolean = false,
    @SerializedName("country")
    val country: Country?,
    @SerializedName("bank")
    val bank: Bank?
) : Serializable