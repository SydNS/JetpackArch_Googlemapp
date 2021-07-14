package com.example.danmech.FragDests.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.example.danmech.R
import com.google.android.material.tabs.TabLayout

class SignUp : Fragment() {

    lateinit var layoutwithtabs: RelativeLayout
    lateinit var tabs: TabLayout
    lateinit var signupbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.signup, container, false)


        signupbtn=v.findViewById(R.id.signupbtn)
        signupbtn.setOnClickListener {
            layoutwithtabs = activity?.findViewById(R.id.fragment_auth)!!

            tabs= layoutwithtabs.findViewById(R.id.tabs)
            tabs.getTabAt(0)
        }







        return v
    }

}
