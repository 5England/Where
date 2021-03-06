package com.devwan.where

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.gms.common.api.internal.GoogleServices.initialize
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.places.PlaceReport
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton
import java.util.jar.Manifest


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentMarker : Marker

    var curLat: Double = 37.555204
    var curLng: Double = 126.970711
    var lat: Double? = null
    var lng: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        currentMarker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(curLat, curLng))
                .title("?????? ?????????")
                .snippet("????????????????????? ??????")
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(curLat, curLng)))
        mMap.setMinZoomPreference(12.0f)
        mMap.uiSettings.isZoomControlsEnabled = true

        setMapClickListener(mMap)
    }

    internal inner class InfoWindowActivity : AppCompatActivity(),
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {
        override fun onMapReady(googleMap: GoogleMap) {
            // Add markers to the map and do other map setup.
            // ...
            // Set a listener for info window events.
            googleMap.setOnInfoWindowClickListener(this)
        }

        override fun onInfoWindowClick(marker: Marker) {

        }
    }

    fun setMapClickListener(mMap: GoogleMap) {
        mMap.setOnMapLongClickListener {
            currentMarker.remove()

            var mOption: MarkerOptions = MarkerOptions()
            mOption.title("?????? ?????????")
            lat = it.latitude
            lng = it.longitude

            mOption.snippet("????????????????????? ??????")
            mOption.position(LatLng(lat!!, lng!!))

            currentMarker = mMap.addMarker(mOption)
        }

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            if (lat == null || lng == null) {
                showMessage(0, "?????? ?????????", "????????????????????? ?????????????????????????")
            } else {
                showMessage(1, "?????? ?????????", "????????????????????? ?????????????????????????")
            }
        })
    }

    fun showMessage(type: Int, title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("???",
            DialogInterface.OnClickListener { dialog, which ->
                //type??? 0?????????, ?????? ????????? ??????????????? ??????????????? lat??? lng??? ???????????? setGeopoint??? ?????????????????????.
                //if(type == 0)

                setGeopoint()
            })

        builder.setNegativeButton("?????????",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "????????? ??????????????????", Toast.LENGTH_LONG).show()
            })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun setGeopoint() {
        var resultIntent = Intent()
        resultIntent.putExtra("latitude", lat)
        resultIntent.putExtra("longitude", lng)

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }






//    private var locationRequest = LocationRequest()
//    private lateinit var fuseLocationProvicerClient: FusedLocationProviderClient
//    private var locationCallback = MyLocationCallBack()
//    private val REQUEST_ACCESS_FINE_LOCATION = 1000
//
//    private fun locationInit(){
//        fuseLocationProvicerClient = FusedLocationProviderClient(this)
//
//        locationCallback = MyLocationCallBack()
//
//        locationRequest = LocationRequest()
//        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        locationRequest.interval = 10000
//        // ?????????. ???????????? ?????? ??????????????? ?????? ??????
//        locationRequest.fastestInterval = 5000
//    }
//
//    private fun showPermissionInfoDialog(){
//        alert("?????? ?????? ????????? ???????????? ?????? ????????? ???????????????", "??????"){
//            yesButton {
//                // ?????? ??????
//                ActivityCompat.requestPermissions(this@MapsActivity,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
//            }
//            noButton {  }
//        }.show()
//    }
//
//    private fun permissionCheck(cancel:()->Unit, ok:()->Unit){
//        // ?????? ????????? ????????? ??????
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            // ????????? ???????????? ??????
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
//                // ????????? ????????? ??? ??? ????????? ?????? ?????? ????????? ????????? ??????
//                cancel()
//            } else{
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
//            }
//        } else {
//            // ????????? ???????????? ??? ????????? ??????
//            ok()
//        }
//    }
//
//    // ???????????? ????????? ??????????????? ???????????? ???
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            REQUEST_ACCESS_FINE_LOCATION->{
//                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    // ?????? ?????????
//                    addLocationListener()
//                } else {
//                    // ?????? ??????
//                    Toast.makeText(this, "?????? ?????? ???", Toast.LENGTH_LONG).show()
//                }
//                return
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun addLocationListener(){
//        fuseLocationProvicerClient.requestLocationUpdates(locationRequest, locationCallback, null)
//    }
//
//    inner class MyLocationCallBack: LocationCallback(){
//        override fun onLocationResult(locationRequest: LocationResult?) {
//            super.onLocationResult(locationRequest)
//
//            val location = locationRequest?.lastLocation
//            currentLatLng = LatLng(location!!.latitude, location!!.longitude)
//
//            location?.run{
//                // 14 level??? ???????????? ?????? ????????? ????????? ??????
//                currentLatLng = LatLng(latitude, longitude)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
//
//                Log.d("MapsActivity", "??????: $latitude, ??????: $longitude")
//            }
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        removeLocationListener()
//    }
//
//    private fun removeLocationListener(){
//        // ?????? ?????? ????????? ??????
//        fuseLocationProvicerClient.removeLocationUpdates(locationCallback)
//    }
}