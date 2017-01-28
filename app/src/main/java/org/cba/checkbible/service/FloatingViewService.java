package org.cba.checkbible.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cba.checkbible.R;

/**
 * Created by jinhwan.na on 2017-01-09.
 */

public class FloatingViewService extends Service {
    private WindowManager mWindowManager;
//    private LinearLayout mLinearLayout;

    private TextView mPopupView;

    @Override
    public void onCreate() {
        super.onCreate();


        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.LEFT | Gravity.BOTTOM;

//        mLinearLayout = new LinearLayout(this);
        mPopupView = new TextView(this);                                         //뷰 생성
        mPopupView.setText("이 뷰는 항상 위에 있다.");                        //텍스트 설정
        mPopupView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18); //텍스트 크기 18sp
        mPopupView.setTextColor(Color.BLUE);//글자 색상

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mPopupView, params);

//        mWindowManager.addView(mLinearLayout, params);

//        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// Here is the place where you can inject whatever layout you want.
//        layoutInflater.inflate(R.layout.check_bible_widget, mLinearLayout);

        mPopupView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(mPopupView, params);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mWindowManager.removeView(this.mPopupView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
