/*
 * Copyright 2020 The Android Open Source Project
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

package com.android.certifications.niap.permissions.utils;

import android.os.Binder;
import android.util.Log;
import com.android.certifications.niap.permissions.BasePermissionTester;
import com.android.certifications.niap.permissions.log.Logger;
import com.android.certifications.niap.permissions.log.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utility methods for invoking methods via reflection.
 */
public class ReflectionUtils {

    private static final Logger mLogger = LoggerFactory.createDefaultLogger("Reflectioni Utils");


    /**
     * Invokes the specified {@code methodName} defined in the {@code targetClass} against the
     * {@code targetObject} (or {@code null} for a static method) that accepts the provided {@code
     * parameterClasses} with values {@code parameters}.
     *
     * <p>To support running reflective calls for permission tests if the reflective method fails
     * due to a SecurityException then the SecurityException is rethrown as is; any other exception
     * results in a {@link BasePermissionTester.UnexpectedPermissionTestFailureException}.
     *
     * @return the result of invoking the specified method
     */
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
                throw new BasePermissionTester.UnexpectedPermissionTestFailureException(e);
            }
        }
    }

   /* public static Object invokeReflectionCall(Object targetObject, String methodName,
                                               Class<?>[] parameterClasses, Object... parameters) {
        invokeReflectionCall(targetObject.getClass(),methodName,targetObject,parameterClasses,parameters);
    }*/

    public static Object invokeReflectionCall(String className, String methodName,
                                              Object targetObject, Class<?>[] parameterClasses, Object... parameters) {
        try {
            Class<?> targetClass = Class.forName(className);
            Method method = targetClass.getMethod(methodName, parameterClasses);
            return method.invoke(targetObject, parameters);
        } catch (ReflectiveOperationException e) {
            Throwable cause = e.getCause();
            if (cause instanceof SecurityException) {
                throw (SecurityException) cause;
            } else {
                throw new BasePermissionTester.UnexpectedPermissionTestFailureException(e);
            }
        }
    }

    public static Object stubFromInterface(String className)  {
        try {
            Class<?> ifListner = Class.forName(className);
            Object listenerInstance = Proxy.newProxyInstance(
                    ifListner.getClassLoader(), new Class<?>[]{ifListner}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //mLogger.logDebug("callback invoked: "+proxy+","+method);
                    if (method.getName().equals("asBinder")) {
                        return new Binder() ;
                    }
                    if(method.getName().equals("onSuccess")){
                        mLogger.logDebug("onSuccess");
                        //System.out.println("ARGS: " + (Integer)args[0] + ", " + (Integer)args[1]);
                        return 1;
                    } else if(method.getName().equals("onError")){
                        mLogger.logDebug("onError");
                        //throw new SecurityException("Errror!");
                        return 1;
                    }
                    else return -1;
                }
            });
            mLogger.logDebug(checkDeclaredMethod(ifListner,"").toString());;

            return ifListner.cast(listenerInstance);
        } catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    public static boolean deviceConfigSetProperty(String namespace,String name,String value,boolean makeDefault)  {
        try {
            Object r = invokeReflectionCall(Class.forName("android.provider.DeviceConfig"),
                    "setProperty", null,
                    new Class[]{String.class, String.class, String.class,
                            boolean.class}, namespace,name, value, makeDefault);
            return (boolean)r;
        } catch (Exception e){
            mLogger.logDebug("DeviceConfig.setProperty failed.("+namespace+","+name+","+value+")");
            e.printStackTrace();
            return false;
        }
    }
    public static String deviceConfigGetProperty(String namespace,String name)  {
        try {
            String r = (String)invokeReflectionCall(Class.forName("android.provider.DeviceConfig"),
                    "getProperty", null,
                    new Class[]{String.class, String.class}, namespace,name);
            return r;
        } catch (Exception e) {
            mLogger.logDebug("DeviceConfig.getProperty failed.(" + namespace+","+name+")");
            return null;
        }
    }

    public static Object stubHiddenObject(String classname)  {
        try {
            Class<?> remoteCallbackClass = Class.forName(classname);
            Constructor<?> remoteCallbackConstructor = remoteCallbackClass.getConstructor();
            return remoteCallbackConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            e.printStackTrace();

            return null;
        }
    }
    public static Object stubRemoteCallback()  {
        try {
            Class<?> onResultListenerClass = Class.forName(
                    "android.os.RemoteCallback$OnResultListener");
            Object onResultListener = Proxy.newProxyInstance(
                    onResultListenerClass.getClassLoader(),
                    new Class[]{onResultListenerClass}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object o, Method method, Object[] objects)
                                throws Throwable {
                            Log.d("", "invoke: " + method);
                            return null;
                        }
                    });

            Class<?> remoteCallbackClass = Class.forName("android.os.RemoteCallback");
            Constructor remoteCallbackConstructor = remoteCallbackClass.getConstructor(
                    onResultListenerClass);
            return remoteCallbackConstructor.newInstance(
                    (Object) onResultListener);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e){
            e.printStackTrace();

            return null;
        }
    }

    //recursive function to search am exception in the nested stack trace
    public static boolean findCauseInStackTraceElement(Boolean resp,Throwable ex,String nameMatches)
    {
        //Log.d("!*!name matches","in:"+resp+">"+ex.getClass().toString());
        //boolean bRet=false;
        if(ex.getCause() != null){
            resp = findCauseInStackTraceElement(resp,ex.getCause(),nameMatches);
        }
        if(resp) {
            return true;
        } else {
            if(ex.getClass().toString().indexOf(nameMatches)>-1){
                //Log.d("!*!name matches", ex.getClass().toString() + "??" + nameMatches);
                resp = true;
            }
            return resp;
        }
    }

    /**
     * List up declared methods of specified object. it is intended to check the prototype of
     * transacts api.
     */
    public static List<String> checkDeclaredMethod(Object target,final String filter){
        List<String> a = new ArrayList<>();
        Class<?> clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            StringBuilder method = new StringBuilder(m.getName() + "(");
            Class<?>[] types = m.getParameterTypes();
            for(Class<?> t:types) method.append(" ").append(t.getTypeName());
            a.add(method+")");
        }
        return a.stream().filter(str->str.startsWith(filter)).collect(Collectors.toList());
    }

    public static List<String> checkDeclaredMethod(Class<?> clazz,final String f){
        List<String> a = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            StringBuilder method = new StringBuilder(m.getName() + "(");
            Class<?>[] types = m.getParameterTypes();
            for(Class<?> t:types){
                method.append(" ").append(t.getTypeName());
            }
            a.add(method+")");
        }
        return a.stream().filter(str->str.startsWith(f)).collect(Collectors.toList());
    }
}
