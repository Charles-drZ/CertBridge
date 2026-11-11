# CertBridge

A minimal Kotlin/Android Enterprise prototype for delegated KeyChain
certificate-selection callbacks.

## Current status

**Diagnostic-only.** The app registers a protected
`DelegatedAdminReceiver.onChoosePrivateKeyAlias()` callback but deliberately
returns `null` for every request. Android therefore falls back to its ordinary
certificate picker. This version does **not** automate certificate selection,
install certificates, configure VPNs, or make network requests.

The Android device/profile owner must explicitly grant
`DevicePolicyManager.DELEGATION_CERT_SELECTION` for the callback to be invoked.
Installing the APK alone does not grant this capability.

## Build and tests

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest
```

No physical-device, MDM-policy, or VPN integration test has been completed.

## Before enabling automatic selection

- Verify the real calling package, UID, and signing identity.
- Verify the actual Android KeyChain alias and its lifecycle.
- Confirm the calling VPN client invokes `KeyChain.choosePrivateKeyAlias()`.
- Test the exact enrollment mode and delegated scope on an isolated device.
- Implement and test a fail-closed allowlist before returning any alias.
- Review rollback impacts: only one certificate-selection delegate can be active.

Never commit private keys, credentials, device identifiers, internal endpoints,
production configuration, or sensitive logs to this public repository.
