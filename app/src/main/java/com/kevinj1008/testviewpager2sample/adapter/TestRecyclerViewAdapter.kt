package com.kevinj1008.testviewpager2sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinj1008.testviewpager2sample.databinding.LayoutTestGalleryBinding

class TestRecyclerViewAdapter : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutTestGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Implement gallery view and behavior")
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(binding: LayoutTestGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        //TODO: get gallery view
    }

}