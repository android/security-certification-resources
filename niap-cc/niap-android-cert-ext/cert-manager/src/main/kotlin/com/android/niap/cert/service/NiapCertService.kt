/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.niap.cert.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.niap.cert.manager.EnrollmentRequest
import com.android.niap.cert.manager.IEnrollmentCallback
import com.android.niap.cert.manager.INiapCertManager
import com.android.niap.cert.manager.IRevocationCallback
import com.android.niap.cert.service.engine.KeyStoreEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NiapCertService : Service() {

    private val orchestrator by lazy { NiapCertOrchestrator(this) }
    private val keyStoreEngine by lazy { KeyStoreEngine() }
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    inner class CertBinder : INiapCertManager.Stub() {
        override fun enroll(
            alias: String?, estServerUrl: String?, authToken: String?, subjectDn: String?,
            sans: List<String>?, keyTypeStr: String?, sigAlgStr: String?,
            trustedCaPem: String?, validatorConfig: android.os.Bundle?,
            callback: IEnrollmentCallback?
        ) {
            if (callback == null) {
                Log.e("NiapCertService", "enroll: callback must not be null")
                return
            }
            if (alias == null || estServerUrl == null || authToken == null || subjectDn == null) {
                Log.e("NiapCertService", "enroll: missing required parameters")
                safeOnError(callback, "Missing required enrollment parameters")
                return
            }
            Log.d("NiapCertService", "Enroll request for alias: $alias")
            val keyType = try { com.android.niap.cert.manager.KeyType.valueOf(keyTypeStr ?: "EC_P384") } catch (e: Exception) { com.android.niap.cert.manager.KeyType.EC_P384 }
            val sigAlg = try { com.android.niap.cert.manager.SigAlg.valueOf(sigAlgStr ?: "SHA384_ECDSA") } catch (e: Exception) { com.android.niap.cert.manager.SigAlg.SHA384_ECDSA }
            val config = if (validatorConfig != null) {
                com.android.niap.cert.manager.ValidatorConfig(
                    strictSigAlg = validatorConfig.getBoolean("strictSigAlg", true),
                    enforceCaConstraints = validatorConfig.getBoolean("enforceCaConstraints", true),
                    enforceMandatoryExtensions = validatorConfig.getBoolean("enforceMandatoryExtensions", true),
                    enforceEku = validatorConfig.getBoolean("enforceEku", true)
                )
            } else com.android.niap.cert.manager.ValidatorConfig()

            val request = EnrollmentRequest(
                alias = alias, estServerUrl = estServerUrl, authToken = authToken,
                subjectDn = subjectDn, sans = sans ?: emptyList(),
                csrSpec = com.android.niap.cert.manager.CsrSpec(keyType, sigAlg),
                trustedCaPem = trustedCaPem ?: "", validatorConfig = config
            )

            serviceScope.launch {
                try {
                    val result = orchestrator.executeWorkflow(request)
                    if (result.certificateData != null) {
                        safeOnSuccess(callback, result.certificateData)
                    } else {
                        safeOnError(callback, result.errorMessage ?: "Unknown enrollment error")
                    }
                } catch (e: Exception) {
                    Log.e("NiapCertService", "Enrollment threw exception: ${e.message}", e)
                    safeOnError(callback, e.message ?: e.javaClass.simpleName)
                }
            }
        }

        override fun revoke(alias: String?, callback: IRevocationCallback?) {
            if (callback == null) {
                Log.e("NiapCertService", "revoke: callback must not be null")
                return
            }
            if (alias.isNullOrEmpty()) {
                safeRevokeError(callback, "Missing alias")
                return
            }
            Log.d("NiapCertService", "Revoke request for alias: $alias")
            serviceScope.launch {
                try {
                    orchestrator.revoke(alias)
                    safeRevokeSuccess(callback)
                } catch (e: Exception) {
                    Log.e("NiapCertService", "Revocation threw exception: ${e.message}", e)
                    safeRevokeError(callback, e.message ?: e.javaClass.simpleName)
                }
            }
        }

        override fun hasEnrollment(alias: String?): Boolean =
            !alias.isNullOrEmpty() && orchestrator.hasEnrollment(alias)

        override fun getCertificateData(alias: String?): ByteArray =
            orchestrator.getCertificateData(alias ?: "")

        /**
         * Signing oracle: signs [digestBytes] with the AndroidKeyStore key bound to [alias].
         * NONEwithECDSA — Conscrypt has already computed the digest.
         */
        override fun sign(alias: String?, digestBytes: ByteArray?): ByteArray {
            requireNotNull(alias) { "alias must not be null" }
            requireNotNull(digestBytes) { "digestBytes must not be null" }
            return keyStoreEngine.sign(alias, digestBytes)
        }
    }

    private val binder = CertBinder()

    override fun onCreate() {
        super.onCreate()
        Log.d("NiapCertService", "Service created")
        createNotificationChannel()
        startForeground(1, createNotification(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("NiapCertService", "Service bound")
        return binder
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel("niap_cert_channel", "NIAP Certificate Manager", NotificationManager.IMPORTANCE_LOW)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
    }

    private fun createNotification() = Notification.Builder(this, "niap_cert_channel")
        .setContentTitle("NIAP Cert Manager")
        .setContentText("Managing certificates securely")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .build()

    // --- callback invocation helpers (swallow RemoteException; caller already gone) ---

    private fun safeOnSuccess(cb: IEnrollmentCallback, data: ByteArray) {
        try { cb.onSuccess(data) } catch (e: android.os.RemoteException) {
            Log.w("NiapCertService", "Enrollment callback unreachable: ${e.message}")
        }
    }

    private fun safeOnError(cb: IEnrollmentCallback, msg: String) {
        try { cb.onError(msg) } catch (e: android.os.RemoteException) {
            Log.w("NiapCertService", "Enrollment error callback unreachable: ${e.message}")
        }
    }

    private fun safeRevokeSuccess(cb: IRevocationCallback) {
        try { cb.onSuccess() } catch (e: android.os.RemoteException) {
            Log.w("NiapCertService", "Revocation callback unreachable: ${e.message}")
        }
    }

    private fun safeRevokeError(cb: IRevocationCallback, msg: String) {
        try { cb.onError(msg) } catch (e: android.os.RemoteException) {
            Log.w("NiapCertService", "Revocation error callback unreachable: ${e.message}")
        }
    }
}
