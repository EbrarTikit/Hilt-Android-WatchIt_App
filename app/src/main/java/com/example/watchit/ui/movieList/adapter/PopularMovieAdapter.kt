package com.example.watchit.ui.movieList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchit.common.loadImage
import com.example.watchit.data.model.Result
import com.example.watchit.databinding.ItemPopularMovieBinding

class PopularMovieAdapter : RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {
    private var movies = listOf<Result>()
    private var onMovieClickListener: ((Int) -> Unit)? = null

    fun setOnMovieClickListener(listener: (Int) -> Unit) {
        onMovieClickListener = listener
    }

    fun updateList(newList: List<Result>) {
        movies = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        return PopularMovieViewHolder(
            ItemPopularMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    inner class PopularMovieViewHolder(
        private val binding: ItemPopularMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Result) {
            binding.apply {
                movieTitle.text = movie.title
                ratingText.text = String.format("%.1f", movie.voteAverage)
                backdropImage.loadImage(movie.backdropPath)
                // Genre bilgisini API'den alıp gösterebilirsiniz
                genreText.text = "Action, Adventure, Sci-Fi" 

                root.setOnClickListener {
                    onMovieClickListener?.invoke(movie.id)
                }
            }
        }
    }
} 