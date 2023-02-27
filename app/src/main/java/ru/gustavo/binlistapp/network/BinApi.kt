package ru.gustavo.binlistapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.gustavo.binlistapp.entities.Card

interface BinApi {
    @GET("/{bin}")
    @Headers("Accept-Version: 3")
    fun getBinInfo(@Path("bin") bin: String): Call<Card>
}