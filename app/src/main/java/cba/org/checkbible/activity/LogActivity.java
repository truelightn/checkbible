package cba.org.checkbible.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-11-07.
 */

public class LogActivity extends AppCompatActivity {

    ListView mListview;
    LogListViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mAdapter = new LogListViewAdapter();

        mListview = (ListView)findViewById(R.id.log_list_view);
        mListview.setAdapter(mAdapter);

        ArrayList<PlanItem> planItemList = PlanDBUtil.getAllPlanItem();
        for (PlanItem planItem : planItemList) {
            mAdapter.addItem(planItem.title, planItem.endTime, planItem.totalCount + "장", null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class LogListViewItem {
        private String titleStr;
        private String duringStr;
        private String totalStr;
        private GridView bibleView;

        public void setTitle(String title) {
            titleStr = title;
        }

        public String getTitle() {
            return this.titleStr;
        }

        public void setDuring(String during) {
            duringStr = during;
        }

        public String getDuring() {
            return this.duringStr;
        }

        public void setTotal(String total) {
            totalStr = total;
        }

        public String getTotal() {
            return totalStr;
        }

        public void setBibleView(GridView bibleView) {
            this.bibleView = bibleView;
        }

        public GridView getBibleView() {
            return bibleView;
        }
    }

    public class LogListViewAdapter extends BaseAdapter {
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<LogListViewItem> listViewItemList = new ArrayList<>();

        // ListViewAdapter의 생성자
        public LogListViewAdapter() {

        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_log_item, parent, false);
            }

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView titleTextView = V.get(convertView, R.id.log_title);
            TextView duringTextView = V.get(convertView, R.id.log_during);
            TextView totalTextView = V.get(convertView, R.id.log_total);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            LogListViewItem listViewItem = listViewItemList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            titleTextView.setText(listViewItem.getTitle());
            duringTextView.setText(listViewItem.getDuring());
            totalTextView.setText(listViewItem.getTotal());

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(String title, String during, String total, String[] bible) {
            LogListViewItem item = new LogListViewItem();

            item.setTitle(title);
            item.setDuring(during);
            item.setTotal(total);

            listViewItemList.add(item);
        }
    }
}
