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

/**
 * Created by jinhwan.na on 2017-01-09.
 */

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
    private LinearLayout mLinearLayout;
    PlanManager mPlanManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlanManager = PlanManager.getInstance(this);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.BOTTOM;

        mLinearLayout = new LinearLayout(this);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// Here is the place where you can inject whatever layout you want.
        View layoutView = layoutInflater.inflate(R.layout.check_bible_widget, mLinearLayout);

        TextView title = (TextView) layoutView.findViewById(R.id.appwidget_title);
        TextView chapter = (TextView) layoutView.findViewById(R.id.appwidget_chapter);
        TextView today = (TextView) layoutView.findViewById(R.id.appwidget_today);
        title.setText(PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        chapter.setText(mPlanManager.getChapterString());
        today.setText(mPlanManager.getTodayString());


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mLinearLayout, params);


        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                            Log.e("checkbible", "onClick");
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
//                            mPlanManager.increaseCount(
//                                    mPlanManager.calculateTodayCount());
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mLinearLayout, params);
                        break;
                }
                return true;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mWindowManager.removeView(this.mLinearLayout);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
