package com.example.watchit.ui.movieList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchit.common.loadImage
import com.example.watchit.data.model.Result
import com.example.watchit.databinding.ItemMovieCardBinding

class MovieCardAdapter : RecyclerView.Adapter<MovieCardAdapter.MovieViewHolder>() {
    private var list: List<Result> = emptyList()
    private var onMovieClickListener: ((Int) -> Unit)? = null

    fun setOnMovieClickListener(listener: (Int) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Result>) {
        list = newList
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Result) {
            binding.apply {
                moviePoster.loadImage(movie.posterPath)
                root.setOnClickListener {
                    onMovieClickListener?.invoke(movie.id)
                }
            }
        }
    }
}