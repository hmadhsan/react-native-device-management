
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.telephony.TelephonyManager;
import android.content.Context;
import com.facebook.react.bridge.Promise;

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
}