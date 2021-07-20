@file:Suppress("DEPRECATION")

package com.example.danmech.FragDests.Auth

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : Fragment() {
    lateinit var loginbtn: Button
    lateinit var logout_customer_btn: Button
    lateinit var login_emailaddress: TextInputLayout
    lateinit var login_password: TextInputLayout


    //firebase variables
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuthListner: FirebaseAuth.AuthStateListener
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var customersDatabaseRef: DatabaseReference
    private lateinit var loadingBar: ProgressDialog
    private lateinit var currentUser: FirebaseUser
    private lateinit var currentUserId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Initialize Firebase Auth

        firebaseAuthListner = FirebaseAuth.AuthStateListener {
            currentUser = FirebaseAuth.getInstance().currentUser!!

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_authFragment_to_home_map)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.theme?.applyStyle(R.style.loginstylemain, true)

        auth = FirebaseAuth.getInstance()
        firebasedatabase = FirebaseDatabase.getInstance()

        loadingBar = ProgressDialog(activity)


        val v: View = inflater.inflate(R.layout.login, container, false)
        loginbtn = v.findViewById(R.id.loginbtn)
        login_emailaddress = v.findViewById(R.id.login_emailaddress)
        login_password = v.findViewById(R.id.login_password)

        login(loginbtn)


        return v
    }

    private fun login(v: Button) {
        v.setOnClickListener {
            val uemail: String = login_emailaddress.editText?.text.toString().trim()
            val upassd: String = login_password.editText?.text.toString().trim()

            //          checking if the value from the email field is empty or  not
            if (TextUtils.isEmpty(uemail)) {
                Toast.makeText(
                    requireActivity(),
                    "Enter email address!",
                    Toast.LENGTH_SHORT
                ).show()

            }
            if (TextUtils.isEmpty(upassd)) {
                Toast.makeText(
                    requireActivity(),
                    "Enter password!",
                    Toast.LENGTH_SHORT
                ).show()

            }

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_authFragment_to_home_map)
        }

    }


}
