package com.example.certbridge.probe

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.security.KeyChain
import android.security.KeyChainAliasCallback
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Independent, emulator-only caller for the Android KeyChain alias-selection flow.
 *
 * Does not create keys, access private keys, contact a server, or log aliases.
 */
class ProbeActivity : Activity() {
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val pad = (24 * resources.displayMetrics.density).toInt()
            setPadding(pad, pad, pad, pad)
        }

        status = TextView(this).apply {
            text = "Ready to test KeyChain certificate selection."
        }
        val requestButton = Button(this).apply {
            text = "Request certificate alias"
            setOnClickListener { requestAlias() }
        }

        content.addView(requestButton)
        content.addView(status)
        setContentView(content)
    }

    private fun requestAlias() {
        status.text = "Requesting alias; this makes no network connection."
        KeyChain.choosePrivateKeyAlias(
            this,
            KeyChainAliasCallback { selectedAlias ->
                runOnUiThread {
                    status.text = if (selectedAlias == null) {
                        "No alias selected (picker cancelled or no certificate)."
                    } else {
                        "An alias was selected (value hidden)."
                    }
                }
            },
            null,
            null,
            Uri.parse("https://certbridge-probe.invalid"),
            null
        )
    }
}
