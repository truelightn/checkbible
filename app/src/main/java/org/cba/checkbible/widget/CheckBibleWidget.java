package org.cba.checkbible.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.cba.checkbible.PlanManager;
import org.cba.checkbible.R;
import org.cba.checkbible.activity.MainActivity;
import org.cba.checkbible.constant.CheckBibleIntent;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;

/**
 * Implementation of App Widget functionality. App Widget Configuration
 * implemented in {@link CheckBibleWidgetConfigureActivity
 * CheckBibleWidgetConfigureActivity}
 */
public class CheckBibleWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // CharSequence widgetText =
        // CheckBibleWidgetConfigureActivity.loadTitlePref(context,
        // appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.check_bible_widget);
        Intent intent = new Intent(context, WidgetUpdateService.class);
        context.startService(intent);

        Intent countIntent = new Intent(CheckBibleIntent.ACTION_COUNT);
        PendingIntent countPIntent = PendingIntent.getBroadcast(context, 0, countIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_chapter, countPIntent);
        views.setOnClickPendingIntent(R.id.appwidget_today, countPIntent);
//        views.setOnClickPendingIntent(R.id.appwidget_during, countPIntent);

        Intent sIntent = new Intent(CheckBibleIntent.ACTION_START_MAIN_ACTIVITY);
        PendingIntent sPIntent = PendingIntent.getBroadcast(context, 0, sIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_title, sPIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated
        // with it.
        for (int appWidgetId : appWidgetIds) {
            // CheckBibleWidgetConfigureActivity.deleteTitlePref(context,
            // appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action) {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                break;
            case CheckBibleIntent.ACTION_START_MAIN_ACTIVITY:
                MainActivity.start(context);
                break;
            case CheckBibleIntent.ACTION_COUNT:
                int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                PlanManager.getInstance(context).increaseCount(
                        PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_COUNT));
                Intent i = new Intent(context, WidgetUpdateService.class);
                context.startService(i);
                break;
            default:
                break;
        }
    }
}
