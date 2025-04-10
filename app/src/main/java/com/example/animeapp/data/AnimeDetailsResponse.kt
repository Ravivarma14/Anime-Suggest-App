package com.example.animeapp.data

data class AnimeDetailsResponse(val data:AnimeDetails)

data class AnimeDetails(val title:String, val synopsis:String?, val genres: List<Genre>, val trailer: Trailer?,
    val episodes:Int?, val rating: String?, val score: String?, val duration: String?)

data class Genre(val name: String)
data class Trailer(val url:String?, val images:TrailerImages)
data class TrailerImages(val large_image_url: String)
