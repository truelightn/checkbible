package cba.org.checkbible.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import cba.org.checkbible.CheckBibleApp;


/**
 * Created by jinhwan.na on 2016-10-14.
 */

public class DBUtil {
    private static final String TAG = DBUtil.class.getSimpleName();

    CheckBibleDBOpenHelper mOpenHelper;
    SQLiteDatabase mDB;
    private static DBUtil sInstance;

    public static DBUtil getInstance() {
        if (sInstance == null) {
            sInstance = new DBUtil();
        }
        return sInstance;
    }
    private DBUtil() {
        if (mOpenHelper == null) {
            mOpenHelper = new CheckBibleDBOpenHelper(CheckBibleApp.getContext());
            mDB = mOpenHelper.getWritableDatabase();
        }
    }

    public long insert(String table, ContentValues value) {
        return mDB.insert(table, null, value);
    }

    public int update(String table, ContentValues value, String where) {
        return mDB.update(table, value, where, null);
    }

    public int delete(String table, String where) {
        return mDB.delete(table, where, null);
    }

    public Cursor query(String table, String[] columns, String selection) {
        return mDB.query(table, columns, selection, null, null, null, null);
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
            db.execSQL(DB.CREATE_TABLE_READINGPLAN);
            db.execSQL(DB.CREATE_TABLE_SETTING);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
