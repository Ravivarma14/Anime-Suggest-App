package com.example.animeapp.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.animeapp.Adapters.AnimeAdapter
import com.example.animeapp.data.AnimeResponse
import com.example.animeapp.Network.RetrofitInstance
import com.example.animeapp.R
import com.example.animeapp.data.Anime
import com.example.animeapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvAnimelist.layoutManager= GridLayoutManager(this,2)

        fetchAnimeList()
    }

    private fun fetchAnimeList(){
        RetrofitInstance.apiService.getAnimeList().enqueue(object : Callback<AnimeResponse>{
            override fun onResponse(call: Call<AnimeResponse>, response: Response<AnimeResponse>) {
                if(response.isSuccessful){
                    Log.d("TAG", "onResponse: data: "+ response.body())
                    val animeList= response.body()?.data ?: emptyList()
                    for (anime:Anime in animeList){
                        Log.d("TAG", "onResponse: data2: "+ anime)
                    }


                    binding.rvAnimelist.adapter= AnimeAdapter(animeList) { anime ->
                        val intent= Intent(this@MainActivity, AnimeDetailsActivity::class.java)
                        intent.putExtra("anime_id",anime.mal_id)
                        startActivity(intent)
                    }

                }
            }

            override fun onFailure(call: Call<AnimeResponse>, t: Throwable) {
                Log.d("TAG", "onFailure: "+ t)
                Toast.makeText(this@MainActivity, "Fetch Anime Failed: "+ t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
}