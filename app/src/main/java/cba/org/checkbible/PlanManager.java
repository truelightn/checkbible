package cba.org.checkbible;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;

/**
 * Created by jinhwan.na on 2016-11-01.
 */

public class PlanManager {

    public static ArrayList<Integer> getCompleteChapterPosition() {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_COMPLETED_CHAPTER);
        if (TextUtils.isEmpty(chapter)) {
            return chapterList;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }

    public static ArrayList<Integer> getPlanedChapterPosition() {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_PLANED_CHAPTER);
        if (TextUtils.isEmpty(chapter)) {
            return chapterList;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }

    public static int getCurrentChapterPosition() {
        ArrayList<Integer> list = getPlanedChapterPosition();
        if (list.isEmpty()) {
            return 0;
        }
        return list.get(0);
    }

    public static int calculateTodayCount() {
        int totalCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT);
        long during = getDuringDay();
        return (int)(totalCount / during);
    }

    public static long getDuringDay() {
        long oneDay = 24 * 60 * 60 * 1000;
        GregorianCalendar endCalendar = getEndDate();
        GregorianCalendar todayCalendar = new GregorianCalendar();

        return (endCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / oneDay + 2;
    }

    public static long getTotalDuringDay(){
        long oneDay = 24 * 60 * 60 * 1000;
        GregorianCalendar endCalendar = getEndDate();
        GregorianCalendar startCalendar = getStartDate();

        return (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / oneDay + 2;
    }

    public static GregorianCalendar getEndDate() {
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
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
    public static GregorianCalendar getStartDate() {
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

    public static String getDuringString() {
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE);
        String startDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_START_DATE);

        return startDateString + " ~ " + endDateString;
    }

    public static void setChapter(String col, ArrayList<Integer> chapter) {
        if (chapter.isEmpty()) {
            return;
        }
        String chapterStr = "";
        for (int i : chapter) {
            chapterStr = chapterStr + String.valueOf(i) + "/";
        }
        PlanDBUtil.updateValue(col, chapterStr);
    }
    
    public static ArrayList<String> getCompleteChapterAbbreviation() {
        String[] bible = CheckBibleApp.getContext().getResources()
                .getStringArray(R.array.abbreviation_bible);
        ArrayList<String> completeList = new ArrayList<>();
        for (Integer i : getCompleteChapterPosition()) {
            completeList.add(bible[i]);
        }
        return completeList;
    }

    public static ArrayList<String> getPlanedChapterAbbreviation() {
        String[] bible = CheckBibleApp.getContext().getResources()
                .getStringArray(R.array.abbreviation_bible);
        ArrayList<String> completeList = new ArrayList<>();
        for (Integer i : getPlanedChapterPosition()) {
            completeList.add(bible[i]);
        }
        return completeList;
    }

}
