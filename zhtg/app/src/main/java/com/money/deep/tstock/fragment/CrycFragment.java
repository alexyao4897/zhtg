package com.money.deep.tstock.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.money.deep.tstock.R;
import com.money.deep.tstock.activity.AdviceActivity;
import com.money.deep.tstock.activity.KLineActivity;
import com.money.deep.tstock.activity.LoginActivity;
import com.money.deep.tstock.activity.SearchActivity;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.framework.picker.DatePicker;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StockPredictItem;
import com.money.deep.tstock.util.DateUtils;
import com.money.deep.tstock.util.SPUtils;
import com.money.deep.tstock.view.ColorBar;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/8/29.
 */
public class CrycFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.zixuan_tv)
    TextView zixuanTv;
    @Bind(R.id.zixuan_slide)
    TextView zixuanSlide;
    @Bind(R.id.linear_zixuan)
    LinearLayout linearZixuan;
    @Bind(R.id.pullstocklistview)
    PullToRefreshListView pullstocklistview;
    @Bind(R.id.recent_predict_time_tv)
    TextView recentPredictTimeTv;
    @Bind(R.id.nodata_view)
    LinearLayout nodataView;
    @Bind(R.id.refresh_view)
    RelativeLayout refreshView;
    @Bind(R.id.tip_view)
    RelativeLayout tipView;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.add_img_btn)
    ImageView addImgBtn;
    @Bind(R.id.add_stock_view)
    LinearLayout addStockView;
    @Bind(R.id.clost_tip_btn)
    ImageView clostTipBtn;
    @Bind(R.id.predict_tv)
    TextView predictTv;
    @Bind(R.id.predict_slide)
    TextView predictSlide;
    @Bind(R.id.linear_predict)
    LinearLayout linearPredict;
    @Bind(R.id.other_tv)
    TextView otherTv;
    @Bind(R.id.other_slide)
    TextView otherSlide;
    @Bind(R.id.linear_other)
    LinearLayout linearOther;
    @Bind(R.id.emudeal_name_list)
    LinearLayout emudealNameList;
    @Bind(R.id.emudeal_name_view)
    RelativeLayout emudealNameView;
    @Bind(R.id.time_tv)
    TextView timeTv;
    @Bind(R.id.time_layout)
    RelativeLayout timeLayout;
    @Bind(R.id.jianyi_tv)
    TextView jianyiTv;
    @Bind(R.id.buy_tv)
    TextView buyTv;
    @Bind(R.id.jianyi_oval_view)
    RelativeLayout jianyiOvalView;
    @Bind(R.id.jianyi_text_tv)
    TextView jianyiTextTv;
    @Bind(R.id.wenhao_iv)
    ImageView wenhaoIv;
    @Bind(R.id.wenhao_view)
    RelativeLayout wenhaoView;
    @Bind(R.id.update_time_tv)
    TextView updateTimeTv;
    @Bind(R.id.predict_jianyi_layout)
    RelativeLayout predictJianyiLayout;
    @Bind(R.id.predict_suggestion_tv)
    TextView predictSuggestionTv;

    private MyAdapter myAdapter;
    private TextView time_tv;
    private String current_year;
    private String current_month;
    private int cur_month;
    private String current_day;
    String current_time;
    String select_day;
    private String current_tag;
    private String params = "";
    String date;

    private static CrycFragment INSTANCE;

    public static CrycFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrycFragment();
        }
        return INSTANCE;
    }

    public CrycFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cryc_fragment, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        initEvent();
        return view;
    }


    private void initView(View view) {
//        time_tv = (TextView) view.findViewById(R.id.time_tv);
//        pullstocklistview.setMode(PullToRefreshBase.Mode.BOTH);
        predictTv.setTextColor(getResources().getColor(R.color.red_color));
        predictSlide.setVisibility(View.VISIBLE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        date = dateFormat.format(new Date());
        System.out.println("select_day:" + select_day);
        select_day = date;
//        predictJianyiLayout.setVisibility(View.VISIBLE);
        initPredictData("20", date, "");
    }

    private void initEvent() {

        pullstocklistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                String time = df.format(new Date());
                recentPredictTimeTv.setText("最近预测时间:" + time);
                pullstocklistview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (current_tag.equals("2")) {
                            params = SPUtils.get(getActivity(), "stockCodes", "").toString();
                            getZixuanStock(params, "1", select_day);
                        } else {
                            initPredictData("20", select_day, "");
                        }

                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //               params = "sh000001,sz399001,sh601003,sh601001,sz300173,sh600735,sh002245,sh600959,sh600089";
                SPUtils.put(getActivity(), "isAddZxStock", true);
                if(MyApplication.login_stauts){
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
//                params = SPUtils.get(getActivity(), "stockCodes", "").toString();
//                getZixuanStock(params, "1", select_day);
            }
        });
    }

    public void initStockRecommendView(String canTrade, String date) {
        if (canTrade.equals("true")) {
            jianyiOvalView.setBackgroundResource(R.drawable.predict_buy_bg_oval);
            jianyiTv.setText("建议");
            buyTv.setText("买入");
            predictSuggestionTv.setText("预测大盘明日上涨,建议买入优选股");
        } else {
            jianyiOvalView.setBackgroundResource(R.drawable.predict_wait_bg_oval);
            jianyiTv.setText("建议");
            buyTv.setText("观望");
            predictSuggestionTv.setText("大盘明日趋势不明朗,建议观望");
        }
        updateTimeTv.setText("预测股更新于:" + date);
    }

    private void initData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String date = sDateFormat.format(new Date());
        current_year = date.substring(0, 4);
        current_month = date.substring(5, 7);
        current_day = date.substring(8, 10);
        cur_month = Integer.valueOf(current_month);


        if (current_day.indexOf("0") == 0) {
            current_day = current_day.replace("0", "");
        }
        current_time = date.substring(11, date.length());
        recentPredictTimeTv.setText("最近预测时间:" + current_time);
        String currentday = current_year + current_month + current_day;
        select_day = currentday;
        current_tag = "3";
        myAdapter = new MyAdapter(getActivity());
        pullstocklistview.setAdapter(myAdapter);
    }

    private void getZixuanStock(String params, String country, String day) {
//        progressbar.setVisibility(View.VISIBLE);
        addStockView.setVisibility(View.GONE);
        DataManager.stockSpecDay(params, country, day, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                        response.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
                if (stockPredictItems != null && stockPredictItems.size() > 0) {
                    String predict_day = stockPredictItems.get(0).getPredictDate();
                    select_day = predict_day.replace("-", "");
                    timeTv.setText(predict_day.substring(5, predict_day.length()));
                    String params = new String();
                    for (int i = 0; i < stockPredictItems.size(); i++) {
                        String str = stockPredictItems.get(i).getStockCode();
                        params += str + ",";
                        System.out.println("params:" + params);
                    }
                    if (params != null) {
                        DataManager.getInfo(params, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                                if (entries != null && entries.size() > 0) {
//                                    refreshView.setVisibility(View.VISIBLE);
//                                    tipView.setVisibility(View.VISIBLE);
                                    nodataView.setVisibility(View.GONE);
                                    pullstocklistview.setVisibility(View.VISIBLE);
                                    myAdapter.refreshData(stockPredictItems, entries);
                                    pullstocklistview.onRefreshComplete();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }
                } else {
                    refreshView.setVisibility(View.GONE);
                    tipView.setVisibility(View.GONE);
                    pullstocklistview.setVisibility(View.GONE);
                    nodataView.setVisibility(View.VISIBLE);
                    System.out.println("no data no data");
//                    pullstocklistview.onRefreshComplete();
                }
//                progressbar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void initPredictData(String count, String day, String sort_by) {
        DataManager.rankAll(count, day, sort_by, new ResponseListener("rankAll") {
            @Override
            public void onSuccess(JSONObject s) {
                final ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                        s.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
                final String StockRecommendTrade = s.optString("StockRecommendTrade");
                final String StockPredictTime = s.optString("StockPredictTime");
                String predict_day = s.optString("StockPredictDate");
                select_day = predict_day.replace("-", "");
                timeTv.setText(predict_day.substring(5, predict_day.length()));
                if (stockPredictItems != null && stockPredictItems.size() > 0) {
                    String params = new String();
                    for (int i = 0; i < stockPredictItems.size(); i++) {
                        String str = stockPredictItems.get(i).getStockCode();
                        params += str + ",";
                    }
                    DataManager.getInfo(params, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.equals("")) {
                                ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                                if (entries != null && entries.size() > 0) {
                                    pullstocklistview.setVisibility(View.VISIBLE);
                                    nodataView.setVisibility(View.GONE);
                                    predictJianyiLayout.setVisibility(View.VISIBLE);
                                    initStockRecommendView(StockRecommendTrade, StockPredictTime);
                                    myAdapter.refreshData(stockPredictItems, entries);
                                    pullstocklistview.onRefreshComplete();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                } else {
                    predictJianyiLayout.setVisibility(View.GONE);
                    pullstocklistview.setVisibility(View.GONE);
                    refreshView.setVisibility(View.GONE);
                    tipView.setVisibility(View.GONE);
                    nodataView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(JSONObject s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(current_tag.equals("2")){
            params = SPUtils.get(getActivity(), "stockCodes", "").toString();
            checkhasZixuanStock();
        }
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

    @OnClick({R.id.linear_zixuan, R.id.clost_tip_btn, R.id.linear_predict, R.id.linear_other, R.id.time_tv, R.id.time_layout, R.id.wenhao_iv, R.id.wenhao_view})
    public void onClick(View view) {
        switch (view.getId()) {
//            case 0:
//                Toast.makeText(getActivity(), "one click", Toast.LENGTH_SHORT).show();
//                break;
//            case 1:
//                Toast.makeText(getActivity(), "two click", Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                Toast.makeText(getActivity(), "three click", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.linear_zixuan:
                zixuanTv.setTextColor(getResources().getColor(R.color.red_color));
                emudealNameView.setVisibility(View.GONE);
                zixuanSlide.setVisibility(View.VISIBLE);
                predictTv.setTextColor(getResources().getColor(R.color.common_black));
                otherTv.setTextColor(getResources().getColor(R.color.common_black));
                predictSlide.setVisibility(View.GONE);
                otherSlide.setVisibility(View.GONE);
                predictJianyiLayout.setVisibility(View.GONE);
                current_tag = "2";
                pullstocklistview.setAdapter(myAdapter);
                checkhasZixuanStock();
                break;
            case R.id.clost_tip_btn:
                tipView.setVisibility(View.GONE);
                break;

            case R.id.time_tv:
            case R.id.time_layout:
                DatePicker datePicker = new DatePicker(getActivity());
                int year = Integer.parseInt(select_day.substring(0, 4));
                int month = Integer.parseInt(select_day.substring(4, 6));
                int day = Integer.parseInt(select_day.substring(6, 8));
                System.out.println("select_day:" + select_day);
                System.out.println("year:" + year);
                System.out.println("month:" + month);
                System.out.println("day:" + day);
                System.out.println("select_day:" + select_day);
                if (month == cur_month - 1 && day <= Integer.valueOf(current_day)) {
                    return;
                }

                String beforeyear = DateUtils.getYear(DateUtils.getBeforeYear(new Date()));
                datePicker.setRange(Integer.parseInt(beforeyear), Integer.parseInt(current_year));
                if (cur_month == 1) {
                    datePicker.setMonthRange(12, 1);
                } else {
                    datePicker.setMonthRange(cur_month - 1, cur_month);
                }
                datePicker.setDayRange(1, Integer.valueOf(current_day));

                datePicker.setSelectedItem(year, month, day);
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {

                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        System.out.println("onDatePicked:" + year + "年" + month + "月" + day);
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        select_day = year + month + day;
                        System.out.println("选择的select_day:" + select_day);
                        if (month.length() > 1 && month.indexOf("0") == 0) {
                            month = month.substring(1);
                        }
                        if (day.length() > 1 && day.indexOf("0") == 0) {
                            day = day.substring(1);
                        }
                        timeTv.setText(month + "-" + day);
                        if (current_tag.equals("2")) {
                            boolean isaddZxStock = (Boolean) SPUtils.get(getActivity(), "isAddZxStock", false);
                            if (!isaddZxStock) {
                                addStockView.setVisibility(View.VISIBLE);
                                refreshView.setVisibility(View.GONE);
                                tipView.setVisibility(View.GONE);
                                nodataView.setVisibility(View.GONE);
                                pullstocklistview.setVisibility(View.GONE);
                            } else {
                                params = SPUtils.get(getActivity(), "stockCodes", "").toString();
                                getZixuanStock(params, "1", select_day);
                            }
                        } else {
                            System.out.println("click predice stock");
                            initPredictData("20", select_day, "");
                        }

                    }
                });
                datePicker.show();
                break;
            case R.id.linear_predict:
                current_tag = "3";
                emudealNameView.setVisibility(View.GONE);
                addStockView.setVisibility(View.GONE);
                nodataView.setVisibility(View.GONE);
                pullstocklistview.setVisibility(View.VISIBLE);
                myAdapter = new MyAdapter(getActivity());
                pullstocklistview.setAdapter(myAdapter);
                predictTv.setTextColor(getResources().getColor(R.color.red_color));
                predictSlide.setVisibility(View.VISIBLE);
                zixuanTv.setTextColor(getResources().getColor(R.color.common_black));
                otherTv.setTextColor(getResources().getColor(R.color.common_black));
                zixuanSlide.setVisibility(View.GONE);
                otherSlide.setVisibility(View.GONE);
                initPredictData("20", date, "");
//                emudealNameView.setVisibility(View.VISIBLE);
                emudealNameList.removeAllViews();
                for (int i = 0; i < 3; i++) {
                    TextView textView = new TextView(getActivity());
                    textView.setText("机器人" + i);
                    textView.setOnClickListener(this);
                    textView.setId(i);
                    textView.setPadding(10, 15, 10, 15);
                    textView.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.gravity = Gravity.CENTER;
                    lp.weight = 1;
                    emudealNameList.addView(textView, lp);
                }
                break;
            case R.id.linear_other:
                addStockView.setVisibility(View.GONE);
                emudealNameView.setVisibility(View.GONE);
                nodataView.setVisibility(View.GONE);
                pullstocklistview.setVisibility(View.VISIBLE);
                otherTv.setTextColor(getResources().getColor(R.color.red_color));
                otherSlide.setVisibility(View.VISIBLE);
                zixuanTv.setTextColor(getResources().getColor(R.color.common_black));
                predictTv.setTextColor(getResources().getColor(R.color.common_black));
                zixuanSlide.setVisibility(View.GONE);
                predictSlide.setVisibility(View.GONE);

                ArrayList<Emudeal> emudeals = new ArrayList<Emudeal>();
                emudeals.add(new Emudeal("深3号机器人", "24%", "23%", "稳健增利", "一日短线", false));
                emudeals.add(new Emudeal("深2号机器人", "34%", "23%", "稳健增利", "一日短线", true));
                emudeals.add(new Emudeal("深3号机器人", "62%", "23%", "稳健增利", "一日短线", false));
                OtherAdapter otherAdapter = new OtherAdapter(getActivity(), emudeals);
                pullstocklistview.setAdapter(otherAdapter);
                break;
            case R.id.wenhao_iv:
            case R.id.wenhao_view:
                Intent intent = new Intent(getActivity(), AdviceActivity.class);
                startActivity(intent);
                break;
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


    public void checkhasZixuanStock() {
//        boolean isaddZxStock = (Boolean) SPUtils.get(getActivity(), "isAddZxStock", false);
//        System.out.println("isaddZxStock:" + isaddZxStock);
//        if (!isaddZxStock) {
//            addStockView.setVisibility(View.VISIBLE);
//            refreshView.setVisibility(View.GONE);
//            tipView.setVisibility(View.GONE);
//            nodataView.setVisibility(View.GONE);
//            pullstocklistview.setVisibility(View.GONE);
//        } else {
//            addStockView.setVisibility(View.GONE);
//            params = SPUtils.get(getActivity(), "stockCodes", "").toString();
//            getZixuanStock(params, "1", select_day);
//        }
        params = SPUtils.get(getActivity(), "stockCodes", "").toString();
        if(!params.equals("")){
            addStockView.setVisibility(View.GONE);
            getZixuanStock(params, "1", select_day);
        }else{
            addStockView.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.GONE);
            tipView.setVisibility(View.GONE);
            nodataView.setVisibility(View.GONE);
            pullstocklistview.setVisibility(View.GONE);
        }
    }

    private class OtherAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<Emudeal> mData = new ArrayList<Emudeal>();

        public OtherAdapter(Context context, ArrayList<Emudeal> data) {
            this.mData = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Emudeal emudeal = mData.get(position);
            convertView = inflater.inflate(R.layout.emudeal_buy_item, null);
            TextView name = (TextView) convertView.findViewById(R.id.emudeal_tv);
            TextView earn_rate_tv = (TextView) convertView.findViewById(R.id.earn_rate_tv);
            TextView current_rate_tv = (TextView) convertView.findViewById(R.id.current_rate_tv);
            TextView stock_param_tv = (TextView) convertView.findViewById(R.id.stock_param_tv);
            TextView stock_label = (TextView) convertView.findViewById(R.id.stock_label);
            TextView buy_tv = (TextView) convertView.findViewById(R.id.buy_tv);
            name.setText(emudeal.getName());
            earn_rate_tv.setText(emudeal.getEarn_rate());
            current_rate_tv.setText(emudeal.getCur_rate());
            stock_param_tv.setText(emudeal.getParam());
            stock_label.setText(emudeal.getLabel());
            if (emudeal.isBuy()) {
                buy_tv.setBackgroundResource(R.drawable.red_rect);
                buy_tv.setText("立即购买(试用99元)");
            } else {
                buy_tv.setBackgroundResource(R.drawable.grey_rect);
                buy_tv.setText("免费试用");
            }
            return convertView;
        }
    }

//    class EmuDealViewHolder{
//        Text
//    }

    class Emudeal {
        String name;
        String earn_rate;
        String cur_rate;
        String param;
        String label;
        boolean buy;

        Emudeal(String name, String earn_rate, String cur_rate, String param, String label, boolean buy) {
            this.name = name;
            this.earn_rate = earn_rate;
            this.cur_rate = cur_rate;
            this.param = param;
            this.label = label;
            this.buy = buy;
        }

        public String getName() {
            return name;
        }

        public String getEarn_rate() {
            return earn_rate;
        }

        public String getCur_rate() {
            return cur_rate;
        }

        public boolean isBuy() {
            return buy;
        }

        public String getLabel() {
            return label;
        }

        public String getParam() {
            return param;
        }
    }

/*    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<StockPredictItem> mData = new ArrayList<StockPredictItem>();
        private ArrayList<ShareEntry> mEntries = new ArrayList<ShareEntry>();
        private final int TYPE_RANK = 0;
        private final int TYPE_ITEM = 1;
        private final int TYPE_TITLE = 2;

        public MyAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void refreshData(ArrayList<StockPredictItem> data, ArrayList<ShareEntry> entries) {
//            progressbar.setVisibility(View.GONE);
            this.mData = data;
            this.mEntries = entries;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (position > 3) {
                return TYPE_RANK;
            } else if (position == 3) {
                return TYPE_TITLE;
            } else {
                return TYPE_ITEM;
            }

        }


        @Override
        public int getViewTypeCount() {
            return 3;
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
            ViewHolder viewHolder = null;
            RankViewHolder rankViewHolder = null;
            ViewHolderTitle viewHolderTitle = null;
            int type = getItemViewType(position);
            StockPredictItem stockPredictItem = mData.get(position);
            final ShareEntry shareEntry = mEntries.get(position);
            if (convertView == null) {
                switch (type) {
                    case TYPE_RANK:
                        viewHolder = new ViewHolder();
                        convertView = inflater.inflate(R.layout.rank_board_list_item, null, false);
                        viewHolder.num_tv = (TextView) convertView.findViewById(R.id.rank_stock_num_tv);
                        viewHolder.name_tv = (TextView) convertView.findViewById(R.id.rank_stock_name_tv);
                        viewHolder.raingbar = (RatingBar) convertView.findViewById(R.id.ratingbar);
                        convertView.setTag(viewHolder);
                        break;
                    case TYPE_ITEM:
                        rankViewHolder = new RankViewHolder();
                        convertView = inflater.inflate(R.layout.rank_board, null, false);
                        rankViewHolder.num_tv = (TextView) convertView.findViewById(R.id.rank_num_text);
                        rankViewHolder.name_tv = (TextView) convertView.findViewById(R.id.stock_name_tv);
                        rankViewHolder.colorBar = (ColorBar) convertView.findViewById(R.id.color_bar_two);
                        rankViewHolder.rank_linear = (LinearLayout) convertView.findViewById(R.id.rank_linear);
                        convertView.setTag(rankViewHolder);
                        break;
                    case TYPE_TITLE:
                        convertView = inflater.inflate(R.layout.rank_board_title_item, null, false);
//                        viewHolderTitle = new ViewHolderTitle();
//                        convertView.setTag(viewHolderTitle);
                        break;
                }

            } else {
                switch (type) {
                    case TYPE_RANK:
                        viewHolder = (ViewHolder) convertView.getTag();
                        break;
                    case TYPE_ITEM:
                        rankViewHolder = (RankViewHolder) convertView.getTag();
                        break;
                }

            }
            switch (type) {
                case TYPE_RANK:
                    viewHolder.name_tv.setText(shareEntry.getName());
                    viewHolder.num_tv.setText(position + "");
                    viewHolder.raingbar.setRating(Float.parseFloat(stockPredictItem.getProbRise()) / 20f);
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
                    break;
                case TYPE_ITEM:
                    rankViewHolder.name_tv.setText(shareEntry.getName());
                    rankViewHolder.num_tv.setText(position + 1 + "");
                    rankViewHolder.colorBar.setFirstPercent(Float.parseFloat(stockPredictItem.getProbRise()) / 100f);
                    rankViewHolder.colorBar.setSecondPercent(Float.parseFloat(stockPredictItem.getProbSmooth()) / 100f);
                    rankViewHolder.colorBar.setThirdPercent(Float.parseFloat(stockPredictItem.getProbFall()) / 100f);

                    rankViewHolder.rank_linear.setOnClickListener(new View.OnClickListener() {
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
                    break;
            }

            return convertView;
        }
    }*/

    class ViewHolder {
        TextView num_tv;
        TextView name_tv;
        RatingBar raingbar;
    }

    class RankViewHolder {
        TextView num_tv;
        TextView name_tv;
        ColorBar colorBar;
        LinearLayout rank_linear;
    }

    class ViewHolderTitle {

    }
}
