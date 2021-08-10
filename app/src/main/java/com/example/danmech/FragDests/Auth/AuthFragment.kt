package com.example.danmech.FragDests.Auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.example.danmech.R
import com.example.danmech.adapters.TablayoutAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var tabs:TabLayout
    lateinit var appbarauth:AppBarLayout
    lateinit var vp:ViewPager
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var tablayoutAdapter:TablayoutAdapter
    lateinit var supoortFragmentManager: FragmentManager
    lateinit var ctx:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


//        if(1==1){
//
//            NavHostFragment.findNavController(this)
//                .navigate(R.id.action_authFragment_to_splashscreen)
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v: View=inflater.inflate(R.layout.fragment_auth, container, false)

        appbarauth=v.findViewById(R.id.appbarauth)
        toolbar=v.findViewById(R.id.toolbar)
        tabs=v.findViewById(R.id.tabs)
        vp=v.findViewById(R.id.vpauth)

//        adapter for attahing the tab with the viewpager that wll swipe thru the frags
        tablayoutAdapter=TablayoutAdapter(ctx,parentFragmentManager)
        vp.adapter=tablayoutAdapter
        tabs.setupWithViewPager(vp)

        return v }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}