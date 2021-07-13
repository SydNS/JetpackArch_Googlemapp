package com.example.danmech.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs


class MainActivity : AppCompatActivity() {
    lateinit var moyosharedprefs:Moyosharedprefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!moyosharedprefs.isUserNotOld  ){



        }


    }
}