package com.android.certifications.niap.permissions.transactids;

import static com.android.certifications.niap.permissions.transactids.Transacts.*;//TRANSACT_PREFIX;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProxyChecker {
    private static final String TAG="tag";
    private static final Map<String,String> alt_descriptor= new HashMap<String,String>();


    static  {
        alt_descriptor.put("android.media.IAudioService","AUDIO_DESCRIPTOR");
        alt_descriptor.put("android.hardware.display.IDisplayManager","DISPLAY_DESCRIPTOR");
        alt_descriptor.put("android.app.IActivityManager","ACTIVITY_DESCRIPTOR");
        alt_descriptor.put("android.content.pm.IPackageManager","PACKAGE_DESCRIPTOR");
        alt_descriptor.put("android.content.IClipboard","CLIPBOARD_DESCRIPTOR");
        alt_descriptor.put("android.hardware.input.IInputManager","INPUT_DESCRIPTOR");
        alt_descriptor.put("android.os.IPowerManager","POWER_DESCRIPTOR");
        alt_descriptor.put("com.android.internal.telephony.ITelephony","TELEPHONY_DESCRIPTOR");
        alt_descriptor.put("android.app.role.IRoleManager","ROLE_DESCRIPTOR");
        alt_descriptor.put("android.os.IStatsManagerService","STATS_DESCRIPTOR");
        alt_descriptor.put("android.health.connect.aidl.IHealthConnectService", "HEALTH_CONNECT_DESCRIPTOR");
        alt_descriptor.put("android.credentials.ICredentialManager","CREDENTIAL_DESCRIPTOR");
        alt_descriptor.put("com.android.internal.view.IInputMethodManager","INPUTMETHOD_DESCRIPTOR");
        alt_descriptor.put("android.app.wearable.IWearableSensingManager","WEARABLES_DESCRIPTOR");
        alt_descriptor.put("com.android.internal.telephony.ISub","SUBSCRIPTION_DESCRIPTOR");
        alt_descriptor.put("android.devicelock.IDeviceLockService","DEVICELOCK_DESCRIPTOR");
        alt_descriptor.put("android.app.ILocaleManager","LOCALE_DESCRIPTOR");
        alt_descriptor.put("android.app.admin.IDevicePolicyManager","DEVICE_POLICY_DESCRIPTOR" );
        alt_descriptor.put(LOCK_SETTINGS_DESCRIPTOR,"LOCK_SETTINGS_DESCRIPTOR" );
        alt_descriptor.put("android.graphicsenv.IGpuService","GPU_DESCRIPTOR");
        alt_descriptor.put(WINDOW_DESCRIPTOR,"WINDOW_DESCRIPTOR");
        alt_descriptor.put(EUICC_CONTROLLER_DESCRIPTOR,"EUICC_CONTROLLER_DESCRIPTOR");
        alt_descriptor.put(PDB_DESCRIPTOR,"PDB_DESCRIPTOR");
        alt_descriptor.put(SYSTEM_CONFIG_DESCRIPTOR,"SYSTEM_CONFIG_DESCRIPTOR");

        //alt_descriptor.put("com.android.internal.widget.ILockSettings","LOCK_SETTINGS_DESCRIPTOR");
        for(String k:alt_descriptor.keySet()){
            System.out.printf("public static final String %s = \"%s\";%n",
                    alt_descriptor.get(k),
                    k);
        }
    }
    private static Class descriptor(String desc){
        Class serviceProxy = null;
        try {
            serviceProxy = Class.forName(desc + "$Stub");
        } catch (ClassNotFoundException e) {
            return null;
        }
        return serviceProxy;
    }

    private static String printMethod(Method m){


        String returnType = m.getReturnType().getTypeName();
        StringBuilder method = new StringBuilder(returnType);
        method.append(" ");
        method.append(m.getName());
        method.append("(");

        Class<?>[] types = m.getParameterTypes();
        for(Class<?> t:types) {
            method.append(" ").append(t.getTypeName());
        }
        return method.append(")").toString();
    }

    public static List<String> checkDeclaredMethod(Class<?> clazz, final String f){
        List<String> a = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method m : methods){
            a.add(printMethod(m));
        }
        return a.stream().filter(str->str.startsWith(f)).collect(Collectors.toList());
    }
    public static List<String> checkDeclaredField(Class<?> clazz, final String f){
        List<String> a = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field ff:fields){
            a.add(ff.toString());
        }
        return a.stream().filter(str->str.startsWith(f)).collect(Collectors.toList());
    }
    public static boolean check(String descriptor,String transactName){
        Class clazz = null;
        //Log.d(TAG,"Checking :"+descriptor+","+transactName);
        try {
            //clazz = Class.forName(descriptor);
            clazz = Class.forName(descriptor + "$Stub");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //Log.d(TAG,"Found :"+clazz);
        try {
            Field transactField = clazz.getDeclaredField(TRANSACT_PREFIX + transactName);
            //Log.d(TAG,"Found " + transactField);
            transactField.setAccessible(true);
            /*Field[] fields = clazz.getDeclaredFields();
            Method[] methods = clazz.getDeclaredMethods();
            System.out.println(fields);
            System.out.println(methods);*/
            int transactId = (int) transactField.get(null);
            System.out.printf("public static final String %s = \"%s\";%n",
                    transactName,transactName);
            System.out.printf("queryTransactId(Transacts.%s, Transacts.%s, descriptorTransacts);%n",
                    alt_descriptor.get(descriptor),
                    transactName);

            //Log.d(TAG,"Found " + transactName + "=" + transactId);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            //Log.d(TAG,"Not Found " + transactName );
            // Exceptions can be expected when this tool is run on a device at an API level that
            // does not support a service / transact method being queried; the Permission Test
            // Tool will use the appropriate transact based on the API level. However the
            // exception is included here so that it can be logged in the resulting java source
            // below for debugging purposes.
            return false;
        }
        return true;
    }

}
