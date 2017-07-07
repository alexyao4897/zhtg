package com.money.deep.tstock.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StockPredictItem;
import com.money.deep.tstock.util.DateUtils;
import com.money.deep.tstock.util.SPUtils;

import org.json.JSONObject;

import java.text.DecimalFormat;
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
public class SpcyFragment extends Fragment {

    @Bind(R.id.zixuan_tv)
    TextView zixuanTv;
    @Bind(R.id.zixuan_slide)
    TextView zixuanSlide;
    @Bind(R.id.linear_zixuan)
    LinearLayout linearZixuan;
    @Bind(R.id.increase_text)
    TextView increaseText;
    @Bind(R.id.gupiao_Relative)
    RelativeLayout gupiaoRelative;
    //    @Bind(R.id.listview)
//    ListView listview;
    @Bind(R.id.right_layout)
    RelativeLayout rightLayout;
    @Bind(R.id.search_icon_img)
    RelativeLayout searchIconImg;
    @Bind(R.id.stock_listview)
    PullToRefreshListView stockListview;
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
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    @Bind(R.id.jianyi_tv)
    TextView jianyiTv;
    @Bind(R.id.buy_tv)
    TextView buyTv;
    @Bind(R.id.jianyi_oval_view)
    RelativeLayout jianyiOvalView;
    @Bind(R.id.wenhao_iv)
    ImageView wenhaoIv;
    @Bind(R.id.wenhao_view)
    RelativeLayout wenhaoView;
    @Bind(R.id.cantrade_tv)
    TextView cantradeTv;
    @Bind(R.id.jianyi_view)
    RelativeLayout jianyiView;
    @Bind(R.id.add_img_btn)
    ImageView addImgBtn;
    @Bind(R.id.add_stock_view)
    LinearLayout addStockView;
    @Bind(R.id.stocklist_layout)
    LinearLayout stocklistLayout;
    private MyAdapter myAdapter;

    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    private String params = "sh000001,sz399001";
    private String stock_sz = "sz00";
    private String stock_zx = "zixuan";
    private String current_stock = stock_sz;
    private final static int TIME = 8000;
    private int CURRENT_TYPE = 1;
    private final static int RATE_TYPE = 1;
    private final static int ZHI_TYPE = 2;

    private static SpcyFragment INSTANCE;
    String date;

    public static SpcyFragment getInstace() {
        if (INSTANCE == null) {
            INSTANCE = new SpcyFragment();
        }
        return INSTANCE;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spcy_fragment, container, false);
        ButterKnife.bind(this, view);
        //离线股票代码
//       initData(stock_sz);
//        handler.postDelayed(runnable, TIME);
        System.out.println("spcyFragment onCreateView");
        myAdapter = new MyAdapter(getActivity());
        stockListview.setAdapter(myAdapter);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date lastday = DateUtils.getLastDay(new Date());
        date = dateFormat.format(lastday);
        params = SPUtils.get(getActivity(), "stockCodes", "").toString();
        init();
        return view;
    }

    /*    private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                initData(current_stock);
                handler.postDelayed(this,TIME);
            }
        };*/
    private void inflateAdviceView(String canTrade) {
        if (canTrade.equals("true")) {
            jianyiOvalView.setBackgroundResource(R.drawable.predict_buy_bg_oval);
            jianyiTv.setText("建议");
            buyTv.setText("买入");
            cantradeTv.setText("昨日建议买入");
        } else {
            jianyiOvalView.setBackgroundResource(R.drawable.predict_wait_bg_oval);
            jianyiTv.setText("建议");
            buyTv.setText("观望");
            cantradeTv.setText("昨日建议观望");
        }
    }

    private void init() {
        current_stock = "1";
        predictTv.setTextColor(getResources().getColor(R.color.red_color));
        predictSlide.setVisibility(View.VISIBLE);
        zixuanTv.setTextColor(getResources().getColor(R.color.common_black));
        zixuanSlide.setVisibility(View.GONE);
        initPredictData("20", date, "1");

        stockListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                System.out.println("onPullDownToRefresh");
                stockListview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (current_stock.equals(stock_zx)) {
                            String params = SPUtils.get(getActivity(), "stockCodes", "").toString();
                            System.out.println("refresh params:" + params);
                            initOffData(params);
                        } else {
                            initPredictData("20", date, "1");
                        }
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                System.out.println("onPullUpToRefresh");
            }
        });
    }

    private void initOffData(String params) {
        DataManager.getInfo(params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && !response.equals("")) {
                    ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                    if (entries != null && entries.size() > 0) {
                        stockListview.setVisibility(View.VISIBLE);
                        myAdapter.update(entries);
                        myAdapter.setType(CURRENT_TYPE);
                    } else {
                        System.out.println("entries:");
                    }
                    stockListview.onRefreshComplete();
//                    progressbar.setVisibility(View.GONE);
                }
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
                ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                        s.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
                final String StockRecommendTrade = s.optString("StockRecommendTrade");
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
                                    inflateAdviceView(StockRecommendTrade);
                                    jianyiView.setVisibility(View.VISIBLE);
                                    stockListview.setVisibility(View.VISIBLE);
                                    gupiaoRelative.setVisibility(View.VISIBLE);
                                    myAdapter.update(entries);
                                    myAdapter.setType(CURRENT_TYPE);
                                    stockListview.setAdapter(myAdapter);
                                    stockListview.onRefreshComplete();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                } else {
                    jianyiView.setVisibility(View.GONE);
                    stockListview.setVisibility(View.GONE);
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
        if (current_stock.equals(stock_zx)) {
//            params = SPUtils.get(getActivity(), "stockCodes", "").toString();
//            initOffData(params);
            params = SPUtils.get(getActivity(), "stockCodes", "").toString();
            if (!params.equals("")) {
                addStockView.setVisibility(View.GONE);
                stockListview.setVisibility(View.VISIBLE);
                gupiaoRelative.setVisibility(View.VISIBLE);
                initOffData(params);
            } else {
                addStockView.setVisibility(View.VISIBLE);
                stockListview.setVisibility(View.GONE);
                gupiaoRelative.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.linear_zixuan, R.id.linear_predict, R.id.linear_other, R.id.right_layout, R.id.search_icon_img, R.id.stock_listview, R.id.wenhao_iv, R.id.wenhao_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_zixuan:
                zixuanTv.setTextColor(getResources().getColor(R.color.red_color));
                predictTv.setTextColor(getResources().getColor(R.color.common_black));
                predictSlide.setVisibility(View.GONE);
                zixuanSlide.setVisibility(View.VISIBLE);
                jianyiView.setVisibility(View.GONE);
                current_stock = stock_zx;
                params = SPUtils.get(getActivity(), "stockCodes", "").toString();
                if (!params.equals("")) {
                    addStockView.setVisibility(View.GONE);
                    stockListview.setVisibility(View.VISIBLE);
                    gupiaoRelative.setVisibility(View.VISIBLE);
                    initOffData(params);
                } else {
                    addStockView.setVisibility(View.VISIBLE);
                    stockListview.setVisibility(View.GONE);
                    gupiaoRelative.setVisibility(View.GONE);
                }

                break;
            case R.id.linear_predict:
                addStockView.setVisibility(View.GONE);
                predictTv.setTextColor(getResources().getColor(R.color.red_color));
                zixuanTv.setTextColor(getResources().getColor(R.color.common_black));
                zixuanSlide.setVisibility(View.GONE);
                predictSlide.setVisibility(View.VISIBLE);
                current_stock = "1";
                initPredictData("20", date, "1");
                break;
            case R.id.linear_other:
                break;

            case R.id.right_layout:
                if (CURRENT_TYPE >= ZHI_TYPE) {
                    Drawable drawable = getResources().getDrawable(R.drawable.up_or_down_unselect);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    increaseText.setCompoundDrawables(null, null, drawable, null);
                    increaseText.setText("涨跌幅");
                    CURRENT_TYPE = RATE_TYPE;
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.up_or_down_select);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    increaseText.setCompoundDrawables(null, null, drawable, null);
                    increaseText.setText("涨跌值");
                    CURRENT_TYPE = ZHI_TYPE;
                }

                myAdapter.setType(CURRENT_TYPE);
//                handler.removeCallbacks(runnable);
//                handler.postDelayed(runnable, TIME);*/
                break;
            case R.id.search_icon_img:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.stock_listview:
                break;
            case R.id.wenhao_iv:
            case R.id.wenhao_view:
                Intent intent_advice = new Intent(getActivity(), AdviceActivity.class);
                startActivity(intent_advice);
                break;
        }
    }

    @OnClick(R.id.add_img_btn)
    public void onClick() {
        if (MyApplication.login_stauts) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private ArrayList<ShareEntry> mShareInfo = new ArrayList<ShareEntry>();
        private int type;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void update(ArrayList<ShareEntry> shareInfo) {
            this.mShareInfo = shareInfo;
            notifyDataSetChanged();
        }

        public void setType(int type) {
            this.type = type;
            notifyDataSetChanged();
        }

        @Override

        public int getCount() {
            return mShareInfo.size();
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
            final ShareEntry shareEntry = mShareInfo.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_item, null, false);
                viewHolder.nameText = (TextView) convertView.findViewById(R.id.share_name);
                viewHolder.codeText = (TextView) convertView.findViewById(R.id.share_id);
                viewHolder.priceText = (TextView) convertView.findViewById(R.id.price_text);
                viewHolder.changeText = (TextView) convertView.findViewById(R.id.increase_item_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.nameText.setText(mShareInfo.get(position).getName());

            float current, close, change, change_rate;
            current = Float.parseFloat(mShareInfo.get(position).getCurrent());
            close = Float.parseFloat(mShareInfo.get(position).getClose());

            if (current == 0) {
                change = 0;
            } else {
                change = current - close;
            }
            change_rate = change / close;

            if (type == RATE_TYPE) {
                if (change_rate > 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.red_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.red_color));
                    viewHolder.changeText.setText("+" + decimalFormat.format(change_rate * 100) + "%");
                } else if (change_rate < 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.green_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.price_down_color));
                    viewHolder.changeText.setText(decimalFormat.format(change_rate * 100) + "%");
                } else if (change_rate == 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.grey_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.code_color));
                    viewHolder.changeText.setText("0.00%");
                }
            } else if (type == ZHI_TYPE) {
                if (change > 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.red_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.red_color));
                    viewHolder.changeText.setText("+" + decimalFormat.format(change));
                } else if (change < 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.green_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.price_down_color));
                    viewHolder.changeText.setText(decimalFormat.format(change));
                } else if (change == 0) {
                    viewHolder.changeText.setBackgroundResource(R.drawable.grey_rect);
                    viewHolder.priceText.setTextColor(getResources().getColor(R.color.code_color));
                    viewHolder.changeText.setText("0.00");
                }
            }


            viewHolder.priceText.setText(decimalFormat.format(Float.parseFloat(mShareInfo.get(position).getCurrent())));
            viewHolder.codeText.setText(mShareInfo.get(position).getCode());

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

    class ViewHolder {
        public TextView nameText;
        public TextView codeText;
        public TextView priceText;
        public TextView changeText;
    }
}
