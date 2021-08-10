@file:Suppress("DEPRECATION")

package com.example.danmech.FragDests.Auth

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUp : Fragment() {

    lateinit var layoutwithtabs: RelativeLayout
    lateinit var tabs: TabLayout
    lateinit var signupbtn: Button
    lateinit var signupbanner: TextView


    //declaring the views for grabbing the text from usrs
    lateinit var username: TextInputLayout
    lateinit var useremailaddress: TextInputLayout
    lateinit var userpassword: TextInputLayout
    private lateinit var userpassword2: TextInputLayout
    lateinit var userphone: TextInputLayout

    //declaring the radio views for grabbing the decision from usrs
    lateinit var userselection: RadioGroup
    lateinit var userradiobtn: RadioButton
    lateinit var delivererradiobtn: RadioButton
    lateinit var radioUserButtonselected: RadioButton


    //firebase variables
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuthListner: FirebaseAuth.AuthStateListener
    private lateinit var firebasedatabase: FirebaseDatabase
    private lateinit var customersDatabaseRef: DatabaseReference
    private lateinit var deliverersDatabaseRef: DatabaseReference
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
        userradiobtn = v.findViewById(R.id.radioButtonUser)
        delivererradiobtn = v.findViewById(R.id.radioButtonDeliverer)
        userselection = v.findViewById(R.id.userselection)


//        setting the action onclicking the button
        signupbtn.setOnClickListener {

//          setting the views to showing no errors when the button is first clicked
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

//       gettting the user input on the click of the button
            val uname: String = username.editText?.text.toString().trim()
            val phone: String = userphone.editText?.text.toString().trim()
            val uemail: String = useremailaddress.editText?.text.toString().trim()
            val password: String = userpassword.editText?.text.toString().trim()
            val password2: String = userpassword2.editText?.text.toString().trim()

//           getting radiobutton values
            val selectedId: Int = userselection.checkedRadioButtonId
            radioUserButtonselected = v.findViewById(selectedId)

            val userdecision: String = radioUserButtonselected.text.toString().trim()


//         flow structure controlled here

            //checking if the value from the email field is empty or  not
            when {
                uname.isEmpty() -> {
                    signupbanner.text = getString(R.string.userameneeded)
                    username.isErrorEnabled = true
                    username.error = getString(R.string.enteruserameneeded)
                }

                uemail.isEmpty() -> {
                    signupbanner.text = getString(R.string.emailrequired)
                    useremailaddress.isErrorEnabled = true
                    useremailaddress.error = getString(R.string.enteremailaddress)
                }
                phone.isEmpty() -> {
                    signupbanner.text = getString(R.string.enterphonenumber)
                    userphone.isErrorEnabled = true
                    userphone.error = getString(R.string.enterphonenumberkindly)
                }
                phone.isNotEmpty() && phone.length != 10 -> {
                    signupbanner.text = getString(R.string.enter10digitsphonenumber)
                    userphone.isErrorEnabled = true
                    userphone.error = getString(R.string.enter10digitnumbers)
                }

                password.isEmpty() -> {
                    signupbanner.text =
                        getString(R.string.confirmpassword)
                    userpassword.isErrorEnabled = true
                    userpassword.error = getString(R.string.kindlyenterpassword)
                }
                password.isNotEmpty() && password.length < 8 -> {
                    signupbanner.text = getString(R.string.eightcharacterlong)
                    userpassword.isErrorEnabled = true
                    userpassword.error = getString(R.string.eightcharacterlongkindly)
                }
                password2.isEmpty() -> {
                    signupbanner.text = getString(R.string.confirmpassword2)
                    userpassword2.isErrorEnabled = true
                    userpassword2.error = getString(R.string.kindlyconfirm)
                }
//
                password != password2 -> {
                    signupbanner.text = getString(R.string.matchingpasswordsneeded)
                    userpassword2.isErrorEnabled = true
                    userpassword2.error = getString(R.string.matchingpassword2)
                }
                userdecision.isEmpty() -> {
                    userradiobtn.error = getString(R.string.radiobtnusertype)
                    delivererradiobtn.error = getString(R.string.radiobtnusertype)
                }

                else -> {
                    signupbanner.text = getString(R.string.thanks)
//            method creating the user with the email & password provided
                    createAccount(uemail, password, v, userdecision)

                }
            }

//            these wonte be necessary becoz we are sending the user directly to the home page skipping the
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

        storeDecidedUser(appuser)

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
                if (appuser=="Client"){
                    customersDatabaseRef = firebasedatabase.reference
                        .child(getString(R.string.users)).child(getString(R.string.clients))
                        .child(currentUserId)
                    customersDatabaseRef.setValue(true)

                    loadingBar.dismiss()
                    Navigation.findNavController(view)
                        .navigate(R.id.action_authFragment_to_home_map)
                }else{

                    deliverersDatabaseRef =
                        FirebaseDatabase.getInstance().reference.child(getString(R.string.users))
                            .child(getString(R.string.deliverers)).child(
                                currentUserId
                            )
                    deliverersDatabaseRef.setValue(true)

                    loadingBar.dismiss()
                    Navigation.findNavController(view)
                        .navigate(R.id.action_authFragment_to_deliverersmap)

                }


                Toast.makeText(
                    activity,
                    "Welcome $currentUserId to the $appuser-side",
                    Toast.LENGTH_SHORT
                ).show()



            } else {
                // If sign in fails, display a message to the user.
                //                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                signupbanner.text = it.exception?.message.toString()
                Toast.makeText(
                    activity,
                    "Error Occured ${it.exception?.message.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
                loadingBar.dismiss()

            }

        }
    }

    private fun storeDecidedUser(usertype: String) {

        val sharedPreferences =
            requireActivity().getSharedPreferences("OldUserType", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Type", usertype)
        editor.apply()

    }


}
