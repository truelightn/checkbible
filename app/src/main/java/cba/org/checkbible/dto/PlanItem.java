package cba.org.checkbible.dto;

import android.content.ContentValues;
import android.database.Cursor;

import cba.org.checkbible.db.DB;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanItem {
    public int id;
    public String title = "";
    public String planedChapter = "";
    public String compeltedChapter = "";
    public int totalCount;
    public int todayReadCount;
    public int chapterReadCount;
    public int totalReadCount;
    public String startTime = "";
    public String endTime = "";
    public boolean complete;

    public PlanItem() {
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DB.COL_READINGPLAN_TITLE, this.title);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, this.planedChapter);
        values.put(DB.COL_READINGPLAN_COMPLETED_CHAPTER, this.compeltedChapter);
        values.put(DB.COL_READINGPLAN_TOTAL_COUNT, this.totalCount);
        values.put(DB.COL_READINGPLAN_TODAY_READ_COUNT, this.todayReadCount);
        values.put(DB.COL_READINGPLAN_CHAPTER_READ_COUNT, this.chapterReadCount);
        values.put(DB.COL_READINGPLAN_TOTAL_READ_COUNT, this.totalReadCount);
        values.put(DB.COL_READINGPLAN_START_DATE, this.startTime);
        values.put(DB.COL_READINGPLAN_END_DATE, this.endTime);
        values.put(DB.COL_READINGPLAN_COMPLETE, this.complete ? 1 : 0);
        values.put(DB.COL_READINGPLAN_IS_ACTIVE, 1);
        return values;
    }

    public void setValues(Cursor cursor) {
        this.id = getInt(cursor, DB.COL_READINGPLAN_ID);
        this.title = getString(cursor, DB.COL_READINGPLAN_TITLE);
        this.planedChapter = getString(cursor, DB.COL_READINGPLAN_PLANED_CHAPTER);
        this.compeltedChapter = getString(cursor, DB.COL_READINGPLAN_COMPLETED_CHAPTER);
        this.totalCount = getInt(cursor, DB.COL_READINGPLAN_TOTAL_COUNT);
        this.todayReadCount = getInt(cursor, DB.COL_READINGPLAN_TODAY_READ_COUNT);
        this.chapterReadCount = getInt(cursor, DB.COL_READINGPLAN_CHAPTER_READ_COUNT);
        this.totalReadCount = getInt(cursor, DB.COL_READINGPLAN_TOTAL_READ_COUNT);
        this.startTime = getString(cursor, DB.COL_READINGPLAN_START_DATE);
        this.endTime = getString(cursor, DB.COL_READINGPLAN_END_DATE);
        this.complete = getInt(cursor, DB.COL_READINGPLAN_COMPLETE) == 1;

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
        return -1;
    }

    public int getId() {
        return id;
    }
}
