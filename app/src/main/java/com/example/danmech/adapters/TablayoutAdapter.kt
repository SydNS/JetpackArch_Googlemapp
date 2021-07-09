package com.example.danmech.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.danmech.FragDests.Login
import com.example.danmech.FragDests.SignUp

class TablayoutAdapter(context: Context, fm: FragmentManager?, totalTabs: Int) :
    FragmentPagerAdapter(fm!!) {
    private val myContext: Context = context
    var totalTabs: Int = totalTabs

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Login()
            }
            1 -> {
                SignUp()
            }

            else -> Login()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> {
                "Login"
            }
            1 -> {
                "SignUp"
            }

            else -> "Login"
        }
    }



}