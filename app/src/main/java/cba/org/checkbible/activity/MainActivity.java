package cba.org.checkbible.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.widget.WidgetUpdateService;

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
        if (PlanDBUtil.hasNoPlanedData()) {
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
        mPlanManager.initCount();
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
        mPlanManager.resetTodayCount();
        refreshView();
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

        // set chaptergrid
        ArrayList<Integer> totalList = new ArrayList<>();
        totalList.addAll(PlanDBUtil.getCompleteChapterPosition(0));
        totalList.addAll(PlanDBUtil.getPlanedChapterPosition(0));
        AbbChapterAdapter addAdapter = new AbbChapterAdapter(this, 0);
        mAbbGridView.setAdapter(addAdapter);
        if (totalList.size() > 7) {
            mAbbGridView.setNumColumns(7);
        } else {
            mAbbGridView.setNumColumns(totalList.size());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        switch (id) {
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
                // increaseCount(Integer.valueOf(SettingDBUtil.getSettingValue(Setting.CUSTOM_COUNT)));
                mPlanManager.increaseCount(mPlanManager.calculateTodayCount());
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
        int percent = (int)(((double)PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT) / (double)PlanDBUtil
                .getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)) * 100.0);
        int totalPercent = 100 - (int)((double)mPlanManager.getDuringDay()
                / (double)mPlanManager.getTotalDuringDay() * 100.0);
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
}
