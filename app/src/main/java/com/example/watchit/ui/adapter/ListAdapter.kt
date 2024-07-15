package com.example.watchit.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.watchit.data.model.Movies
import com.example.watchit.data.model.Result
import retrofit2.Response

class ListAdapter(private var list: List<Result>): RecyclerView.Adapter<ListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder = ListViewHolder(parent)

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(newList: List<Result>) {
        list = newList
        notifyDataSetChanged()
    }
}