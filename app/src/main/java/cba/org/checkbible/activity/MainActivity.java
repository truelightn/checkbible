package cba.org.checkbible.activity;

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

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;

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
    String[] mAbbreviationBible;
    int[] mBibleCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAbbreviationBible = getResources().getStringArray(R.array.abbreviation_bible);
        mBibleCount = getResources().getIntArray(R.array.bible_count);
        mTodayReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_READ_COUNT);

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
        refreshView();

    }

    public void refreshView() {
        // setTitle
        if (!PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE).isEmpty()) {
            mTitleTextView.setText(PlanDBUtil.getPlanString(DB.COL_READINGPLAN_TITLE));
        }

        // setChapter ex:신5장/34장
        mChaterTextView.setText(getChapterString());

        // setToday ex: 3장/10장
        String todayMsg = mTodayReadCount + "장/" + PlanManager.calculateTodayCount() + "장";
        mTodayTextView.setText(todayMsg);

        // setTotal ex: 34장/145장
        String totalMsg = mTotalReadCount + "장/"
                + PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT) + "장";
        mTotalTextView.setText(totalMsg);

        // set progress

        // set during ex: 16.10.16~16.12.31
//        String duringMsg = PlanManager.getStartDate() + "~" + PlanManager.getEndDate();
        mDuringTextView.setText("16.10.16~16.12.31");

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
                mProgress.setProgress(mTodayReadCount);
                mChaterTextView.setText(getChapterString());
                break;

            case R.id.plus_btn:
                increaseCount(1);
                mProgress.setProgress(mTodayReadCount);
                mProgress.setSecondaryProgress(mTodayReadCount + 10);
//                mChaterTextView.setText(getChapterString());
                refreshView();
                break;
            case R.id.custom_btn:
                // ArrayList<Integer> a =
                // PlanManager.getPlanedChapterPosition(2);
                // mChaterTextView.setText(a.toString());
                // int todayCount = 0;
                // String settext =
                // mAbbreviationBible[PlanManager.getCurrentChapterPosition(2)] + " "
                // + todayCount + "/" +
                // Bible.getCount(PlanManager.getCurrentChapterPosition(2));
                // mChaterTextView.setText(settext);

                break;
            default:
                break;
            }
        }

    };

    @NonNull
    public String getChapterString() {
        return mAbbreviationBible[PlanManager.getCurrentChapterPosition()] + " " + mTodayReadCount + "/"
                + mBibleCount[PlanManager.getCurrentChapterPosition()]+"장";
    }

    public void increaseCount(int i) {
        mChapterReadCount = mChapterReadCount + i;
        mTodayReadCount = mTodayReadCount + i;
        mTotalReadCount = mTotalReadCount + i;
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
    }

    public void decreaseCount(int i) {
        mChapterReadCount = mChapterReadCount - i;
        mTodayReadCount = mTodayReadCount - i;
        mTotalReadCount = mTotalReadCount - i;
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
    }
}
