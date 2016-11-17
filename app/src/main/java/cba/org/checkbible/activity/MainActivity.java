package cba.org.checkbible.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.db.Setting;
import cba.org.checkbible.db.SettingDBUtil;

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

    private int mTodayReadCount;
    private int mChapterReadCount;
    private int mTotalReadCount;
    private int mTotalCount;

    String[] mAbbreviationBible;
    int[] mBibleCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAbbreviationBible = getResources().getStringArray(R.array.abbreviation_bible);
        mBibleCount = getResources().getIntArray(R.array.bible_count);

        initialView();

    }

    public void initialView() {
        mLayout = V.get(this, R.id.layout);

        mMinusBtn = V.get(this, R.id.minus_btn);
        mPlusBtn = V.get(this, R.id.plus_btn);
        mCustomBtn = V.get(this, R.id.custom_btn);

        mTitleTextView = V.get(this, R.id.title);
        mChaterTextView = V.get(this, R.id.chapter);

        mTodayTextView = V.get(this, R.id.today);
        mTotalTextView = V.get(this, R.id.total);

        mProgress = V.get(this, R.id.progressBar);

        mDuringTextView = V.get(this, R.id.during);

        mMinusBtn.setOnClickListener(onClickListener);
        mPlusBtn.setOnClickListener(onClickListener);
        mCustomBtn.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetTodayCount();
        mChapterReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_CHAPTER_READ_COUNT);
        mTodayReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_READ_COUNT);
        mTotalReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT);
        mTotalCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT);
        refreshView();

    }

    private void resetTodayCount() {
        String previousDay = SettingDBUtil.getSettingValue(Setting.TODAY);
        if (previousDay.isEmpty()) {
            SettingDBUtil.setSettingValue(Setting.TODAY, String.valueOf(0));
            return;
        }

        GregorianCalendar gc = new GregorianCalendar();
        int today = gc.get(Calendar.DAY_OF_MONTH);
        if (today != Integer.valueOf(previousDay)) {
            SettingDBUtil.setSettingValue(Setting.TODAY, String.valueOf(today));
            mTodayReadCount = 0;
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        }
    }

    public void refreshView() {
        // setTitle
        if (!PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE).isEmpty()) {
            mTitleTextView.setText(PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        }

        // setChapter ex:신5장/34장
        mChaterTextView.setText(getChapterString());

        // setToday ex: 3장/10장
        String todayMsg = "Today " + mTodayReadCount + "장/" + PlanManager.calculateTodayCount()
                + "장";
        mTodayTextView.setText(todayMsg);

        // setTotal ex: 34장/145장
        String totalMsg = "Total " + mTotalReadCount + "장/"
                +mTotalCount + "장";
        mTotalTextView.setText(totalMsg);

        // set progress
        setProgress();

        // set during ex: 16.10.16~16.12.31
        String duringMsg = PlanManager.getDuringString();
        mDuringTextView.setText(duringMsg);

        // set chaptergrid
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
        case R.id.menu_settings:

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
            switch (v.getId()) {
            case R.id.minus_btn:
                decreaseCount(1);

                refreshView();
                break;

            case R.id.plus_btn:
                increaseCount(1);
//                mProgress.setSecondaryProgress(mTodayReadCount + 10);
                // mChaterTextView.setText(getChapterString());z
                refreshView();
                break;
            case R.id.custom_btn:
                increaseCount(6);
                // ArrayList<Integer> a =
                // PlanManager.getPlanedChapterPosition(2);
                // mChaterTextView.setText(a.toString());
                // int todayCount = 0;
                // String settext =
                // mAbbreviationBible[PlanManager.getCurrentChapte rPosition(2)]
                // + " "
                // + todayCount + "/" +
                // Bible.getCount(PlanManager.getCurrentChapterPosition(2));
                // mChaterTextView.setText(settext);

                break;
            default:
                break;
            }
        }

    };

    public void setProgress() {
        int percent = (int)(((double)mTotalReadCount / (double)mTotalCount) * 100.0);
        int totalPercent = 100 - (int)((double)PlanManager.getDuringDay()
                / (double)PlanManager.getTotalDuringDay() * 100.0);
        mProgress.setProgress(percent);
        mProgress.setSecondaryProgress(totalPercent);

        if (percent < (totalPercent - 5)) {
            mProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else if (percent > (totalPercent + 5)) {
            mProgress.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        } else {
            mProgress.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        }
    }

    @NonNull
    public String getChapterString() {
        return mAbbreviationBible[PlanManager.getCurrentChapterPosition()] + " " + mChapterReadCount
                + "/" + mBibleCount[PlanManager.getCurrentChapterPosition()] + "장";
    }

    public void increaseCount(int i) {
        if (mTotalReadCount >=mTotalCount) {
            Toast.makeText(this, "모두 읽었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mTotalReadCount ==mTotalCount-1) {
            //이순간 다읽음 처리해야함
            mTotalReadCount = mTotalReadCount + 1;
            mTodayReadCount = mTodayReadCount + 1;
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
            return;
        }
        mTodayReadCount = mTodayReadCount + i;
        mTotalReadCount = mTotalReadCount + i;
        mChapterReadCount = mChapterReadCount + i;
        if (mChapterReadCount > mBibleCount[PlanManager.getCurrentChapterPosition()]) {
            mChapterReadCount = 1;
            ArrayList<Integer> plan = PlanManager.getPlanedChapterPosition();
            ArrayList<Integer> complete = PlanManager.getCompleteChapterPosition();
            complete.add(plan.get(0));
            plan.remove(0);
            PlanManager.setChapter(DB.COL_READINGPLAN_COMPLETED_CHAPTER, complete);
            PlanManager.setChapter(DB.COL_READINGPLAN_PLANED_CHAPTER, plan);
        }
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);



    }

    public void decreaseCount(int i) {
        if (mTotalReadCount <= 0) {
            Toast.makeText(this, "잘못된 입력 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        mChapterReadCount = mChapterReadCount - i;
        mTodayReadCount = mTodayReadCount - i;
        mTotalReadCount = mTotalReadCount - i;

        if (mChapterReadCount <= 0) {
            ArrayList<Integer> plan = PlanManager.getPlanedChapterPosition();
            ArrayList<Integer> complete = PlanManager.getCompleteChapterPosition();
            plan.add(0, complete.get(complete.size() - 1));
            complete.remove(complete.size() - 1);
            PlanManager.setChapter(DB.COL_READINGPLAN_COMPLETED_CHAPTER, complete);
            PlanManager.setChapter(DB.COL_READINGPLAN_PLANED_CHAPTER, plan);
            mChapterReadCount = mBibleCount[PlanManager.getCurrentChapterPosition()];
        }

    
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
    }
}
