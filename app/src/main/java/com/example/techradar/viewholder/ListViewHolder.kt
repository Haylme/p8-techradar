package com.example.techradar.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techradar.databinding.ItemBinding
import com.example.techradar.model.Content

class ListViewHolder(private val binding : ItemBinding): RecyclerView.ViewHolder(binding.root) {


    fun bind(content: Content){

        Glide.with(binding.icon.context)
            .load(content.picture)
            .into(binding.icon)

        binding.prenom.text = content.firstname
        binding.nom.text  = content.name
        binding.text.text = content.note


    }








}