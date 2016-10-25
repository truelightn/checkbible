package cba.org.checkbible.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.TextView;

import cba.org.checkbible.R;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class CheckableLayout extends GridLayout implements Checkable {
    private boolean mIsChecked;

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsChecked = false;
    }

    @Override
    public void setChecked(boolean checked) {
        TextView tv = (TextView)findViewById(R.id.textView1);
        if (checked) {
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(Color.GRAY);
        }
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(mIsChecked ? false : true);
    }
}
