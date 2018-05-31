package com.andreacioccarelli.samplecryptoprefs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.andreacioccarelli.cryptoprefs.CryptoPrefs

import kotlinx.android.synthetic.main.activity_presentation.*
import kotlinx.android.synthetic.main.content_presentation.*


/**
 * Presentation containing the most useful approaches to the library
 * */
class PresentationActivity : AppCompatActivity() {

    private lateinit var prefs: CryptoPrefs
    private val fileName = "Crypto"
    private val startCountKey = "start_count"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        setSupportActionBar(toolbar)

        prefs = CryptoPrefs(applicationContext, fileName, "U29maWE=")
        prefs.put(startCountKey, prefs.getInt(startCountKey, 0) + 1)

        button.setOnClickListener {
            prefs.put("crypto_sample_string", "a")
            prefs.put("crypto_sample_int", 1)
            prefs.put("crypto_sample_boolean", true)

            updateView()
        }

        button2.setOnClickListener {
            val x = "${prefs.getString("crypto_sample_string", "a")}\n" +
                    "${prefs.getString("crypto_sample_int", 1)}\n" +
                    prefs.getString("crypto_sample_boolean", true)
            toast(x)
        }


        button3.setOnClickListener {
            toast(prefs.getInt(startCountKey, 0))
        }

        button4.setOnClickListener {
            for (i in 1..10) {
                prefs.queue("index[$i]", i)
            }

            prefs.apply()
            updateView()
        }

        button5.setOnClickListener {
            var x = ""
            for (pref in getSharedPreferences(fileName, Context.MODE_PRIVATE).all) {
                x += "${pref.key} ${pref.value}\n"
            }

            toast(x)
        }

        button6.setOnClickListener {
            prefs.erase()
            updateView()
        }

        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        content.text = "*CryptoPrefs view*\n\n"

        for (pref in prefs.allPrefsMap) {
            content.text = "${content.text}key ${pref.key}, value=${pref.value};\n"
        }


        content.text = "${content.text}\n\n\n*Shared Prefs view*\n\n"
        for (pref in getSharedPreferences(fileName, Context.MODE_PRIVATE).all) {
            content.text = "${content.text}key \"${pref.key}\", value=\"${pref.value}\";\n"
        }

        content.text = "${content.text}\n\n\n"
    }

    private fun toast(message: Any) {
        Toast.makeText(applicationContext, message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_presentation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
