package cba.org.checkbible;

import android.app.Application;
import android.content.Context;

import cba.org.checkbible.db.DBUtil;
import cba.org.checkbible.db.SettingDBUtil;

/**
 * Created by jinhwan.na on 2016-10-14.
 */

public class CheckBibleApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        DBUtil.getInstance();
        SettingDBUtil.initialize();
    }

    public static Context getContext(){
        return mContext;
    }
}
