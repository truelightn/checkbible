package org.cba.checkbible.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cba.checkbible.PlanManager;
import org.cba.checkbible.R;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;
import org.cba.checkbible.widget.WidgetUpdateService;

/**
 * Created by jinhwan.na on 2017-01-09.
 */

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private LinearLayout mLinearLayout;
    private WindowManager.LayoutParams mParams;
    PlanManager mPlanManager;
    TextView mTitle;
    TextView mChapter;
    TextView mToday;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlanManager = PlanManager.getInstance(this);

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mParams.gravity = Gravity.LEFT | Gravity.BOTTOM;

        mLinearLayout = new LinearLayout(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = layoutInflater.inflate(R.layout.check_bible_widget, mLinearLayout);

        mTitle= (TextView) layoutView.findViewById(R.id.appwidget_title);
        mChapter = (TextView) layoutView.findViewById(R.id.appwidget_chapter);
        mToday = (TextView) layoutView.findViewById(R.id.appwidget_today);
        mTitle.setText(PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        mChapter.setText(mPlanManager.getChapterString());
        mToday.setText(mPlanManager.getTodayString());


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mLinearLayout, mParams);

        mTitle.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mParams.x;
                        initialY = mParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        Log.e("checkbible", "ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("checkbible", "ACTION_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        mParams.y = initialY + (int) (initialTouchY - event.getRawY());

                        mWindowManager.updateViewLayout(mLinearLayout, mParams);
                        return true;
                }
                return false; // onTouchListener 이후에 다른 Listener들이 동작하게함
            }
        });

        mChapter.setOnClickListener(onClickListener);
        mToday.setOnClickListener(onClickListener);
        mTitle.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlanManager.isComplete()) {
                return;
            }
            switch (v.getId()) {
                case R.id.appwidget_chapter:
                case R.id.appwidget_today:
                    Log.e("checkbible", "selected view");
                    mPlanManager.increaseCount(1);
                    mChapter.setText(mPlanManager.getChapterString());
                    mToday.setText(mPlanManager.getTodayString());
                    break;
                case R.id.appwidget_title:
//                    mLinearLayout = new LinearLayout(mContext);
//                    LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View layoutView = layoutInflater.inflate(R.layout.check_bible_widget, mLinearLayout);
//                    mTitle= (TextView) layoutView.findViewById(R.id.appwidget_title);
//                    mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                    mWindowManager.addView(mLinearLayout, mParams);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mWindowManager.removeView(this.mLinearLayout);
        Intent i = new Intent(this, WidgetUpdateService.class);
        this.startService(i);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
