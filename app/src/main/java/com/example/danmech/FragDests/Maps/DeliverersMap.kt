@file:Suppress("DEPRECATION")

package com.example.danmech.FragDests.Maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Usertypeselection.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeliverersMap : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private var mMap: GoogleMap? = null
    var googleApiClient: GoogleApiClient? = null
    var LastLocation: Location? = null
    var locationRequest: LocationRequest? = null
    private var LogoutDriverBtn: Button? = null
    private var SettingsDriverButton: Button? = null
    private lateinit var callingbtn: Button
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var currentLogOutUserStatus: Boolean = false

    //getting request customer's id
    private var customerID: String = ""
    private var delivererID: String? = null
    private var phone: String? = null
    private var AssignedCustomerRef: DatabaseReference? = null
    private var AssignedCustomerPickUpRef: DatabaseReference? = null
    var PickUpMarker: Marker? = null
    private var AssignedCustomerPickUpRefListner: ValueEventListener? = null
    private var txtName: TextView? = null
    private var txtPhone: TextView? = null
    private var profilePic: CircleImageView? = null
    private var relativeLayout: RelativeLayout? = null


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = inflater.inflate(R.layout.fragment_deliverers_map, container, false)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        delivererID = mAuth!!.currentUser?.uid
        LogoutDriverBtn = v.findViewById<View>(R.id.logout_driv_btn) as Button?
        SettingsDriverButton = v.findViewById<View>(R.id.settings_driver_btn) as Button?

        callingbtn = v.findViewById(R.id.callingbtn)
        txtName = v.findViewById(R.id.name_customer)
        txtPhone = v.findViewById(R.id.phone_customer)
        profilePic = v.findViewById(R.id.profile_image_customer)
        relativeLayout = v.findViewById(R.id.rel2)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        SettingsDriverButton!!.setOnClickListener {

        }
        LogoutDriverBtn!!.setOnClickListener {
            currentLogOutUserStatus = true
            DisconnectDriver()
            mAuth!!.signOut()
            LogOutUser()
        }

        callingbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
            startActivity(intent)

        }

//        assignedCustomersRequest
        return v
    }

    //getting assigned customer location
    private val assignedCustomersRequest: Unit
        get() {
            AssignedCustomerRef = FirebaseDatabase.getInstance().reference.child("Users")
                .child("Deliverers").child((delivererID)!!).child("CustomerID")
            AssignedCustomerRef!!.addValueEventListener(object : ValueEventListener {


                public override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        customerID = dataSnapshot.value.toString()
                        //getting assigned customer location
                        GetAssignedCustomerPickupLocation()
                        relativeLayout!!.visibility = View.VISIBLE
                        assignedCustomerInformation
                    } else {
                        customerID = ""
                        if (PickUpMarker != null) {
                            PickUpMarker!!.remove()
                        }
                        if (AssignedCustomerPickUpRefListner != null) {
                            AssignedCustomerPickUpRef!!.removeEventListener(
                                AssignedCustomerPickUpRefListner!!
                            )
                        }
                        relativeLayout!!.visibility = View.GONE
                    }
                }

                public override fun onCancelled(databaseError: DatabaseError) {}
            })
        }


    private fun GetAssignedCustomerPickupLocation() {
        AssignedCustomerPickUpRef =
            FirebaseDatabase.getInstance().reference.child("Customer Requests")
                .child(customerID).child("l")
        AssignedCustomerPickUpRefListner =
            AssignedCustomerPickUpRef!!.addValueEventListener(object : ValueEventListener {
                public override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val customerLocationMap: List<Any?>? =
                            dataSnapshot.value as List<Any?>?
                        var LocationLat: Double = 0.0
                        var LocationLng: Double = 0.0
                        if (customerLocationMap!![0] != null) {
                            LocationLat = customerLocationMap[0].toString().toDouble()
                        }
                        if (customerLocationMap[1] != null) {
                            LocationLng = customerLocationMap[1].toString().toDouble()
                        }
                        val DriverLatLng: LatLng = LatLng(LocationLat, LocationLng)
                        PickUpMarker = mMap!!.addMarker(
                            MarkerOptions().position(DriverLatLng)
                                .title("Customers Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.posn))
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // now let set user location enable
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        buildGoogleApiClient()
        mMap!!.isMyLocationEnabled = true
    }


    override fun onLocationChanged(location: Location) {
        //getting the updated location
        LastLocation = location
        val latLng: LatLng = LatLng(location.latitude, location.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(12f))

        val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

        val WaterTruckAvailabilityRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Deliverers Available")
        val geoFireAvailability: GeoFire = GeoFire(WaterTruckAvailabilityRef)

        val DeliverersWorkingRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Deliverers Working")
        val geoFireWorking: GeoFire = GeoFire(DeliverersWorkingRef)

        when (customerID) {
            "" -> {
                geoFireWorking.removeLocation(currentUser?.uid)
                geoFireAvailability.setLocation(
                    currentUser?.uid,
                    GeoLocation(location.latitude, location.longitude)
                )
            }
            else -> {
                geoFireAvailability.removeLocation(userID)
                geoFireWorking.setLocation(
                    userID,
                    GeoLocation(location.latitude, location.longitude)
                )
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        locationRequest = LocationRequest()
        locationRequest!!.interval = 1000
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //
            return
        }
        //it will handle the refreshment of the location
        //if we dont call it we will get location only once
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    public override fun onConnectionSuspended(i: Int) {}
    public override fun onConnectionFailed(connectionResult: ConnectionResult) {}

    //create this method -- for useing apis
    @Synchronized
    protected fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(requireActivity())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        if (!currentLogOutUserStatus) {
            DisconnectDriver()
        }
    }

    private fun DisconnectDriver() {
        val userID: String? = FirebaseAuth.getInstance().currentUser?.uid
        val WaterTruckAvailabilityRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("Deliverers Available")
        val geoFire: GeoFire = GeoFire(WaterTruckAvailabilityRef)
        geoFire.removeLocation(userID)
    }

    fun LogOutUser() {
        activity?.finish()
    }

    private val assignedCustomerInformation: Unit
        get() {
            val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
                .child("Users").child("Customers").child(customerID)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        val name: String = dataSnapshot.child("name").value.toString()
                        phone = dataSnapshot.child("phone").value.toString()
                        txtName!!.text = name
                        txtPhone!!.text = phone
                        if (dataSnapshot.hasChild("image")) {
                            val image: String = dataSnapshot.child("image").value.toString()
                            Picasso.get().load(image).into(profilePic)
                        }
                    }
                }

                public override fun onCancelled(databaseError: DatabaseError) {}
            })
        }


    override fun onStart() {
        super.onStart()
//         Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth?.currentUser
//        Toast.makeText(requireActivity(),"${currentUser?.email}",Toast.LENGTH_LONG).show()
        if (currentUser == null) {

            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.authFragment)
        }
    }

}