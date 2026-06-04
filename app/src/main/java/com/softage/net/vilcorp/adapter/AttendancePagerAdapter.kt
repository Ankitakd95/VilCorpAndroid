package com.softage.net.vilcorp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


import com.softage.net.vilcorp.ui.fragment.HistoryFragment
import com.softage.net.vilcorp.ui.fragment.PunchFragment

class AttendancePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0 -> PunchFragment()
            else -> HistoryFragment()

        }
    }
}