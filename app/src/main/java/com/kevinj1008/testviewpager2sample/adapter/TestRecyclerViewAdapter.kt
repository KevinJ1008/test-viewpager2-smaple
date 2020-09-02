package com.kevinj1008.testviewpager2sample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kevinj1008.testviewpager2sample.customview.transformer.ScaleTransformer
import com.kevinj1008.testviewpager2sample.databinding.LayoutTestGalleryBinding

class TestRecyclerViewAdapter : RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutTestGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adapter = TestGalleryAdapter()
        val transFormer = ScaleTransformer()
        holder.viewPager.apply {
            isInfiniteLoop = true
            setPageTransformer(transFormer)
            setAdapter(adapter)
            setAutoScroll(3000)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    class ViewHolder(binding: LayoutTestGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewPager = binding.viewpagerGallery
    }

}