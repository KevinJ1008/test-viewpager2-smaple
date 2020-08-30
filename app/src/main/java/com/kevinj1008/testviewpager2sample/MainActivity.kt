package com.kevinj1008.testviewpager2sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kevinj1008.testviewpager2sample.adapter.TestFragmentStateAdapter
import com.kevinj1008.testviewpager2sample.databinding.ActivityMainBinding
import com.kevinj1008.testviewpager2sample.fragment.TestFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var testTabLayout: TabLayoutMediator? = null
    private var adapter: TestFragmentStateAdapter? = null
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        val view = bind.root
        setContentView(view)
        initViewPager2()
        bindTabLayout()
    }

    private fun initViewPager2() {
        adapter = TestFragmentStateAdapter(this)
        bind.testViewPager2.adapter = adapter
    }

    private fun bindTabLayout() {
        testTabLayout?.detach()
        testTabLayout = TabLayoutMediator(bind.layoutTestTab, bind.testViewPager2) { tab, position ->
            if (position == TabLayout.Tab.INVALID_POSITION) return@TabLayoutMediator
            tab.text = adapter?.fragmentTitle(position)
        }
        testTabLayout?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        testTabLayout?.detach()
        bind.testViewPager2.adapter = null
        adapter = null
    }
}
