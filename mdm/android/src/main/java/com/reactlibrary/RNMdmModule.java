
package com.reactlibrary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;

public class RNMdmModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private final HashMap<Integer, Promise> mdmPromises;
    private final int deviceAdminRequestCode;

    public RNMdmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mdmPromises = new HashMap<Integer, Promise>();
        deviceAdminRequestCode = 1;
    }

    @Override
    public String getName() {
        return "RNMdm";
    }

    @ReactMethod
    public void isAdminActive(Promise promise) {
        try {
            DevicePolicyManager mDPM = (DevicePolicyManager) this.reactContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mDeviceAdmin = new ComponentName(this.reactContext, DeviceAdmin.class);
            boolean IS_DEVICE_ADMIN_ACTIVE = mDPM.isAdminActive(mDeviceAdmin);
            promise.resolve(IS_DEVICE_ADMIN_ACTIVE);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableDeviceAdmin(Promise promise) {
        try {
            ComponentName mDeviceAdmin = new ComponentName(this.reactContext, DeviceAdmin.class);
            Activity currentActivity = getCurrentActivity();
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enabling Device Admin privilege.");
            currentActivity.startActivityForResult(intent, deviceAdminRequestCode);
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void disableDeviceAdmin(Promise promise) {
        try {
            DevicePolicyManager mDPM = (DevicePolicyManager) this.reactContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName mDeviceAdmin = new ComponentName(this.reactContext, DeviceAdmin.class);
            boolean active = mDPM.isAdminActive(mDeviceAdmin);
            if (active) {
                mDPM.removeActiveAdmin(mDeviceAdmin);
            }
            promise.resolve("Success");
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void startOrStopApplicationAliveService(boolean isStart, String packageName, Promise promise) {
        try {
            Intent intent = new Intent(this.reactContext, ApplicationToFrontService.class);
            intent.putExtra("packageName", packageName);
            if (isStart) {
                this.reactContext.startService(intent);
            } else {
                this.reactContext.stopService(intent);
                cancelAlarm();
            }
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void isOSAndModelValidForMDM(Promise promise) {
        if (Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= 17) {
            promise.resolve(true);
        } else {
            promise.resolve(false);
        }
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this.reactContext, ApplicationToFrontService.class);
        PendingIntent sender = PendingIntent.getService(this.reactContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.reactContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(sender);
        }
    }
}