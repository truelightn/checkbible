package cba.org.checkbible.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

import cba.org.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanDBUtil {

    public static boolean addPlan(PlanItem planItem) {
        boolean result = false;
        if (planItem == null) {
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
        values.put(DB.COL_READINGPLAN_COMPLETE, planItem.complete ? 1 : 0);

        DBUtil.getInstance().insert(DB.TABLE_READINGPLAN, values);
        result = true;

        return result;
    }

    public static ArrayList<PlanItem> getAllPlanItem() {
        return null;
    }

    public static String getTitle(int id) {

        return getPlanString(DB.COL_READINGPLAN_TITLE, id);
    }

    public static String getPlanString(String col, int id) {
        String retValue = "";
        Cursor c = null;
        String selection = DB.COL_READINGPLAN_ID + " = \'" + id + "\'";
        try {
            c = DBUtil.getInstance().query(DB.TABLE_READINGPLAN, new String[] { col }, selection);
            if (c != null) {
                if (c.moveToFirst()) {
                    String value = c.getString(c.getColumnIndexOrThrow(col));
                    if (!TextUtils.isEmpty(value)) {
                        retValue = value;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return retValue;
    }

    public static int getPlanInt(String col, int id) {
        int retValue = -1;
        Cursor c = null;
        String selection = DB.COL_READINGPLAN_ID + " = \'" + id + "\'";
        try {
            c = DBUtil.getInstance().query(DB.TABLE_READINGPLAN, new String[] { col }, selection);
            if (c != null) {
                if (c.moveToFirst()) {
                    int value = c.getInt(c.getColumnIndexOrThrow(col));
                    retValue = value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return retValue;
    }
}
