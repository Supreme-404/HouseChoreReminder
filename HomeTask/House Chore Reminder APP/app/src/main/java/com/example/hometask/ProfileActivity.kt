package com.example.hometask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnNotifications =
            findViewById<Button>(R.id.btnNotifications)

        btnNotifications.setOnClickListener {

            val intent = Intent(
                this,
                NotificationsActivity::class.java
            )

            startActivity(intent)
        }
    }
}