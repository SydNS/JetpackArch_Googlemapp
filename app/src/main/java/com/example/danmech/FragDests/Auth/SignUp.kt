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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
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
    lateinit var signupbanner: TextView


    //declaring the views for grabbing the text from usrs
    lateinit var username: TextInputLayout
    lateinit var useremailaddress: TextInputLayout
    lateinit var userpassword: TextInputLayout
    lateinit var userpassword2: TextInputLayout
    lateinit var userphone: TextInputLayout

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


//    override fun onStart() {
//        super.onStart()
//
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
////        if (currentUser != null) {
////            1=1
////        }
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.signup, container, false)


        auth = FirebaseAuth.getInstance()
        firebasedatabase = FirebaseDatabase.getInstance()



        loadingBar = ProgressDialog(activity)

//        hooking views to e used in the class
        username = v.findViewById(R.id.username)
        userphone = v.findViewById(R.id.phone)
        useremailaddress = v.findViewById(R.id.emailaddress)
        userpassword = v.findViewById(R.id.password)
        userpassword2 = v.findViewById(R.id.password2)
        signupbtn = v.findViewById(R.id.signupbtn)
        signupbanner = v.findViewById(R.id.signupbanner)

//        setting the action onclicking the button
        signupbtn.setOnClickListener {

//            setting the views to showing no errors when the button is first clicked

            useremailaddress.isErrorEnabled = false
            useremailaddress.error = ""
            username.isErrorEnabled = false
            username.error = ""
            userpassword.isErrorEnabled = false
            userpassword.error = ""
            userpassword2.isErrorEnabled = false
            userpassword2.error = ""
            userphone.isErrorEnabled = false
            userphone.error = ""

//            gettting the user input on the click of the button
            val uname: String = username.editText?.text.toString().trim()
            val phone: String = userphone.editText?.text.toString().trim()
            val uemail: String = useremailaddress.editText?.text.toString().trim()
            val password: String = userpassword.editText?.text.toString().trim()
            val password2: String = userpassword2.editText?.text.toString().trim()

//         flow structure controlled here

            //checking if the value from the email field is empty or  not
            when {
                uname.isEmpty() -> {
                    signupbanner.text = "You're required to fill your Username"
                    username.isErrorEnabled = true
                    username.error = "Please Kindly Enter Your Username"
                }

                uemail.isEmpty() -> {
                    signupbanner.text = "You're required to fill your Email Address"
                    useremailaddress.isErrorEnabled = true
                    useremailaddress.error = "Please Kindly Enter Your Email Address"
                }
                phone.isEmpty() -> {
                    signupbanner.text = "You're required to fill your Phone Number"
                    userphone.isErrorEnabled = true
                    userphone.error = "Please Kindly Enter Your Phone Number"
                }
                phone.isNotEmpty() && phone.length != 10 -> {
                    signupbanner.text = "You're required to Enter a 10 Digit Phone Number"
                    userphone.isErrorEnabled = true
                    userphone.error = "Please Kindly Enter a 10 Digit Phone Number"
                }

                password.isEmpty() -> {
                    signupbanner.text =
                        "You're required to Have a Password That You'll confirm next"
                    userpassword.isErrorEnabled = true
                    userpassword.error = "Please Kindly Enter Your Password"
                }
                password.isNotEmpty() && password.length < 8 -> {
                    signupbanner.text = "You're required a Password 8 Character long"
                    userpassword.isErrorEnabled = true
                    userpassword.error = "Please Kindly Enter An 8 Character Long Password"
                }
                password2.isEmpty() -> {
                    signupbanner.text = "You're required to Confirm Your Password"
                    userpassword2.isErrorEnabled = true
                    userpassword2.error = "Please Kindly Confirm your Password"
                }
//
                password != password2 -> {
                    signupbanner.text = "You're required to Have Matching Passwords"
                    userpassword2.isErrorEnabled = true
                    userpassword2.error = "Please Kindly Enter Your Matching Passwords"
                }
//
//                uemail.isNotEmpty() and password.isNotEmpty() -> {
//                    signupbanner.text = "Thank you"
//                }

                else -> {
                    signupbanner.text = "Thank you"
//            method creating the user with the email & password provided
                    createAccount(uemail, password, v, "User")

                }
            }

//            these wonte be necessary becoz we are sendind the user directly to the home page skipping the
//            and combining the signup and login
//            layoutwithtabs = activity?.findViewById(R.id.fragment_auth)!!
//            tabs = layoutwithtabs.findViewById(R.id.tabs)
//            tabs.getTabAt(0)
        }

        return v
    }

    private fun createAccount(
        email: String,
        password: String,
        view: View,
        appuser: String
    ) {

        // loading bar that show the user some thing is happening
        loadingBar.setTitle("Please wait :")
        loadingBar.setMessage("While system is performing processing on your data...")
        loadingBar.show()

//        firebase code for signing up a user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {

                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser

                currentUserId = user?.uid.toString()
                customersDatabaseRef = firebasedatabase.reference
                    .child("Users").child("Clients").child(currentUserId)
                customersDatabaseRef.setValue(true)

                Toast.makeText(
                    activity,
                    "Welcome $currentUserId to the $appuser-side",
                    Toast.LENGTH_SHORT
                ).show()

                loadingBar.dismiss()


                Navigation.findNavController(view)
                    .navigate(R.id.action_authFragment_to_home_map)
            } else {
                // If sign in fails, display a message to the user.
                //                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                signupbanner.text="${it.exception?.message.toString()}"
                Toast.makeText(
                    activity,
                    "Error Occured ${it.exception?.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
                loadingBar.dismiss()

            }

        }
    }


//        override fun onStart() {
//            super.onStart()
//            // Check if user is signed in (non-null) and update UI accordingly.
//            val currentUser = auth?.currentUser
//            firebaseAuthListner?.let { auth?.addAuthStateListener(it) }
//
//        }
//
//        override fun onStop() {
//            super.onStop()
//            firebaseAuthListner?.let { auth?.removeAuthStateListener(it) }
//        }


}
