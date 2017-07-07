package com.money.deep.tstock.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.component.FlowLayout;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.listener.FragmentListener;
import com.money.deep.tstock.model.EmuDealer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fengxg on 2016/8/29.
 */
public class GmFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.viewgroup)
    LinearLayout viewgroup;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    //    @Bind(R.id.case_listview)
//    ListView caseListview;
    private static GmFragment INSTANCE;
    @Bind(R.id.pullstocklistview)
    PullToRefreshListView caseListview;


    private FragmentManager fm;
    private FragmentTransaction ft;

    private ImageView[] tips;
    private ImageView[] imageViews;
    private int[] imgIdArray;
    private GmFragment gmFragment;
    private CaseDetailFragment caseDetailFragment;
    MyCaseAdapter myCaseAdapter;


    public static GmFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GmFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gm_fragment, container, false);
        ButterKnife.bind(this, view);
//        initData();
        imgIdArray = new int[]{R.drawable.home_banner, R.drawable.home_banner, R.drawable.home_banner};
        gmFragment = GmFragment.this;
        fm = getActivity().getSupportFragmentManager();
        ft = fm.beginTransaction();
        tips = new ImageView[imgIdArray.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.enabletrue_oval);
            } else {
                tips[i].setBackgroundResource(R.drawable.enablefalse_oval);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            viewgroup.addView(imageView, layoutParams);
        }

        imageViews = new ImageView[imgIdArray.length];
        for (int i = 0; i < imageViews.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }
        viewpager.setAdapter(new MyAdapter());
        viewpager.setOnPageChangeListener(this);

        caseListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                caseListview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData("10");
                    }
                },1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        initData("10");
        return view;
    }


    private void initData(String count) {
        myCaseAdapter = new MyCaseAdapter(getActivity());
        caseListview.setAdapter(myCaseAdapter);

        DataManager.emudealerList(count, new ResponseListener("/emudealer/list") {
            @Override
            public void onSuccess(JSONObject s) {
                ArrayList<EmuDealer> emuDealers = new Gson().fromJson(s.optString("EmuDealerList"),
                        new TypeToken<List<EmuDealer>>() {
                        }.getType());
                if (emuDealers != null) {
                    myCaseAdapter.update(emuDealers);
                    caseListview.onRefreshComplete();
                }
            }

            @Override
            public void onError(JSONObject s) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getRequestQueue().cancelAll(DataManager.REQUEST_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData("10");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % imageViews.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private FragmentListener.GMListener mGMListener = null;

    public void setGMListener(FragmentListener.GMListener listener) {
        this.mGMListener = listener;
    }

    public class MyAdapter extends PagerAdapter {
        public MyAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return imageViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(imageViews[position % imageViews.length]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(imageViews[position % imageViews.length], 0);
            return imageViews[position % imageViews.length];
        }
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.enabletrue_oval);
            } else {
                tips[i].setBackgroundResource(R.drawable.enablefalse_oval);
            }
        }
    }

    class MyCaseAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private ArrayList<EmuDealer> emuDealers = new ArrayList<EmuDealer>();

        public MyCaseAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void update(ArrayList<EmuDealer> emuDealers) {
            this.emuDealers = emuDealers;
            notifyDataSetChanged();
        }

        @Override

        public int getCount() {
            return emuDealers.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final EmuDealer emuDealer = emuDealers.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.case_listview_item, null, false);
                viewHolder.rate_tv = (TextView) convertView.findViewById(R.id.rate_tv);
                viewHolder.day_rate_tv = (TextView) convertView.findViewById(R.id.day_rate_tv);
                viewHolder.stock_name = (TextView) convertView.findViewById(R.id.stock_name_tv);
                viewHolder.operate_day_tv = (TextView) convertView.findViewById(R.id.operate_day_tv);
                viewHolder.stock_key_view = (LinearLayout) convertView.findViewById(R.id.stock_key_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.rate_tv.setText(emuDealer.getEarningRate() + "%");
            viewHolder.day_rate_tv.setText("盈利天比率" + emuDealer.getSuccessRate() + "%");
            viewHolder.stock_name.setText(emuDealer.getStockDealerName());
            String keywords = emuDealer.getKeyWords();
            String[] keys = keywords.split(",");
            viewHolder.stock_key_view.removeAllViews();
            FlowLayout flowLayout = new FlowLayout(getActivity());
            flowLayout.setPadding(5, 5, 5, 5);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            flowLayout.setLayoutParams(lp);
            flowLayout.setPAD_H(10);
            flowLayout.setPAD_V(10);
            for (int i = 0; i < keys.length; i++) {
                TextView textView = new TextView(getActivity());
                textView.setText(keys[i]);
                textView.setPadding(7, 3, 7, 3);
                textView.setBackgroundResource(R.drawable.stock_label_coner_rect);
                textView.setTextColor(getResources().getColor(R.color.stock_label_color));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(10, 3, 0, 3);
//                textView.setLayoutParams(layoutParams);
                flowLayout.addView(textView, layoutParams);
            }
            viewHolder.stock_key_view.addView(flowLayout);
            viewHolder.operate_day_tv.setText("已操作:" + emuDealer.getStartDealDays() + "天");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mGMListener) {
                        mGMListener.onClickItem(position + 1);
                    }
////                    FragmentManager fm = getActivity().getFragmentManager();
////                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.hide(GmFragment.this);
//                    if (caseDetailFragment == null) {
//                        caseDetailFragment = new CaseDetailFragment();
////                        ft.add(R.id.case_fl_content,caseDetailFragment);
//                        ft.add(R.id.fl_content,caseDetailFragment);
//                    }
//                    ft.show(caseDetailFragment);
//                    ft.commitAllowingStateLoss();
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        TextView rate_tv;
        TextView day_rate_tv;
        TextView stock_name;
        TextView stock_label;
        TextView stock_label_two;
        TextView operate_day_tv;
        LinearLayout stock_key_view;
    }
}





