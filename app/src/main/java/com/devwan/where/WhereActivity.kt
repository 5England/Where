package com.devwan.where

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_where.*

class WhereActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_where)

        val image_intent = intent.getStringExtra("image")
        val like_intent = intent.getLongExtra("like", 0)
        val title_intent = intent.getStringExtra("title")
        val gp : GeoPoint = GeoPoint(intent.getDoubleExtra("address_latitude", 0.0), intent.getDoubleExtra("address_longitude", 0.0))
        val detail_intent = intent.getStringExtra("detail")

        Glide.with(this@WhereActivity).load(image_intent!!).into(image)
        like.text = like_intent.toString()
        title_.text = title_intent!!
        detail.text = detail_intent!!
    }
}