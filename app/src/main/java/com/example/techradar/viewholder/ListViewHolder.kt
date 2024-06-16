package com.example.techradar.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techradar.databinding.ItemBinding
import com.example.techradar.model.Content

class ListViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

val container = binding.itemClickable

    fun bind(content: Content) {
        Glide.with(binding.icon.context)
            .load(content.picture)
            .into(binding.icon)

        binding.prenom.text = content.firstname
        binding.nom.text = content.name
        binding.text.text = content.note
    }

    override fun onClick(v: View?) {
        val position = adapterPosition

    }
}
