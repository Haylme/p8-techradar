package com.example.techradar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.techradar.databinding.ItemBinding
import com.example.techradar.model.Content
import com.example.techradar.model.SimpleResponse
import com.example.techradar.viewholder.ListViewHolder

class ListAdapter(private var list: List<Content>) : RecyclerView.Adapter<ListViewHolder>() {


    fun updateList(newList: SimpleResponse<List<Content?>>) {

        val filteredList = newList.data?.filterNotNull() ?: emptyList()
        list = filteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
       val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)


    }

    override fun getItemCount(): Int  = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       holder.bind(list[position])
    }


}


