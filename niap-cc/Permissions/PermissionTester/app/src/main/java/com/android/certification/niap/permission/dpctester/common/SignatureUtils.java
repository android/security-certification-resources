package com.android.certification.niap.permission.dpctester.common;
/*
 * Copyright (C) 2024 The Android Open Source Project
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
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;

/**
 * Provides utility methods for obtaining and comparing signatures.
 */
public class SignatureUtils {
    private static Signature mAppSignature;
    private static final Object mAppSignatureLock = new Object();

    /**
     * Returns whether the currently running app has the same signing certificate as the specified
     * {@code packageName}.
     */
    public static boolean hasSameSigningCertificateAsPackage(Context context, String packageName) {
        Signature appSignature = getTestAppSigningCertificate(context);
        return context.getPackageManager().hasSigningCertificate(packageName,
                appSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509);
    }

    /**
     * Returns the currently running app's {@code Signature} using the provided {@code context}
     * to obtain the app's {@link android.content.pm.SigningInfo}.
     */
    public static Signature getTestAppSigningCertificate(Context context) {
        if (mAppSignature != null) {
            return mAppSignature;
        }
        synchronized (mAppSignatureLock) {
            mAppSignature = getAppSigningCertificate(context, context.getPackageName());
        }
        return mAppSignature;
    }

    /**
     * Returns the current signing certificate for the specified {@code packageName} using the
     * provided {@code context}.
     *
     * <p>This method is only intended to support packages with a single signing certificate. Any
     * handling of packages with multiple signatures should directly invoke the PackageManager
     * APIs.
     */
    public static Signature getAppSigningCertificate(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES);
            Signature[] signatures = packageInfo.signingInfo.getApkContentsSigners();
            if (signatures != null && signatures.length > 0) {
                return signatures[0];
            }
            StaticLogger.error("No signatures returned from getApkContentsSigners");
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            StaticLogger.error("Caught a NameNotFoundException querying PackageInfo for package "
                    + packageName, e);
            return null;
        }
    }
}
