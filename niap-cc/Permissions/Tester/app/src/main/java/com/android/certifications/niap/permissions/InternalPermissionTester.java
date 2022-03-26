/*
 * Copyright 2021 The Android Open Source Project
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

package com.android.certifications.niap.permissions;

import static com.android.certifications.niap.permissions.utils.InternalPermissions.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import com.android.certifications.niap.permissions.config.TestConfiguration;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;
import com.android.certifications.niap.permissions.log.StatusLogger;
import com.android.certifications.niap.permissions.utils.SignatureUtils;
import com.android.certifications.niap.permissions.utils.Transacts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permission tester to verify all platform declared internal permissions properly guard their API,
 * resources, etc. An internal protection level permission is different from a signature permission
 * in that the requesting app will not be granted the permission just by being signed with the same
 * signing key as the platform, but instead must meet other requirements as defined by the
 * permission's flags.
 */
public class InternalPermissionTester extends BasePermissionTester {
    private static final String TAG = "InternalPermissionTester";
    private final Logger mLogger = LoggerFactory.createDefaultLogger(TAG);

    /**
     * Map of internal protection level permissions tto their corresponding {@link
     * PermissionTest}s.
     */
    private final Map<String, BasePermissionTester.PermissionTest> mPermissionTasks;

    public InternalPermissionTester(TestConfiguration configuration, Activity activity) {
        super(configuration, activity);

        mPermissionTasks = new HashMap<>();

        mPermissionTasks.put(permission.MANAGE_HOTWORD_DETECTION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    try {
                        mTransacts.invokeTransact(Transacts.VOICE_INTERACTION_SERVICE,
                                Transacts.VOICE_INTERACTION_DESCRIPTOR, Transacts.updateState, null,
                                null, null);
                    } catch (SecurityException e) {
                        // Note, there are two places where this transact can throw a
                        // SecurityException; the first is during the permission check, the second
                        // is after the permission check is successful when determining if the
                        // current caller is the VoiceInteractionService. This could be flaky but
                        // treat the API as successful if the error message indicates the caller
                        // is not the VoiceInteractionService.
                        if (!e.getMessage().contains(
                                "Caller is not the current voice interaction service")) {
                            throw e;
                        } else {
                            mLogger.logDebug(
                                    "MANAGE_HOTWORD_DETECTION passed permission check, caught the"
                                            + " following exception: ", e);
                        }
                    }
                }));

        mPermissionTasks.put(permission.OBSERVE_SENSOR_PRIVACY,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.SENSOR_PRIVACY_SERVICE,
                            Transacts.SENSOR_PRIVACY_DESCRIPTOR, Transacts.isSensorPrivacyEnabled);
                }));

        mPermissionTasks.put(permission.DOMAIN_VERIFICATION_AGENT,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.DOMAIN_VERIFICATION_SERVICE,
                            Transacts.DOMAIN_VERIFICATION_DESCRIPTOR,
                            Transacts.queryValidVerificationPackageNames);
                }));

        mPermissionTasks.put(permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_IMS_SERVICE,
                            Transacts.TELEPHONY_IMS_DESCRIPTOR, Transacts.requestAvailability, 0,
                            mPackageName, null,
                            null, null);
                }));

        mPermissionTasks.put(permission.ASSOCIATE_COMPANION_DEVICES,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    Signature signature = SignatureUtils.getTestAppSigningCertificate(mContext);
                    MessageDigest messageDigest;
                    try {
                        messageDigest = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        throw new UnexpectedPermissionTestFailureException(e);
                    }
                    byte[] certDigest = messageDigest.digest(signature.toByteArray());
                    if (!mPackageManager.hasSigningCertificate(mPackageName, certDigest,
                            PackageManager.CERT_INPUT_SHA256)) {
                        throw new UnexpectedPermissionTestFailureException(
                                "PackageManager reported test app does not have the provided "
                                        + "signing certificate");
                    }
                    mTransacts.invokeTransact(Transacts.COMPANION_DEVICE_SERVICE,
                            Transacts.COMPANION_DEVICE_DESCRIPTOR, Transacts.createAssociation,
                            mPackageName, "11:22:33:44:55:66", 0, certDigest);
                }));

        mPermissionTasks.put(permission.BYPASS_ROLE_QUALIFICATION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.ROLE_SERVICE, Transacts.ROLE_DESCRIPTOR,
                            Transacts.setBypassingRoleQualification, false);
                }));

        mPermissionTasks.put(permission.PERFORM_IMS_SINGLE_REGISTRATION,
                new PermissionTest(false, Build.VERSION_CODES.S, () -> {
                    mTransacts.invokeTransact(Transacts.TELEPHONY_IMS_SERVICE,
                            Transacts.TELEPHONY_IMS_DESCRIPTOR,
                            Transacts.triggerNetworkRegistration, 0, null, 0, "test-sip-reason");
                }));
    }

    @Override
    public boolean runPermissionTests() {
        boolean allTestsPassed = true;
        List<String> permissions = mConfiguration.getInternalPermissions().orElse(
                new ArrayList<>(mPermissionTasks.keySet()));
        for (String permission : permissions) {
            boolean testPassed = runPermissionTest(permission, mPermissionTasks.get(permission),
                    true);
            if (!testPassed) {
                allTestsPassed = false;
            }
        }
        if (allTestsPassed) {
            StatusLogger.logInfo(
                    "*** PASSED - all internal permission tests completed successfully");
        } else {
            StatusLogger.logInfo(
                    "!!! FAILED - one or more internal permission tests failed");
        }
        return allTestsPassed;
    }
}
