package com.example.hometask

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import androidx.core.content.ContextCompat
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        val btnAddChore = findViewById<Button>(R.id.btnAddChore)
        val btnMyChores = findViewById<Button>(R.id.btnMyChores)
        val btnEditChores = findViewById<Button>(R.id.btnEditChores)
        val btnProfile = findViewById<Button>(R.id.btnProfile)



        btnAddChore.setOnClickListener {

            val intent = Intent(
                this,
                AddChoreActivity::class.java
            )

            startActivity(intent)
        }
        btnMyChores.setOnClickListener{
            val intent = Intent(
                this,
                MyChoresActivity::class.java
            )

            startActivity(intent)
        }
        btnEditChores.setOnClickListener{
            val intent = Intent(
                this,
                EditChoreActivity::class.java
            )

            startActivity(intent)
        }
        btnProfile.setOnClickListener{
            val intent = Intent(
                this,
                ProfileActivity::class.java
            )

            startActivity(intent)
        }
    }
}