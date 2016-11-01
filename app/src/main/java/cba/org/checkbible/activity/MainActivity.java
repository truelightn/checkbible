package cba.org.checkbible.activity;

import android.os.Bundle;
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

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.db.SettingDBUtil;

public class MainActivity extends AppCompatActivity {
    private Button mPlusBtn;
    private Button mMinusBtn;
    private Button mCustomBtn;

    private TextView mTitleTextView;
    private TextView mTodayTextView;
    private TextView mTotalTextView;
    private TextView mChaterTextView;

    private ProgressBar mProgress;
    private LinearLayout mLayout;
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = V.get(this, R.id.layout);

        mMinusBtn = V.get(this, R.id.minus_btn);
        mPlusBtn = V.get(this, R.id.plus_btn);
        mCustomBtn = V.get(this, R.id.custom_btn);

        mTitleTextView = V.get(this, R.id.title);
        mTodayTextView = V.get(this, R.id.today);
        mTotalTextView = V.get(this, R.id.total);
        mChaterTextView = V.get(this, R.id.chapter);

        mProgress = V.get(this, R.id.progressBar);

        // mText.setText(String.valueOf(mCount));
        mMinusBtn.setOnClickListener(onClickListener);
        mPlusBtn.setOnClickListener(onClickListener);
        mCustomBtn.setOnClickListener(onClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();

        prepareDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PlanDBUtil.getTitle(2).isEmpty()) {
            mTitleTextView.setText(PlanDBUtil.getTitle(2));
        }

        if (PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT, 2) != -1) {
            mTotalTextView.setText(String.valueOf(PlanDBUtil.getPlanInt(
                    DB.COL_READINGPLAN_TOTAL_COUNT, 2)));
        }

        mTodayTextView.setText(String.valueOf(PlanManager.calculateTodayCount(2)));
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
                mCount = mCount + 5;
                mProgress.setProgress(mCount);
                SettingDBUtil.setSettingValue("test", String.valueOf(mCount));
                String s1 = SettingDBUtil.getSettingValue("test");
                // mText.setText(String.valueOf(mCount));
                break;

            case R.id.plus_btn:
                mCount = mCount - 5;
                mProgress.setProgress(mCount);
                mProgress.setSecondaryProgress(mCount + 10);
                SettingDBUtil.setSettingValue("test", String.valueOf(mCount));
                String s = SettingDBUtil.getSettingValue("test");
                // mText.setText(s);
                break;
            case R.id.custom_btn:

                break;
            default:
                break;
            }
        }
    };

    public void prepareDisplay() {

    }
}
