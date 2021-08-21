package com.devwan.where

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

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

        val sydney = LatLng(36.14616644449355, 128.39375936393668)
        mMap.addMarker(MarkerOptions().position(sydney).title("현재 위치"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setMinZoomPreference(12.0f)
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapLongClickListener {
            var mOption: MarkerOptions = MarkerOptions()
            mOption.title("히든 스트리트")
            lat = it.latitude
            lng = it.longitude

            mOption.snippet(lat.toString() + " | " + lng.toString())
            mOption.position(LatLng(lat!!, lng!!))
            googleMap.addMarker(mOption)
        }

        mMap.setOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener {
            if (lat == null || lng == null) {
                showMessage("현재 위치입니다.", "현 위치의 위도경도가 히든스트리트가 맞습니까?")
            } else {
                showMessage("해당 위치를", "히든스트리트로 설정하시겠습니까?")
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

    fun showMessage(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("예",
            DialogInterface.OnClickListener { dialog, which ->
                setGeopoint()
            })

        builder.setNegativeButton("아니오",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(this, "위치를 설정해주세요", Toast.LENGTH_LONG)
            })

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun setGeopoint() {

    }
}