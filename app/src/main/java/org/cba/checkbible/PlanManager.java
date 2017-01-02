package org.cba.checkbible;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.cba.checkbible.constant.CheckBibleIntent;
import org.cba.checkbible.db.DB;
import org.cba.checkbible.db.PlanDBUtil;
import org.cba.checkbible.db.Setting;
import org.cba.checkbible.db.SettingDBUtil;

/**
 * Created by jinhwan.na on 2016-11-01.
 */

public class PlanManager {
    private static final String TAG = PlanManager.class.getSimpleName();
    private static PlanManager sInstance;

    private Context mContext;
    private int mTodayReadCount;
    private int mChapterReadCount;
    private int mTotalReadCount;
    private int mTotalCount;
    String[] mAbbreviationBible;
    int[] mBibleCount;

    private PlanManager(Context context) {
        mContext = context;
        mAbbreviationBible = mContext.getResources().getStringArray(R.array.abbreviation_bible);
        mBibleCount = mContext.getResources().getIntArray(R.array.bible_count);
        initCount();
    }

    public static PlanManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PlanManager(context);
        }
        return sInstance;
    }

    public void initCount() {
        mChapterReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_CHAPTER_READ_COUNT);
        mTodayReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_READ_COUNT);
        mTotalReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT);
        mTotalCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT);
    }

    public int getCurrentChapterPosition() {
        ArrayList<Integer> list = PlanDBUtil.getPlanedChapterPosition(0);
        if (list.isEmpty()) {
            return 0;
        }
        return list.get(0);
    }

    public int calculateTodayCount() {
        int retValue = 0;
        int totalCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT)
                - PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT);
        long during = getDuringDay();
        retValue = (int)(totalCount / during);
        if (retValue <= 0) {
            return 1;
        }
        return retValue;
    }

    public long getDuringDay() {
        long oneDay = 24 * 60 * 60 * 1000;
        GregorianCalendar endCalendar = getEndDate();
        GregorianCalendar todayCalendar = new GregorianCalendar();

        return (endCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / oneDay + 2;
    }

    public long getTotalDuringDay() {
        long oneDay = 24 * 60 * 60 * 1000;
        GregorianCalendar endCalendar = getEndDate();
        GregorianCalendar startCalendar = getStartDate();

        return (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / oneDay + 2;
    }

    public GregorianCalendar getEndDate() {
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
        GregorianCalendar calendar = new GregorianCalendar();
        if (TextUtils.isEmpty(endDateString)) {
            return calendar;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date = formatter.parse(endDateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public GregorianCalendar getStartDate() {
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_START_DATE);
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date date = formatter.parse(endDateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public String getDuringString() {
        String retVaule;
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
        String startDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_START_DATE);
        retVaule = startDateString + " ~ " + endDateString;
        retVaule = retVaule.replace("/", ".");
        return retVaule;
    }

    public void setChapter(String col, ArrayList<Integer> chapter) {
        String chapterStr = "";
        for (int i : chapter) {
            chapterStr = chapterStr + String.valueOf(i) + "/";
        }
        PlanDBUtil.updateValue(col, chapterStr);
    }

    public String getAbbreviation(int i) {
        String[] bible = CheckBibleApp.getContext().getResources()
                .getStringArray(R.array.abbreviation_bible);
        if (i < 0 || i > bible.length) {
            return "NA";
        }
        return bible[i];
    }

    public boolean isComplete() {
        return (PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_COMPLETE) == 1 ? true : false);
    }

    public String getChapterString() {
        int mChapterReadCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_CHAPTER_READ_COUNT);
        if (isComplete()) {
            return "계획된 성경을 모두 읽었습니다";
        } else {
            return mAbbreviationBible[getCurrentChapterPosition()] + " " + mChapterReadCount + "장/"
                    + mBibleCount[getCurrentChapterPosition()] + "장";
        }
    }

    public String getTodayString() {
        return "오늘 " + PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TODAY_READ_COUNT) + "장/"
                + calculateTodayCount() + "장";
    }

    public String getTotalString() {
        return "전체 " + PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_READ_COUNT) + "장/"
                + PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT) + "장";
    }

    public void increaseCount(int count) {
        initCount();
        if (mTotalReadCount >= mTotalCount - count) {
            // 이순간 다읽음 처리해야함
            mTodayReadCount = calculateTodayCount();
            mTotalReadCount = mTotalCount;
            mChapterReadCount = mBibleCount[getCurrentChapterPosition()];
            ArrayList<Integer> plan = PlanDBUtil.getPlanedChapterPosition(0);
            ArrayList<Integer> complete = PlanDBUtil.getCompleteChapterPosition(0);
            if (plan.isEmpty()) {
                return;
            }
            complete.add(plan.get(0));
            plan.remove(0);
            setChapter(DB.COL_READINGPLAN_COMPLETED_CHAPTER, complete);
            setChapter(DB.COL_READINGPLAN_PLANED_CHAPTER, plan);

            PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_COMPLETE, 1);
            return;
        }

        mTodayReadCount = mTodayReadCount + count;
        mTotalReadCount = mTotalReadCount + count;
        mChapterReadCount = mChapterReadCount + count;

        // 1장씩 있는 chapter들때문에 while을 사용
        while (mChapterReadCount > mBibleCount[getCurrentChapterPosition()]) {
            mChapterReadCount = mChapterReadCount - mBibleCount[getCurrentChapterPosition()];
            ArrayList<Integer> plan = PlanDBUtil.getPlanedChapterPosition(0);
            ArrayList<Integer> complete = PlanDBUtil.getCompleteChapterPosition(0);
            complete.add(plan.get(0));
            plan.remove(0);
            setChapter(DB.COL_READINGPLAN_COMPLETED_CHAPTER, complete);
            setChapter(DB.COL_READINGPLAN_PLANED_CHAPTER, plan);
        }

        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
    }

    public void decreaseCount(int i) {
        if (mTotalReadCount <= 0) {
            Toast.makeText(mContext, "잘못된 입력 입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        mChapterReadCount = mChapterReadCount - i;
        mTodayReadCount = mTodayReadCount - i;
        mTotalReadCount = mTotalReadCount - i;

        if (mChapterReadCount <= 0) {
            ArrayList<Integer> plan = PlanDBUtil.getPlanedChapterPosition(0);
            ArrayList<Integer> complete = PlanDBUtil.getCompleteChapterPosition(0);
            plan.add(0, complete.get(complete.size() - 1));
            complete.remove(complete.size() - 1);
            setChapter(DB.COL_READINGPLAN_COMPLETED_CHAPTER, complete);
            setChapter(DB.COL_READINGPLAN_PLANED_CHAPTER, plan);
            mChapterReadCount = mBibleCount[getCurrentChapterPosition()];
        }

        PlanDBUtil.updateValue(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, mChapterReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        PlanDBUtil.updateValue(DB.COL_READINGPLAN_TOTAL_READ_COUNT, mTotalReadCount);
    }

    public void resetTodayCount() {
        String previousDay = SettingDBUtil.getSettingValue(Setting.TODAY);
        Log.e("checkbible", "resetTodaycount [" + previousDay + "]");

        GregorianCalendar gc = new GregorianCalendar();
        int today = gc.get(Calendar.DAY_OF_MONTH);

        if (previousDay.isEmpty()) {
            SettingDBUtil.setSettingValue(Setting.TODAY, String.valueOf(today));
            return;
        }

        if (today != Integer.valueOf(previousDay)) {
            SettingDBUtil.setSettingValue(Setting.TODAY, String.valueOf(today));
            mTodayReadCount = 0;
            PlanDBUtil.updateValue(DB.COL_READINGPLAN_TODAY_READ_COUNT, mTodayReadCount);
        }
    }

    public void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CheckBibleIntent.ACITON_RESET_TODAY);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent,
                0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Log.e("checkbible", "setAlarm [" + calendar + "]");
        Log.e("checkbible", "setAlarm millis[" + calendar.getTimeInMillis() + "]");

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }
}
