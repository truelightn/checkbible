package cba.org.checkbible.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.db.SettingDBUtil;
import cba.org.checkbible.dto.PlanItem;
import cba.org.checkbible.enums.Bible;

/**
 * Created by jinhwan.na on 2016-10-19.
 */

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = PlanActivity.class.getSimpleName();

    private Button mStartBtn;
    private Button mEndBtn;
    private Button mConfrimBtn;
    private CheckBox mOldTestamentChk;
    private CheckBox mNewTestamentChk;
    private CheckBox mMyChk;
    private EditText mTitle;
    private LinearLayout mBibleGridLayout;
    private GridView mBibleGridView;
    private GridBibleAdapter mBibleGridBibleAdapter;
    PlanItem mPlanItem;

    int[] mBibleCount;

    int mYear, mMonth, mDay;
    int mPlanedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mBibleCount = getResources().getIntArray(R.array.bible_count);
        mPlanItem = new PlanItem();
//        mPlanItem = new PlanItem(0, "", "", "", 0, 0, 0, "", "", true);

        mTitle = V.get(this, R.id.editTitle);

        mStartBtn = V.get(this, R.id.startBtn);
        mEndBtn = V.get(this, R.id.endBtn);
        mConfrimBtn = V.get(this, R.id.confrimBtn);
        mStartBtn.setOnClickListener(onClickListener);
        mEndBtn.setOnClickListener(onClickListener);
        mConfrimBtn.setOnClickListener(onClickListener);

        mBibleGridLayout = V.get(this, R.id.bible_grid);
        mBibleGridLayout.setVisibility(View.GONE);

        mOldTestamentChk = V.get(this, R.id.OldTestamentChk);
        mNewTestamentChk = V.get(this, R.id.NewTestamentChk);
        mMyChk = V.get(this, R.id.MyChk);

        mBibleGridView = V.get(this, R.id.gridview);
        mBibleGridBibleAdapter = new GridBibleAdapter();
        mBibleGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mBibleGridView.setAdapter(mBibleGridBibleAdapter);

        GregorianCalendar calendar = new GregorianCalendar();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String startT = SettingDBUtil.getSettingValue("startdate");
        String endT = SettingDBUtil.getSettingValue("enddate");
        if (startT.isEmpty()) {
            mStartBtn.setText(String.format("%d/%d/%d", mYear, mMonth + 1, mDay));
        } else {
            mStartBtn.setText(startT);
        }
        if (endT.isEmpty()) {
            mEndBtn.setText("");
        } else {
            mEndBtn.setText(endT);
        }

        mBibleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (mBibleGridView.isItemChecked(position)) {
                    mPlanedCount = mPlanedCount + mBibleCount[position];
                    Toast.makeText(PlanActivity.this, "" + mPlanedCount, Toast.LENGTH_SHORT).show();
                } else {
                    mPlanedCount = mPlanedCount - mBibleCount[position];
                    Toast.makeText(PlanActivity.this, "" + mPlanedCount, Toast.LENGTH_SHORT).show();
                }
            }
        });



        mMyChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBibleGridLayout.setVisibility(View.VISIBLE);
                } else {
                    mBibleGridLayout.setVisibility(View.GONE);
                }
            }
        });

        mOldTestamentChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < 39; i++) {
                        mBibleGridView.setItemChecked(i, true);
                    }
                } else {
                    for (int i = 0; i < 39; i++) {
                        mBibleGridView.setItemChecked(i, false);
                    }
                }
            }
        });
        mNewTestamentChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 39; i < 66; i++) {
                        mBibleGridView.setItemChecked(i, true);
                    }
                } else {
                    for (int i = 39; i < 66; i++) {
                        mBibleGridView.setItemChecked(i, false);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.startBtn:
                // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String startTime = String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth);
//                        SettingDBUtil.setSettingValue("startdate", msg);
                        mStartBtn.setText(startTime);
                        mPlanItem.startTime = startTime;
//                        Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }, mYear, mMonth, mDay).show();
                break;

            case R.id.endBtn:
                new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                    // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String endTime = String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth);
//                        SettingDBUtil.setSettingValue("enddate", msg);
                        mEndBtn.setText(endTime);
                        mPlanItem.endTime = endTime;
//                        Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }, mYear, mMonth, mDay).show();
                break;

            case R.id.confrimBtn:
                SparseBooleanArray checkedItems = mBibleGridView.getCheckedItemPositions();
                int count = mBibleGridBibleAdapter.getCount();
                int planedCount = 0;
                String planedChapter = "";


                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        planedCount = planedCount + mBibleCount[i];
                        planedChapter = planedChapter + String.valueOf(i) + "/";
                    }
                }
                mPlanItem.totalCount = planedCount;
                mPlanItem.planedChapter = planedChapter;
                mPlanItem.title = String.valueOf(mTitle.getText());
                Log.d(TAG,"total : " + planedCount + " - chapter: " + planedChapter);
                PlanDBUtil.setCurrentActiveRowToInActive();
                PlanDBUtil.addPlan(mPlanItem);
                break;
            default:
                break;
            }
        }
    };

    public static void start(Context context) {
        Intent intent = new Intent(context, PlanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public class GridBibleAdapter extends BaseAdapter {
        private String[] mBible = getResources().getStringArray(R.array.bible);

        public GridBibleAdapter() {
        }

        @Override
        public int getCount() {
            return mBible.length;
        }

        @Override
        public Object getItem(int position) {
            return mBible[position];
        }

        @Override
        public long getItemId(int position) {
            return mBible[position].hashCode();
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.grid_bible_item, parent, false);
            }
            TextView textTextView = V.get(convertView, R.id.textView1);
            textTextView.setText((String)getItem(position));

            return convertView;
        }

    }
}
