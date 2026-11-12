package com.example.certbridge.probe

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.security.KeyChain
import android.security.KeyChainAliasCallback
import android.util.Log
import android.view.WindowInsets
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

        val pad = (24 * resources.displayMetrics.density).toInt()
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)

            // Android 15 enforces edge-to-edge for targetSdk >= 35.
            // Keep the button outside the status bar and camera cutout.
            setOnApplyWindowInsetsListener { view, insets ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val safeArea = insets.getInsets(
                        WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout()
                    )
                    view.setPadding(
                        pad + safeArea.left,
                        pad + safeArea.top,
                        pad + safeArea.right,
                        pad + safeArea.bottom
                    )
                } else {
                    @Suppress("DEPRECATION")
                    view.setPadding(
                        pad + insets.systemWindowInsetLeft,
                        pad + insets.systemWindowInsetTop,
                        pad + insets.systemWindowInsetRight,
                        pad + insets.systemWindowInsetBottom
                    )
                }
                insets
            }
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
        Log.i("CertBridgeProbe", "Request button tapped; invoking KeyChain picker")
        status.text = "Requesting alias; this makes no network connection."
        try {
            KeyChain.choosePrivateKeyAlias(
                this,
                KeyChainAliasCallback { selectedAlias ->
                    Log.i("CertBridgeProbe", "KeyChain callback returned; alias value not logged")
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
        } catch (error: RuntimeException) {
            Log.e("CertBridgeProbe", "KeyChain invocation failed: ${error.javaClass.simpleName}")
            status.text = "KeyChain request failed: ${error.javaClass.simpleName}"
        }
    }
}
