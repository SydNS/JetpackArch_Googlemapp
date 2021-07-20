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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView


class Home_map : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    lateinit var logout_customer_btn: Button
    lateinit var details: Button
    lateinit var request_button: Button
    private lateinit var moyosharedprefs: Moyosharedprefs


    private var mMap: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    var LastLocation: Location? = null
    var locationRequest: LocationRequest? = null
    private var Logout: Button? = null
    private var SettingsButton: Button? = null
    private var CallDelivererButton: Button? = null
    private var callingbtn: ImageView? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var CustomerDatabaseRef: DatabaseReference? = null
    private var CustomerPickUpLocation: LatLng? = null
    private var delivererAvailableRef: DatabaseReference? = null
    private var DriverLocationRef: DatabaseReference? = null
    private var DriversRef: DatabaseReference? = null
    private var radius: Int = 1
    private var delivererFound: Boolean? = false
    private var requestType: Boolean = false
    private var delivererFoundID: String? = null
    private var customerID: String? = null
    var DriverMarker: Marker? = null
    var PickUpMarker: Marker? = null
    var geoQuery: GeoQuery? = null
    private var DriverLocationRefListner: ValueEventListener? = null
    private var txtName: TextView? = null
    private var txtPhone: TextView? = null
    private var txtCarName: TextView? = null
    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isUserOld()) {
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
        val v: View = inflater.inflate(R.layout.fragment_home_map, container, false)

//        initialising variable on the creation of the frag
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        customerID = FirebaseAuth.getInstance().currentUser?.uid
        CustomerDatabaseRef =
            FirebaseDatabase.getInstance().reference.child("Customer Requests")
        delivererAvailableRef =
            FirebaseDatabase.getInstance().reference.child("Deliverers Available")
        DriverLocationRef = FirebaseDatabase.getInstance().reference.child("Deliverer Working")

//      initializing a share pref to use when checking if the user id old or not
        moyosharedprefs = Moyosharedprefs(requireActivity().applicationContext)


        logout_customer_btn = v.findViewById(R.id.logout_customer_btn)
        details = v.findViewById(R.id.details)
        request_button = v.findViewById(R.id.request_button)
        logout(logout_customer_btn)

        return v
    }

    private val closestDeliiverer: Unit
        private get() {
            val geoFire: GeoFire = GeoFire(DelivererAvailableRef)
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
                    if (!delivererFound!! && requestType) {
                        delivererFound = true
                        delivererFoundID = key


                        //we tell driver which customer he is going to have
                        DriversRef = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child("Drivers").child(
                                delivererFoundID!!
                            )
                        val driversMap = HashMap<String?, String?>()
                        driversMap["CustomerRideID"] = customerID
                        DriversRef!!.updateChildren(driversMap as Map<String, Any>)

                        //Show driver location on customerMapActivity
                        GettingDriverLocation()
                        CallDelivererButton!!.setText("Looking for Mechanic Location...")
                    }
                }

                public override fun onKeyExited(key: String) {}
                public override fun onKeyMoved(key: String, location: GeoLocation) {}
                public override fun onGeoQueryReady() {
                    if (!delivererFound!!) {
                        radius += 1
                        closetDriverCab
                    }
                }

                override fun onGeoQueryError(error: DatabaseError) {}
            })
        }


    private fun isUserOld(): Boolean {
        val sharedPreferences =
            requireActivity().getSharedPreferences("", Context.MODE_PRIVATE)

        return sharedPreferences.getBoolean("Old", false)
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