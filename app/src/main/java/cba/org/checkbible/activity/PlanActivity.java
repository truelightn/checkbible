package cba.org.checkbible.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cba.org.checkbible.R;
import cba.org.checkbible.db.SettingDBUtil;

/**
 * Created by jinhwan.na on 2016-10-19.
 */

public class PlanActivity extends AppCompatActivity {
    private Button mStartBtn;
    private Button mEndBtn;
    private Button mConfrimBtn;
    int mYear, mMonth, mDay;
    private ArrayList<CheckBox> mCheckboxlist = new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

//        initializeArray();
//        initializeCheckBox();

        mStartBtn = (Button)findViewById(R.id.startBtn);
        mEndBtn = (Button)findViewById(R.id.endBtn);
        mConfrimBtn = (Button)findViewById(R.id.confrimBtn);

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


        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new TextAdapter(this));
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(PlanActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mConfrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                confirmCheckedValue();
            }
        });

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PlanActivity.this, startDateSetListener, mYear, mMonth, mDay)
                        .show();
            }
        });

        mEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PlanActivity.this, endDateSetListener, mYear, mMonth, mDay)
                        .show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth);
            SettingDBUtil.setSettingValue("startdate", msg);
            mStartBtn.setText(msg);
            Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    private DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth);
            SettingDBUtil.setSettingValue("enddate", msg);
            mEndBtn.setText(msg);
            Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    };

    public static void start(Context context) {
        Intent intent = new Intent(context, PlanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
//
//    private void initializeArray() {
//        // Must obey input order
//        mCheckBox2 = (CheckBox)findViewById(R.id.checkBox2);
//        mCheckBox3 = (CheckBox)findViewById(R.id.checkBox3);
//        mCheckboxlist.clear();
//        mCheckboxlist.add(mCheckBox2);
//        mCheckboxlist.add(mCheckBox3);
//    }
//
//    private void initializeCheckBox() {
//        for (int count = 0; count < mCheckboxlist.size(); count++) {
//            if (!mCheckboxlist.get(count).isChecked()) {
//                mCheckboxlist.get(count).setOnCheckedChangeListener(mCheckListener);
//            }
//        }
//    }
//
//    CompoundButton.OnCheckedChangeListener mCheckListener = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//            String msg = view.toString() + " / " + isChecked;
//            Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
//            if (isChecked) {
//                ((ViewGroup)view.getParent()).getChildAt(1).setEnabled(true);
//            } else {
//                ((ViewGroup)view.getParent()).getChildAt(1).setEnabled(false);
//            }
//        }
//    };
//
//    private void confirmCheckedValue() {
//        for (CheckBox checkbox : mCheckboxlist) {
//            if (checkbox.isChecked()) {
//                String msg = checkbox.toString() + " / " + checkbox.isChecked();
//                Toast.makeText(PlanActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    private void resetCheckedValue() {
//        for (CheckBox checkbox : mCheckboxlist) {
//            checkbox.setChecked(false);
//        }
//    }

    public class TextAdapter extends BaseAdapter {
        private Context mContext;

        public TextAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mBible.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView)convertView;
//            CheckedTextView chview =
            if (convertView == null) {
                convertView = new TextView(mContext);
                label = (TextView)convertView;
            }

            label.setText(mBible[position]);

            return convertView;
        }

        // references to our images
        private Integer[] mBible = { R.string.action_settings, R.string.app_name,
                R.string.appbar_scrolling_view_behavior, R.string.bottom_sheet_behavior };
    }
}
