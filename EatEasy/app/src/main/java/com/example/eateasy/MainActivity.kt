package com.example.eateasy

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val signButton: ImageButton = findViewById(R.id.signButton)
        signButton.setOnClickListener {
            val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.activity_sign_up_page, null)

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            val signUpPopup = PopupWindow(popupView, width, height, true)


            signUpPopup.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            val editUsername: EditText = popupView.findViewById(R.id.editUsername)
            val editEmail: EditText = popupView.findViewById(R.id.editEmail)
            val editPassword: EditText = popupView.findViewById(R.id.editPassword)
            val editConfirmPassword: EditText = popupView.findViewById(R.id.editConfirmPassword)
            val popupsignButton: ImageButton = popupView.findViewById(R.id.popupsignButton) // Add this in your popup layout

            //Handles sign-up
            popupsignButton.setOnClickListener {
                val username = editUsername.text.toString().trim()
                val email = editEmail.text.toString().trim()
                val password = editPassword.text.toString().trim()
                val confirmPassword = editConfirmPassword.text.toString().trim()

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //When pw don't match
                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //Saves to Shared Preferences
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("username", username)
                editor.putString("email", email)
                editor.putString("password", password)
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                //Closes popup
                signUpPopup.dismiss()

                //Goes to Profile or Main screen
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}