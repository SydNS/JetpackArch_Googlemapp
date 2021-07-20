//@file:Suppress("DEPRECATION")
//
//package com.example.danmech.FragDests
//
//import android.os.Bundle
//import android.os.Handler
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.os.HandlerCompat.postDelayed
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.NavHostFragment
//import com.example.danmech.R
//
///**
// * An example full-screen fragment that shows and hides the system UI (i.e.
// * status bar and navigation/system bar) with user interaction.
// */
//class Splashscreen : Fragment() {
//    private val hideHandler = Handler()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        var view: View = inflater.inflate(R.layout.fragment_splashscreen, container, false)
//
//        move()
//        return view
//    }
//
//    private fun move() {
////        Handler().postDelayed({
//                NavHostFragment.findNavController(this)
//                .navigate(R.id.action_splashscreen_to_walkThrough)
//
////        }, 200)
//
//    }
//
//
//
//
//
//
//    companion object {
//        /**
//         * Whether or not the system UI should be auto-hidden after
//         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
//         */
//        private const val AUTO_HIDE = true
//
//        /**
//         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
//         * user interaction before hiding the system UI.
//         */
//        private const val AUTO_HIDE_DELAY_MILLIS = 3000
//
//        /**
//         * Some older devices needs a small delay between UI widget updates
//         * and a change of the status and navigation bar.
//         */
//        private const val UI_ANIMATION_DELAY = 300
//    }
//}