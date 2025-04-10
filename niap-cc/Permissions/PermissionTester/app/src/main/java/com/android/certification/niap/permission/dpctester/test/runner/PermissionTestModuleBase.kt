package com.android.certification.niap.permission.dpctester.test.runner
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
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.certification.niap.permission.dpctester.DpcApplication
import com.android.certification.niap.permission.dpctester.common.Constants
import com.android.certification.niap.permission.dpctester.common.SignatureUtils
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException
import com.android.certification.niap.permission.dpctester.test.log.ActivityLogger
import com.android.certification.niap.permission.dpctester.test.log.LoggerFactory
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import com.google.common.base.CaseFormat
import java.util.function.Consumer

//Base class for test cases
open class PermissionTestModuleBase(activity: Activity) {
    open var TAG: String = PermissionTestModuleBase::class.java.simpleName
    val title: String? = javaClass.getAnnotation(PermissionTestModule::class.java)?.name
    @JvmField
    val isSync:Boolean = javaClass.getAnnotation(PermissionTestModule::class.java)?.sync?:false
    var key =  CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaClass.simpleName)
    var prflabel = javaClass.getAnnotation(PermissionTestModule::class.java)?.prflabel?:""

    @JvmField
    var acceptDangerousApi = false

    @JvmField
    protected var logger: ActivityLogger
    @JvmField
    var testCases: MutableList<PermissionTestRunner.Data>
    @JvmField
    var enabled = true;

    open var testSize: Int = 0

    @JvmField
    val mActivity: Activity = activity
    @JvmField
    val mContext: Context = activity.applicationContext
    @JvmField
    protected val mContentResolver = mContext.contentResolver
    @JvmField
    protected val mPackageManager = mContext.packageManager
    @JvmField
    protected val mExecutor = (mContext.applicationContext as DpcApplication).executorService
    @JvmField
    val appUid = mContext.applicationInfo.uid
    @JvmField
    protected val mAppSignature : Signature =
        SignatureUtils.getTestAppSigningCertificate(mContext);
    @JvmField
    var inverseForPlatformTesting = false

    @JvmField
    val isPlatformSignatureMatch:Boolean =
        mPackageManager.hasSigningCertificate(
            Constants.PLATFORM_PACKAGE,
            mAppSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509
        );
    // An app should only have access to the GMS signature permissions if it is signed with the
    // GMS signing key or the platform signing key.
    val gmsSignatureMatch:Boolean =
        mPackageManager.hasSigningCertificate(Constants.GMS_PACKAGE_NAME,
        mAppSignature.toByteArray(), PackageManager.CERT_INPUT_RAW_X509)

    @JvmField
    val info = Info();
    var additionalTestSize=0
        set(value) {
            field = value
            info.count_additional_tests = value
        }

    var prefList:MutableList<Pair<String,String> > = mutableListOf();

    init {
        testCases = ReflectionTool.checkPermissionTestMethod(this)
        prefList  = ReflectionTool.checkPermissionTestPref(this)
        logger = LoggerFactory.createActivityLogger(
            title!!,
            activity as ActivityLogger.LogListAdaptable
        ) as ActivityLogger
        info.title = title
        info.count_errors = 0
        info.count_bypassed = 0
        testSize = testCases.size
        info.count_tests = testSize
        inverseForPlatformTesting=false
        //logger.system("The module `$title` has ${testSize} test cases.")
    }

    open class Info {
        var title: String? = null
        var details:String? = null
        var count_tests: Int = 0
        var count_errors: Int = 0
        var count_bypassed: Int =0
        var count_additional_tests: Int =0
        var skipped=false
        var self:PermissionTestModuleBase? = null
        val moduleLog:MutableList<LogBox> = mutableListOf();
        override fun toString(): String {
            return "title=$title count_tests=$count_tests count_errors=$count_errors count_bypassed=$count_bypassed"
        }
    }

    open class PrepareInfo {
        var count_tests: Int = 0
        var count_errors: Int = 0
        var count_bypassed: Int =0
        var count_passed: Int = 0
    }

    fun checkPermissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(mContext,permission)==PackageManager.PERMISSION_GRANTED
    }
    fun <T> getService(serviceClass: Class<T>): T? {
        val service = ContextCompat.getSystemService(mContext, serviceClass)
        return service
    }

    fun <T> getService(serviceClass: Class<T>,contextName:String): T? {
        val service_ = mContext.getSystemService(contextName)
        //val service = ContextCompat.getSystemService(mContext, serviceClass)
        if(service_.javaClass == serviceClass){
            return service_ as T
        }
        return null
    }

    /**
     * Returns the [IBinder] token for the current activity.
     *
     *
     * This token can be used in any binder transaction that requires the activity's token.
     */
    @SuppressLint("DiscouragedPrivateApi")
    fun getActivityToken(): IBinder {
        try {
            val tokenField = Activity::class.java.getDeclaredField("mToken")
            tokenField.isAccessible = true
            return tokenField[mActivity] as IBinder
        } catch (e: ReflectiveOperationException) {
            throw UnexpectedTestFailureException(e)
        }
    }

    open fun prepare(callback: Consumer<PermissionTestRunner.Result>?):PrepareInfo{
        return PrepareInfo()
    }
    open fun resultHook(result:PermissionTestRunner.Result):PermissionTestRunner.Result{
        return result
    }
    open fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray){

    }
}