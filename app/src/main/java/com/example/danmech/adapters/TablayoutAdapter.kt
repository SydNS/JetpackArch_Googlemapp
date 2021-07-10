package com.example.danmech.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.danmech.FragDests.Auth.Login
import com.example.danmech.FragDests.Auth.SignUp

class TablayoutAdapter(context: Context, fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {
    private val myContext: Context = context

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
        return 2
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