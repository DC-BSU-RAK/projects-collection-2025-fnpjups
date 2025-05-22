package com.example.eateasy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textUsername: TextView = findViewById(R.id.textUsername)
        val textEmail: TextView = findViewById(R.id.textEmail)


        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = prefs.getString("username", "Unknown")
        val email = prefs.getString("email", "Unknown")


        textUsername.text = username
        textEmail.text = email

        val backButton : ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }
    }
}