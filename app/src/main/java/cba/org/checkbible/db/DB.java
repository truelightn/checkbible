package cba.org.checkbible.db;

/**
 * Created by jinhwan.na on 2016-10-13.
 */

public class DB {
    // Table
    static final String TABLE_SETTING = "setting";

    // Settings Database columns
    public static final String COL_SETTING_KEY = "key";
    public static final String COL_SETTING_VALUE = "value";

    // SQL
    public final static String CREATE_TABLE_SETTING = "CREATE TABLE " + DB.TABLE_SETTING + " ("
            + "_id INTEGER PRIMARY KEY, " + DB.COL_SETTING_KEY + " TEXT, " + DB.COL_SETTING_VALUE
            + " TEXT); ";
}
