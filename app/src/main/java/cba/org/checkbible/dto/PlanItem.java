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
    public int todayCount;
    public int duration;
    public String startTime = "";
    public String endTime = "";
    public boolean complete;
    public ArrayList<Integer> arrayList;

    public PlanItem() {
    }

    public PlanItem(long id, String title, String planedChapter, String compeltedChapter,
            int totalCount, int todayCount, int duration, String startTime, String endTime,
            boolean complete) {
        this.id = id;
        this.title = title;
        this.planedChapter = planedChapter;
        this.compeltedChapter = compeltedChapter;
        this.totalCount = totalCount;
        this.todayCount = todayCount;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
        this.complete = complete;
    }
}
