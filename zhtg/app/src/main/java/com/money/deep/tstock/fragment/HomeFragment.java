package com.money.deep.tstock.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.money.deep.tstock.R;
import com.money.deep.tstock.activity.InAppWebViewActivity;
import com.money.deep.tstock.activity.KLineActivity;
import com.money.deep.tstock.activity.LoginActivity;
import com.money.deep.tstock.activity.SearchActivity;
import com.money.deep.tstock.activity.UserCenterActivity;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.listener.FragmentListener;
import com.money.deep.tstock.model.EmuDealer;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StatusInfo;
import com.money.deep.tstock.model.StockPredictItem;
import com.money.deep.tstock.util.DateUtils;
import com.money.deep.tstock.util.SPUtils;
import com.money.deep.tstock.view.ColorBar;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/18.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener {
    @Bind(R.id.user_center_icon_view)
    LinearLayout userCenterIconView;
    @Bind(R.id.search_icon_view)
    LinearLayout searchIconView;
    @Bind(R.id.next_day_list)
    ListView nextDayList;
    @Bind(R.id.next_day_view)
    LinearLayout nextDayView;
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.today_view)
    LinearLayout todayView;
    @Bind(R.id.earn_rate_tv)
    TextView earnRateTv;
    @Bind(R.id.current_rate_tv)
    TextView currentRateTv;
    @Bind(R.id.emudeal_name_tv)
    TextView emudealNameTv;
    @Bind(R.id.operate_day_tv)
    TextView operateDayTv;
    //    @Bind(R.id.earn_rate_three_tv)
//    TextView earnRateThreeTv;
//    @Bind(R.id.earn_rate_six_tv)
//    TextView earnRateSixTv;
    @Bind(R.id.actual_case_view)
    LinearLayout actualCaseView;


    MyAdapter myAdapter;
    MyGridViewAdapter myGridViewAdapter;
    private static HomeFragment INSTANCE;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    FragmentManager fm = null;
    FragmentTransaction ft = null;
    String date;
    String lastdate;
    float density;
    @Bind(R.id.earn_day_rate_tv)
    TextView earnDayRateTv;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.dotLayout)
    LinearLayout dotLayout;
    @Bind(R.id.user_center_iv)
    ImageView userCenterIv;
    @Bind(R.id.search_iv)
    ImageView searchIv;

    private ImageView[] tips;
    private ImageView[] imageViews;
    private List<ImageView> banner_list;
    private List<ImageView> dotViewList;
    private int[] imgIdArray;
    private boolean isLooper;
    private LayoutInflater layoutInflater;
    private int currentItem = 0;
    boolean isAutoPlay = true;//是否自动轮播
    private ScheduledExecutorService scheduledExecutorService;

    public static HomeFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HomeFragment();
        }
        return INSTANCE;
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    ArrayList<StockPredictItem> stockPredictItems = (ArrayList<StockPredictItem>) bundle.getSerializable("stockPredicts");
                    ArrayList<ShareEntry> entries = (ArrayList<ShareEntry>) bundle.getSerializable("entries");
                    myAdapter.refreshData(stockPredictItems, entries);
                    break;
                case 2:
                    ArrayList<ShareEntry> shareEntries = (ArrayList<ShareEntry>) msg.obj;
                    int length = shareEntries.size();
                    int gridviewWidth = (int) (130 * length * density);
                    int itemWidth = (int) (120 * density);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
                    gridview.setLayoutParams(params);
                    gridview.setColumnWidth(itemWidth);
                    gridview.setHorizontalSpacing(10);
                    gridview.setStretchMode(GridView.NO_STRETCH);
                    gridview.setNumColumns(length);

                    myGridViewAdapter.updateData(shareEntries);
                    break;
                case 3:
                    viewpager.setCurrentItem(currentItem);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        myAdapter = new MyAdapter(getActivity());
        nextDayList.setAdapter(myAdapter);
        myGridViewAdapter = new MyGridViewAdapter(getActivity());
        gridview.setAdapter(myGridViewAdapter);

        layoutInflater = LayoutInflater.from(getActivity());
        initScrollBanner();
        if (isAutoPlay) {
            startPlay();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.format(new Date());
        Date lastday = DateUtils.getLastDay(new Date());
        lastdate = dateFormat.format(lastday);
        fm = getActivity().getSupportFragmentManager();
        density = (float) SPUtils.get(getActivity(), "density", 0f);
        initNextDayView("2", date, "0");
        initTodayView("5", lastdate, "1");
        initEmuDealView();

        return view;
    }

    public void initScrollBanner() {
        dotLayout.removeAllViews();
        imgIdArray = new int[]{R.drawable.banner, R.drawable.banner_two};
        banner_list = new ArrayList<ImageView>();
        dotViewList = new ArrayList<ImageView>();

        for (int i = 0; i < imgIdArray.length; i++) {
            ImageView dotView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 10;
            layoutParams.height = 10;

            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.enabletrue_oval);
            } else {
                dotView.setBackgroundResource(R.drawable.enablefalse_oval);
            }
            dotLayout.addView(dotView, layoutParams);
            dotViewList.add(dotView);
        }
        ImageView img_one = (ImageView) layoutInflater.inflate(R.layout.scroll_vew_item, null);
        ImageView img_two = (ImageView) layoutInflater.inflate(R.layout.scroll_vew_item, null);
        img_one.setBackgroundResource(R.drawable.banner);
        img_two.setBackgroundResource(R.drawable.banner_two);
        banner_list.add(img_one);
        banner_list.add(img_two);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter((ArrayList) banner_list);
        viewpager.setAdapter(myPagerAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new MyPageChangeListener());

    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 10, 10, TimeUnit.SECONDS);
    }



    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            synchronized (viewpager) {
                currentItem = (currentItem + 1) % banner_list.size();
                mhandler.sendEmptyMessage(3);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void initNextDayView(String return_count, String day, String sort_by) {
        DataManager.rankAll(return_count, day, sort_by, new ResponseListener("rankAll") {
            @Override
            public void onSuccess(JSONObject s) {
                final ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                        s.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
//                //预测时间下午1点
//                String PredictTimeEarly = response.optString("PredictTimeEarly");
                if (stockPredictItems != null && stockPredictItems.size() > 0) {
                    nextDayView.setVisibility(View.VISIBLE);
                    String params = new String();
                    final ArrayList<StockPredictItem> stockPredicts = new ArrayList<StockPredictItem>();
//                    for (int i = 0; i < stockPredictItems.size(); i++) {
                    for (int i = 0; i < 2; i++) {
                        String str = stockPredictItems.get(i).getStockCode();
                        stockPredicts.add(stockPredictItems.get(i));
                        params += str + ",";
                    }
                    if (params != null && !params.equals("")) {
                        DataManager.getInfo(params, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null && !response.equals("")) {
                                    ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                                    if (entries.size() > 0) {
                                        Message message = mhandler.obtainMessage();
                                        message.what = 1;
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("stockPredicts", stockPredicts);
                                        bundle.putSerializable("entries", entries);
                                        message.setData(bundle);
                                        mhandler.sendMessage(message);
                                    }
//                                    myAdapter.refreshData(stockPredicts, entries);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }
                } else {
                    nextDayView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(JSONObject s) {

            }
        });
    }

    public void initTodayView(String return_count, String day, String sort_by) {
        DataManager.rankAll(return_count, day, sort_by, new ResponseListener("rankAll") {
            @Override
            public void onSuccess(JSONObject s) {
                final ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                        s.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
                if (stockPredictItems != null && stockPredictItems.size() > 0) {
                    todayView.setVisibility(View.VISIBLE);
                    String params = new String();
                    for (int i = 0; i < 5; i++) {
                        String str = stockPredictItems.get(i).getStockCode();
                        params += str + ",";
                    }
                    if (params != null && !params.equals("")) {
                        DataManager.getInfo(params, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response != null && !response.equals("")) {
                                    ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                                    if (entries.size() > 0) {
                                        Message message = mhandler.obtainMessage();
                                        message.what = 2;
                                        message.obj = entries;
                                        mhandler.sendMessage(message);
                                    }
//                                    myAdapter.refreshData(stockPredicts, entries);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }
                } else {
                    todayView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(JSONObject s) {

            }
        });
    }

    public void setData(EmuDealer emuDealer) {
        earnRateTv.setText(emuDealer.getEarningRate() + "%");
        currentRateTv.setText("当前收益率:" + emuDealer.getSuccessRate() + "%");
        emudealNameTv.setText(emuDealer.getStockDealerName());
        operateDayTv.setText("已运行天数:" + emuDealer.getStartDealDays() + "天");
        earnDayRateTv.setText(emuDealer.getEarningRate() + "%");
//        earnRateThreeTv.setText("近3月收益率:" + "+"+emuDealer.getEarningRate90D()+"%");
//        earnRateSixTv.setText("近6月收益率:" + "+"+emuDealer.getEarningRate6M()+"%");
    }

    public void initEmuDealView() {
        String defaultStockBotId = SPUtils.get(getActivity(), "DefaultStockBotId", "1").toString();
        if (defaultStockBotId != null) {
            DataManager.emudealer(defaultStockBotId, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    EmuDealer emuDealer = new Gson().fromJson(response.optString("EmuDealer"), new TypeToken<EmuDealer>() {
                    }.getType());
                    if (emuDealer != null) {
                        actualCaseView.setVisibility(View.VISIBLE);
                        setData(emuDealer);
                    } else {
                        actualCaseView.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }

    }

    private FragmentListener.HomeToPredictListener homeToPredictListener = null;
    private FragmentListener.HomeToNextDayListener homeToNextDayListener = null;
    private FragmentListener.HomeToCaseViewListener homeToCaseViewListener = null;

    public void setHomeToPredictListener(FragmentListener.HomeToPredictListener listener) {
        this.homeToPredictListener = listener;
    }

    public void setHomeToNextDayListener(FragmentListener.HomeToNextDayListener listener) {
        this.homeToNextDayListener = listener;
    }

    public void setHomeToCaseViewListener(FragmentListener.HomeToCaseViewListener listener) {
        this.homeToCaseViewListener = listener;
    }

    @OnClick({R.id.user_center_icon_view, R.id.search_icon_view, R.id.next_day_view, R.id.today_view, R.id.actual_case_view, R.id.user_center_iv,R.id.search_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_center_iv:
            case R.id.user_center_icon_view:

                DataManager.check(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("response:" + response);
                        StatusInfo statusInfo = new Gson().fromJson(response.optString("StatusInfo"),
                                new TypeToken<StatusInfo>() {
                                }.getType());
                        if (statusInfo.getStatusCode().equals("2")) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else if(statusInfo.getStatusCode().equals("0")){
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), UserCenterActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                break;
            case R.id.search_iv:
            case R.id.search_icon_view:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.next_day_view:
                if (homeToPredictListener != null) {
                    homeToPredictListener.goPredictClick();
                }
                break;
            case R.id.today_view:
                if (homeToNextDayListener != null) {
                    homeToNextDayListener.goNextDayView();
                }
                break;
            case R.id.actual_case_view:
                if (homeToCaseViewListener != null) {
                    homeToCaseViewListener.goCaseView();
                }
                break;
//            case R.id.banner_iv:
//                Intent intent_web = new Intent(getActivity(), InAppWebViewActivity.class);
//                startActivity(intent_web);
//                break;
        }
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<StockPredictItem> mData = new ArrayList<StockPredictItem>();
        private ArrayList<ShareEntry> mEntries = new ArrayList<ShareEntry>();

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void refreshData(ArrayList<StockPredictItem> data, ArrayList<ShareEntry> entries) {
            this.mData = data;
            this.mEntries = entries;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            RankViewHolder rankViewHolder = null;
            StockPredictItem stockPredictItem = mData.get(position);
            final ShareEntry shareEntry = mEntries.get(position);
            if (convertView == null) {
                rankViewHolder = new RankViewHolder();
                convertView = inflater.inflate(R.layout.home_rank_board, null, false);
                rankViewHolder.name_tv = (TextView) convertView.findViewById(R.id.stock_name);
                rankViewHolder.num_tv = (TextView) convertView.findViewById(R.id.stock_code);
                rankViewHolder.colorBar = (ColorBar) convertView.findViewById(R.id.color_bar_two);
                convertView.setTag(rankViewHolder);
            } else {
                rankViewHolder = (RankViewHolder) convertView.getTag();
            }
            rankViewHolder.name_tv.setText(shareEntry.getName());
            rankViewHolder.num_tv.setText(shareEntry.getCode());
            rankViewHolder.colorBar.setFirstPercent(Float.parseFloat(stockPredictItem.getProbRise()) / 100f);
            rankViewHolder.colorBar.setSecondPercent(Float.parseFloat(stockPredictItem.getProbSmooth()) / 100f);
            rankViewHolder.colorBar.setThirdPercent(Float.parseFloat(stockPredictItem.getProbFall()) / 100f);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), KLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shareInfo", shareEntry);
                    bundle.putString("current_stock", shareEntry.getQ_code().contains("sh") ? "sh60" : "sz");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class RankViewHolder {
        TextView num_tv;
        TextView name_tv;
        ColorBar colorBar;
    }

    private class MyGridViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<ShareEntry> mShareInfo = new ArrayList<ShareEntry>();

        public MyGridViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void updateData(ArrayList<ShareEntry> data) {
            this.mShareInfo = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mShareInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return mShareInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.home_gridview_item, null);
            TextView stockValue = (TextView) convertView.findViewById(R.id.change_rate_tv);
            TextView stockName = (TextView) convertView.findViewById(R.id.stock_name_tv);
            TextView stockCode = (TextView) convertView.findViewById(R.id.stock_code_tv);
            TextView low_value = (TextView) convertView.findViewById(R.id.low_value_tv);
            TextView high_value = (TextView) convertView.findViewById(R.id.high_value_tv);

            float current, close, change, change_rate, high, low, high_rate, low_rate;
            current = Float.parseFloat(mShareInfo.get(position).getCurrent());
            close = Float.parseFloat(mShareInfo.get(position).getClose());
            high = Float.parseFloat(mShareInfo.get(position).getHigh());
            low = Float.parseFloat(mShareInfo.get(position).getLow());

            change = current - close;
            if (current == 0) {
                change_rate = 0;
            } else {
                change_rate = change / close;
            }
            if (high == 0) {
                high_rate = 0;
            } else {
                high_rate = (high - close) / close;
            }

            if (low == 0) {
                low_rate = 0;
            } else {
                low_rate = (low - close) / close;
            }

            if (change >= 0) {
                stockValue.setBackgroundResource(R.drawable.home_rate_bg_red_rect);
                stockValue.setText("+" + decimalFormat.format(change_rate * 100) + "%");
            } else {
                stockValue.setBackgroundResource(R.drawable.home_rate_bg_green_rect);
                stockValue.setText(decimalFormat.format(change_rate * 100) + "%");
            }
            if (high_rate >= 0) {
                high_value.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                high_value.setText("+" + decimalFormat.format(high_rate * 100) + "%");
            } else {
                high_value.setTextColor(getResources().getColor(R.color.price_down_bg_color));
                high_value.setText(decimalFormat.format(high_rate * 100) + "%");
            }
            if (low_rate >= 0) {
                low_value.setTextColor(getResources().getColor(R.color.subview_text_red_color));
                low_value.setText("+" + decimalFormat.format(low_rate * 100) + "%");
            } else {
                low_value.setTextColor(getResources().getColor(R.color.price_down_bg_color));
                low_value.setText(decimalFormat.format(low_rate * 100) + "%");
            }
            stockName.setText(mShareInfo.get(position).getName());
            stockCode.setText(mShareInfo.get(position).getCode());


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), KLineActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shareInfo", mShareInfo.get(position));
                    bundle.putString("current_stock", mShareInfo.get(position).getQ_code().contains("sh") ? "sh60" : "sz");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getRequestQueue().cancelAll(DataManager.REQUEST_TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("home onDestroy...........");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % imageViews.length);
//        int newPosition = position % viewList.size();
//        int length = imgIdArray.length;
//        for(int i=0;i<length;i++){
//            tips[newPosition].setBackgroundResource(R.drawable.enabletrue_oval);
//            if(newPosition != i){
//                tips[i].setBackgroundResource(R.drawable.enablefalse_oval);
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    @Override
    public void onResume() {
        super.onResume();
    }

    public class MyPagerAdapter extends PagerAdapter {
        private ArrayList<ImageView> viewList;

        public MyPagerAdapter(ArrayList<ImageView> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = viewList.get(position);
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            ((ViewPager) container).addView(viewList.get(position));
            if (position == 0) {
                viewList.get(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent_web = new Intent(getActivity(), InAppWebViewActivity.class);
                        startActivity(intent_web);
                    }
                });
            }
            return viewList.get(position);
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewpager.setCurrentItem(0);
                    } else if (viewpager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewpager.setCurrentItem(viewpager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for (int i = 0; i < dotViewList.size(); i++) {
                if (i == position) {
                    ((View) dotViewList.get(position)).setBackgroundResource(R.drawable.enabletrue_oval);
                } else {
                    ((View) dotViewList.get(i)).setBackgroundResource(R.drawable.enablefalse_oval);
                }
            }
        }
    }

}
