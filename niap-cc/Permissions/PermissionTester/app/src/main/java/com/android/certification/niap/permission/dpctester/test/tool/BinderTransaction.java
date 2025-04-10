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
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.certification.niap.permission.dpctester.common.ReflectionUtil;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTestFailureException;
import com.android.certification.niap.permission.dpctester.test.log.StaticLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class BinderTransaction  {

    static Map<String, Map<String,Integer>> dictTransacts;

    Context mContext;

    private static BinderTransaction instance = null;// = new BinderTransaction();
    private BinderTransaction(){}
    public static BinderTransaction getInstance(){
        if(instance == null){
            // block the multiple access from multiple thread
            synchronized (BinderTransaction.class) {
                 if(instance == null){
                    instance = new BinderTransaction();
                }
            }
        }
        return instance;
    }
    private void build(Builder builder) {
        this.mContext = builder.mContext;
    }

    public static class Builder {
        private final Context mContext; // Mandatory
        //private Builder(){}
        public Builder(Context context) {
            this.mContext = context;
        }
        public void build(){
            BinderTransaction.getInstance().build(this);
        }
    }


    private static Parcel handleBinderInput(Parcel data,Object parameter,Boolean useCharSequence,Class<?> clazzRemoteCallback) throws ReflectionUtil.ReflectionIsTemporaryException {


        if (parameter instanceof CharSequence && useCharSequence) {
            /*if(parameter == null) {
                data.writeInt(0);
            }*/
            data.writeInt(1);
            TextUtils.writeToParcel((CharSequence) parameter, data, 0);

        } else if (parameter instanceof String) {
            data.writeString((String) parameter);
        } else if (parameter instanceof Long) {
            data.writeLong((Long) parameter);
        } else if (parameter instanceof Integer) {
            data.writeInt((Integer) parameter);
        } else if (parameter instanceof Boolean) {
            data.writeInt((Boolean) parameter ? 1 : 0);
        } else if (parameter instanceof int[]) {
            data.writeIntArray((int[]) parameter);
        } else if (parameter instanceof byte[]) {
            data.writeByteArray((byte[]) parameter);
        } else if (parameter instanceof Proxy) {
            //ReflectionUtils.checkDeclaredMethod(parameter,"");
        } else if (parameter instanceof IInterface) {
            data.writeStrongBinder(
                    parameter != null ? ((IInterface) parameter).asBinder() : null);
        } else if (parameter instanceof IBinder) {
            data.writeStrongBinder((IBinder) parameter);
        } else if (parameter instanceof ComponentName) {
            data.writeInt(1);
            ((ComponentName) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof Uri) {
            data.writeInt(1);
            ((Uri) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof String[]) {
            data.writeStringArray((String[]) parameter);
        } else if (parameter instanceof Account) {
            data.writeInt(1);
            ((Account) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof AccessibilityServiceInfo) {
            data.writeInt(1);
            ((AccessibilityServiceInfo) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof ParcelUuid) {
            data.writeInt(1);
            ((ParcelUuid) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof PendingIntent) {
            data.writeInt(1);
            ((PendingIntent) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof NetworkCapabilities) {
            data.writeInt(1);
            ((NetworkCapabilities) parameter).writeToParcel(data, 0);
        } else if (parameter instanceof ParcelFileDescriptor) {
            //mLogger.logSystem("here");
            data.writeInt(1);
            ((ParcelFileDescriptor) parameter).writeToParcel(data, 0);
        } else if (clazzRemoteCallback.isInstance(parameter)) {
            data.writeInt(0);
            ReflectionUtil.invoke(parameter, "writeToParcel",data,0);

        } else if (parameter instanceof Parcelable) {
            data.writeInt(1);
            ((Parcelable) parameter).writeToParcel(data, 0);
        }
        return data;
    }


    //
    public Parcel invoke(String serviceName, String descriptor, String methodName, Object ... parameters)
    {
        return invoke(serviceName,descriptor,methodName,false,parameters);
    }
    //Parameter has Charsequence? instead of String
    public Parcel invokeCS(String serviceName, String descriptor, String methodName, Object ... parameters)
    {
        return invoke(serviceName,descriptor,methodName,true,parameters);
    }
    public Parcel invokeIBinder(IBinder ibinder, String descriptor, String methodName, Boolean useCharSequence, Object ... parameters)
    {
        return invoke(ibinder,descriptor,methodName,useCharSequence,parameters);
    }

    @SuppressLint("PrivateApi")
    private Parcel invoke(String serviceName, String descriptor, String methodName,boolean useCharSequence, Object ... parameters)
    {
        IBinder binder;
        try {
            binder = (IBinder) Class.forName("android.os.ServiceManager")
                    .getMethod("getService",String.class).invoke(null,serviceName);
            if(binder == null){
                throw new BypassTestException("The " + serviceName
                        + " service guarded by this permission is not available on this device");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return invoke(binder,descriptor,methodName,useCharSequence,parameters);
    }

    @SuppressLint("PrivateApi")
    private Parcel invoke(IBinder binder, String descriptor, String methodName, Boolean useCharSequence, Object ... parameters){
        try {
            int tId = BinderTransactsDict.getInstance().getTransactId(descriptor,methodName);
            Class<?> clazzRemoteCallback =  Class.forName("android.os.RemoteCallback");
            Parcel reply = Parcel.obtain();
            Parcel data  = Parcel.obtain();
            data.writeInterfaceToken(descriptor);

            for(Object parameter : parameters) {
               data =  handleBinderInput(data, parameter, useCharSequence, clazzRemoteCallback);
            }
            //Log.d("tag",">"+tId+":"+methodName);
            binder.transact(tId, data, reply, 0);
            //Log.d("tag",">"+tId+":"+reply.toString());
            reply.readException();
            return reply;
        } catch (RemoteException e) {
            if (e.getCause() != null && e.getCause() instanceof SecurityException) {
                throw (SecurityException)e.getCause();
            } else {
                throw new UnexpectedTestFailureException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new UnexpectedTestFailureException(e);

        } catch (ReflectionUtil.ReflectionIsTemporaryException e) {
            throw new UnexpectedTestFailureException(e);
        }
    }

    /**
     * Invokes a direct binder transact using the specified {@code intent} to bind to the service
     * within the provided {@code context}; the service's {@code transactName} method is invoked
     * with the provided {@code parameters} using the {@code descriptor} to look up the transact ID.
     *
     * <p>To facilitate invoking direct transacts for permission tests this method will check if
     * the {@link RemoteException} caught as a result of running the transact is a {@link
     * SecurityException}; if so the {@code SecurityException} is rethrown as is, otherwise an
     * {@link UnexpectedTestFailureException} is thrown.
     *
     * @return the {@link Parcel} received as a result of invoking the transact
     */
    public Parcel invokeViaServiceFromIntent(Context context, Intent intent,
                                                      String descriptor, String transactName, Object... parameters) {
        final CountDownLatch latch = new CountDownLatch(1);
        IBinder[] connectedBinder = new IBinder[1];
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder binder) {
                StaticLogger.debug("onServiceConnected: className = " + className);
                connectedBinder[0] = binder;
                latch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                StaticLogger.debug("onServiceDisconnected: componentName = " + componentName);
            }
        };
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        boolean connectionSuccessful = false;
        try {
            connectionSuccessful = latch.await(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            StaticLogger.error("Caught an InterruptedException waiting for the service from " + intent
                    + " to connect: ", e);
        }
        if (!connectionSuccessful) {
            throw new UnexpectedTestFailureException(
                    "Failed to connect to the service for descriptor " + descriptor);
        }
        return invoke(connectedBinder[0], descriptor, transactName, false,
                parameters);

    }
}
