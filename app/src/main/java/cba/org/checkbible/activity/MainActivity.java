package cba.org.checkbible.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DBUtil;
import cba.org.checkbible.db.SettingDBUtil;
import cba.org.checkbible.enums.Bible;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private Button mBtn2;
    private Button mBtn3;
    private TextView mText;
    private TextView mText2;
    private ProgressBar mProgress;
    private LinearLayout mLayout;
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = V.get(this, R.id.layout);
        mBtn = V.get(this, R.id.button);
        mBtn2 = V.get(this, R.id.button2);
        mBtn3 = V.get(this, R.id.button3);

        mText = V.get(this, R.id.title);
        mText2 = V.get(this, R.id.today);
        mProgress = V.get(this, R.id.progressBar);
        mText.setText(String.valueOf(mCount));
        mBtn.setOnClickListener(onClickListener);
        mBtn2.setOnClickListener(onClickListener);
        mBtn3.setOnClickListener(onClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();

        prepareDisplay();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SettingDBUtil.getSettingValue("test").isEmpty()) {
            mCount = Integer.parseInt(SettingDBUtil.getSettingValue("test"));
            mText.setText(String.valueOf(mCount));
            mProgress.setProgress(mCount);
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
                mProgress.setSecondaryProgress(mCount + 10);
                SettingDBUtil.setSettingValue("test", String.valueOf(mCount));
                String s = SettingDBUtil.getSettingValue("test");
                mText.setText(s);
                break;
            case R.id.button3:
                break;
            default:
                break;
            }
        }
    };

    public void prepareDisplay(){

    }
}
