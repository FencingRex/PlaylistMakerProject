package com.practicum.project.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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


        val mediaBtn = findViewById<Button>(R.id.media_btn)
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)

        settingsBtn.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
        mediaBtn.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        searchBtn.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        /*
                val mediaClickListener: View.OnClickListener = object : View.OnClickListener {
                    override fun onClick(v: View?){
                        Toast.makeText(this@MainActivity,"Button Media clicked", Toast.LENGTH_LONG).show()
                    }
                }

                mediaBtn.setOnClickListener(mediaClickListener)

                searchBtn.setOnClickListener {
                    Toast.makeText(this@MainActivity, "Button search tapped",Toast.LENGTH_LONG).show()
                }
        */
    }
}