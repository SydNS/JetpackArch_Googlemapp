@file:Suppress("DEPRECATION")

package com.example.danmech.FragDests.Auth

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SignUp : Fragment() {

    lateinit var layoutwithtabs: RelativeLayout
    lateinit var tabs: TabLayout
    lateinit var signupbtn: Button

    //declaring the views for grabbing the text from usrs
    lateinit var username: TextInputLayout
    lateinit var useremailaddress: TextInputLayout
    lateinit var userpassword: TextInputLayout
    lateinit var userpassword2: TextInputLayout

    //firebase variables
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuthListner: FirebaseAuth.AuthStateListener
    private lateinit var firbasedatabase: FirebaseDatabase
    private lateinit var database: DatabaseReference
    private lateinit var customersDatabaseRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loadingBar: ProgressDialog
    private lateinit var currentUser: FirebaseUser
    lateinit var currentUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        firebaseAuthListner = FirebaseAuth.AuthStateListener {
            currentUser = FirebaseAuth.getInstance().currentUser!!

            NavHostFragment.findNavController(this)
                .navigate(R.id.action_authFragment_to_home_map)
        }
    }


    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            1=1
//        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.signup, container, false)

        database = Firebase.database.reference
        loadingBar= ProgressDialog(activity)
//        hooking views to e used in the class
        username = v.findViewById(R.id.username)
        useremailaddress = v.findViewById(R.id.emailaddress)
        userpassword = v.findViewById(R.id.password)
        userpassword2 = v.findViewById(R.id.password2)



        signupbtn = v.findViewById(R.id.signupbtn)
        signupbtn.setOnClickListener {

            val email: String = useremailaddress.editText?.text.toString().trim()
            val password: String = userpassword.editText?.text.toString().trim()
            val password2: String = userpassword2.editText?.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    requireActivity(),
                    "Enter email address!",
                    Toast.LENGTH_SHORT
                ).show()

            }

            if (TextUtils.isEmpty(password)) {
                userpassword.error = "Password is empty"
                Toast.makeText(
                    requireActivity(),
                    "Enter password!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (password.length < 6) {
                userpassword.error = "Password is short"
                Toast.makeText(
                    requireActivity(),
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", it.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }



            layoutwithtabs = activity?.findViewById(R.id.fragment_auth)!!

            tabs = layoutwithtabs.findViewById(R.id.tabs)
            tabs.getTabAt(0)
        }







        return v
    }


//override fun onStop() {
//    super.onStop()
//    firebaseAuthListner?.let { auth?.removeAuthStateListener(it) }
//}


}
