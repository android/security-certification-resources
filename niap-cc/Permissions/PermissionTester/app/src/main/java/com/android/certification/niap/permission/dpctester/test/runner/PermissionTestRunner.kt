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
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.android.certification.niap.permission.dpctester.common.ReflectionUtil
import com.android.certification.niap.permission.dpctester.data.LogBox
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger
import com.android.certification.niap.permission.dpctester.test.tool.ReflectionTool
import com.android.certification.niap.permission.dpctester.test.tool.TesterUtils
import kotlinx.coroutines.sync.Mutex
import java.lang.reflect.InvocationTargetException
import java.util.function.Consumer

class PermissionTestRunner {
    val TAG: String = PermissionTestRunner::class.java.simpleName

    companion object {
        private var instance_ : PermissionTestRunner? = null
        var running = false
        @JvmField
        var inverse_test_result = false

        fun getInstance(): PermissionTestRunner {
            if(instance_ == null){
                instance_ = PermissionTestRunner()
            }
            return instance_!!
        }
        var testThreadMutex = Mutex(false);


    }
    /**
     * Inverse the results of the test cases
     */
    fun setInverseTestResult(inverse:Boolean){
        inverse_test_result = inverse
    }



    // Memo :
    // 1. Is the test result should be reversed?
    //    When the test should be failure, For example no permission declared or any other reason,
    //    We reverse a flag for evaluation results (B_SUCCESS,B_FAILURE).
    //    In these reversed cases the api should not be succeeded or permission should be granted.
    //    But if both api and permission are granted unexpectedly it's also a success case.
    //    (It suggests system grants undeclared permissions automatically)

    fun newTestThread(root: PermissionTestModuleBase,testCase:Data,callback: Consumer<Result>?):Thread {
        return Thread {

            val is_inverse = inverse_test_result || root.inverseForPlatformTesting
            val B_SUCCESS = if(is_inverse) false else true
            val B_FAILURE = if(is_inverse) true else false
            var success=B_SUCCESS

            var throwable:Throwable? = null
            var bypassed = false
            var apisuccess = true
            var message = "(none)";// else "In this case the test should be failed."
            val granted =
                (ActivityCompat.checkSelfPermission(root.mContext,testCase.permission)==
                        PackageManager.PERMISSION_GRANTED);
            //StaticLogger.info("1:${testCase.permission}:${granted}")
            try {
                try {
                    //Preliminary Conditions Check



                    // Check required Permissions
                    testCase.requiredPermissions.forEach {
                        if (ActivityCompat.checkSelfPermission(root.mContext, it)
                            != PackageManager.PERMISSION_GRANTED
                        ) {
                            throw BypassTestException(
                                "${testCase.permission} : `$it`permission should be granted to run ."
                            )
                        }
                    }

                    // Check Required Services : *difficult to check*
                    // Check Android Version

                    var SDK_INT = Build.VERSION.SDK_INT
                    if(TesterUtils.isAtLeastBaklava()){
                        SDK_INT = 36
                    }

                    if(SDK_INT<testCase.sdkMin){
                        throw BypassTestException(
                            "${testCase.permission} : SDK${SDK_INT} is not supported to run.(SDK MIN:${testCase.sdkMin})"
                        )
                    }
                    if(SDK_INT>testCase.sdkMax){
                        throw BypassTestException(
                            "${testCase.permission} : SDK${SDK_INT} is not supported to run.(SDK MAX:${testCase.sdkMax})"
                        )
                    }
                    if(testCase.ignore){
                        throw BypassTestException(
                            "${testCase.permission} is set to be ignored for some reasons. Please. Check source code."
                        )
                    }

                    //StaticLogger.debug("running=>"+testCase.methodName)
                    ReflectionUtil.invoke(root, testCase.methodName)


                } catch (ex: ReflectionUtil.ReflectionIsTemporaryException) {
                    ex.printStackTrace();

                    if (ex.cause is InvocationTargetException) {
                        val exi = ex.cause
                        throwable = exi?.cause
                    } else {
                        throwable = ex.cause
                    }

                    //StaticLogger.info(">>"+testCase.permission+">>>"+throwable?.message)
                    throw throwable!! //rethrow
                }
            } catch(ex:NullPointerException){

                //Is intended null pointer exception? (missing system service=>bypass)
                if(ex.message !=null && ex.message!!.startsWith("[npe_system_service]")) {
                    throwable = ex
                    success = true //bypassed test always returns true
                    bypassed = true
                    apisuccess=false
                    message =
                        "The system does not have the hardware feature required to run this test."
                }else if(ex.message !=null &&
                    (ex.message!!.startsWith("Attempt to invoke interface")||
                     ex.message!!.startsWith("Attempt to invoke virtual method")
                    )){
                    //Binder transaction was failed due to the parameter is null.
                    //And we treat this case as a successful.
                    throwable = ex
                    success = true //bypassed test always returns true
                    bypassed = false
                    apisuccess=false
                    message =
                        "A NPE has been caused, but the binder transaction was executed."
                } else {
                    throwable = ex
                    success = B_FAILURE
                    apisuccess=false
                    message = if (ex.message != null) ex.message!! else ex.toString()
                }//ex.message!!
            } catch(ex:SecurityException) {
                throwable = ex
                success = B_FAILURE
                apisuccess=false

                if(ex.message != null)
                    message = ex.message!!
            } catch(ex:NoSuchMethodException) {
                throwable = ex
                success = B_FAILURE
                apisuccess=false

                if(ex.message != null)
                    message = ex.message!!
            } catch(ex: BypassTestException){
                throwable = ex
                success=true //bypassed test always returns true
                bypassed=true
                apisuccess=false
                message = ex.message!!
            } catch (ex:UnexpectedTestFailureException) {
                //Unexpected Failures
                //StaticLogger.info("Unexpected:"+ex.message);
                //ex.printStackTrace()
                throwable = ex.cause
                success = B_FAILURE
                apisuccess=false
                bypassed=false
                message = ex.cause?.message!!
            } catch (ex:Exception){
                //Unexpected Failures

                throwable = ex
                success = B_FAILURE
                apisuccess=false
                message = if(ex.message != null) ex.message!! else ex.toString()
            }

            //StaticLogger.info("2:${testCase.permission}:${granted}/${apisuccess}&&${granted}")
            //healthy case
            if(apisuccess && granted){
                //if the result should be reversed, success case would be regarded as failure
                if(is_inverse){
                    success = B_FAILURE
                } else {
                    success = B_SUCCESS
                }
                message = "Target permission is granted and api successfully executed";
                //StaticLogger.info("3:${testCase.permission}:${granted}:${success}")
            }

            val result = root.resultHook(
                Result(
                    success=success,
                    throwable=throwable,
                    source=testCase,
                    bypassed=bypassed,
                    granted=ActivityCompat.checkSelfPermission(root.mContext, testCase.permission)
                            == PackageManager.PERMISSION_GRANTED,
                    api_successful=apisuccess,
                    platform_signature_match = root.isPlatformSignatureMatch,
                    gms_signature_match = root.gmsSignatureMatch,
                    developmentProtection = testCase.developmentProtection,
                    isInverse = is_inverse,
                    message=message)
            )

            if(result.bypassed){
                suite.info.count_bypassed += 1
                root.info.count_bypassed  += 1 // suite.info.count_bypassed + 1
                root.info.moduleLog.add(
                    LogBox(type = "bypassed", name =testCase.permission, description = message));
            }

            if(!result.success && !result.bypassed){
                suite.info.count_errors += 1
                root.info.count_errors  += 1 // suite.info.count_errors + 1
                root.info.moduleLog.add(
                    LogBox(type = "error", name =testCase.permission, description = message));
                if(throwable != null){
                    throwable.printStackTrace()
                }
            }
            //safe call
            //testLatch = CountDownLatch(1);
            //suite.info.count_errors = suite.info.count_errors + if(success) 0 else 1
            root.mActivity.runOnUiThread{
                callback?.accept(result)
                suite.cbModuleControl?.accept(root.info)
                suite.cbTestControl?.accept(root.info)
            }

        }
    }


    var modulePos = 0;

    fun runNextModule(suite_: PermissionTestSuiteBase, callback: Consumer<Result>?):Boolean {

        if(modulePos>=suite_.modules.size){
            //suite_.info.ellapsed_time = System.currentTimeMillis() - suite_.info.start_time
            suite_.info.ellapsed_time = System.currentTimeMillis() - suite_.info.start_time;
            //mActivity.runOnUiThread {
            suite_.cbSuiteFinish?.accept(suite_.info)
            //}
            return false;
        }
        val m:PermissionTestModuleBase = suite_.modules.get(modulePos);

        modulePos+=1;

        val sp = PreferenceManager.getDefaultSharedPreferences(m.mContext);
        m.enabled = sp.getBoolean(m.key,true);
        if(m.enabled) {
            val prepareInfo = m.prepare(callback)
            val testCases = ReflectionTool.checkPermissionTestMethod(m)
            m.info.moduleLog.clear()
            m.info.count_tests = prepareInfo.count_tests + testCases.size
            m.info.count_errors = prepareInfo.count_errors
            m.info.count_bypassed = prepareInfo.count_bypassed
            m.info.self = m;
            if (prepareInfo.count_tests > 0) {
                StaticLogger.info("there are ${prepareInfo.count_tests} pre-running testcases")
            }
            m.mActivity.runOnUiThread {
                suite_.cbModuleStart?.accept(m.info)
            }
            val threads = mutableListOf<Thread>()

            for (testCase in testCases) {
                // Block If the version is not supported.
                // If the permission has a corresponding task then run it.
                val thread = newTestThread(m, testCase, callback)
                //m.mActivity.runOnUiThread(thread);
                thread.start();
                synchronized(threads) {
                    thread.join(1000)
                }
                if(m.isSync) testThreadMutex.tryLock()
            }
            //wait all thread finished
            /*for (thread in threads) {
                thread.join(500)
            }*/
            m.mActivity.runOnUiThread{
                this.suite.cbModuleFinish?.accept(m.info)
            }
        } else {
            //Skip
            m.info.skipped=true
            val prepareInfo = m.prepare(callback)
            val testCases = ReflectionTool.checkPermissionTestMethod(m)
            m.info.count_tests = prepareInfo.count_tests + testCases.size
            m.mActivity.runOnUiThread{
                this.suite.cbModuleFinish?.accept(m.info)
            }
            runNextModule(suite_,this.suite.methodCallback)

        }

        return true
    }

    lateinit var suite: PermissionTestSuiteBase
    fun start(suite_: PermissionTestSuiteBase, callback: Consumer<Result>?) {
        this.suite = suite_;
        this.suite.methodCallback = callback;
        this.suite.info.start_time = System.currentTimeMillis()
        suite_.cbSuiteStart?.accept(suite_.info)
        if(running){
            throw IllegalStateException("Other Suite Already running")
        }
        running = true;
        modulePos=0
        runNextModule(suite_,this.suite.methodCallback)

    }

    data class Result(
        var success:Boolean, val throwable:Throwable? = null, val source: Data,
        var bypassed: Boolean = false,
        var granted:Boolean=false,
        var api_successful:Boolean=false,
        var gms_signature_match:Boolean=false,
        var platform_signature_match:Boolean=false,
        var developmentProtection: Boolean=false,
        var isInverse:Boolean=false,
        var message:String="")

    data class Data(
        var permission: String,
        val sdkMin: Int,
        val sdkMax: Int,
        val methodName: String,
        val requiredPermissions: Array<String>,
        val requestedPermissions: Array<String>,
        val developmentProtection: Boolean,
        val ignore:Boolean
    ){
        constructor(permission: String) : this(permission=permission,
            sdkMin = 0,sdkMax=1000, methodName = "", requiredPermissions= emptyArray(),
            requestedPermissions = emptyArray(),
            developmentProtection=false,ignore=false,
        )

        init {
            if(!permission.contains(".")){
                permission ="android.permission."+permission;
            }
        }


    }
}