package org.cba.checkbible.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import org.cba.checkbible.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jinhwan.na on 2017-01-05.
 * http://stackoverflow.com/questions/3873659/android-how-can-i-get-the-current-foreground-activity-from-a-service?lq=1
 */

public class DetectService extends AccessibilityService {
    List<String> mResourceList;

    private boolean isStartChurch;
    private boolean isStopChurch;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                String dectedPackage = event.getPackageName().toString();
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );
                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {
                    String packageName = componentName.getPackageName().toString();
                    Log.i("checkbible", packageName);
                    boolean ischurch = mResourceList.contains(packageName);
                    if (ischurch && !isStartChurch) {
                        isStartChurch = true;
                        isStopChurch = false;
                        Log.i("checkbible", "start Floating service");
                        startService(new Intent(getApplicationContext(), FloatingViewService.class));
                    } else if(!isStopChurch){
                        isStartChurch = false;
                        isStopChurch = true;
                        Log.i("checkbible", "stop Floating service");
                        stopService(new Intent(getApplicationContext(), FloatingViewService.class));
                    }

                }
            }
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        isStartChurch = false;
        isStopChurch = true;
        mResourceList = Arrays.asList(getResources().getStringArray(R.array.church_list));
        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }
}
