package org.cba.checkbible.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinhwan.na on 2016-10-18.
 */

public enum Bible {
    GEN("",50),//0
    EX("", 40),//1
    LEV("", 27),//2
    NUM("", 36),//3
    DEUT("", 34),//4
    JOSH("", 24),//5
    JUDG("", 21),//6
    RUTH("", 4),//7
    SAM1("", 31),//8
    SAM2("", 24),//9
    KGS1("", 22),//10
    KGS2("", 25),//11
    CHR1("", 29),//12
    CHR2("", 36),//13
    EZRA("", 10),//14
    NEH("", 13),//15
    ESTH("", 10),//16
    JOB("", 42),//17
    PS("", 150),//18
    PROV("", 31),//19
    ECCL("", 12),//20
    SONG("", 8),//21
    LS("", 66),//22
    JER("", 52),//23
    LAM("", 5),//24
    EZEK("", 48),//25
    DAN("", 12),//26
    HOS("", 14),//27
    JOEL("", 3),//28
    AMOS("", 9),//29
    OBAD("", 1),//30
    JON("", 4),//31
    MIC("", 7),//32
    NAH("", 3),//33
    HAB("", 3),//34
    ZEPH("", 3),//35
    HAG("", 2),//36
    ZECH("", 14),//37
    MAL("", 4),//38


        /////////
    MT("", 28),//39
    MK("", 16),//40
    LK("", 24),//41
    JN("", 21),//42
    ACTS("", 28),//43
    ROM("", 16),//44
    COR1("", 16),//45
    COR2("", 13),//46
    GAL("", 6),//47
    EPH("", 6),//48
    PHI("", 4),//49
    COL("", 4),//50
    THE1("", 5),//51
    THE2("", 3),//52
    TIMO1("", 6),//53
    TIMO2("", 4),//54
    TIT("", 3),//55
    PHILM("", 1),//56
    HEB("", 13),//57
    JAS("", 5),//58
    PET1("", 5),//59
    PET2("", 3),//60
    JN1("", 5),//61
    JN2("", 1),//62
    JN3("", 1),//63
    JUDE("", 1),//64
    REV("", 22);//65

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
