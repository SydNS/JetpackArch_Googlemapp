package com.example.danmech.FragDests.Auth

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : Fragment() {
    lateinit var loginbtn:Button
    lateinit var logout_customer_btn:Button


    private var auth: FirebaseAuth? = null
    private var firebaseAuthListner: FirebaseAuth.AuthStateListener? = null
    private var loadingBar: ProgressDialog? = null
    private var currentUser: FirebaseUser? = null
    var currentUserId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.theme?.applyStyle(R.style.loginstylemain,true)
        auth= FirebaseAuth.getInstance()
        val v : View=inflater.inflate(R.layout.login,container,false)
        loginbtn=v.findViewById(R.id.loginbtn)
        login(loginbtn)


        return v
    }

    private fun login(v: Button) {
        v.setOnClickListener {


            NavHostFragment.findNavController(this)
                .navigate(R.id.action_authFragment_to_home_map)
        }

    }



}
