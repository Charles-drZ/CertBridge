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
./gradlew :app:assembleDebug :app:testDebugUnitTest :keychain-probe:assembleDebug
```

The `keychain-probe` module is a **separate test-only Android application**,
not a component of the CertBridge APK. It uses a button to invoke
`KeyChain.choosePrivateKeyAlias()` with a reserved `.invalid` URI, without any
network request, credential access, or alias logging.

### Emulator callback smoke test

Only use a disposable Android Enterprise emulator with a trusted test DPC
already configured as Device Owner. Grant **Choose private key** (and no other
delegation scopes) to `com.example.certbridge`.

```sh
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 install -r keychain-probe/build/outputs/apk/debug/keychain-probe-debug.apk
adb -s emulator-5554 shell am start -n com.example.certbridge.probe/.ProbeActivity
```

Tap **Request certificate alias** in the Probe app, then inspect the CertBridge
receiver log:

```sh
adb -s emulator-5554 logcat -d -s CertBridge:I
```

Expected evidence, if the DPC delegation and Android broadcast are working:

```text
Certificate selection callback received; mode=observe-only; fallback=system-picker
```

The ordinary system KeyChain picker may appear, and with no test certificates
installed it may have nothing to choose. **Do not install a production
certificate to make this test pass.** The probe does not verify any Cisco or VPN
integration. No physical-device, MDM-policy, or VPN integration test has been
completed.

## Before enabling automatic selection

- Verify the real calling package, UID, and signing identity.
- Verify the actual Android KeyChain alias and its lifecycle.
- Confirm the calling VPN client invokes `KeyChain.choosePrivateKeyAlias()`.
- Test the exact enrollment mode and delegated scope on an isolated device.
- Implement and test a fail-closed allowlist before returning any alias.
- Review rollback impacts: only one certificate-selection delegate can be active.

Never commit private keys, credentials, device identifiers, internal endpoints,
production configuration, or sensitive logs to this public repository.
