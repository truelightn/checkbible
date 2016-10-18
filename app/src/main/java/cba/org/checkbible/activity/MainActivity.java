package cba.org.checkbible.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cba.org.checkbible.R;
import cba.org.checkbible.db.SettingDBUtil;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private Button mBtn2;
    private TextView mText;
    private ProgressBar mProgress;
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button)findViewById(R.id.button);
        mBtn2 = (Button)findViewById(R.id.button2);
        mText = (TextView)findViewById(R.id.textView);
        mProgress = (ProgressBar)findViewById(R.id.progressBar);
//        mText.setText(String.valueOf(mCount));
        mBtn.setOnClickListener(onClickListener);
        mBtn2.setOnClickListener(onClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCount = Integer.parseInt(SettingDBUtil.getSettingValue("test"));
        mText.setText(String.valueOf(mCount));
        mProgress.setProgress(mCount);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button:
                mCount = mCount + 5;
                mProgress.setProgress(mCount);
                SettingDBUtil.setSettingValue("test", String.valueOf(mCount));
                String s1 = SettingDBUtil.getSettingValue("test");
                mText.setText(String.valueOf(mCount));
                break;

            case R.id.button2:
                mCount = mCount - 5;
                mProgress.setProgress(mCount);
                SettingDBUtil.setSettingValue("test", String.valueOf(mCount));
                String s = SettingDBUtil.getSettingValue("test");
                mText.setText(s);
                break;
            default:
                break;
            }
        }
    };
}
