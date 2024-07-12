package com.example.techradar.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.techradar.R
import com.example.techradar.databinding.ItemBinding
import com.example.techradar.model.Content
import com.example.techradar.ui.edit.getDrawableUri

class ListViewHolder(
    private val binding: ItemBinding,

    onItemClicked: (Int) -> Unit

) : RecyclerView.ViewHolder(binding.root) {
    init {

        binding.root.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }


    fun bind(content: Content) {

        val colourInt = ContextCompat.getColor(binding.root.context, R.color.transparent)

        if (content.picture?.isNotEmpty() == true) {

            binding.icon.setBackgroundColor(colourInt)

            val drawable = getDrawableUri(binding.root.context,R.drawable.baseline_category_24).toString()
            if(content.picture == drawable) {

                val colourNew = ContextCompat.getColor(binding.root.context, R.color.picture)
                binding.icon.setBackgroundColor(colourNew)
                val drawable2 = ContextCompat.getDrawable(binding.root.context,R.drawable.baseline_category_24)
                binding.icon.setImageDrawable(drawable2)

            }
        } else {
            val colourNew = ContextCompat.getColor(binding.root.context, R.color.picture)
            binding.icon.setBackgroundColor(colourNew)

            val drawable = ContextCompat.getDrawable(binding.root.context,R.drawable.baseline_category_24)
            binding.icon.setImageDrawable(drawable)
        }

        Glide.with(binding.icon.context)
            .load(content.picture)

            .into(binding.icon)

        binding.prenom.text = content.firstname
        binding.nom.text = content.name
        binding.text.text = content.note
    }


}
