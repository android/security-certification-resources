/*
 * Copyright 2022 The Android Open Source Project
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

import com.android.certifications.niap.permissions.BasePermissionTester;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utility methods for invoking methods via reflection.
 */
public class ReflectionUtils {
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
