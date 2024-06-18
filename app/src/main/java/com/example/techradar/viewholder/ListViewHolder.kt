package com.example.techradar.viewholder

import android.view.View
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techradar.adapter.ListAdapter
import com.example.techradar.databinding.ItemBinding
import com.example.techradar.model.Content

class ListViewHolder(
    private val binding: ItemBinding,
    //private val itemView: View,
    onItemClicked: (Int) -> Unit

) : RecyclerView.ViewHolder(binding.root) {
    init {

        binding.root.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }


    fun bind(content: Content) {
        Glide.with(binding.icon.context)
            .load(content.picture)
            .into(binding.icon)

        binding.prenom.text = content.firstname
        binding.nom.text = content.name
        binding.text.text = content.note
    }


}
