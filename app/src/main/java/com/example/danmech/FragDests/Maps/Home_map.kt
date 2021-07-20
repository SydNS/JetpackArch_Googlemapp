@file:Suppress("DEPRECATION")

package com.example.danmech.FragDests.Maps

import android.content.Context
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Home_map : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
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

    private val closestDeliiverer: Unit
        private get() {
            val geoFire: GeoFire = GeoFire(DriverAvailableRef)
            geoQuery = geoFire.queryAtLocation(
                GeoLocation(
                    CustomerPickUpLocation!!.latitude,
                    CustomerPickUpLocation!!.longitude
                ), radius.toDouble()
            )
            geoQuery?.removeAllListeners()
            geoQuery?.addGeoQueryEventListener(object : GeoQueryEventListener {
                public override fun onKeyEntered(key: String, location: GeoLocation) {
                    //anytime the driver is called this method will be called
                    //key=driverID and the location
                    if (!driverFound!! && requestType) {
                        driverFound = true
                        driverFoundID = key


                        //we tell driver which customer he is going to have
                        DriversRef = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child("Drivers").child(
                                driverFoundID!!
                            )
                        val driversMap = HashMap<String?, String?>()
                        driversMap["CustomerRideID"] = customerID
                        DriversRef!!.updateChildren(driversMap as Map<String, Any>)

                        //Show driver location on customerMapActivity
                        GettingDriverLocation()
                        CallCabCarButton!!.setText("Looking for Mechanic Location...")
                    }
                }

                public override fun onKeyExited(key: String) {}
                public override fun onKeyMoved(key: String, location: GeoLocation) {}
                public override fun onGeoQueryReady() {
                    if (!driverFound!!) {
                        radius += 1
                        closetDriverCab
                    }
                }

                override fun onGeoQueryError(error: DatabaseError) {}
            })
        }


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

    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
    }


    public override fun onConnectionSuspended(i: Int) {}
    public override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    public override fun onLocationChanged(location: Location) {
        //getting the updated location
        LastLocation = location
        val latLng: LatLng = LatLng(location.latitude, location.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))
    }

    override fun onMapReady(p0: GoogleMap?) {
        TODO("Not yet implemented")
    }
}