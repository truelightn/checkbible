package cba.org.checkbible.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.PlanDBUtil;

/**
 * Created by jinhwan.na on 2016-11-25.
 */

public class AbbChapterAdapter extends BaseAdapter {
    private ArrayList<String> mTotalList = new ArrayList<>();
    private ArrayList<String> mPlanedList = new ArrayList<>();
    private ArrayList<String> mCompleteList = new ArrayList<>();
    private Activity mActivity;
    int mId;

    public AbbChapterAdapter(Activity activity, int id) {
        mId = id;
        mActivity = activity;
        mCompleteList = PlanDBUtil.getCompleteChapterAbbreviation(mId);
        mPlanedList = PlanDBUtil.getPlanedChapterAbbreviation(mId);
        mTotalList.addAll(mCompleteList);
        mTotalList.addAll(mPlanedList);
    }

    @Override
    public int getCount() {
        return mTotalList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTotalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_abb_chapter_item, parent, false);
        }

        String addChapterString = (String) getItem(position);
        TextView textTextView = V.get(convertView, R.id.textView2);

        if (mCompleteList.contains(addChapterString)) {
            textTextView.setBackgroundColor(Color.GRAY);
            textTextView.setTextColor(Color.WHITE);
            textTextView.setPaintFlags(textTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (!mPlanedList.isEmpty() && mPlanedList.get(0).equals(addChapterString)) {
            textTextView.setTextColor(Color.BLUE);
        }
        textTextView.setText(addChapterString);

        return convertView;
    }
}
