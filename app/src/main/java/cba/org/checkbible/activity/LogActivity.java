package cba.org.checkbible.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cba.org.checkbible.PlanManager;
import cba.org.checkbible.R;
import cba.org.checkbible.afw.V;
import cba.org.checkbible.db.DB;
import cba.org.checkbible.db.PlanDBUtil;
import cba.org.checkbible.dto.PlanItem;

/**
 * Created by jinhwan.na on 2016-11-07.
 */

public class LogActivity extends AppCompatActivity {
    public static final String TAG = LogActivity.class.getSimpleName();
    ListView mListview;
    LogListViewAdapter mAdapter;
    ArrayList<PlanItem> mPlanItemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        mAdapter = new LogListViewAdapter();

        mListview = (ListView) findViewById(R.id.log_list_view);
        mListview.setAdapter(mAdapter);
        mListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        registerForContextMenu(mListview);

        mPlanItemList = PlanDBUtil.getAllPlanItem();
        for (PlanItem planItem : mPlanItemList) {
            Log.e(TAG, "add item title " + planItem.title);
            mAdapter.addItem(planItem);
        }
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.log_context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(mPlanItemList.get(info.position).title);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        int index = info.position; // AdapterView안에서 ContextMenu를 보여즈는 항목의 위치
        switch (item.getItemId()) {
        case R.id.context_delete:
            PlanDBUtil.removePlan(mPlanItemList.get(index).getId());
            mAdapter.remove(index);
            mAdapter.notifyDataSetChanged();
            break;
        case R.id.context_select:
            PlanDBUtil.setCurrentActiveRowToInActive();
            PlanDBUtil.updateValueByID(DB.COL_READINGPLAN_IS_ACTIVE, 1, mPlanItemList.get(index).getId());
            mAdapter.changeActive(index);
            mAdapter.notifyDataSetChanged();
            break;
            default:
            return super.onContextItemSelected(item);
        }
        return true;
    }

    public class LogListViewAdapter extends BaseAdapter {
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<PlanItem> listViewItemList = new ArrayList<>();

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
            PlanItem listViewItem = listViewItemList.get(position);
            if (listViewItem.getActive() == 1) {
                titleTextView.setTextColor(Color.RED);
            } else {
                titleTextView.setTextColor(Color.GRAY);
            }
            // 아이템 내 각 위젯에 데이터 반영
            titleTextView.setText(listViewItem.getTitle());
            duringTextView.setText(listViewItem.getStartTime() + "~" + listViewItem.getEndTime());
            totalTextView.setText(String.valueOf(listViewItem.getTotalCount()));

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

        public void remove(int position) {
            listViewItemList.remove(position);
        }

        public void addItem(PlanItem planItem) {
            listViewItemList.add(planItem);
        }

        public void changeActive(int position) {
            for (PlanItem planItem : listViewItemList) {
                if (planItem.active == 1) {
                    planItem.active = 0;
                }
            }
            listViewItemList.get(position).setActive(1);
        }
    }
}
