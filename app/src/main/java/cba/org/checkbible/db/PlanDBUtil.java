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
        final ContentValues values = planItem.getContentValues();
        DBUtil.getInstance().insert(DB.TABLE_READINGPLAN, values);
        result = true;
        return result;
    }

    public static boolean removePlan(int id) {
        String selection = DB.COL_READINGPLAN_ID + " = \'" + id + "\'";
        return DBUtil.getInstance().delete(DB.TABLE_READINGPLAN, selection) > 1 ? true : false;
    }

    public static ArrayList<PlanItem> getAllPlanItem() {
        ArrayList<PlanItem> retList = new ArrayList<>();
        Cursor c = null;
        c = DBUtil.getInstance().query(DB.TABLE_READINGPLAN, null, null);
        if (c != null) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                PlanItem i = new PlanItem();
                i.setValues(c);
                retList.add(i);
                c.moveToNext();
            }
        }
        c.close();
        return retList;
    }

    public static String getPlanString(String col) {
        String retValue = "";
        Cursor c = null;
        String selection = DB.COL_READINGPLAN_IS_ACTIVE + " = \'" + 1 + "\'";
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

    public static int getPlanInt(String col) {
        int retValue = -1;
        Cursor c = null;
        String selection = DB.COL_READINGPLAN_IS_ACTIVE + " = \'" + 1 + "\'";
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

    public static void updateValue(String col, Object item) {
        Cursor c = null;
        String selection = DB.COL_READINGPLAN_IS_ACTIVE + " = \'" + 1 + "\'";
        final ContentValues values = new ContentValues();
        if (item instanceof String) {
            values.put(col, (String)item);
        }
        if (item instanceof Integer) {
            values.put(col, (int)item);
        }
        try {
            c = DBUtil.getInstance().query(DB.TABLE_READINGPLAN, new String[] { col }, selection);
            if (c != null) {
                DBUtil.getInstance().update(DB.TABLE_READINGPLAN, values, selection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static void setCurrentActiveRowToInActive() {
        updateValue(DB.COL_READINGPLAN_IS_ACTIVE, 0);
    }

    public static boolean hasNoPlanedData() {
        boolean result = false;
        Cursor cursor = DBUtil.getInstance().query(DB.TABLE_READINGPLAN, null, null);
        if (cursor != null) {
            result = cursor.getCount() <= 0;
            cursor.close();
        }
        return result;
    }

}
