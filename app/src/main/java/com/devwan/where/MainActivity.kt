package com.devwan.where

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FeedFragment.OnSignOutListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FirebaseAuth.getInstance().currentUser == null) {
            signIn()
        } else {
            initNavigationBar()
        }
    }


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    private fun signIn(){
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        // Create and launch sign-in intent
        val signInIntent =
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                .setLogo(R.drawable.icon_app)
                .setTheme(R.style.Theme_Where)
                .build()
        signInLauncher.launch(signInIntent)
    }
    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            initNavigationBar()
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            finish()
            // ...
        }
    }
    override fun signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            Toast.makeText(this@MainActivity, "Log-Out", Toast.LENGTH_SHORT)
        }
        signIn()
    }
    // [END auth_fui_result]


    private fun initNavigationBar() {
        bnv_main.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.icon_feed -> changeFragment(FeedFragment())
                    R.id.icon_upload -> changeFragment(UploadFragment())
                    R.id.icon_my -> changeFragment(MyFragment())
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