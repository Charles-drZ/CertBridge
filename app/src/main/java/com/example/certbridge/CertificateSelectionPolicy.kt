package com.example.certbridge

/**
 * Deliberate safety gate for the diagnostic MVP.
 *
 * Returning null makes Android show its normal certificate selector. No caller
 * identity or approved KeyChain alias has been verified for automatic selection.
 */
internal object CertificateSelectionPolicy {

    @Suppress("UNUSED_PARAMETER")
    fun chooseAlias(callerUid: Int, preselectedAlias: String?): String? = null
}
