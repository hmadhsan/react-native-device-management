package com.reactlibrary;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceAdmin extends DeviceAdminReceiver {

    void showToast(Context context, CharSequence msg) {
        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        showToast(context, "");
        return "";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, "");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast(context, "");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "");
    }
}
