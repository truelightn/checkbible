package org.cba.checkbible.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import org.cba.checkbible.PlanManager;
import org.cba.checkbible.R;
import org.cba.checkbible.constant.CheckBibleIntent;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;
import org.cba.checkbible.db.Setting;
import org.cba.checkbible.db.SettingDBUtil;

/**
 * Created by jinhwan.na on 2016-12-08.
 */

public class WidgetUpdateService extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.check_bible_widget);

        String backGroundColor = SettingDBUtil.getSettingValue(Setting.BACKGROUND_COLOR);
        String textColor = SettingDBUtil.getSettingValue(Setting.TEXT_COLOR);

        PlanManager planManager = PlanManager.getInstance(this);
        planManager.resetTodayCount();
        planManager.initCount();
        Log.e("checkbible", "widgetupdateservice");
        views.setTextViewText(R.id.appwidget_title,
                PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        views.setTextViewText(R.id.appwidget_chapter, planManager.getChapterString());
        views.setTextViewText(R.id.appwidget_today, planManager.getTodayString());
//        String endDate = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
//        endDate = endDate.replace("/", ".");
//        views.setTextViewText(R.id.appwidget_during, "~" + endDate);

        //Set text Color
        views.setTextColor(R.id.appwidget_title, getTextStatusColor(planManager));
        if (!(textColor.isEmpty() || backGroundColor.isEmpty())) {
            views.setInt(R.id.appwidget_layout, "setBackgroundColor", Color.parseColor(backGroundColor));
            views.setInt(R.id.appwidget_title, "setBackgroundColor", Color.parseColor(backGroundColor));
            views.setTextColor(R.id.appwidget_chapter, Color.parseColor(textColor));
            views.setTextColor(R.id.appwidget_today, Color.parseColor(textColor));
        }


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
        long elapseDay = planManager.getTotalDuringDay() - planManager.getDuringDay();
        int totalPercent = (int) ((elapseDay * PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_COUNT) / (double) PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);

        if (percent < (totalPercent - 5)) {
            retValue = getResources().getColor(android.R.color.holo_red_dark);
            // red
        } else if (percent > (totalPercent + 5)) {
            retValue = getResources().getColor(android.R.color.holo_blue_dark);
            // blue
        } else {
            if (!SettingDBUtil.getSettingValue(Setting.TEXT_COLOR).isEmpty()) {
                retValue = Color.parseColor((SettingDBUtil.getSettingValue(Setting.TEXT_COLOR)));
            } else {
                retValue = getResources().getColor(R.color.colorTextWidget);
            }
        }
        return retValue;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
