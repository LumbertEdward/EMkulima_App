package com.example.emkulimaapp.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewFragmentPagerAdapter(fm: Fragment): FragmentStateAdapter(fm) {

    private var fragments: ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getFragmentDetails(f: Fragment){
        fragments.add(f)
    }

}