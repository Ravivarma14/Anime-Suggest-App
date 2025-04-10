package com.example.animeapp.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val BASE_URL="https://api.jikan.moe/v4/"

     val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(
                ApiService::class.java)
    }

}