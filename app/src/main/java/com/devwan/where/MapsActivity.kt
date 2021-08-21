package com.devwan.where

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.GeoPoint


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var curLat : Double? = null
    var curLng : Double? = null

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

        //set current location
        val currentGeopoint = LatLng(36.14616644449355, 128.39375936393668)

        var currentMarker = mMap.addMarker(MarkerOptions()
            .position(currentGeopoint)
            .title("현재 위치를")
            .snippet("히든스트리트로 설정")
        )
        currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(20.0f))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentGeopoint))
        mMap.setMinZoomPreference(12.0f)
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapLongClickListener {
            var mOption: MarkerOptions = MarkerOptions()
            mOption.title("해당 위치를")
            lat = it.latitude
            lng = it.longitude

            mOption.icon(BitmapDescriptorFactory.defaultMarker(20.0f))
            mOption.snippet("히든스트리트로 설정")
            mOption.position(LatLng(lat!!, lng!!))
            mMap.addMarker(mOption)
        }

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            if (lat == null || lng == null) {
                showMessage(0, "현재 위치를", "히든스트리트로 설정하시겠습니까?")
            } else {
                showMessage(1,"해당 위치를", "히든스트리트로 설정하시겠습니까?")
            }
        })
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

    fun showMessage(type : Int, title: String, message: String) {
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
}