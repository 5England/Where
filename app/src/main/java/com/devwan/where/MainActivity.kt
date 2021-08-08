package com.devwan.where

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentFeed = FeedFragment()
    private val fragmentUpload = UploadFragment()
    private val fragmentMy = MyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        bnv_main.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.icon_feed -> changeFragment(fragmentFeed)
                    R.id.icon_upload -> changeFragment(fragmentUpload)
                    R.id.icon_my -> changeFragment(fragmentMy)
                }
                true
            }
            selectedItemId = R.id.icon_feed
        }
    }
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}