package com.example.watchit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.watchit.R
import com.example.watchit.data.model.Result

class ListViewHolder(val container : ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(container.context)
            .inflate(
                R.layout.main_item,
                container,
                false
            )
    ){

        val movieName : TextView = itemView.findViewById(R.id.mtv)
        val movieImg : ImageView = itemView.findViewById(R.id.img)
        fun bind(movData: Result) {
            val posterBaseUrl = "https://image.tmdb.org/t/p/w500/"
            movieName.text = movData.title
            Glide
                .with(itemView.context)
                .load(posterBaseUrl + movData.posterPath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.error_img)
                .into(movieImg)

        }

}