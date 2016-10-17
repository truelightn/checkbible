package cba.org.checkbible.context;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by jinhwan.na on 2016-10-14.
 */


public class AppContext extends ContextWrapper {
    private static AppContext sMe;

    public AppContext(Context base) {
        super(base);
        sMe = this;
    }

    public static Context get() {
        return sMe;
    }
}

