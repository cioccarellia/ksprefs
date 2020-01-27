package com.cioccarellia.samplecryptoprefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.cioccarellia.cryptoprefs.CryptoPrefs
import kotlinx.android.synthetic.main.activity_presentation.*
import kotlinx.android.synthetic.main.content_presentation.*
import org.json.JSONObject
import java.util.*

/**
 * Presentation containing the most useful approaches to the library
 * */

open class PresentationActivity : AppCompatActivity() {

    private val namespace = "pref-file"
    private lateinit var prefs: CryptoPrefs

    @SuppressLint("LogConditional")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = CryptoPrefs(namespace, this)

        setContentView(R.layout.activity_presentation)
        setSupportActionBar(toolbar)

        prefs.set("sc", prefs.get("sc", 0) + 1)

        button.setOnClickListener {
            // set some sample values to the preferences
            for (i in 0..10) {
                prefs.set(UUID.randomUUID().toString(), UUID.randomUUID().toString())
            }

            for (i in 0..10) {
                prefs.get(UUID.randomUUID().toString(), UUID.randomUUID().toString())
            }

            prefs.auto()

            updateView()
        }

        button2.setOnClickListener {
            // Standard reading operations
            val jsonErrorLog = "{\n" +
                    "    \"key\": \"Error\",\n" +
                    "    \"details\": \"PizzaWithPineappleException\",\n" +
                    "    \"mistakenIngredients\": {\n" +
                    "        \"name\": \"Pineapple\",\n" +
                    "        \"description\": \"Tropical fruit on pizza throws an exception\"\n" +
                    "    }\n" +
                    "}"

            val key = "json_response"
            prefs.set(key, jsonErrorLog)
            prefs.auto()

            val jsonFromPrefs = JSONObject(prefs.get(key, ""))

            updateView()
        }

        button3.setOnClickListener {
            prefs.set("A", "B")
            updateView()
        }

        button5.setOnClickListener { updateView() }

        button6.setOnClickListener { prefs.erase() }

        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        content.text = buildString {
            append("[CryptoPrefs view]\n\n")

            val all = prefs.all.toList().map { "key=${it.first}, value=${it.second}" }
            append(all)

            append("\n\n\n[Shared Prefs view]\n\n")
            for (pref in getSharedPreferences(namespace, Context.MODE_PRIVATE).all) {
                append("${content.text}key \"${pref.key}\", value=\"${pref.value}\";\n")
            }

            append("\n\n\n")
        }
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

    protected fun randomString(length: Int = 18): String {
        val saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz<>-@/"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < length) {
            val index = (rnd.nextFloat() * saltChars.length).toInt()
            salt.append(saltChars[index])
        }
        return salt.toString()
    }
}
