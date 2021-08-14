package com.devwan.where

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_where.*


class WhereActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_where)

        val image_intent : String? = intent.getStringExtra("image")
        val like_intent : Long = intent.getLongExtra("like", 0)
        val title_intent : String? = intent.getStringExtra("title")
        val gp : GeoPoint = GeoPoint(
            intent.getDoubleExtra("address_latitude", 0.0),
            intent.getDoubleExtra("address_longitude", 0.0)
        )
        val detail_intent : String? = intent.getStringExtra("detail")

//        btn_where.setOnClickListener(View.OnClickListener {
//            val intent : Intent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("geo:0,0?q="+gp.latitude+","+gp.longitude+"(kit)"))
//            startActivity(intent)
//        })

        Glide.with(this@WhereActivity).load(image_intent!!).into(image)
        like.text = like_intent.toString()
        title_.text = title_intent!!
        detail.text = detail_intent!!
    }
}