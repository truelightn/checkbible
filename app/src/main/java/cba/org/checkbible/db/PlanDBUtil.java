package cba.org.checkbible.db;

import android.content.ContentValues;

import cba.org.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanDBUtil {

    public static boolean addPlan(PlanItem planItem){
        boolean result=false;
        if(planItem==null){
            return result;
        }
        final ContentValues values = new ContentValues();
        values.put(DB.COL_READINGPLAN_TITLE, planItem.title);
        values.put(DB.COL_READINGPLAN_PLANED_CHAPTER, planItem.planedChapter);
        values.put(DB.COL_READINGPLAN_COMPLETED_CHAPTER, planItem.compeltedChapter);
        values.put(DB.COL_READINGPLAN_TOTAL_COUNT, planItem.totalCount);
        values.put(DB.COL_READINGPLAN_TODAY_COUNT, planItem.todayCount);
        values.put(DB.COL_READINGPLAN_DURATION, planItem.duration);
        values.put(DB.COL_READINGPLAN_START_DATE, planItem.startTime);
        values.put(DB.COL_READINGPLAN_END_DATE, planItem.endTime);
//        values.put(DB.COL_READINGPLAN_COMPLETE, planItem.complete);

        DBUtil.getInstance().insert(DB.TABLE_READINGPLAN, values);
        result = true;

        return result;

    }
}
