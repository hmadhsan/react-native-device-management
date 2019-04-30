package com.reactlibrary;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.samsung.android.knox.EnterpriseDeviceManager;
import com.samsung.android.knox.application.ApplicationPolicy;
import com.samsung.android.knox.license.EnterpriseLicenseManager;
import com.samsung.android.knox.license.KnoxEnterpriseLicenseManager;
import com.samsung.android.knox.location.LocationPolicy;
import com.samsung.android.knox.restriction.PhoneRestrictionPolicy;
import com.samsung.android.knox.restriction.RestrictionPolicy;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RNMdmSamsungPolicies extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNMdmSamsungPolicies(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNSamsungMdm";
    }

    public void activateKnoxLicense(Context context, String licenseKey,String packageName) {
        try{
            KnoxEnterpriseLicenseManager knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(context);
            knoxEnterpriseLicenseManager.activateLicense(licenseKey,packageName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void activateBackwardLicense(Context context, String licenseKey) {

        try{
            EnterpriseLicenseManager enterpriseLicenseManager = EnterpriseLicenseManager.getInstance(context);
            enterpriseLicenseManager.activateLicense(licenseKey);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @ReactMethod
    public void deactivateKey(Promise promise,String key){
        try {
            KnoxEnterpriseLicenseManager knoxEnterpriseLicenseManager = KnoxEnterpriseLicenseManager.getInstance(reactContext);
            knoxEnterpriseLicenseManager.deActivateLicense(key);
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve(false);
        }
    }

    @ReactMethod
    public void isSamsungPoliciesEnabled(Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            RestrictionPolicy restrictionPolicy = mEDM.getRestrictionPolicy();
            restrictionPolicy.setCameraState(true);
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve(false);
        }
    }

    public boolean isKnoxSdkSupported() {
        try {
            EnterpriseDeviceManager.getAPILevel();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public boolean isMdmApiSupported(int requiredVersion) {
//        try {
//            return EnterpriseDeviceManager.getAPILevel() < requiredVersion;
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @ReactMethod
    public void enableSamsungPolicies(String packageName, String elmLicenseKey, String backwardKey,Promise promise) {
        try {
            // isKnoxSdkSupported();

            this.activateBackwardLicense(this.reactContext,backwardKey);
            this.activateKnoxLicense(this.reactContext,elmLicenseKey,packageName);

            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableGps(boolean gpsMandatory, Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            LocationPolicy locationPolicy = mEDM.getLocationPolicy();
            if (gpsMandatory && !locationPolicy.getLocationProviderState("gps")) {
                locationPolicy.startGPS(true);
            }
            locationPolicy.setGPSStateChangeAllowed(!gpsMandatory);
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableAndroidBrowser(boolean isAndroidBrowserDisabled, Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            if (isAndroidBrowserDisabled) {
                appPolicy.disableAndroidBrowser();
            } else {
                appPolicy.enableAndroidBrowser();
            }
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisablePlayStore(boolean isPlayStoreDisabled, Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            if (isPlayStoreDisabled) {
                appPolicy.disableAndroidMarket();
            } else {
                appPolicy.enableAndroidMarket();
            }
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableYouTube(boolean isYouTubeDisabled, Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            if (isYouTubeDisabled) {
                appPolicy.disableYouTube();
            } else {
                appPolicy.enableYouTube();
            }
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void unInstallApp(String appsToBeUninstalled, Promise promise) {
        try {
            JSONArray appsToBeUninstalledList = new JSONArray(appsToBeUninstalled);
            List<String> appList = new ArrayList<>();
            for (int index = 0; index < appsToBeUninstalledList.length(); index++) {
                appList.add(appsToBeUninstalledList.getString(index));
            }
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            appPolicy.uninstallApplications(appList);
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void blackListApps(String appsToBeBlackListed, Promise promise) {
        try {
            boolean result = false;
            JSONArray appsToBeBlackListedList = new JSONArray(appsToBeBlackListed);
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            appPolicy.clearAppPackageNameFromList();
            for (int index = 0; index < appsToBeBlackListedList.length(); index++) {
                result = appPolicy.addAppPackageNameToBlackList(appsToBeBlackListedList.getString(index));
                result = appPolicy.setDisableApplication(appsToBeBlackListedList.getString(index));
            }
            promise.resolve(true);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void keepApplicationAlive(String packageName, boolean isForceStopEnabled, Promise promise) {
        try {
            boolean result = false;
            List packageList = new ArrayList();
            packageList.add(packageName);
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            if (isForceStopEnabled) {
                result = appPolicy.addPackagesToForceStopBlackList(packageList);
            } else {
                result = appPolicy.removePackagesFromForceStopBlackList(packageList);
            }
            promise.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableNotification(String packageName, boolean isNotificationDisabled, Promise promise) {
        try {
            boolean result = false;
            List packageList = new ArrayList();
            packageList.add(packageName);
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            ApplicationPolicy appPolicy = mEDM.getApplicationPolicy();
            if (isNotificationDisabled) {
                result = appPolicy.addPackagesToNotificationWhiteList(packageList);
            } else {
                result = appPolicy.removePackagesFromNotificationWhiteList(packageList);
            }
            promise.resolve(result);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableSetting(boolean isSettingChangeDisabled, Promise promise) {
        try {
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            RestrictionPolicy restrictionPolicy = mEDM.getRestrictionPolicy();
            if (isSettingChangeDisabled) {
                restrictionPolicy.allowSettingsChanges(false);
            } else {
                restrictionPolicy.allowSettingsChanges(true);
            }
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableOutgoingCall(boolean isOutgoingCallDisabled, Promise promise) {
        try {
            boolean result = false;
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            PhoneRestrictionPolicy phoneRestrictionPolicy = mEDM.getPhoneRestrictionPolicy();
            if (isOutgoingCallDisabled) {
                phoneRestrictionPolicy.removeOutgoingCallExceptionPattern();
                phoneRestrictionPolicy.removeOutgoingCallRestriction();
                result = phoneRestrictionPolicy.addOutgoingCallRestriction(".*");
            } else {
                phoneRestrictionPolicy.removeOutgoingCallRestriction();
                result = phoneRestrictionPolicy.removeOutgoingCallExceptionPattern();
            }
            promise.resolve(result);
        } catch (Exception e) {
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void enableOrDisableOutgoingSMS(boolean isOutgoingSMSDisabled, Promise promise) {
        try {
            boolean result = false;
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            PhoneRestrictionPolicy phoneRestrictionPolicy = mEDM.getPhoneRestrictionPolicy();
            if (isOutgoingSMSDisabled) {
                phoneRestrictionPolicy.removeOutgoingSmsExceptionPattern();
                phoneRestrictionPolicy.removeOutgoingSmsRestriction();
                result = phoneRestrictionPolicy.allowOutgoingSms(false);
            } else {
                phoneRestrictionPolicy.removeOutgoingCallRestriction();
                result = phoneRestrictionPolicy.removeOutgoingCallExceptionPattern();
            }
            promise.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

    @ReactMethod
    public void setOutgoingCallLimit(int limit, Promise promise) {
        try {
            boolean result = false;
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            PhoneRestrictionPolicy phoneRestrictionPolicy = mEDM.getPhoneRestrictionPolicy();
            result = phoneRestrictionPolicy.enableLimitNumberOfCalls(true);
            if (result) {
                result = phoneRestrictionPolicy.setLimitOfOutgoingCalls(limit, 0, 0);
            }
            promise.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }


    }

    @ReactMethod
    public void setOutgoingSMSLimit(int limit, Promise promise) {
        try {
            boolean result = false;
            EnterpriseDeviceManager mEDM = EnterpriseDeviceManager.getInstance(reactContext);
            PhoneRestrictionPolicy phoneRestrictionPolicy = mEDM.getPhoneRestrictionPolicy();
            result = phoneRestrictionPolicy.enableLimitNumberOfSms(true);
            if (result) {
                result = phoneRestrictionPolicy.setLimitOfOutgoingSms(limit, 0, 0);
            }
            promise.resolve(result);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject(e.getMessage());
        }
    }

}
