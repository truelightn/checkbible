package cba.org.checkbible;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;

/**
 * Created by jinhwan.na on 2016-11-01.
 */

public class PlanManager {

    public static ArrayList<Integer> getCompeletChapterPosition(int id) {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_COMPLETED_CHAPTER, id);
        if (TextUtils.isEmpty(chapter)) {
            return null;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }

    public static ArrayList<Integer> getPlanedChapterPosition(int id) {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_PLANED_CHAPTER, id);
        if (TextUtils.isEmpty(chapter)) {
            return null;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }

    public static int calculateTodayCount(int id) {
        int totalCount = PlanDBUtil.getPlanInt(DB.COL_READINGPLAN_TOTAL_COUNT, id);
        long during = getDuringDay(id);
        return (int)(totalCount / during);
    }

    public static long getDuringDay(int id) {
        long oneDay = 24 * 60 * 60 * 1000;
        GregorianCalendar endCalendar = getEndDate(id);
        GregorianCalendar todayCalendar = new GregorianCalendar();

        return (endCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / oneDay + 2;
    }

    public static GregorianCalendar getEndDate(int id) {
        String endDateString = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_END_DATE, id);
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            Date endDate = formatter.parse(endDateString);
            calendar.setTime(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}
