package com.example.watchit.ui.movieList.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchit.data.model.Result

interface MovieClickListener {
    fun onMovieClicked(movieId: Int?)
}

class ListAdapter(private var list: List<Result>, private val movieClickListener: MovieClickListener) : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(parent)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            movieClickListener.onMovieClicked(list[position].id)
        }
    }

    fun updateList(newList: List<Result>) {
        list = newList
        notifyDataSetChanged()
    }
}
