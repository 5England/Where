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
                .title("현재 위치를")
                .snippet("히든스트리트로 설정")
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
            mOption.title("해당 위치를")
            lat = it.latitude
            lng = it.longitude

            mOption.snippet("히든스트리트로 설정")
            mOption.position(LatLng(lat!!, lng!!))

            currentMarker = mMap.addMarker(mOption)
        }

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            if (lat == null || lng == null) {
                showMessage(0, "현재 위치를", "히든스트리트로 설정하시겠습니까?")
            } else {
                showMessage(1, "해당 위치를", "히든스트리트로 설정하시겠습니까?")
            }
        })
    }

    fun showMessage(type: Int, title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("예",
            DialogInterface.OnClickListener { dialog, which ->
                //type이 0이라면, 현재 위치의 위도경도를 전역변수인 lat과 lng에 넣어주고 setGeopoint를 실행시켜야한다.
                //if(type == 0)

                setGeopoint()
            })

        builder.setNegativeButton("아니오",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "위치를 설정해주세요", Toast.LENGTH_LONG).show()
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
//        // 정확함. 이것보다 짧은 업데이트는 하지 않음
//        locationRequest.fastestInterval = 5000
//    }
//
//    private fun showPermissionInfoDialog(){
//        alert("현재 위치 정보를 얻으려면 위치 권한이 필요합니다", "권한"){
//            yesButton {
//                // 권한 요청
//                ActivityCompat.requestPermissions(this@MapsActivity,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
//            }
//            noButton {  }
//        }.show()
//    }
//
//    private fun permissionCheck(cancel:()->Unit, ok:()->Unit){
//        // 위치 권한이 있는지 검사
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            // 권한이 허용되지 않음
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
//                // 이전에 권한을 한 번 거부한 적이 있는 경우에 실행할 함수
//                cancel()
//            } else{
//                ActivityCompat.requestPermissions(this,
//                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
//            }
//        } else {
//            // 권한을 수락했을 때 실행할 함수
//            ok()
//        }
//    }
//
//    // 사용자가 권한을 수락하거나 거부했을 때
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            REQUEST_ACCESS_FINE_LOCATION->{
//                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
//                    // 권한 허용됨
//                    addLocationListener()
//                } else {
//                    // 권한 거부
//                    Toast.makeText(this, "권한 거부 됨", Toast.LENGTH_LONG).show()
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
//                // 14 level로 확대하고 현재 위치로 카메라 이동
//                currentLatLng = LatLng(latitude, longitude)
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
//
//                Log.d("MapsActivity", "위도: $latitude, 경도: $longitude")
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
//        // 현재 위치 요청을 삭제
//        fuseLocationProvicerClient.removeLocationUpdates(locationCallback)
//    }
}