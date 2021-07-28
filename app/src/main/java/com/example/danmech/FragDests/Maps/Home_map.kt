@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.example.danmech.FragDests.Maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.danmech.R
import com.example.danmech.Sharedprefs.Moyosharedprefs
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class Home_map : Fragment(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    lateinit var logout_customer_btn: Button
    private lateinit var details: Button
    lateinit var request_button: Button
    private lateinit var moyosharedprefs: Moyosharedprefs


    private var mMap: GoogleMap? = null
    private var googleApiClient: GoogleApiClient? = null
    private var LastLocation: Location? = null
    private var locationRequest: LocationRequest? = null
    private var Logout: Button? = null
    private var SettingsButton: Button? = null
    private var CallDelivererButton: Button? = null
    private var callingbtn: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var CustomerDatabaseRef: DatabaseReference? = null
    private var CustomerPickUpLocation: LatLng? = null
    private var delivererAvailableRef: DatabaseReference? = null
    private var DriverLocationRef: DatabaseReference? = null
    private var DeliverersRef: DatabaseReference? = null
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

        mAuth = FirebaseAuth.getInstance()
        mAuth?.currentUser
        if (!isUserOld()) {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_home_map_to_walkThrough)
//        }else if(currentUser == null){
//            NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.action_home_map_to_authFragment)
//        }
//        Toast.makeText(requireActivity(),"Kindly Sign In",Toast.LENGTH_LONG).show()

        } else {
            onStart()
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home_map, container, false)


//        initialising variable on the creation of the frag
//        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth!!.currentUser
        customerID = FirebaseAuth.getInstance().currentUser?.uid
        CustomerDatabaseRef =
            FirebaseDatabase.getInstance().reference.child("Customer Requests")
        delivererAvailableRef =
            FirebaseDatabase.getInstance().reference.child("Deliverers Available")
        DriverLocationRef = FirebaseDatabase.getInstance().reference.child("Deliverers Working")

//      initializing a share pref to use when checking if the user id old or not
        moyosharedprefs = Moyosharedprefs(requireActivity().applicationContext)
        logout_customer_btn = v.findViewById(R.id.logout_customer_btn)
        details = v.findViewById(R.id.details)
        request_button = v.findViewById(R.id.request_button)
        txtName = v.findViewById(R.id.name_driver)
        txtPhone = v.findViewById(R.id.phone_driver)
        txtCarName = v.findViewById(R.id.deliverername)
        profilePic = v.findViewById(R.id.profile_image_driver)
        relativeLayout = v.findViewById(R.id.rel1)
        callingbtn = v.findViewById(R.id.callingbtn)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        request_button.setOnClickListener {


//            disable the button aftter clicking it
            it.isEnabled = false
//            toasting the current user email to show that really a user is active
            Toast.makeText(requireActivity(), "${mAuth?.currentUser?.email}", Toast.LENGTH_LONG)
                .show()


            if (requestType) {
                requestType = false
                geoQuery!!.removeAllListeners()
                DriverLocationRef!!.removeEventListener((DriverLocationRefListner)!!)
                if (delivererFound != null) {
                    DeliverersRef = FirebaseDatabase.getInstance().reference
                        .child("Users").child("Deliverers").child((delivererFoundID)!!)
                        .child("CustomerRideID")
                    DeliverersRef!!.removeValue()
                    delivererFoundID = null
                }
                delivererFound = false
                radius = 1
                val customerId: String? = FirebaseAuth.getInstance().currentUser?.uid
                val geoFire = GeoFire(CustomerDatabaseRef)
                geoFire.removeLocation(customerId)
                if (PickUpMarker != null) {
                    PickUpMarker!!.remove()
                }
                if (DriverMarker != null) {
                    DriverMarker!!.remove()
                }
                request_button.text = getString(R.string.makeorder)
                relativeLayout?.visibility = View.GONE
            } else {
                requestType = true
                val customerId: String? = FirebaseAuth.getInstance().currentUser?.uid
                val geoFire = GeoFire(CustomerDatabaseRef)
                geoFire.setLocation(
                    customerId,
                    GeoLocation(LastLocation!!.latitude, LastLocation!!.longitude)
                )
                CustomerPickUpLocation =
                    LatLng(LastLocation!!.latitude, LastLocation!!.longitude)
                PickUpMarker = mMap!!.addMarker(
                    MarkerOptions().position(CustomerPickUpLocation!!).title("Your Location")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.psn))
                )
                request_button.text = getString(R.string.waitingmessage)
                closestDeliverer
            }

        }


        logout_customer_btn.setOnClickListener {
            logout()

        }
        callingbtn!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:0701315149"))
            startActivity(intent)
        }
        details.setOnClickListener {
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(requireActivity())

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
            val btnClose = view.findViewById<ImageView>(R.id.close_button)

            // on below line we are adding on click listener
            // for our dismissing the dialog button.
            btnClose.setOnClickListener {
                // on below line we are calling a dismiss
                // method to close our dialog.
                dialog.dismiss()
            }
            // below line is use to set cancelable to avoid
            // closing of dialog box when clicking on the screen.
            dialog.setCancelable(true)

            // on below line we are setting
            // content view to our view.
            dialog.setContentView(view)

            // on below line we are calling
            // a show method to display a dialog.
            dialog.show()


        }

        return v
    }

    override fun onStart() {
        super.onStart()
//         Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth?.currentUser

//        Toast.makeText(requireActivity(),"${currentUser?.email}",Toast.LENGTH_LONG).show()
        if (currentUser == null) {

            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.action_home_map_to_authFragment)
        }
    }

    private val closestDeliverer: Unit
        get() {
            val geoFire = GeoFire(delivererAvailableRef)
            geoQuery = geoFire.queryAtLocation(
                GeoLocation(
                    CustomerPickUpLocation!!.latitude,
                    CustomerPickUpLocation!!.longitude
                ), radius.toDouble()
            )
            geoQuery?.removeAllListeners()
            geoQuery?.addGeoQueryEventListener(object : GeoQueryEventListener {
                @SuppressLint("SetTextI18n")
                override fun onKeyEntered(key: String, location: GeoLocation) {
                    //anytime the driver is called this method will be called
                    //key=driverID and the location
                    if (!delivererFound!! && requestType) {
                        delivererFound = true
                        delivererFoundID = key


                        //we tell driver which customer he is going to have
                        DeliverersRef = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child("deliverers").child(
                                delivererFoundID!!
                            )
                        val deliverersMap = HashMap<String?, String?>()
                        deliverersMap["CustomerID"] = customerID
                        DeliverersRef!!.updateChildren(deliverersMap as Map<String, Any>)

                        //Show driver location on customerMapActivity
                        GettingDriverLocation()
                        CallDelivererButton!!.text = "Looking for a Water Truck Around.."
                    }
                }

                override fun onKeyExited(key: String) {}
                override fun onKeyMoved(key: String, location: GeoLocation) {}
                override fun onGeoQueryReady() {
                    if (!delivererFound!!) {
                        radius += 1
                        closestDeliverer
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


    //and then we get to the driver location - to tell customer where is the driver
    private fun GettingDriverLocation() {
        DriverLocationRefListner = DriverLocationRef!!.child((delivererFoundID)!!).child("l")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists() && requestType) {
                        val driverLocationMap: List<Any?>? = dataSnapshot.value as List<Any?>?
                        var LocationLat = 0.0
                        var LocationLng = 0.0
                        request_button.text = "Water Truck  Found"
                        relativeLayout!!.visibility = View.VISIBLE
                        assignedDriverInformation
                        if (driverLocationMap!!.get(0) != null) {
                            LocationLat = driverLocationMap[0].toString().toDouble()
                        }
                        if (driverLocationMap.get(1) != null) {
                            LocationLng = driverLocationMap[1].toString().toDouble()
                        }

                        //adding marker - to pointing where driver is - using this lat lng
                        val DriverLatLng = LatLng(LocationLat, LocationLng)
                        if (DriverMarker != null) {
                            DriverMarker!!.remove()
                        }
                        val location1 = Location("")
                        location1.latitude = CustomerPickUpLocation!!.latitude
                        location1.longitude = CustomerPickUpLocation!!.longitude
                        val location2 = Location("")
                        location2.latitude = DriverLatLng.latitude
                        location2.longitude = DriverLatLng.longitude
                        val Distance: Float = location1.distanceTo(location2)
                        if (Distance < 90) {
                            request_button.text = "Water Truck  has Arrived"
                        } else {
                            request_button.text = "Water Truck is  $Distance.toString() away"
                        }
                        DriverMarker = mMap!!.addMarker(
                            MarkerOptions().position(DriverLatLng).title("The Water Truck is here")
                                .icon(BitmapDescriptorFactory.defaultMarker())
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.getMapAsync(callback)
//    }

    private fun logout() {

        mAuth?.signOut()
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_home_map_to_authFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
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
            return showLocationPrompt()
        }
        buildGoogleApiClient()
        mMap!!.isMyLocationEnabled = true
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
            return showLocationPrompt()
        }
        //it will handle the refreshment of the location
        //if we dont call it we will get location only once
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient,
            locationRequest,
            this
        )
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onLocationChanged(location: Location) {
//        onStart()
        //getting the updated location
        LastLocation = location
        val latLng = LatLng(location.latitude, location.longitude)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
    }

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


    private val assignedDriverInformation: Unit
        get() {
            val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
                .child(getString(R.string.Users_node_in_raltimedb))
                .child(getString(R.string.deliverers_under_Users_node_in_raltimedb))
                .child((delivererFoundID)!!)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        val name: String = dataSnapshot.child("name").value.toString()
                        val phone = dataSnapshot.child("phone").value.toString()
                        val car: String = dataSnapshot.child("car").value.toString()
                        txtName!!.text = name
                        txtPhone!!.text = phone
                        txtCarName!!.text = car
                        if (dataSnapshot.hasChild("image")) {
                            val image: String = dataSnapshot.child("image").value.toString()
                            Picasso.get().load(image).into(profilePic)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }


//    getting the user to turn on the

    private fun showLocationPrompt() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            // Cast to a resolvable exception.
                            val resolvable: ResolvableApiException =
                                exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                activity, LocationRequest.PRIORITY_HIGH_ACCURACY
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.

                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationRequest.PRIORITY_HIGH_ACCURACY -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("Status: ", "On")
                } else {
                    Log.e("Status: ", "Off")
                    Toast.makeText(
                        requireActivity(),
                        "${mAuth?.currentUser?.email} the app requires Location to  be enable",
                        Toast.LENGTH_LONG
                    ).show()
//                    request_button.isEnabled=false


                    requireActivity().finish()

                }
            }
        }
    }

}


