package cba.org.checkbible.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import cba.org.checkbible.CheckBibleApp;
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
    private PlanManager mPlanManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mPlanManager = PlanManager.getInstance(this);
        mAdapter = new LogListViewAdapter(PlanDBUtil.getAllPlanItem());

        mListview = (ListView)findViewById(R.id.log_list_view);
        mListview.setAdapter(mAdapter);
        mListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        registerForContextMenu(mListview);

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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.log_context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        PlanItem pi = (PlanItem)mAdapter.getItem(info.position);
        menu.setHeaderTitle(pi.getTitle());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item
                .getMenuInfo();
        int index = info.position; // AdapterView안에서 ContextMenu를 보여즈는 항목의 위치
        PlanItem pi = (PlanItem)mAdapter.getItem(info.position);
        switch (item.getItemId()) {
        case R.id.context_delete:
            if (pi.getActive() == 1) {
                Toast.makeText(LogActivity.this, "활성화된 계획은 삭제 할수 없습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
            PlanDBUtil.removePlan(pi.getId());
            mAdapter.remove(index);
            mAdapter.notifyDataSetChanged();
            break;
        case R.id.context_select:
            PlanDBUtil.setCurrentActiveRowToInActive();
            PlanDBUtil.updateValueByID(DB.COL_READINGPLAN_IS_ACTIVE, 1, pi.getId());
            mAdapter.changeActive(index);
            mAdapter.notifyDataSetChanged();
            break;
        default:
            return super.onContextItemSelected(item);
        }
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API. See
     * https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder().setName("Log Page") // TODO: Define a
                                                               // title for the
                                                               // content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]")).build();
        return new Action.Builder(Action.TYPE_VIEW).setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED).build();
    }

    public class LogListViewAdapter extends BaseAdapter {
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<PlanItem> mViewItemList = new ArrayList<>();
        private GridView mAbbGridView;

        // ListViewAdapter의 생성자
        public LogListViewAdapter(ArrayList<PlanItem> listViewItemList) {
            this.mViewItemList = listViewItemList;
        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return mViewItemList.size();
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
            PlanItem listViewItem = mViewItemList.get(position);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView titleTextView = V.get(convertView, R.id.log_title);
            TextView duringTextView = V.get(convertView, R.id.log_during);
            TextView totalTextView = V.get(convertView, R.id.log_total);
            TextView chapterTextView = V.get(convertView, R.id.log_chapter);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            ColorStateList oldColors = duringTextView.getTextColors();
            if (listViewItem.getActive() == 1) {
                titleTextView.setTextColor(Color.RED);
            } else if (listViewItem.isComplete()) {
                titleTextView.setTextColor(Color.BLUE);
            } else {
                titleTextView.setTextColor(oldColors);
            }
            // 아이템 내 각 위젯에 데이터 반영
            titleTextView.setText(listViewItem.getTitle());

            String duringStr = listViewItem.getStartTime() + " ~ " + listViewItem.getEndTime();
            duringStr = duringStr.replace("/", ".");
            duringTextView.setText(duringStr);

            String totalMsg = "Total " + listViewItem.getTotalReadCount() + "장/"
                    + listViewItem.getTotalCount() + "장";
            totalTextView.setText(totalMsg);
            chapterTextView.setText(getStrings(listViewItem));

            return convertView;
        }

        public String getStrings(PlanItem listViewItem) {
            String retValue = "말씀";
            boolean isOldTestament = false;
            boolean isNewTestament = false;
            ArrayList<Integer> totalList = new ArrayList<>();
            totalList.addAll(PlanDBUtil.getCompleteChapterPosition(listViewItem.getId()));
            totalList.addAll(PlanDBUtil.getPlanedChapterPosition(listViewItem.getId()));
            ArrayList<Integer> oldTestament = new ArrayList<>();
            for (int i = 0; i < 39; i++) {
                oldTestament.add(i);
            }
            ArrayList<Integer> newTestament = new ArrayList<>();
            for (int i = 39; i < 66; i++) {
                newTestament.add(i);
            }
            isOldTestament = totalList.containsAll(oldTestament);
            isNewTestament = totalList.containsAll(newTestament);
            if (isOldTestament) {
                retValue = retValue + "/구약";
                totalList.removeAll(oldTestament);
            }
            if (isNewTestament) {
                retValue = retValue + "/신약";
                totalList.removeAll(newTestament);
            }
            if (!totalList.isEmpty()) {
                int firsti = totalList.get(0);
                int lasti = totalList.get(totalList.size() - 1);
                if ((lasti - firsti + 1) == totalList.size()) {
                    retValue = retValue + "/" + mPlanManager.getAbbreviation(firsti) + "~"
                            + mPlanManager.getAbbreviation(lasti);

                } else {
                    for (int i = 0; i < totalList.size() - 1; i++) {
                        retValue = retValue + "/" + mPlanManager.getAbbreviation(i);
                    }
                }
            }

            return retValue;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return mViewItemList.get(position);
        }

        public void remove(int position) {
            mViewItemList.remove(position);
        }

        public void changeActive(int position) {
            for (PlanItem planItem : mViewItemList) {
                if (planItem.active == 1) {
                    planItem.active = 0;
                }
            }
            mViewItemList.get(position).setActive(1);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
