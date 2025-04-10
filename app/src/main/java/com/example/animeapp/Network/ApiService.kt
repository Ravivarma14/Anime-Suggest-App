package com.example.animeapp.Network

import com.example.animeapp.data.AnimeCharacterResponse
import com.example.animeapp.data.AnimeDetailsResponse
import com.example.animeapp.data.AnimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("top/anime")
    fun getAnimeList(): Call<AnimeResponse>

    @GET("anime/{id}")
    fun getAnimeDetails(@Path("id") animeId:Int): Call<AnimeDetailsResponse>

    @GET("anime/{id}/characters")
    fun getAnimeCharacters(@Path("id") animeId: Int): Call<AnimeCharacterResponse>
}