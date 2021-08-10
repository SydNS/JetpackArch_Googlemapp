package com.example.danmech.FragDests

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.example.danmech.R
import com.example.danmech.R.*
import com.example.danmech.Sharedprefs.Moyosharedprefs
import com.example.danmech.adapters.ViewPagerAdapter


class WalkThrough : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var prev: Button
    private lateinit var next: Button
    private lateinit var welcome: Button


    private lateinit var dot1: ImageView
    private lateinit var dot2: ImageView
    private lateinit var dot3: ImageView
    private lateinit var dot4: ImageView
    private lateinit var dot5: ImageView
    private lateinit var dot6: ImageView

    private lateinit var moyosharedprefs: Moyosharedprefs

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(layout.fragment_walk_through, container, false)
        val imagesList = listOf(

            drawable.walkthrough2,
            drawable.clean,
            drawable.walkthrough1
        )
        val sloganList = listOf(

            getString(string.firstwalkthroughdescription),
            getString(string.secondwalkthroughdescription),
            getString(string.thirdwalkthroughdescription)
        )
        val titlesList = listOf(

            getString(string.firsttitle),
            getString(string.secondwalkthroughtitle),
            getString(string.thirdwalkthroughtitle)

        )

        moyosharedprefs = Moyosharedprefs(requireActivity().applicationContext)

        val adapter = ViewPagerAdapter(imagesList, titlesList, sloganList)
        viewPager2 = view.findViewById(R.id.viewPager)
        viewPager2.adapter = adapter
        prev = view.findViewById(R.id.prev)
        next = view.findViewById(R.id.next)
        welcome = view.findViewById(R.id.welcome)

        dot1 = view.findViewById(R.id.dot1)
        dot2 = view.findViewById(R.id.dot2)
        dot3 = view.findViewById(R.id.dot3)
        welcome.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_walkThrough_to_authFragment)
        }


        managingViewPager(prev, next, welcome, dot1, dot2, dot3)

        return view
    }

    private fun managingViewPager(
        prev: Button,
        next: Button,
        welcome: Button,
        ind1: ImageView,
        ind2: ImageView,
        ind3: ImageView


    ) {
        prev.setOnClickListener {
            viewPager2.currentItem = viewPager2.currentItem - 1
        }
        next.setOnClickListener {
            viewPager2.currentItem = viewPager2.currentItem + 1
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        prev.visibility = View.INVISIBLE
                        next.visibility = View.VISIBLE
                        welcome.visibility = View.INVISIBLE

                        ind3.setImageResource(drawable.dotactive)
                        ind1.setImageResource(drawable.dotinactive)
                        ind2.setImageResource(drawable.dotinactive)


                    }
                    1 -> {
                        prev.visibility = View.VISIBLE
                        next.visibility = View.VISIBLE
                        welcome.visibility = View.INVISIBLE

                        ind3.setImageResource(drawable.dotinactive)
                        ind1.setImageResource(drawable.dotactive)
                        ind2.setImageResource(drawable.dotinactive)


                    }
                    2 -> {
                        prev.visibility = View.INVISIBLE
                        next.visibility = View.INVISIBLE
                        welcome.visibility = View.VISIBLE

                        ind3.setImageResource(drawable.dotinactive)
                        ind1.setImageResource(drawable.dotinactive)
                        ind2.setImageResource(drawable.dotactive)


                    }
                }

            }
//override method(s) what you need it
        })

        welcome.setOnClickListener {
            MakeOld()
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_walkThrough_to_authFragment)

        }


    }

    fun MakeOld() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("OldUser", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("Old", true)
        editor.apply()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *

         * @return A new instance of fragment WalkThrough.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            WalkThrough().apply {


            }
    }
}