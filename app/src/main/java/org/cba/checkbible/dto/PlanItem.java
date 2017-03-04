package org.cba.checkbible.dto;

import android.content.ContentValues;
import android.database.Cursor;

import org.cba.checkbible.db.DB;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanItem {
    public int id;
    public String title = "";
    public String planedChapter = "";
    public String compeltedChapter = "";
    public int totalCount;
    public int todayCount;
    public int todayReadCount;
    public int chapterReadCount;
    public int totalReadCount;
    public String startTime = "";
    public String endTime = "";
    public boolean complete;
    public int active;

    public PlanItem() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlanedChapter() {
        return planedChapter;
    }

    public String getCompeltedChapter() {
        return compeltedChapter;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTodayCount() {
        return todayCount;
    }

    public int getTodayReadCount() {
        return todayReadCount;
    }

    public int getChapterReadCount() {
        return chapterReadCount;
    }

    public int getTotalReadCount() {
        return totalReadCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int i) {
        active = i;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DB.COL_READINGPLAN_TITLE, title);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planedChapter);
        values.put(DB.COL_READINGPLAN_COMPLETED_CHAPTER, compeltedChapter);
        values.put(DB.COL_READINGPLAN_TOTAL_COUNT, totalCount);
        values.put(DB.COL_READINGPLAN_TODAY_COUNT, todayCount);
        values.put(DB.COL_READINGPLAN_TODAY_READ_COUNT, todayReadCount);
        values.put(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, chapterReadCount);
        values.put(DB.COL_READINGPLAN_TOTAL_READ_COUNT, totalReadCount);
        values.put(DB.COL_READINGPLAN_START_DATE, startTime);
        values.put(DB.COL_READINGPLAN_END_DATE, endTime);
        values.put(DB.COL_READINGPLAN_COMPLETE, complete ? 1 : 0);
        values.put(DB.COL_READINGPLAN_IS_ACTIVE, active);
        return values;
    }

    public void setValues(Cursor cursor) {
        this.id = getInt(cursor, DB.COL_READINGPLAN_ID);
        this.title = getString(cursor, DB.COL_READINGPLAN_TITLE);
        this.planedChapter = getString(cursor, DB.COL_READINGPLAN_PLANED_CHAPTER);
        this.compeltedChapter = getString(cursor, DB.COL_READINGPLAN_COMPLETED_CHAPTER);
        this.totalCount = getInt(cursor, DB.COL_READINGPLAN_TOTAL_COUNT);
        this.todayCount = getInt(cursor, DB.COL_READINGPLAN_TODAY_COUNT);
        this.todayReadCount = getInt(cursor, DB.COL_READINGPLAN_TODAY_READ_COUNT);
        this.chapterReadCount = getInt(cursor, DB.COL_READINGPLAN_CHAPTER_READ_COUNT);
        this.totalReadCount = getInt(cursor, DB.COL_READINGPLAN_TOTAL_READ_COUNT);
        this.startTime = getString(cursor, DB.COL_READINGPLAN_START_DATE);
        this.endTime = getString(cursor, DB.COL_READINGPLAN_END_DATE);
        this.complete = getInt(cursor, DB.COL_READINGPLAN_COMPLETE) == 1;
        this.active = getInt(cursor, DB.COL_READINGPLAN_IS_ACTIVE);
    }

    public String getString(Cursor cursor, String col) {
        String tempString;
        tempString = cursor.getString(cursor.getColumnIndexOrThrow(col));
        if (!tempString.isEmpty()) {
            return tempString;
        }
        return "";
    }

    public int getInt(Cursor cursor, String col) {
        int tempInt;
        tempInt = cursor.getInt(cursor.getColumnIndexOrThrow(col));
        if (tempInt > 0) {
            return tempInt;
        }
        return 0;
    }

}
