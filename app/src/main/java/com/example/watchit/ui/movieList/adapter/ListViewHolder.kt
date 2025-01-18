package com.example.watchit.ui.movieList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchit.R
import com.example.watchit.data.model.Result
import androidx.core.content.ContextCompat
import com.example.watchit.common.loadImage
import com.example.watchit.databinding.MainItemBinding

class ListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
) {
    private val binding = MainItemBinding.bind(itemView)

    fun bind(movie: Result) {
        binding.apply {
            mtv.text = movie.title
            img.loadImage(movie.posterPath)
            rating.text = String.format("%.1f", movie.voteAverage)
            
            // Puana göre renk değiştirme
            val ratingColor = when {
                movie.voteAverage >= 7.0 -> ContextCompat.getColor(itemView.context, R.color.rating_good)
                movie.voteAverage >= 5.0 -> ContextCompat.getColor(itemView.context, R.color.rating_medium)
                else -> ContextCompat.getColor(itemView.context, R.color.rating_bad)
            }
            rating.setTextColor(ratingColor)
        }
    }
}