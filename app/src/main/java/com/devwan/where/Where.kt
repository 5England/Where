package com.devwan.where

import android.location.Geocoder
import com.google.firebase.firestore.GeoPoint

data class Where(
    val title : String,
    val detail : String,
    val image : String,
    val address : GeoPoint,
    val like : Long
)