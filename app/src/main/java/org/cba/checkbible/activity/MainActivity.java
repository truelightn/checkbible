package org.cba.checkbible.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import org.cba.checkbible.CheckBibleApp;
import org.cba.checkbible.PlanManager;
import org.cba.checkbible.R;
import org.cba.checkbible.afw.V;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;
import org.cba.checkbible.service.DetectService;
import org.cba.checkbible.widget.WidgetUpdateService;

public class MainActivity extends AppCompatActivity {
    private Button mPlusBtn;
    private Button mMinusBtn;
    private Button mCustomBtn;

    private TextView mTitleTextView;
    private TextView mTodayTextView;
    private TextView mTotalTextView;
    private TextView mChaterTextView;
    private TextView mDuringTextView;

    private ProgressBar mProgress;
    private LinearLayout mLayout;

    private GridView mAbbGridView;

    private PlanManager mPlanManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlanManager = PlanManager.getInstance(this);
        initialView();
        mPlanManager.resetTodayCount();
        if (PlanDBUtil.hasNoPlanedData()) {
            mPlanManager.setAlarm(this);
            PlanActivity.start(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(this, WidgetUpdateService.class);
        this.startService(i);
    }

    public void initialView() {

        mLayout = V.get(this, R.id.layout);

        mMinusBtn = V.get(this, R.id.minus_btn);
        mPlusBtn = V.get(this, R.id.plus_btn);
        mCustomBtn = V.get(this, R.id.today_btn);

        mTitleTextView = V.get(this, R.id.title);
        mChaterTextView = V.get(this, R.id.chapter);

        mTodayTextView = V.get(this, R.id.today);
        mTotalTextView = V.get(this, R.id.total);

        mProgress = V.get(this, R.id.progressBar);
        mDuringTextView = V.get(this, R.id.during);

        mAbbGridView = V.get(this, R.id.abb_gridview);

        mMinusBtn.setOnClickListener(onClickListener);
        mPlusBtn.setOnClickListener(onClickListener);
        mCustomBtn.setOnClickListener(onClickListener);
        mCustomBtn.setOnLongClickListener(onLongClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
        mPlanManager.initCount();
    }

    public void refreshView() {
        // setTitle
        if (!PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE).isEmpty()) {
            mTitleTextView.setText(PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        }

        // setChapter ex:신5장/34장
        mChaterTextView.setText(mPlanManager.getChapterString());

        // setToday ex: 3장/10장
        mTodayTextView.setText(mPlanManager.getTodayString());

        // setTotal ex: 34장/145장
        mTotalTextView.setText(mPlanManager.getTotalString());

        // set progress
        setProgress();

        // set during ex: 16.10.16~16.12.31
        mDuringTextView.setText(mPlanManager.getDuringString());

        // Today butn ex: today(3)
        mCustomBtn.setText("+ " + PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TODAY_COUNT));
        mCustomBtn.setTextSize(15);

        // set chaptergrid
        ArrayList<Integer> totalList = new ArrayList<>();
        totalList.addAll(PlanDBUtil.getCompleteChapterPosition(0));
        totalList.addAll(PlanDBUtil.getPlanedChapterPosition(0));
        AbbChapterAdapter addAdapter = new AbbChapterAdapter(this, 0);
        mAbbGridView.setAdapter(addAdapter);
        if (totalList.size() > 6) {
            mAbbGridView.setNumColumns(6);
        } else {
            mAbbGridView.setNumColumns(totalList.size());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean bchange = false;
        if (Settings.canDrawOverlays(this) && isAccessibilitySettingsOn(CheckBibleApp.getContext())) {
            bchange = true;
        }
        if (bchange) {
            menu.findItem(R.id.add_floating).setIcon(R.drawable.ic_bookmark_black_24dp);
        } else {
            menu.findItem(R.id.add_floating).setIcon(R.drawable.ic_bookmark_border_black_24dp);

        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.add_floating:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getOverlayDrawPermission();
                }
                break;
            case R.id.menu_reading_plan:
                PlanActivity.start(this);
                break;
            case R.id.menu_logs:
                LogActivity.start(this);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlanManager.isComplete()) {
                Toast.makeText(MainActivity.this, "모두 읽었습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                case R.id.minus_btn:
                    mPlanManager.decreaseCount(1);
                    refreshView();
                    break;

                case R.id.plus_btn:
                    mPlanManager.increaseCount(1);
                    refreshView();
                    break;
                case R.id.today_btn:
                    mPlanManager.increaseCount(PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_COUNT));
                    refreshView();
                    break;
                default:
                    break;
            }
        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.minus_btn:
                    break;
                case R.id.plus_btn:
                    break;
                case R.id.today_btn:

                    break;
                default:
                    break;
            }
            return false;
        }
    };

    public void setProgress() {
        int percent = (int) (((double) PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT) / (double) PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);
        int totalPercent = (int) ((((double) mPlanManager.getTotalDuringDay() -
                (double) mPlanManager.getDuringDay()) * PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_READ_COUNT) / (double) PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);
        mProgress.setProgress(percent);
        mProgress.setSecondaryProgress(totalPercent);
        if (percent < (totalPercent - 5)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));
                mProgress.setSecondaryProgressTintList(ColorStateList.valueOf(Color.RED));
            } else {
                mProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            }
            // mProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
        } else if (percent > (totalPercent + 5)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgress.setProgressTintList(ColorStateList.valueOf(Color.BLUE));
            } else {
                mProgress.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgress.setProgressTintList(ColorStateList.valueOf(Color.BLACK));
                mProgress.setSecondaryProgressTintList(ColorStateList.valueOf(Color.BLACK));
            } else {
                mProgress.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            }

        }
    }

    // Detect Service를 수행하 코드
    public static final int OVERLAY_PERMISSION_REQ_CODE = 1234;
    public static final int ACCESSIBILITY_PERMISSION_REQ_CODE = 12345;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getOverlayDrawPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else if (!isAccessibilitySettingsOn(CheckBibleApp.getContext())) {
            getAccessibilityPermission();
        } else {
            getAccessibilityPermission();
        }
    }

    public void getAccessibilityPermission() {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, ACCESSIBILITY_PERMISSION_REQ_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OVERLAY_PERMISSION_REQ_CODE:
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                } else {
                    getAccessibilityPermission();
                }
                break;
            case ACCESSIBILITY_PERMISSION_REQ_CODE:
                if (isAccessibilitySettingsOn(CheckBibleApp.getContext())) {
                    Toast.makeText(MainActivity.this, "Floating On", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Floating Off", Toast.LENGTH_SHORT).show();
                }
                this.invalidateOptionsMenu();
                break;

        }
    }

    public boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + DetectService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
