package com.example.animeapp.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionScene.Transition.TransitionOnClick
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapp.data.Anime
import com.example.animeapp.databinding.ActivityMainBinding
import com.example.animeapp.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class AnimeAdapter(private val animeList: List<Anime>,
                   private val onItemClick: (Anime) ->Unit) :
    RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(animeItem: Anime){
            binding.tvTitle.text=animeItem.title
            binding.tvEpisodes.text= "Episodes: ${animeItem.episodes ?: "NA"}"
            binding.tvRating.text= "Rating: ${animeItem.score ?: "NA"}"

//            Picasso.get().load(animeItem.images.jpg.image_url).into(binding.ivPoster)
            Glide.with(binding.ivPoster.context).load(animeItem.images.jpg.image_url).into(binding.ivPoster)

            binding.root.setOnClickListener{ onItemClick(animeItem) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding= ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AnimeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animeList.size
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(animeList[position])
    }


}