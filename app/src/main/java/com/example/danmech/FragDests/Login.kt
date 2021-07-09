package com.example.danmech.FragDests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.danmech.R

class Login : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.theme?.applyStyle(R.style.loginstylemain,true)
        val v : View=inflater.inflate(R.layout.login,container,false)
        return v
    }

}
