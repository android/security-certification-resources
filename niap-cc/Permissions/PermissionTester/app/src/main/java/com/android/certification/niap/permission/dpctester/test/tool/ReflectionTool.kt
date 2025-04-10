package com.android.certification.niap.permission.dpctester.test.tool
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
import android.util.Log
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner
import com.google.common.base.CaseFormat
import java.util.stream.Collectors

class ReflectionTool {
    companion object {


        /**
         * Dynamically invoke a method.
         *
         * @param obj
         *     The object or class to get the method from.
         * @param name
         *     The name of the method to invoke.
         * @param types
         *     the parameter types of the requested method.
         * @param args
         *     the arguments to the method
         * @param <T>
         *     the method's return type
         * @return the result of dynamically invoking this method.
         */
        inline fun <reified T> invoke2(
            obj: Any?,
            name: String,
            types: Array<Class<*>> = emptyArray(),
            vararg args: Any
        ): T? =
            try {
                //obj!!.javaClass.get
                obj!!.javaClass.getMethod(name, *types).run {
                    invoke(obj, *args) as? T
                }
            } catch (e: Exception) {
                null
            }

        /**
         * ******** This method has a compatibility problem when run it on the kotlin ************
         *
         *
         * @return the result of invoking the specified method
         */
        fun invoke(
            targetClass: Class<*>, methodName: String,
            targetObject: Any?, parameterClasses: Array<Class<*>?>, vararg parameters: Any?
        ): Any {
            try {
                val method = targetClass.getMethod(methodName, *parameterClasses)
                return method.invoke(targetObject, *parameters)
            } catch (e: ReflectiveOperationException) {
                Log.e("TAG", "Reflection failed.")
                e.printStackTrace()
                val cause = e.cause
                if (cause is SecurityException) {
                    throw (cause as SecurityException?)!!
                } else {
                    throw cause!!//UnexpectedPermissionTestFailureException(e)
                }
            } catch (e:SecurityException){
                Log.e("TAG", "Other error")
                throw e
            }
        }

        /**
         * List up declared methods of specified object. it is intended to check the prototype of
         * transacts api.
         */
        fun checkDeclaredMethod(target: Any, filter: String?): List<String> {
            val a: MutableList<String> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val methods = clazz.declaredMethods
            for (m in methods) {
                val method = StringBuilder(m.name + "(")
                val types = m.parameterTypes
                for (t in types) method.append(" ").append(t.typeName)
                a.add("$method)")
            }
            return a.stream().filter { str: String ->
                str.startsWith(
                    filter!!
                )
            }.collect(Collectors.toList())
        }

        fun checkDeclaredMethod(javaClass: Class<*>, filter: String?): List<String> {
            val a: MutableList<String> = ArrayList()
            val clazz: Class<*> = javaClass
            val methods = clazz.declaredMethods
            for (m in methods) {
                val method = StringBuilder(m.name + "(")
                val types = m.parameterTypes
                for (t in types) method.append(" ").append(t.typeName)
                a.add("$method)")
            }
            return a.stream().filter { str: String ->
                str.startsWith(
                    filter!!
                )
            }.collect(Collectors.toList())
        }

        fun checkDeclaredFields(target: Any, filter: String?): List<String> {
            val a: MutableList<String> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val fields = clazz.declaredFields
            for (f in fields) {
                val field = StringBuilder(f.name + "(")
                a.add(field.toString() + "<" + f.type + ">")
            }
            return a.stream().filter { str: String ->
                str.startsWith(
                    filter!!
                )
            }.collect(Collectors.toList())
        }

        fun checkPermissionTestPref(target: Any): MutableList<Pair<String,String> >{
            val a: MutableList<Pair<String,String> > = ArrayList()
            val clazz: Class<*> = target.javaClass
            val fields = clazz.declaredFields
            //Take all methods that has PermissionTest annotation
            for (m in fields) {
                val ann = m.getAnnotation(PreferenceBool::class.java)
                if (ann == null) continue;
                //val className = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, javaClass.simpleName)
                val container:Pair<String,String> = Pair("bool", ann.label+":"+ann.prflabel);
                 //   PermissionTestRunner.Data(ann.permission, ann.sdkMin, ann.sdkMax,
                 //  m.name,ann.requiredPermissions,ann.requestedPermissions,ann.developmentProtection);//ann.requiredPermissions,ann.requiredServices)
                 //Log.d("TAG",container.toString())
                a.add(container)
            }
            return a;
        }

        fun checkPermissionTestMethod(target: Any): MutableList<PermissionTestRunner.Data> {
            val a: MutableList<PermissionTestRunner.Data> = ArrayList()
            val clazz: Class<*> = target.javaClass
            val methods = clazz.declaredMethods
            //Take all methods that has PermissionTest annotation
            for (m in methods) {
                val ann = m.getAnnotation(PermissionTest::class.java)
                if (ann == null) continue;
                val container =
                    PermissionTestRunner.Data(ann.permission, ann.sdkMin, ann.sdkMax,
                        m.name,ann.requiredPermissions,ann.requestedPermissions,
                        ann.developmentProtection,ann.ignore);//ann.requiredPermissions,ann.requiredServices)
                //Log.d("TAG",container.toString())
                a.add(container)
            }
            return a;
        }
        /*
        fun deviceConfigSetProperty(
            namespace: String,
            name: String,
            value: String,
            makeDefault: Boolean
        ): Boolean {
            try {
                val r: Any =
                    invokeReflectionCall(
                        Class.forName("android.provider.DeviceConfig"),
                        "setProperty", null,
                        arrayOf<Class<*>?>(
                            String::class.java, String::class.java, String::class.java,
                            Boolean::class.javaPrimitiveType
                        ), namespace, name, value, makeDefault
                    )
                return r as Boolean
            } catch (e: Exception) {
                debug("DeviceConfig.setProperty failed.($namespace,$name,$value)")
                e.printStackTrace()
                return false
            }
        }

        fun deviceConfigGetProperty(namespace: String, name: String): String? {
            try {
                return invokeReflectionCall(
                    Class.forName("android.provider.DeviceConfig"),
                    "getProperty", null,
                    arrayOf<Class<*>>(String::class.java, String::class.java), namespace, name
                )
            } catch (e: Exception) {
                debug("DeviceConfig.getProperty failed.($namespace,$name)")
                return null
            }
        }*/
    }//companion object
}
