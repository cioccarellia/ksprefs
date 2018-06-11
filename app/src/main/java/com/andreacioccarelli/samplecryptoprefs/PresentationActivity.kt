package com.andreacioccarelli.samplecryptoprefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.andreacioccarelli.cryptoprefs.CryptoPrefs
import kotlinx.android.synthetic.main.activity_presentation.*
import kotlinx.android.synthetic.main.content_presentation.*
import org.json.JSONObject


/**
 * Presentation containing the most useful approaches to the library
 * */
class PresentationActivity : AppCompatActivity() {

    private lateinit var prefs: CryptoPrefs
    private val fileName = "Crypto"
    private val key = "U29maWE="
    private val startCountKey = "start_count"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)
        setSupportActionBar(toolbar)

        prefs = CryptoPrefs(applicationContext, fileName, key)
        prefs.put(startCountKey, prefs.getInt(startCountKey, 0) + 1)

        button.setOnClickListener {
            // Put some sample values to the preferences
            prefs.put("crypto_sample_string", "a")
            prefs.put("crypto_sample_int", 1)
            prefs.put("crypto_sample_boolean", true)

            // You don't have to cast them before passing as arguments
            updateView()
        }

        button2.setOnClickListener {
            // Standard reading operations
            val jsonErrorLog = "{\n" +
                    "    \"key\": \"Error\",\n" +
                    "    \"details\": \"PizzaWithPineappleException\",\n" +
                    "    \"mistakenIngredients\": {\n" +
                    "        \"name\": \"Pineapple\",\n" +
                    "        \"description\": \"Tropical fruit\"\n" +
                    "    }\n" +
                    "}"

            val key = "json_response"
            prefs.put(key, jsonErrorLog)
            val jsonFromPrefs = JSONObject(prefs.getString(key, ""))

            toast(jsonFromPrefs.getString("details"))

            updateView()
        }


        button3.setOnClickListener {
            // Function that will return back the number of times the app has started
            toast(prefs.getInt(startCountKey, 0))
        }

        button4.setOnClickListener {
            // Example for enqueuing
            for (i in 1..10) {
                prefs.queue("index[$i]", i)
            }

            // Calling apply() to commit changes
            prefs.apply()


            for (pref in prefs.allPrefsMap) {
                Log.d(this.javaClass.name, "${pref.key}: ${pref.value}")
            }


            updateView()
        }

        button5.setOnClickListener {
            // Reading raw file to check the values are encrypted
            var x = ""
            for (pref in getSharedPreferences(fileName, Context.MODE_PRIVATE).all) {
                x += "${pref.key} ${pref.value}\n"
            }

            toast(x)
        }

        button6.setOnClickListener {
            // Clear all preferences
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

    private fun Context.toast(message: Any) {
        Toast.makeText(applicationContext, message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_presentation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_github -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/AndreaCioccarelli/CryptoPrefs")))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
