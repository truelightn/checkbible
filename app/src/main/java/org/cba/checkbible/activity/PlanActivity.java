package org.cba.checkbible.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cba.checkbible.R;
import org.cba.checkbible.afw.V;
import org.cba.checkbible.db.PlanDBUtil;
import org.cba.checkbible.db.SettingDBUtil;
import org.cba.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-10-19.
 */

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = PlanActivity.class.getSimpleName();

    private Button mStartBtn;
    private Button mEndBtn;
    private CheckBox mOldTestamentChk;
    private CheckBox mNewTestamentChk;
    private CheckBox mMyChk;
    private EditText mTitle;
    private GridView mBibleGridView;
    private GridBibleAdapter mBibleGridBibleAdapter;
    PlanItem mPlanItem;

    private MenuItem mStringItem;

    int[] mBibleCount;
    int mStartChapter = 0;
    int mEndChapter = 0;

    int mStartY, mStartM, mStartD;
    int mEndY, mEndM, mEndD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mBibleCount = getResources().getIntArray(R.array.bible_count);
        mPlanItem = new PlanItem();
        // mPlanItem = new PlanItem(0, "", "", "", 0, 0, 0, "", "", true);

        mTitle = V.get(this, R.id.editTitle);

        mStartBtn = V.get(this, R.id.startBtn);
        mEndBtn = V.get(this, R.id.endBtn);
        mStartBtn.setOnClickListener(onClickListener);
        mEndBtn.setOnClickListener(onClickListener);

        mOldTestamentChk = V.get(this, R.id.OldTestamentChk);
        mNewTestamentChk = V.get(this, R.id.NewTestamentChk);
        mMyChk = V.get(this, R.id.MyChk);

        mBibleGridView = V.get(this, R.id.gridview);
        mBibleGridBibleAdapter = new GridBibleAdapter();
        mBibleGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mBibleGridView.setAdapter(mBibleGridBibleAdapter);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        mStartY = calendar.get(Calendar.YEAR);
        mStartM = calendar.get(Calendar.MONTH);
        mStartD = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 2);
        mEndY = calendar.get(Calendar.YEAR);
        mEndM = calendar.get(Calendar.MONTH);
        mEndD = calendar.get(Calendar.DAY_OF_MONTH);

        String startT = SettingDBUtil.getSettingValue("startdate");
        String endT = SettingDBUtil.getSettingValue("enddate");
        if (startT.isEmpty()) {
            String startTime = String.format("%d/%d/%d", mStartY, mStartM + 1, mStartD);
            mPlanItem.startTime = startTime;
            mStartBtn.setText(startTime.replace("/", "."));
        } else {
            mStartBtn.setText(startT.replace("/", "."));
        }
        if (endT.isEmpty()) {
            String endTime = String.format("%d/%d/%d", mEndY, mEndM + 1, mEndD);
            mPlanItem.endTime = endTime;
            mEndBtn.setText(endTime.replace("/", "."));
        } else {
            mEndBtn.setText(endT.replace("/", "."));
        }

        mBibleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mTitle.clearFocus();
                if (mMyChk.isChecked()) {
                    if (mBibleGridView.isItemChecked(position)) {
                        if (mStartChapter == 0) {
                            mStartChapter = position;
                        } else if (mEndChapter == 0 && mStartChapter != position) {
                            if (position < mStartChapter) {
                                Toast.makeText(PlanActivity.this, "선택하신 장보다 뒤에 장을 선택해주세요",
                                        Toast.LENGTH_SHORT).show();
                                mBibleGridView.setItemChecked(position, false);

                                // for (int i = mStartChapter; i < 66; i++) {
                                // mBibleGridView.setItemChecked(i, true);
                                // }
                                // for (int i = 0; i < mEndChapter; i++) {
                                // mBibleGridView.setItemChecked(i, true);
                                // }
                            } else {
                                mEndChapter = position;
                                for (int i = mStartChapter; i < mEndChapter; i++) {
                                    mBibleGridView.setItemChecked(i, true);
                                }
                            }
                        }
                    }
                }
                setConfirmText();
            }
        });

        mMyChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTitle.clearFocus();
                mNewTestamentChk.setChecked(false);
                mOldTestamentChk.setChecked(false);
                if (isChecked) {
                    Toast.makeText(PlanActivity.this, "처음과 끝을 선택하시면 구간이 선택됩니다.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Toast.makeText(PlanActivity.this,
                    // "다시 체크하시면 구간을 선택하실수 있습니다.", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < 66; i++) {
                        mBibleGridView.setItemChecked(i, false);
                    }
                    mStartChapter = 0;
                    mEndChapter = 0;
                }
                setConfirmText();
            }
        });

        mOldTestamentChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTitle.clearFocus();
                if (isChecked) {
                    for (int i = 0; i < 39; i++) {
                        mBibleGridView.setItemChecked(i, true);
                    }
                } else {
                    for (int i = 0; i < 39; i++) {
                        mBibleGridView.setItemChecked(i, false);
                    }
                }
                setConfirmText();
            }
        });
        mNewTestamentChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTitle.clearFocus();
                if (isChecked) {
                    for (int i = 39; i < 66; i++) {
                        mBibleGridView.setItemChecked(i, true);
                    }
                } else {
                    for (int i = 39; i < 66; i++) {
                        mBibleGridView.setItemChecked(i, false);
                    }
                }
                setConfirmText();
            }
        });

        mTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void setConfirmText() {
        int count = 0;
        for (int i = 0; i < 66; i++) {
            if (mBibleGridView.isItemChecked(i)) {
                count = count + mBibleCount[i];
            }
        }
        mStringItem.setTitle("총 " + count + "장 선택 ");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTitle.clearFocus();
            switch (v.getId()) {
            case R.id.startBtn:
                // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String startTime = String.format("%d/%d/%d", year, monthOfYear + 1,
                                dayOfMonth);
                        mPlanItem.startTime = startTime;

                        mStartBtn.setText(startTime.replace("/", "."));

                        // endBtn text를 설정한 startDate+2달로 변경
                        GregorianCalendar gCalendar = getCalendar(startTime);
                        gCalendar.add(Calendar.MONTH, 2);
                        mEndY = gCalendar.get(Calendar.YEAR);
                        mEndM = gCalendar.get(Calendar.MONTH);
                        mEndD = gCalendar.get(Calendar.DAY_OF_MONTH);
                        String endTime = String.format("%d/%d/%d", mEndY, mEndM + 1, mEndD);
                        mPlanItem.endTime = endTime;

                        mEndBtn.setText(endTime.replace("/", "."));

                    }
                }, mStartY, mStartM, mStartD).show();
                break;

            case R.id.endBtn:
                new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    // DatePickerDialog로 가져온 날짜를 planItem에 String 형태로 저장한다.
                    // 나중에 DB에서 가져올때 /로 파싱해서 가져올예정
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String endTime = String.format("%d/%d/%d", year, monthOfYear + 1,
                                dayOfMonth);
                        mPlanItem.endTime = endTime;
                        mEndBtn.setText(endTime.replace("/", "."));
                    }
                }, mEndY, mEndM, mEndD).show();
                break;

            default:
                break;
            }
        }
    };

    public GregorianCalendar getCalendar(String startTime) {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date endDate = null;
        try {
            endDate = formatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(endDate);
        return calendar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.plan_menu, menu);
        mStringItem = menu.findItem(R.id.plan_menu_string);
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
        case R.id.plan_menu_confirm:
            mPlanItem.title = String.valueOf(mTitle.getText());
            if (mPlanItem.title.isEmpty()) {
                Toast.makeText(PlanActivity.this, "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                return false;
            }
            SparseBooleanArray checkedItems = mBibleGridView.getCheckedItemPositions();
            int count = mBibleGridBibleAdapter.getCount();
            int planedCount = 0;
            String planedChapter = "";
            for (int i = count - 1; i >= 0; i--) {
                if (checkedItems.get(i)) {
                    planedCount = planedCount + mBibleCount[i];
                    planedChapter = String.valueOf(i) + "/" + planedChapter;
                }
            }
            mPlanItem.totalCount = planedCount;
            mPlanItem.planedChapter = planedChapter;
            if (mPlanItem.planedChapter.isEmpty()) {
                Toast.makeText(PlanActivity.this, "성경을 선택해주세요", Toast.LENGTH_SHORT).show();
                return false;
            }

            mPlanItem.chapterReadCount = 1;
            Log.d(TAG, "total : " + planedCount + " - chapter: " + planedChapter);
            PlanDBUtil.setCurrentActiveRowToInActive();
            mPlanItem.active = 1;

            PlanDBUtil.addPlan(mPlanItem);
            Toast.makeText(PlanActivity.this, "계획이 만들어 졌습니다.", Toast.LENGTH_SHORT).show();
            finish();
            break;
        default:
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(PlanActivity.this.getCurrentFocus()
                .getWindowToken(), 0);
    }

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
