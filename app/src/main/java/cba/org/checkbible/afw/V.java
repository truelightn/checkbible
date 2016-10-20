package cba.org.checkbible.afw;

import android.app.Activity;
import android.view.View;

/**
 * Created by jinhwan.na on 2016-10-20.
 */

public class V {
    public static final String TAG = V.class.getSimpleName();

    @SuppressWarnings("unchecked") public static <T extends View> T get(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    @SuppressWarnings("unchecked") public static <T extends View> T get(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}
