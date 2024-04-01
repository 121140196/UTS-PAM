package com.example.utspam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var iconHome: ImageView
    private lateinit var iconProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_main)

            iconHome = findViewById(R.id.iconHome)
            iconProfile = findViewById(R.id.iconProfile)

            iconHome.setOnClickListener {
                val uri = Uri.parse("https://example.com/fragmentHome")
                findNavController(R.id.nav_host_fragment).navigate(uri)
            }

            iconProfile.setOnClickListener {
                val uri = Uri.parse("https://example.com/fragmentProfile")
                findNavController(R.id.nav_host_fragment).navigate(uri)
            }
        }
    }
}
