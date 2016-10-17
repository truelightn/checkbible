package cba.org.checkbible;

import android.app.Application;

import cba.org.checkbible.context.AppContext;

/**
 * Created by jinhwan.na on 2016-10-14.
 */

public class CheckBibleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new AppContext(getApplicationContext());
    }
}
