package cba.org.checkbible.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.GridLayout;
import android.widget.TextView;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class CheckableLayout extends GridLayout implements Checkable {
    public boolean mChecked = false;

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        TextView tv = (TextView)findViewById(R.id.textView1);
        if (checked) {
            tv.setTextColor(Color.WHITE);
            tv.setBackgroundColor(Color.GRAY);
        } else {
            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(android.R.color.background_light);
        }
        if (checked != mChecked) {
            mChecked = checked;
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(mChecked ? false : true);
    }

}
