
package com.reactlibrary;

import android.app.Activity;
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

public class RNMdmModule extends ReactContextBaseJavaModule implements ActivityEventListener {

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
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Fareye enabling Device Admin privelage.");
            currentActivity.startActivityForResult(intent, deviceAdminRequestCode);
            mdmPromises.put(deviceAdminRequestCode, promise);
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

    public boolean isOSAndModelValidForMDM() {
        if (Build.MANUFACTURER.contains("samsung") && Build.VERSION.SDK_INT >= 17) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Promise promise = mdmPromises.get(requestCode);
        if (requestCode == deviceAdminRequestCode) {
            if (promise != null && resultCode == Activity.RESULT_OK) {
                promise.resolve("Accepted");
            } else {
                promise.reject("Not Accepted");
            }
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        getReactApplicationContext().addActivityEventListener(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        getReactApplicationContext().removeActivityEventListener(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        /* Do nothing */
    }
}