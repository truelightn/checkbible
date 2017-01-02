package org.cba.checkbible.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;

import org.cba.checkbible.CheckBibleApp;
import org.cba.checkbible.R;
import org.cba.checkbible.dto.PlanItem;

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
    public static String getPlanStringByID(String col,int id) {
        String retValue = "";
        Cursor c = null;
        String selection =  DB.COL_READINGPLAN_ID + " = \'" + id + "\'";
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

    public static void updateValueByID(String col, Object item, int id) {
        Cursor c = null;
        String selection =  DB.COL_READINGPLAN_ID + " = \'" + id + "\'";
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

    public static ArrayList<String> getCompleteChapterAbbreviation(int id) {
        String[] bible = CheckBibleApp.getContext().getResources()
                .getStringArray(R.array.abbreviation_bible);
        ArrayList<String> completeList = new ArrayList<>();
        for (Integer i : getCompleteChapterPosition(id)) {
            completeList.add(bible[i]);
        }
        return completeList;
    }

    public static ArrayList<String> getPlanedChapterAbbreviation(int id) {
        String[] bible = CheckBibleApp.getContext().getResources()
                .getStringArray(R.array.abbreviation_bible);
        ArrayList<String> completeList = new ArrayList<>();
        for (Integer i : getPlanedChapterPosition(id)) {
            completeList.add(bible[i]);
        }
        return completeList;
    }

    public static ArrayList<Integer> getCompleteChapterPosition(int id) {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter;
        if (id == 0) {
            chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_COMPLETED_CHAPTER);
        } else {
            chapter = PlanDBUtil.getPlanStringByID(DB.COL_READINGPLAN_COMPLETED_CHAPTER, id);
        }
        if (TextUtils.isEmpty(chapter)) {
            return chapterList;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }

    public static ArrayList<Integer> getPlanedChapterPosition(int id) {
        ArrayList<Integer> chapterList = new ArrayList<>();
        String chapter;
        if (id == 0) {
            chapter = PlanDBUtil.getPlanString(DB.COL_READINGPLAN_PLANED_CHAPTER);
        } else {
            chapter = PlanDBUtil.getPlanStringByID(DB.COL_READINGPLAN_PLANED_CHAPTER, id);
        }
        if (TextUtils.isEmpty(chapter)) {
            return chapterList;
        }
        String[] chapterString = chapter.split("/");
        for (String s : chapterString) {
            chapterList.add(Integer.parseInt(s));
        }
        return chapterList;
    }
}
