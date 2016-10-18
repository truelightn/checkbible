package cba.org.checkbible.enums;

/**
 * Created by jinhwan.na on 2016-10-18.
 */

public enum Bible {
    GENESIS("Genesis", "Gen",50),
    EXODUS("Exodus", 40);
    private String mTitle;
    private int mCount;

    Bible(String title, String abb, int count) {
        this.mTitle = title;
        this.mCount = count;

    }

    public String getTitle() {
        return mTitle;
    }

    public int getCount() {
        return mCount;
    }
}
