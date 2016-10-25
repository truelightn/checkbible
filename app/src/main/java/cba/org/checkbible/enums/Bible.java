package cba.org.checkbible.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinhwan.na on 2016-10-18.
 */

public enum Bible {
    GEN("Genesis",50),
    EX("Exodus", 40),
    LEV("", 27),
    NUM("", 36),
    DEUT("", 34),
    JOSH("", 24),
    JUDG("", 21),
    RUTH("", 4),
    SAM1("", 31),
    SAM2("", 24),
    KGS1("", 22),
    KGS2("", 25),
    CHR1("", 29),
    CHR2("", 36),
    EZRA("", 10),
    NEH("", 13),
    ESTH("", 10),
    JOB("", 42),
    PS("", 150),
    PROV("", 31),
    ECCL("", 12),
    SONG("", 8),
    LS("", 66),
    JER("", 52),
    LAM("", 5),
    EZEK("", 48),
    DAN("", 12),
    HOS("", 14),
    JOEL("", 3),
    AMOS("", 9),
    OBAD("", 1),
    JON("", 4),
    MIC("", 7),
    NAH("", 3),
    HAB("", 3),
    ZEPH("", 3),
    HAG("", 2),
    ZECH("", 14),
    MAL("", 4),


        /////////
    MT("", 28),
    MK("", 16),
    LK("", 24),
    JN("", 21),
    ACTS("", 28),
    ROM("", 16),
    COR1("", 16),
    COR2("", 13),
    GAL("", 6),
    EPH("", 6),
    PHI("", 4),
    COL("", 4),
    THE1("", 5),
    THE2("", 3),
    TIMO1("", 6),
    TIMO2("", 4),
    TIT("", 3),
    PHILM("", 1),
    HEB("", 13),
    JAS("", 5),
    PET1("", 5),
    PET2("", 3),
    JN1("", 5),
    JN2("", 1),
    JN3("", 1),
    JUDE("", 1),
    REV("", 22);

    private String mTitle;
    private int mCount;
    private static Map<Integer, Integer> enumCountMap;

    Bible(String title, int count) {
        this.mTitle = title;
        this.mCount = count;

    }

    public static int getCount(int i) {
        if (enumCountMap == null) {
            initializeMap();
        }
        return enumCountMap.get(i);
    }

    private static Map<Integer, Integer> initializeMap() {
        enumCountMap = new HashMap<>();
        for (Bible b : Bible.values()) {
            enumCountMap.put(b.ordinal(), b.getCount());
        }
        return enumCountMap;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getCount() {
        return mCount;
    }
}
