package org.cba.checkbible;

import android.app.Application;
import android.content.Context;

import org.cba.checkbible.db.DBUtil;
import org.cba.checkbible.db.SettingDBUtil;

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
