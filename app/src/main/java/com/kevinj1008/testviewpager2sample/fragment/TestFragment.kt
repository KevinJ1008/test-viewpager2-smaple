package com.kevinj1008.testviewpager2sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kevinj1008.testviewpager2sample.adapter.TestRecyclerViewAdapter
import com.kevinj1008.testviewpager2sample.databinding.LayoutTestFragmentBinding

class TestFragment : Fragment() {

    private var _binding: LayoutTestFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutTestFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testRecyclerview.adapter = TestRecyclerViewAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}