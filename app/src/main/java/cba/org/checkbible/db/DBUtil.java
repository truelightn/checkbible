package cba.org.checkbible.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cba.org.checkbible.context.AppContext;

/**
 * Created by jinhwan.na on 2016-10-14.
 */

public class DBUtil {
    CheckBibleDBOpenHelper mOpenHelper;
    SQLiteDatabase mDB;

    public DBUtil() {
        mOpenHelper = new CheckBibleDBOpenHelper(AppContext.get());
        mDB = mOpenHelper.getWritableDatabase();
    }

    public void insert(){
        ContentValues addRowValue = new ContentValues();
//        addRowValue.put();
    }

    /**
     * Created by jinhwan.na on 2016-10-13.
     */

    public static class CheckBibleDBOpenHelper extends SQLiteOpenHelper {
        static final String DB_NAME = "checkbible.db";
        static final int DB_VERSION = 1;

        public CheckBibleDBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB.CREATE_TABLE_SETTING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
