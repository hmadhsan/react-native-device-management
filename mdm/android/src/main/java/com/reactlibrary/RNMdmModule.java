
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.telephony.TelephonyManager;
import android.content.Context;
import com.facebook.react.bridge.Promise;

import android.app.admin.DevicePolicyManager;
import android.app.enterprise.EnterpriseDeviceManager;
import android.app.enterprise.LocationPolicy;

public class RNMdmModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNMdmModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNMdm";
  }

  @ReactMethod
  public void getIMEI(Promise promise) {
    try {
      TelephonyManager tm = (TelephonyManager) this.reactContext.getSystemService(Context.TELEPHONY_SERVICE);
      promise.resolve(tm.getDeviceId());
    } catch (Exception e) {
      promise.reject(e.getMessage());
    }
  }

  @ReactMethod
  public void isAdminActive(Promise promise) {
    try {
      DevicePolicyManager mDPM = (DevicePolicyManager) this.reactContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
      ComponentName mDeviceAdmin = new ComponentName(this, RNMdmModule.class);
      boolean IS_DEVICE_ADMIN_ACTIVE = mDPM.isAdminActive(mDeviceAdmin);
      promise.resolve(IS_DEVICE_ADMIN_ACTIVE);
    } catch (Exception e) {
      promise.reject(e.getMessage());
    }
  }
}