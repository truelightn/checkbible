package org.cba.checkbible.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.cba.checkbible.PlanManager;
import org.cba.checkbible.constant.CheckBibleIntent;
import org.cba.checkbible.widget.WidgetUpdateService;

/**
 * Created by jinhwan.na on 2016-12-13.
 */

public class CheckBibleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return; // Exception
        }
        String action = intent.getAction();
        switch (action) {
        case Intent.ACTION_BOOT_COMPLETED:
            PlanManager.getInstance(context).setAlarm(context);
            break;
        case CheckBibleIntent.ACITON_RESET_TODAY:
            PlanManager.getInstance(context).resetTodayCount();
            Intent i = new Intent(context, WidgetUpdateService.class);
            context.startService(i);
            break;
        }
    }

}
