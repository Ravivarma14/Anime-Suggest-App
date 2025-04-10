package com.example.animeapp.data

data class AnimeCharacterResponse(val data: List<AnimeCharacter>)
data class AnimeCharacter(val character:CharacterInfo)
data class CharacterInfo(val name:String)