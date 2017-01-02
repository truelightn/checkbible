package org.cba.checkbible.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by jinhwan.na on 2016-10-13.
 */

public class SettingDBUtil {
    private static final String TAG = SettingDBUtil.class.getSimpleName();

    public SettingDBUtil() {

    }

    public static void setSettingValue(String key, String value) {
        Log.v(TAG, "setSettingValue/" + key + "[" + value + "]");
        final ContentValues values = new ContentValues();
        values.put(DB.COL_SETTING_KEY, key);
        values.put(DB.COL_SETTING_VALUE, value);
        String where = DB.COL_SETTING_KEY + " = \'" + key + "\'";

        if (DBUtil.getInstance().update(DB.TABLE_SETTING, values, where) == 0) {
            DBUtil.getInstance().insert(DB.TABLE_SETTING, values);
        }
    }

    public static String getSettingValue(String key) {
        String retValue = "";
        if (TextUtils.isEmpty(key)) {
            return retValue;
        }
        String selection = DB.COL_SETTING_KEY + " = \'" + key + "\'";
        Cursor c = null;
        try {
            c = DBUtil.getInstance().query(DB.TABLE_SETTING, new String[] { DB.COL_SETTING_VALUE },
                    selection);
            if (c != null) {
                if (c.moveToFirst()) {
                    String value = c.getString(c.getColumnIndexOrThrow(DB.COL_SETTING_VALUE));
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

    public static void initialize() {
    }
}
