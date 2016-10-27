package cba.org.checkbible.db;

import android.content.ContentValues;

import cba.org.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanDBUtil {
    public long id;
    public String title;
    public String planedChapter;
    public String compeltedChapter;
    public int totalCount;
    public int remainCount;
    public int duration;
    public long startTime;
    public long endTime;
    public boolean complete;

    public static void addPlan(PlanItem planItem){
        final ContentValues values = new ContentValues();
        values.put(DB.COL_READINGPLAN_TITLE, planItem.title);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.planedChapter);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.compeltedChapter);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.totalCount);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.remainCount);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.duration);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.startTime);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.endTime);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.complete);

    }
}
