package com.hajarslamah.erthquickapi.api


import com.hajarslamah.erthquickapi.ErthResponse
import retrofit2.Call
import retrofit2.http.GET

interface ErthQuckApi {
    @GET("query?format=geojson&limit=10")
    fun fetchContents(): Call<ErthResponse>
}