package com.example.danmech.Activitys

import android.R.attr.fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.danmech.FragDests.Auth.AuthFragment
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs


class MainActivity : AppCompatActivity() {
    lateinit var moyosharedprefs:Moyosharedprefs
    lateinit var authFragment:AuthFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        moyosharedprefs= Moyosharedprefs(this)
//        authFragment=AuthFragment()
//
//
//        val manager: FragmentManager = supportFragmentManager
//        val transaction: FragmentTransaction = manager.beginTransaction()
//
//        transaction.replace(R.id.container, authFragment)
//        transaction.commit()

//        if (!moyosharedprefs.isUserNotOld  ){
//            authFragment.supoortFragmentManager.
//
//        }


    }
}