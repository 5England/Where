package com.devwan.where

import android.location.Geocoder

data class Where(
    val where_title : String,
    val where_detail : String,
    val where_image : String,
    val where_address : String,
    val like : Int
)