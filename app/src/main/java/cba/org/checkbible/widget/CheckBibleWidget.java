package cba.org.checkbible.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.activity.MainActivity;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;

/**
 * Implementation of App Widget functionality. App Widget Configuration
 * implemented in {@link CheckBibleWidgetConfigureActivity
 * CheckBibleWidgetConfigureActivity}
 */
public class CheckBibleWidget extends AppWidgetProvider {

    private static final String ACTION_COUNT = "org.cba.checkbible.COUNT";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // CharSequence widgetText =
        // CheckBibleWidgetConfigureActivity.loadTitlePref(context,
        // appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.check_bible_widget);

        Intent intent = new Intent(context, WidgetUpdateService.class);
        context.startService(intent);

        Intent countIntent = new Intent(ACTION_COUNT);
        PendingIntent countPIntent = PendingIntent.getBroadcast(context, 0, countIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_title, countPIntent);

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
        case ACTION_COUNT:
            int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            PlanManager.getInstance(context).increaseCount(
                    PlanManager.getInstance(context).calculateTodayCount());
            Intent i = new Intent(context, WidgetUpdateService.class);
            context.startService(i);
            break;
        default:
            break;
        }
    }
}
