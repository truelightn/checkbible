package cba.org.checkbible.dto;

import java.util.ArrayList;

/**
 * Created by jinhwan.na on 2016-10-25.
 */

public class PlanItem {
    public long id;
    public String title = "";
    public String planedChapter = "";
    public String compeltedChapter = "";
    public int totalCount;
    public int todayReadCount;
    public int chapterReadCount;
    public int totalReadCount;
    public String startTime = "";
    public String endTime = "";
    public boolean complete;
    public ArrayList<Integer> arrayList;

    public PlanItem() {
    }
}
