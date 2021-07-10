package com.example.danmech.FragDests.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R

class Login : Fragment() {
    lateinit var loginbtn:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.theme?.applyStyle(R.style.loginstylemain,true)
        val v : View=inflater.inflate(R.layout.login,container,false)
        loginbtn=v.findViewById(R.id.loginbtn)
        login(v)
        return v
    }

    private fun login(v: View) {
        v.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_authFragment_to_home_map)
        }

    }

}
