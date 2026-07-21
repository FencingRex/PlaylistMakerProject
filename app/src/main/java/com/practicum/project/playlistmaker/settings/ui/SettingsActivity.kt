package com.practicum.project.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.project.playlistmaker.R
import com.practicum.project.playlistmaker.creator.Creator
import com.practicum.project.playlistmaker.settings.domain.SettingsInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<Toolbar>(R.id.toolbar)
        val shareBtn = findViewById<TextView>(R.id.shareBtn)
        val supportBtn = findViewById<TextView>(R.id.supportBtn)
        val userAgreementBtn = findViewById<TextView>(R.id.userAgreemBtn)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        backBtn.setOnClickListener { finish() }

        settingsInteractor = Creator.provideSettingsInteractor()
        themeSwitcher.isChecked = settingsInteractor.getTheme()
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsInteractor.switchTheme(checked) }

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
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
                startActivity(userAgreementIntent)
        }
    }
}