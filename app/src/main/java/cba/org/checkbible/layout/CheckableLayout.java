package cba.org.checkbible.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CheckBox;
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
//        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
//        cb.setChecked(checked);
        if (checked) {
            tv.setTextColor(Color.GREEN);
        } else {
            tv.setTextColor(Color.GRAY);
        }
//        if (cb.isChecked() != checked) {
//            cb.setChecked(checked);
//        }
    }

    @Override
    public boolean isChecked() {
//        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;

//        return cb.isChecked() ;
        return mIsChecked;
    }

    @Override
    public void toggle() {
//        CheckBox cb = (CheckBox) findViewById(R.id.checkBox) ;
//        setChecked(cb.isChecked() ? false : true) ;
        setChecked(mIsChecked ? false : true);
    }

}
