package cba.org.checkbible.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.constant.CheckBibleIntent;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.widget.WidgetUpdateService;

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
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, 0);
            Log.e("checkbible", "resetTodaycount");
            Intent i = new Intent(context, WidgetUpdateService.class);
            context.startService(i);
            break;
        }
    }

}
