package com.example.emkulimaapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.R
import com.example.emkulimaapp.adapters.ViewFragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrdersFragment : Fragment() {
    @BindView(R.id.tabOrders)
    lateinit var tabLayout: TabLayout
    @BindView(R.id.viewPagerOrders)
    lateinit var viewPager: ViewPager2
    @BindView(R.id.imgBackOrder)
    lateinit var back: ImageView

    private lateinit var pendingOrdersFragment: PendingOrdersFragment
    private lateinit var completeOrdersFragment: CompleteOrdersFragment

    private lateinit var viewFragmentPagerAdapter: ViewFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = View.inflate(activity, R.layout.fragment_orders, null)
        ButterKnife.bind(this, view)
        back.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        viewFragmentPagerAdapter = ViewFragmentPagerAdapter(this)
        pendingOrdersFragment = PendingOrdersFragment()
        completeOrdersFragment = CompleteOrdersFragment()
        viewFragmentPagerAdapter.getFragmentDetails(pendingOrdersFragment)
        viewFragmentPagerAdapter.getFragmentDetails(completeOrdersFragment)
        viewPager.adapter = viewFragmentPagerAdapter
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
                when(position){
                    0 -> {
                        tab.text = "Pending Orders"
                    }
                    1 -> {
                        tab.text = "Complete Orders"
                    }
                    else -> {
                        tab.text = ""
                    }
                }
        }.attach()
        return view
    }
}