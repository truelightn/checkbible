package cba.org.checkbible.acitivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cba.org.checkbible.R;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private TextView mText;
    private ProgressBar mProgress;
    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtn = (Button)findViewById(R.id.button);
        mText = (TextView)findViewById(R.id.textView);
        mProgress = (ProgressBar)findViewById(R.id.progressBar);
        mText.setText(String.valueOf(mCount));
        mBtn.setOnClickListener(onClickListener);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.button:
                mProgress.incrementProgressBy(5);
                mCount++;
                mText.setText(String.valueOf(mCount));
                break;
            default:
                break;
            }
        }
    };
}
