package cba.org.checkbible.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.SettingDBUtil;
import cba.org.checkbible.enums.Bible;

/**
 * Created by jinhwan.na on 2016-10-19.
 */

public class PlanActivity extends AppCompatActivity {
    private Button mStartBtn;
    private Button mEndBtn;
    private Button mConfrimBtn;
    int mYear, mMonth, mDay;
    int mPlanedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mStartBtn = V.get(this, R.id.startBtn);
        mEndBtn = V.get(this, R.id.endBtn);
        mConfrimBtn = V.get(this, R.id.confrimBtn);

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

        final GridView gridview = V.get(this, R.id.gridview);
        final GridAdapter adapter = new GridAdapter();
        gridview.setAdapter(adapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mPlanedCount = mPlanedCount + Bible.getCount(position);
                Toast.makeText(PlanActivity.this, "" + mPlanedCount, Toast.LENGTH_SHORT).show();
            }
        });

        mConfrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = gridview.getCheckedItemPositions();
                int count = adapter.getCount();
                int planedCount = 0;
                String msg = "";
                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        planedCount = planedCount + Bible.getCount(i);
//                        msg = msg + String.valueOf(i) + "/";
                    }
                }
                Toast.makeText(PlanActivity.this, "total : " + planedCount, Toast.LENGTH_SHORT).show();
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

    public class GridAdapter extends BaseAdapter {
        private String[] mBible = getResources().getStringArray(R.array.bible);

        public GridAdapter() {
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
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.grid, parent, false);
            }
            TextView textTextView = V.get(convertView, R.id.textView1);

            textTextView.setText(mBible[position]);

            return convertView;
        }

    }
}
