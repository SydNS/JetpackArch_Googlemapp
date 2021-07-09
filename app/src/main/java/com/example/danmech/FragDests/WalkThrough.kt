package com.example.danmech.FragDests

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.danmech.R
import com.example.danmech.adapters.ViewPagerAdapter


class WalkThrough : Fragment() {

    private lateinit var viewPager2: ViewPager2
    private lateinit var prev: Button
    private lateinit var next: Button
    private lateinit var welcome: Button

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_walk_through, container, false)
        val imagesList = listOf(

                R.drawable.bg,
                R.drawable.bg,
                R.drawable.bg
        )
        val sloganList = listOf(

                "Mechanics probably won’t need the ones that show the basic stuff like changing oil.",
                "Bringing the best mechanics to your convenience whenever you have a motor breackage.",
                "Mechanics probably won’t need the ones that show the basic stuff like changing oil."
        )
        val titlesList = listOf(

                "Mechanics",
                "Mechanics",
                "Mechanics",

                )

        val adapter = ViewPagerAdapter(imagesList, titlesList, sloganList)
        viewPager2 = view.findViewById(R.id.viewPager)
        viewPager2.adapter = adapter
        prev = view.findViewById(R.id.prev)
        next = view.findViewById(R.id.next)
        welcome= view.findViewById(R.id.skip)


        managingViewPager(prev, next,welcome)


        return view
    }

    private fun managingViewPager(prev: Button, next: Button,welcome:Button) {
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
                        prev.visibility=View.INVISIBLE
                        next.visibility=View.VISIBLE
                        welcome.visibility=View.INVISIBLE

                    }
                    1 -> {
                        prev.visibility=View.VISIBLE
                        next.visibility=View.VISIBLE
                        welcome.visibility=View.INVISIBLE

                    }
                    2 -> {
                        prev.visibility=View.INVISIBLE
                        next.visibility=View.INVISIBLE
                        welcome.visibility=View.VISIBLE
                    }
                }

            }
//override method(s) what you need it
        })




    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WalkThrough.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WalkThrough().apply {


                }
    }
}