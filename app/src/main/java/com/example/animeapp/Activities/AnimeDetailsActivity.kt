package com.example.animeapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.animeapp.Network.RetrofitInstance
import com.example.animeapp.R
import com.example.animeapp.data.AnimeCharacterResponse
import com.example.animeapp.data.AnimeDetailsResponse
import com.example.animeapp.databinding.ActivityAnimeDetailsBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAnimeDetailsBinding
    private var trailerUrl: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        val animeId= intent.getIntExtra("anime_id",-1)
        if(animeId != -1){
            fetchAnimeDetails(animeId)
            fetchCharacters(animeId)
        }


    }

    private fun fetchAnimeDetails(animeId: Int){
        RetrofitInstance.apiService.getAnimeDetails(animeId).enqueue(object : Callback<AnimeDetailsResponse>{
            override fun onResponse(
                call: Call<AnimeDetailsResponse>,
                response: Response<AnimeDetailsResponse>
            ) {
                Log.d("TAG", "onResponse: " + animeId + " details:" + response.body() + response)

                if (response.isSuccessful) {
                    val anime = response.body()?.data ?: return

                    binding.tvTitle.text = anime.title
                    binding.tvRating.text = "Rating: ${anime.rating ?: "NA"}"
                    binding.tvEpisodes.text = "Episodes: ${anime.episodes ?: "NA"}"
                    binding.tvGenres.text = "Genres: ${anime.genres.joinToString(", ") { it.name }}"
                    if(anime.synopsis.isNullOrEmpty()) binding.tvSynopsis.text= "No Synopsis Available"
                    else binding.tvSynopsis.text = anime.synopsis
                    binding.tvDuration.text = "Duration: ${anime.duration ?: "NA"}"

                    trailerUrl = anime.trailer?.url
                    val trailerImage = anime.trailer?.images?.large_image_url

                    if (trailerImage != null) {
                        Glide.with(this@AnimeDetailsActivity).load(trailerImage)
                            .into(binding.ivTrailerImg)
                    }


                    // Extract YouTube video ID from URL
                    anime.trailer?.url?.let { url ->
                        val videoId = Uri.parse(url).getQueryParameter("v")
                        videoId?.let {
                            binding.youtubePlayerView.addYouTubePlayerListener(object :
                                AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.cueVideo(it, 0f)
                                }
                            })
                        }

                    }
                }
            }

            override fun onFailure(call: Call<AnimeDetailsResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: "+ t)
                Toast.makeText(this@AnimeDetailsActivity, "Fail to fetch details",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCharacters(id: Int) {
        RetrofitInstance.apiService.getAnimeCharacters(id).enqueue(object : Callback<AnimeCharacterResponse> {
            override fun onResponse(call: Call<AnimeCharacterResponse>, response: Response<AnimeCharacterResponse>) {
                Log.d("TAG", "onResponse: characters: "+ response.body())
                if (response.isSuccessful) {
                    val characterList = response.body()?.data?.take(4)?.map { it.character.name } ?: emptyList()
                    val charactersText = characterList.joinToString(", ")
                    binding.tvCharacters.text = "Main Cast: $charactersText"
                }
            }

            override fun onFailure(call: Call<AnimeCharacterResponse>, t: Throwable) {
                binding.tvCharacters.text = "Main Cast: Not available"
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }
}