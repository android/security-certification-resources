package com.android.certification.niap.permission.dpctester.test.tool;
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
import android.util.Log;

import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionToolJava {


    public static Object invokeReflectionCall(Class<?> targetClass, String methodName,
                                              Object targetObject, Class<?>[] parameterClasses, Object... parameters) {
        try {
            Method method = targetClass.getMethod(methodName, parameterClasses);
            return method.invoke(targetObject, parameters);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new UnexpectedTestFailureException(e);
            }
        }
    }

    public static Object stubHiddenObject(String classname)  {
        try {
            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getConstructor();
            return remoteCallbackConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            Log.e("ReflectionTool", "StubHiddenObject Error:"+e.getMessage());

            return null;
        }
    }

    public static Object stubHiddenObjectSub(String classname)  {
        try {

            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getConstructor();
            return remoteCallbackClass.asSubclass(remoteCallbackClass).newInstance();
            //remoteCallbackConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException e){
            //e.printStackTrace();
            Log.e("ReflectionTool", "StubHiddenObject Error:"+e.getMessage());
            return null;
        }
    }

    public static Object stubHiddenObject(String classname,Class<?>[] parameters,Object... args)  {
        try {
            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getDeclaredConstructor(parameters);
            return remoteCallbackConstructor.newInstance(args);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            Log.e("ReflectionTool", "StubHiddenObject Error:"+e.getMessage());

            return null;
        }
    }
}
