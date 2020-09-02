package com.kevinj1008.testviewpager2sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kevinj1008.testviewpager2sample.R
import com.kevinj1008.testviewpager2sample.databinding.LayoutGalleryItemBinding

class TestGalleryAdapter : RecyclerView.Adapter<TestGalleryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutGalleryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var color: Int = R.color.default_color
        when (position) {
            0 -> {
                color = R.color.position_0_color
            }
            1 -> {
                color = R.color.position_1_color
            }
            2 -> {
                color = R.color.position_2_color
            }
            3 -> {
                color = R.color.position_3_color
            }
            4 -> {
                color = R.color.position_4_color
            }
        }
        holder.image.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, color))
    }

    override fun getItemCount(): Int {
        return 5
    }

    class ViewHolder(binding: LayoutGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageItem
    }
}