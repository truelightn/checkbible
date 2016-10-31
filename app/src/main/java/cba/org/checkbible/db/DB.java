package cba.org.checkbible.db;

/**
 * Created by jinhwan.na on 2016-10-13.
 */

public class DB {
    // Table
    static final String TABLE_BIBLE = "bible";
    static final String TABLE_SETTING = "setting";
    static final String TABLE_READINGPLAN = "reading_plan";

    // Settings Database columns
    public static final String COL_SETTING_KEY = "key";
    public static final String COL_SETTING_VALUE = "value";

    // Reading_Plan Database columns
    public static final String COL_READINGPLAN_TITLE = "title";
    public static final String COL_READINGPLAN_PLANED_CHAPTER = "palned_chapter";
    public static final String COL_READINGPLAN_COMPLETED_CHAPTER = "checked_chapter";
    public static final String COL_READINGPLAN_TOTAL_COUNT = "total_count";
    public static final String COL_READINGPLAN_TODAY_COUNT = "today_count";
    public static final String COL_READINGPLAN_START_DATE = "start_date";
    public static final String COL_READINGPLAN_END_DATE = "end_date";
    public static final String COL_READINGPLAN_DURATION = "duration";
    public static final String COL_READINGPLAN_COMPLETE = "complete";

    // SQL
    public final static String CREATE_TABLE_READINGPLAN =
            "CREATE TABLE " + DB.TABLE_READINGPLAN +" ("
                    + "_id INTEGER PRIMARY KEY, "+
                    DB.COL_READINGPLAN_TITLE + " text, " +
                    DB.COL_READINGPLAN_PLANED_CHAPTER + " text, " +
                    DB.COL_READINGPLAN_COMPLETED_CHAPTER + " text, " +
                    DB.COL_READINGPLAN_TOTAL_COUNT + " integer, " +
                    DB.COL_READINGPLAN_TODAY_COUNT + " integer, " +
                    DB.COL_READINGPLAN_START_DATE + " text, " +
                    DB.COL_READINGPLAN_END_DATE + " text, " +
                    DB.COL_READINGPLAN_DURATION + " integer " +
                    DB.COL_READINGPLAN_COMPLETE + " integer) " ;

    public final static String CREATE_TABLE_SETTING =
            "CREATE TABLE " + DB.TABLE_SETTING + " ("
            + "_id INTEGER PRIMARY KEY, " +
            DB.COL_SETTING_KEY + " TEXT, " +
            DB.COL_SETTING_VALUE   + " TEXT); ";
}
