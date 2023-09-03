package com.thorcode.githubuserapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thorcode.githubuserapp.detail.ListFollowFragment

class SectionPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        val fragment = ListFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(ListFollowFragment.ARG_POSITION, position + 1)
            putString(ListFollowFragment.ARG_USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return ITEM_NUMBER
    }

    companion object {
        const val ITEM_NUMBER = 2
    }
}