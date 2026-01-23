package com.practicum.project.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val shareBtn = findViewById<TextView>(R.id.shareBtn)
        val supportBtn = findViewById<TextView>(R.id.supportBtn)
        val userAgreementBtn = findViewById<TextView>(R.id.userAgreemBtn)

        backBtn.setOnClickListener { finish() }

        shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val url = getString(R.string.shareMsg)
            shareIntent.putExtra(Intent.EXTRA_TEXT,url)
            val chooserMsg = getString(R.string.chooserMsg)
            startActivity(Intent.createChooser(shareIntent,  chooserMsg))
        }

        supportBtn.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                val mail = getString(R.string.supportMailTo)
                val subject = getString(R.string.supportMailSubject)
                val message =  getString(R.string.supportMailBody)

                val msg = "mailto:$mail" +
                        "?subject=${Uri.encode(subject)}" +
                        "?body=${Uri.encode(message)}"
                data = Uri.parse(msg)
                putExtra(Intent.EXTRA_SUBJECT,subject)
                putExtra(Intent.EXTRA_TEXT, message)

            }
            startActivity(Intent.createChooser(supportIntent,getString(R.string.support)))
        }

        userAgreementBtn.setOnClickListener {
            val url = Uri.parse(getString(R.string.userAgreemLink))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW,url)
                startActivity(userAgreementIntent)
        }
    }
}