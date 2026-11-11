package com.example.certbridge

import org.junit.Assert.assertNull
import org.junit.Test

class CertificateSelectionPolicyTest {

    @Test
    fun missingAliasFallsBackToSystemPicker() {
        assertNull(CertificateSelectionPolicy.chooseAlias(callerUid = 10001, preselectedAlias = null))
    }

    @Test
    fun suggestedAliasIsNotAutomaticallyGranted() {
        assertNull(CertificateSelectionPolicy.chooseAlias(callerUid = 10001, preselectedAlias = "example-alias"))
    }

    @Test
    fun unknownCallerNeverReceivesAnAlias() {
        assertNull(CertificateSelectionPolicy.chooseAlias(callerUid = -1, preselectedAlias = "example-alias"))
    }
}
