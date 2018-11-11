package com.reactlibrary;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class ApplicationToFrontService extends IntentService {


    int callBackSeconds = 5;
    Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public ApplicationToFrontService() {
        super("ApplicationToFrontService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String packageName = "";
        context = this;
        if (bundle != null) {
            packageName = bundle.getString("packageName");
        }
        if (isApplicationBroughtToBackground(packageName)) {
            startApplication(packageName);
        }
        setWakeupAlarm(packageName);
    }

    public void startApplication(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isApplicationBroughtToBackground(String packageName) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            if (runningProcesses == null) {
                return true;
            }
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.processName.equals(packageName) && processInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
            return false;
        } else {
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks.isEmpty()) {
                return false;
            }
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(packageName)) {
                return true;
            }
            return false;
        }
    }

    private void setWakeupAlarm(String packageName) {
        Intent intent = new Intent(this, ApplicationToFrontService.class);
        intent.putExtra("packageName", packageName);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, callBackSeconds);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
