package cba.org.checkbible;

import android.app.Application;
import android.content.Context;

import cba.org.checkbible.db.DBUtil;

/**
 * Created by jinhwan.na on 2016-10-14.
 */

public class CheckBibleApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        DBUtil.getInstance();

    }

    public static Context getContext(){
        return mContext;
    }
}
