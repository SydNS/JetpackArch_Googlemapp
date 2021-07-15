package com.example.danmech.FragDests.Auth

import android.content.ContentValues.TAG
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
import com.example.danmech.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

    //progressbar
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Initialize Firebase Auth
        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
//            reload();
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.signup, container, false)


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
                userpassword.error="Password is empty"
                Toast.makeText(
                    requireActivity(),
                    "Enter password!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (password.length < 6) {
                userpassword.error="Password is short"
                Toast.makeText(
                    requireActivity(),
                    "Password too short, enter minimum 6 characters!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            requireActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
//                        updateUI(null)
                    }
                }




            layoutwithtabs = activity?.findViewById(R.id.fragment_auth)!!

            tabs = layoutwithtabs.findViewById(R.id.tabs)
            tabs.getTabAt(0)
        }







        return v
    }

}
