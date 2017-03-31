package org.cba.checkbible.widget;

import android.app.Activity;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.cba.checkbible.R;

/**
 * The configuration screen for the {@link CheckBibleWidget CheckBibleWidget} AppWidget.
 */
public class CheckBibleWidgetConfigureActivity extends Activity {

    int mAppWidgetId;
    Context mContext;

    private Button mAddBtn;

    private RadioGroup mRadioGroup;
    private RadioButton mBlackBtn;
    private RadioButton mWhiteBtn;
    private RadioButton mGrayBtn;

    RemoteViews mRemoteView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.check_bible_widget_configure);
        mAddBtn = (Button) findViewById(R.id.add_button);
        mRadioGroup = (RadioGroup) findViewById(R.id.widget_radiogroup);
        mBlackBtn = (RadioButton) findViewById(R.id.widget_black_btn);
        mWhiteBtn = (RadioButton) findViewById(R.id.widget_white_btn);
        mGrayBtn = (RadioButton) findViewById(R.id.widget_gray_btn);

        mAddBtn.setOnClickListener(mOnClickListener);

        ImageView view = (ImageView)findViewById(R.id.imageView2);
        view.setImageDrawable(WallpaperManager.getInstance(this).getDrawable());

        mRemoteView = new RemoteViews(this.getPackageName(),
                R.layout.check_bible_widget);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int selectedId = mRadioGroup.getCheckedRadioButtonId();
            switch (selectedId) {
                case R.id.widget_black_btn:
                    Toast.makeText(mContext,"Black",Toast.LENGTH_SHORT).show();
                    mRemoteView.setInt(R.id.appwidget_layout, "setBackgroundColor", android.R.color.transparent);
                    mRemoteView.setTextColor(R.id.appwidget_title, Color.BLACK);
                    mRemoteView.setInt(R.id.appwidget_title, "setBackgroundColor", android.R.color.transparent);
                    mRemoteView.setTextColor(R.id.appwidget_chapter, Color.BLACK);
                    mRemoteView.setInt(R.id.appwidget_chapter, "setBackgroundColor", android.R.color.transparent);
                    mRemoteView.setTextColor(R.id.appwidget_today, Color.BLACK);
                    mRemoteView.setInt(R.id.appwidget_today, "setBackgroundColor", android.R.color.transparent);
                    break;
                case R.id.widget_white_btn:
                    mRemoteView.setTextColor(R.id.appwidget_title, Color.WHITE);
                    mRemoteView.setTextColor(R.id.appwidget_chapter, Color.WHITE);
                    mRemoteView.setTextColor(R.id.appwidget_today, Color.WHITE);
                    break;
                case R.id.widget_gray_btn:
                    break;
            }
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            appWidgetManager.updateAppWidget(mAppWidgetId, mRemoteView);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
}

