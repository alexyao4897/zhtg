package com.money.deep.tstock.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.money.deep.tstock.R;
import com.money.deep.tstock.activity.KLineActivity;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.http.ResponseListener;
import com.money.deep.tstock.listener.FragmentListener;
import com.money.deep.tstock.model.EmuDealModel;
import com.money.deep.tstock.model.EmuDealer;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StockPredictItem;
import com.money.deep.tstock.view.ColorBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
 * Created by Administrator on 2016/9/13.
 */
public class CaseDetailFragment extends Fragment {
    @Bind(R.id.left_view)
    RelativeLayout leftView;
    @Bind(R.id.summary_tv)
    TextView summaryTv;
    @Bind(R.id.summary_bottom_view)
    View summaryBottomView;
    @Bind(R.id.history_tv)
    TextView historyTv;
    @Bind(R.id.history_bottom_view)
    View historyBottomView;
    @Bind(R.id.case_summary_view)
    LinearLayout caseSummaryView;
    @Bind(R.id.summary_listview_view)
    LinearLayout summaryListviewView;
    @Bind(R.id.robot_avar_iv)
    ImageView robotAvarIv;
    @Bind(R.id.robot_name_tv)
    TextView robotNameTv;
    @Bind(R.id.history_detail_listview)
    ListView historyDetailListview;
    @Bind(R.id.emudealer_stock_key_view)
    LinearLayout emudealerStockKeyView;
    @Bind(R.id.current_rate_tv)
    TextView currentRateTv;
    @Bind(R.id.deal_day_tv)
    TextView dealDayTv;
    @Bind(R.id.success_rate_tv)
    TextView successRateTv;
    @Bind(R.id.jiancang_num_tv)
    TextView jiancangNumTv;
    @Bind(R.id.earn_rate_three_month_tv)
    TextView earnRateThreeMonthTv;
    @Bind(R.id.earn_rate_six_month_tv)
    TextView earnRateSixMonthTv;
    @Bind(R.id.total_assets_tv)
    TextView totalAssetsTv;
    @Bind(R.id.yingkui_num_tv)
    TextView yingkuiNumTv;
    @Bind(R.id.max_earn_tv)
    TextView maxEarnTv;
    @Bind(R.id.max_loss_tv)
    TextView maxLossTv;
    @Bind(R.id.predict_trade_tv)
    TextView predictTradeTv;
    @Bind(R.id.predict_trade_bottom_view)
    View predictTradeBottomView;
    @Bind(R.id.price_tv)
    TextView priceTv;
    @Bind(R.id.buy_tv)
    TextView buyTv;
    @Bind(R.id.predict_trad_view)
    LinearLayout predictTradView;
    @Bind(R.id.refresh_view)
    RelativeLayout refreshView;
    @Bind(R.id.predict_trade_list)
    ListView predictTradeList;
    @Bind(R.id.predict_list_layout)
    LinearLayout predictListLayout;
    @Bind(R.id.detail_info_topview)
    LinearLayout detailInfoTopview;
    @Bind(R.id.refresh_tv)
    TextView refreshTv;
    @Bind(R.id.trade_tip_view)
    LinearLayout tradeTipView;
    private CaseDetailFragment caseDetailFragment;
    private GmFragment gmFragment;
    private CaseSummaryFragment caseSummaryFragment;
    private Fragment mCurFragment = null;
    MyHistoryAdapter myHistoryAdapter;
    MyAdapter myAdapter;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static CaseDetailFragment INSTANCE;

    public static CaseDetailFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CaseDetailFragment();
        }
        return INSTANCE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.case_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        System.out.println("onCreateView");
        String position = getArguments().getString("position");
        System.out.println("position:" + position);
        initData(position);
        initHistoryListData("1", "1", "10");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:ss");
        String date = simpleDateFormat.format(new Date());
        refreshTv.setText("更新于" + date);
        myHistoryAdapter = new MyHistoryAdapter(MyApplication.getContext());
        historyDetailListview.setAdapter(myHistoryAdapter);
        myAdapter = new MyAdapter(getActivity());
        predictTradeList.setAdapter(myAdapter);
        ScrollListener scrollListener = new ScrollListener();
        predictTradeList.setOnScrollListener(scrollListener);
        historyDetailListview.setOnScrollListener(scrollListener);
        return view;
    }

    private class ScrollListener implements AbsListView.OnScrollListener {
        private int lastVisibleItemPosition = 0;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem > lastVisibleItemPosition && firstVisibleItem == 1) {
                detailInfoTopview.setVisibility(View.GONE);
            } else if (firstVisibleItem < lastVisibleItemPosition && firstVisibleItem == 1) {
                detailInfoTopview.setVisibility(View.VISIBLE);
            }
            lastVisibleItemPosition = firstVisibleItem;
        }
    }

    public void initData(String position) {
        DataManager.emudealer(position, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("机器人详情" + response);
                EmuDealer emuDealer = new Gson().fromJson(response.optString("EmuDealer"), new TypeToken<EmuDealer>() {
                }.getType());
                robotNameTv.setText(emuDealer.getStockDealerName());
                currentRateTv.setText("+" + emuDealer.getEarningRate() + "%");
                dealDayTv.setText(emuDealer.getStartDealDays() + "天");
                successRateTv.setText(emuDealer.getSuccessRate() + "%");
                jiancangNumTv.setText(emuDealer.getDealTimes() + "笔");
                earnRateThreeMonthTv.setText(decimalFormat.format(Float.parseFloat(emuDealer.getEarningRate90D())));
                earnRateSixMonthTv.setText(decimalFormat.format(Float.parseFloat(emuDealer.getEarningRate6M())));
                totalAssetsTv.setText(emuDealer.getCurrentFunds());
                float CurrentFunds = Float.parseFloat(emuDealer.getCurrentFunds());
                float InitFunds = Float.parseFloat(emuDealer.getInitFunds());
                float value = CurrentFunds - InitFunds;
                if (value >= 0) {
                    totalAssetsTv.setTextColor(getResources().getColor(R.color.red_color));
                    yingkuiNumTv.setTextColor(getResources().getColor(R.color.red_color));
                } else {
                    totalAssetsTv.setTextColor(getResources().getColor(R.color.price_down_bg_color));
                    yingkuiNumTv.setTextColor(getResources().getColor(R.color.price_down_bg_color));
                }
                yingkuiNumTv.setText(value + "");
                maxEarnTv.setText(emuDealer.getLastAction());
                maxLossTv.setTextColor(getResources().getColor(R.color.price_down_bg_color));
                maxLossTv.setText(decimalFormat.format(Float.parseFloat(emuDealer.getWithdrawalRate())));
//                yingkuiNumTv.setText();
//                maxEarnTv.setText();
//                maxLossTv.setText();
                String keywords = emuDealer.getKeyWords();
                String[] keys = keywords.split(",");
                emudealerStockKeyView.removeAllViews();
                for (int i = 0; i < keys.length; i++) {
                    TextView textView = new TextView(getActivity());
                    textView.setText(keys[i]);
                    textView.setBackgroundResource(R.drawable.stock_label_graycorner_rect);
                    textView.setTextColor(getResources().getColor(R.color.common_white));
                    textView.setPadding(4, 4, 4, 4);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT );
                    lp.setMargins(0,0,15,5);
                    textView.setLayoutParams(lp);
                    emudealerStockKeyView.addView(textView);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void initHistoryListData(String stock_bot_id, String currentPage, String pageSize) {
        DataManager.emudealLog(stock_bot_id, currentPage, pageSize, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response:" + response);
                ArrayList<EmuDealModel> emuDealModels = new Gson().fromJson(response.optString("EmuDealLogList"),
                        new TypeToken<List<EmuDealModel>>() {
                        }.getType());
                myHistoryAdapter.update(emuDealModels);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public CaseDetailFragment() {
        super();
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getRequestQueue().cancelAll(DataManager.REQUEST_TAG);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        System.out.println("onViewStateRestored");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("onDetach");
    }


    public void setSelected(int i) {
        switch (i) {
            case 0:
                caseSummaryView.setVisibility(View.VISIBLE);
                summaryListviewView.setVisibility(View.GONE);
                predictTradView.setVisibility(View.GONE);
                predictListLayout.setVisibility(View.GONE);
                tradeTipView.setVisibility(View.GONE);
                break;
            case 1:
                caseSummaryView.setVisibility(View.GONE);
                summaryListviewView.setVisibility(View.VISIBLE);
                predictTradView.setVisibility(View.GONE);
                predictListLayout.setVisibility(View.GONE);
                tradeTipView.setVisibility(View.GONE);
                break;
            case 2:
                caseSummaryView.setVisibility(View.GONE);
                predictListLayout.setVisibility(View.GONE);
                predictTradView.setVisibility(View.GONE);
                tradeTipView.setVisibility(View.VISIBLE);
                summaryListviewView.setVisibility(View.GONE);

                //测试数据
                DataManager.rankAll("50", "20160920","", new ResponseListener("rankAll") {
                    @Override
                    public void onSuccess(JSONObject s) {
                        final ArrayList<StockPredictItem> stockPredictItems = (new Gson()).fromJson(
                                s.optString("StockPredictItems"),
                                new TypeToken<List<StockPredictItem>>() {
                                }.getType());
                        if (stockPredictItems != null && stockPredictItems.size() > 0) {
                            String params = new String();
                            for (int i = 0; i < stockPredictItems.size(); i++) {
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
                                                myAdapter.refreshData(stockPredictItems, entries);
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
                        }
                    }

                    @Override
                    public void onError(JSONObject s) {

                    }
                });
                break;
        }

    }


    private FragmentListener.CaseDetailListener mCaseDetailListener = null;

    public void setCaseDetailListener(FragmentListener.CaseDetailListener listenr) {
        this.mCaseDetailListener = listenr;
    }

    @OnClick(R.id.left_view)
    public void onClick() {
        if (mCaseDetailListener != null) {
            mCaseDetailListener.onLeftBtnClick();
        }
    }

    @OnClick({R.id.summary_tv, R.id.history_tv, R.id.predict_trade_tv, R.id.refresh_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.summary_tv:
                summaryTv.setTextColor(getResources().getColor(R.color.red_color));
                historyTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                predictTradeTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                summaryBottomView.setVisibility(View.VISIBLE);
                historyBottomView.setVisibility(View.GONE);
                predictTradeBottomView.setVisibility(View.GONE);
//                setSelect(0);
                setSelected(0);
                break;
            case R.id.history_tv:
                historyTv.setTextColor(getResources().getColor(R.color.red_color));
                summaryTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                predictTradeTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                historyBottomView.setVisibility(View.VISIBLE);
                summaryBottomView.setVisibility(View.GONE);
                predictTradeBottomView.setVisibility(View.GONE);
                setSelected(1);
//                setSelect(1);
                break;
            case R.id.predict_trade_tv:
                predictTradeTv.setTextColor(getResources().getColor(R.color.red_color));
                summaryTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                historyTv.setTextColor(getResources().getColor(R.color.case_history_text_color));
                predictTradeBottomView.setVisibility(View.VISIBLE);
                summaryBottomView.setVisibility(View.GONE);
                historyBottomView.setVisibility(View.GONE);
//                ArrayList<ShareEntry> entries = new ArrayList<ShareEntry>();
//                ShareEntry shareEntry = new ShareEntry();
//                shareEntry.setName("");
//                entries.add();
//                myAdapter.refreshData();
                setSelected(2);
                break;
            case R.id.refresh_view:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:ss");
                String date = simpleDateFormat.format(new Date());
                refreshTv.setText("更新于" + date);
                break;
        }
    }


    class MyHistoryAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        private ArrayList<EmuDealModel> emuDealModels = new ArrayList<EmuDealModel>();

        public MyHistoryAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public void update(ArrayList<EmuDealModel> emuDealModels) {
            this.emuDealModels = emuDealModels;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return emuDealModels.size();
        }

        @Override
        public Object getItem(int position) {
            return emuDealModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final EmuDealModel emuDealModel = emuDealModels.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.case_historylist_item, null, false);
                viewHolder.buy_iv = (ImageView) convertView.findViewById(R.id.buy_or_sale_iv);
//                viewHolder.rate_tv = (TextView) convertView.findViewById(R.id.buy_or_sale_tv);
                viewHolder.day_rate_tv = (TextView) convertView.findViewById(R.id.stock_name_tv);
                viewHolder.stock_name = (TextView) convertView.findViewById(R.id.trade_time_tv);
                viewHolder.stock_label = (TextView) convertView.findViewById(R.id.average_price_tv);
                viewHolder.stock_label_two = (TextView) convertView.findViewById(R.id.trade_number_tv);
                viewHolder.operate_day_tv = (TextView) convertView.findViewById(R.id.price_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (!emuDealModel.isSale()) {
//                viewHolder.rate_tv.setTextColor(getResources().getColor(R.color.red_color));
//                viewHolder.rate_tv.setText("买");
                viewHolder.buy_iv.setImageResource(R.drawable.buy_img);
            } else {
//                viewHolder.rate_tv.setTextColor(getResources().getColor(R.color.history_sale_text_color));
//                viewHolder.rate_tv.setText("卖");
                viewHolder.buy_iv.setImageResource(R.drawable.sell_img);
            }
            viewHolder.day_rate_tv.setText(emuDealModel.getStockName());
            viewHolder.stock_name.setText(emuDealModel.getActionDate());
            Float price = Float.parseFloat(emuDealModel.getActionAmount()) / Float.parseFloat(emuDealModel.getShares());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            viewHolder.stock_label.setText(decimalFormat.format(price));
            viewHolder.stock_label_two.setText(emuDealModel.getShares());
            viewHolder.operate_day_tv.setText(emuDealModel.getActionAmount());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView buy_iv;
        TextView rate_tv;
        TextView day_rate_tv;
        TextView stock_name;
        TextView stock_label;
        TextView stock_label_two;
        TextView operate_day_tv;
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
}
