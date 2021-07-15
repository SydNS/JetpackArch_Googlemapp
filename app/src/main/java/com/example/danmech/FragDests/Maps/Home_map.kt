package com.example.danmech.FragDests.Maps

import android.content.Context
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Home_map : Fragment() {
    lateinit var logout_customer_btn: Button

    private lateinit var moyosharedprefs: Moyosharedprefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!isUserOld()){
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_home_map_to_walkThrough)
        }

    }


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v:View=inflater.inflate(R.layout.fragment_home_map, container, false)

        moyosharedprefs=Moyosharedprefs(requireActivity().applicationContext)
//
//        if(!isUserOld()){
//            NavHostFragment
//                .findNavController(this)
//                .navigate(R.id.action_home_map_to_walkThrough)
//        }


        logout_customer_btn=v.findViewById(R.id.logout_customer_btn)

        logout(logout_customer_btn)

        return v }

    fun isUserOld(): Boolean {
        val sharedPreferences =
            requireActivity().getSharedPreferences("", Context.MODE_PRIVATE)

        return sharedPreferences.getBoolean("Old",false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun logout(v: Button) {
        v.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_home_map_to_authFragment)
        }

    }
}