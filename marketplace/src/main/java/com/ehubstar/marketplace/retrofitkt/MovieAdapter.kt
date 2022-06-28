package com.ehubstar.marketplace.retrofitkt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ehubstar.marketplace.databinding.MpAdapterMovieBinding
import com.ehubstar.marketplace.models.Movie

class MovieAdapter : RecyclerView.Adapter<MainViewHolder>() {
    var movieList = mutableListOf<Movie>()
    fun setMovies(movies: List<Movie>) {
        movieList = movies.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MpAdapterMovieBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val movie = movieList[position]
        holder.binding.name.text = movie.name
        Glide.with(holder.itemView.context)
            .load(movie.imageUrl)
            .into(holder.binding.imageview)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

}

class MainViewHolder(val binding: MpAdapterMovieBinding) : RecyclerView.ViewHolder(binding.root)