package com.example.certbridge

import android.app.admin.DelegatedAdminReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

/**
 * Receives Android Enterprise certificate-selection callbacks when a device or
 * profile owner explicitly delegates certificate selection to this application.
 *
 * Diagnostic-only implementation: never selects or grants a certificate.
 */
class CertSelectionReceiver : DelegatedAdminReceiver() {

    override fun onChoosePrivateKeyAlias(
        context: Context,
        intent: Intent,
        uid: Int,
        uri: Uri?,
        alias: String?
    ): String? {
        // Do not log the caller identity, URI, requested alias, or certificates.
        Log.i(TAG, "Certificate selection callback received; mode=observe-only; fallback=system-picker")
        return CertificateSelectionPolicy.chooseAlias(uid, alias)
    }

    private companion object {
        const val TAG = "CertBridge"
    }
}
