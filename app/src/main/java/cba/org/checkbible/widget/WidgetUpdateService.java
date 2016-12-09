package cba.org.checkbible.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;

/**
 * Created by jinhwan.na on 2016-12-08.
 */

public class WidgetUpdateService extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.check_bible_widget);
        PlanManager planManager = PlanManager.getInstance(this);
        planManager.initCount();
//        views.setTextViewText(R.id.appwidget_title,
//                PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        views.setTextViewText(R.id.appwidget_button, PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        views.setTextViewText(R.id.appwidget_chapter, planManager.getChapterString());
        views.setTextViewText(R.id.appwidget_today, planManager.getTodayString());
//        views.setTextViewText(R.id.appwidget_during, planManager.getDuringString());
        views.setTextColor(R.id.appwidget_button, getTextStatusColor(planManager));
        ComponentName componentname = new ComponentName(this, CheckBibleWidget.class);
        AppWidgetManager appwidgetmanager = AppWidgetManager.getInstance(this);

        appwidgetmanager.updateAppWidget(componentname, views);
    }

    public int getTextStatusColor(PlanManager planManager) {
        int retValue;
        int percent = (int)(((double)PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT) / (double)PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);
        int totalPercent = 100 - (int)((double)planManager.getDuringDay()
                / (double)planManager.getTotalDuringDay() * 100.0);
        if (percent < (totalPercent - 5)) {
            retValue = Color.RED;
            // red
        } else if (percent > (totalPercent + 5)) {
            retValue = Color.BLUE;
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
