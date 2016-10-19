package cba.org.checkbible.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

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
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        mStartBtn = (Button)findViewById(R.id.startBtn);
        mEndBtn = (Button)findViewById(R.id.endBtn);

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
}
