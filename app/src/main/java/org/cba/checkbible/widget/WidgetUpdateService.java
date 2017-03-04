package org.cba.checkbible.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import org.cba.checkbible.PlanManager;
import org.cba.checkbible.R;
import org.cba.checkbible.constant.CheckBibleIntent;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;

/**
 * Created by jinhwan.na on 2016-12-08.
 */

public class WidgetUpdateService extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.check_bible_widget);
        PlanManager planManager = PlanManager.getInstance(this);
        planManager.resetTodayCount();
        planManager.initCount();
        // views.setTextViewText(R.id.appwidget_button,
        // PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        Log.e("checkbible", "widgetupdateservice");
        views.setTextViewText(R.id.appwidget_title,
                PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        views.setTextViewText(R.id.appwidget_chapter, planManager.getChapterString());
        views.setTextViewText(R.id.appwidget_today, planManager.getTodayString());
//        String endDate = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
//        endDate = endDate.replace("/", ".");
//        views.setTextViewText(R.id.appwidget_during, "~" + endDate);
        views.setTextColor(R.id.appwidget_title, getTextStatusColor(planManager));

        Intent countIntent = new Intent(CheckBibleIntent.ACTION_COUNT);
        PendingIntent countPIntent = PendingIntent.getBroadcast(this, 0, countIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_chapter, countPIntent);
        views.setOnClickPendingIntent(R.id.appwidget_today, countPIntent);
//        views.setOnClickPendingIntent(R.id.appwidget_during, countPIntent);

        Intent sIntent = new Intent(CheckBibleIntent.ACTION_START_MAIN_ACTIVITY);
        PendingIntent sPIntent = PendingIntent.getBroadcast(this, 0, sIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_title, sPIntent);


        ComponentName componentname = new ComponentName(this, CheckBibleWidget.class);
        AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(this);


        appwidgetmanager.updateAppWidget(componentname, views);
    }

    public int getTextStatusColor(PlanManager planManager) {
        int retValue;
        int percent = (int) (((double) PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT) / (double) PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);
        int totalPercent = 100 - (int) ((double) planManager.getDuringDay()
                / (double) planManager.getTotalDuringDay() * 100.0);
        if (percent < (totalPercent - 5)) {
            retValue = getResources().getColor(android.R.color.holo_red_dark);
            // red
        } else if (percent > (totalPercent + 5)) {
            retValue = getResources().getColor(android.R.color.holo_blue_dark);
            // blue
        } else {
            retValue = getResources().getColor(android.R.color.primary_text_dark);
        }
        return retValue;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
