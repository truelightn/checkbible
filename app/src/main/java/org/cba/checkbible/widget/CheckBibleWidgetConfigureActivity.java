package org.cba.checkbible.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.cba.checkbible.R;
import org.cba.checkbible.db.DBUtil;
import org.cba.checkbible.db.Setting;
import org.cba.checkbible.db.SettingDBUtil;

/**
 * The configuration screen for the {@link CheckBibleWidget CheckBibleWidget} AppWidget.
 */
public class CheckBibleWidgetConfigureActivity extends Activity {

    int mAppWidgetId;
    Context mContext;

    private Button mAddBtn;
    private RadioGroup mRadioGroup;
    private ImageView mImageView;

    RemoteViews mRemoteView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
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

        setContentView(R.layout.check_bible_widget_configure);
        mAddBtn = (Button) findViewById(R.id.add_button);
        mRadioGroup = (RadioGroup) findViewById(R.id.widget_radiogroup);
        mImageView = (ImageView) findViewById(R.id.preImageView);
        mAddBtn.setOnClickListener(mOnClickListener);

        mRemoteView = new RemoteViews(this.getPackageName(),
                R.layout.check_bible_widget);


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.widget_black_btn:
                        mImageView.setImageResource(R.drawable.widget_black);
                        break;
                    case R.id.widget_white_btn:
                        mImageView.setImageResource(R.drawable.widget_white);
                        break;
                    case R.id.widget_gray_btn:
                        mImageView.setImageResource(R.drawable.widget_gray);
                        break;

                }
            }
        });
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        String backGroundColor = "";
        String textColor = "";

        public void onClick(View v) {
            int selectedId = mRadioGroup.getCheckedRadioButtonId();
            switch (selectedId) {
                case R.id.widget_black_btn:
                    Toast.makeText(mContext, "Black", Toast.LENGTH_SHORT).show();
                    backGroundColor = "#00000000";
                    textColor = "#FF000000";
                    break;
                case R.id.widget_white_btn:
                    backGroundColor = "#00000000";
                    textColor = "#FFFFFFFF";
                    break;
                case R.id.widget_gray_btn:
                    backGroundColor = "";
                    textColor = "";
                    break;
            }

            SettingDBUtil.setSettingValue(Setting.BACKGROUND_COLOR, backGroundColor);
            SettingDBUtil.setSettingValue(Setting.TEXT_COLOR, textColor);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            CheckBibleWidget.updateAppWidget(mContext, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();

        }
    };


}

