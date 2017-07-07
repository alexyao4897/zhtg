package com.money.deep.tstock.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.data.ConstantTest;
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.data.DataParse;
import com.money.deep.tstock.data.KLineBean;
import com.money.deep.tstock.data.MinutesBean;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StockPredictItem;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.DateUtils;
import com.money.deep.tstock.util.MyUtils;
import com.money.deep.tstock.util.SPUtils;
import com.money.deep.tstock.util.VolFormatter;
import com.money.deep.tstock.view.ColorBar;
import com.money.deep.tstock.view.CoupleChartGestureListener;
import com.money.deep.tstock.view.MyBarChart;
import com.money.deep.tstock.view.MyCombinedChart;
import com.money.deep.tstock.view.MyCombinedLeftMarkerView;
import com.money.deep.tstock.view.MyLeftMarkerView;
import com.money.deep.tstock.view.MyLineChart;
import com.money.deep.tstock.view.MyRightMarkerView;
import com.money.deep.tstock.view.MyXAxis;
import com.money.deep.tstock.view.MyYAxis;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KLineActivity extends Activity {

//    @Bind(R.id.combinedchart)
//    CombinedChart combinedchart;
    @Bind(R.id.barchart)
    BarChart barChart;
    @Bind(R.id.left_btn)
    ImageView leftBtn;
    @Bind(R.id.share_name)
    TextView shareName;
    @Bind(R.id.share_code)
    TextView shareCode;
    @Bind(R.id.zhishu_tv)
    TextView zhishuTv;
    @Bind(R.id.rate_tv)
    TextView rateTv;
    @Bind(R.id.zhi_tv)
    TextView zhiTv;
    @Bind(R.id.open_tv)
    TextView openTv;
    @Bind(R.id.high_tv)
    TextView highTv;
    @Bind(R.id.close_tv)
    TextView closeTv;
    @Bind(R.id.low_tv)
    TextView lowTv;
    @Bind(R.id.fenshi)
    TextView fenshi;
    @Bind(R.id.dayk_tv)
    TextView daykTv;
    @Bind(R.id.weekk_tv)
    TextView weekkTv;
    @Bind(R.id.monthk_tv)
    TextView monthkTv;
    @Bind(R.id.fenshi_view)
    TextView fenshiView;
    @Bind(R.id.dayk_view)
    TextView daykView;
    @Bind(R.id.weekk_view)
    TextView weekkView;
    @Bind(R.id.monthk_view)
    TextView monthkView;
    @Bind(R.id.kline_linear)
    LinearLayout klineLinear;
    @Bind(R.id.line_chart)
    MyLineChart lineChart;
    @Bind(R.id.fen_bar_chart)
    MyBarChart fenBarChart;
    @Bind(R.id.fenshi_linear)
    LinearLayout fenshiLinear;
    @Bind(R.id.new_linechart)
    LineChart newLinechart;
    @Bind(R.id.yuce_colorbar)
    ColorBar yuceColorbar;
    @Bind(R.id.ciriyuce_tv)
    TextView ciriyuceTv;
    @Bind(R.id.zuoriyuce_tv)
    TextView zuoriyuceTv;
    @Bind(R.id.yuce_linear)
    LinearLayout yuceLinear;
    @Bind(R.id.yuce_tv)
    TextView yuceTv;
    @Bind(R.id.yuce_bar_relative)
    RelativeLayout yuceBarRelative;
    @Bind(R.id.predict_no_tv)
    TextView predictNoTv;
    @Bind(R.id.ciri_relative)
    RelativeLayout ciriRelative;
    @Bind(R.id.add_zixuan_tv)
    TextView addZixuanTv;
    @Bind(R.id.left_btn_view)
    RelativeLayout leftBtnView;
    @Bind(R.id.left_layout)
    LinearLayout leftLayout;
    @Bind(R.id.line_view)
    View lineView;
    @Bind(R.id.my_combinedchart)
    MyCombinedChart combinedchart;
    //    @Bind(R.id.my_combinedchart)
//    MyCombinedChart combinedchart;
    private ColorBar yuce_colorbar;
    private DataParse mData;
    private ArrayList<KLineBean> kLineDatas;
    XAxis xAxisBar, xAxisK;
    YAxis axisLeftBar, axisLeftK;
    YAxis axisRightBar, axisRightK;
    private LineDataSet d1, d2;
    BarDataSet barDataSet;
    BarDataSet barDataSet_increse, barDataSet_decrease;
    private ShareEntry shareEntry;
    private BarLineChartTouchListener mChartTouchListener;
    private CoupleChartGestureListener coupleChartGestureListener;
    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
    float sum = 0;
    String current_stock;
    String current_day_str;
    String before_day_str;
    private final static int TIME = 60000;

    MyXAxis xAxisLine_fenshi;
    MyYAxis axisRightLine_fenshi;
    MyYAxis axisLeftLine_fenshi;
    MyXAxis xAxisBar_fenshi;
    MyYAxis axisLeftBar_fenshi;
    MyYAxis axisRightBar_fenshi;
    SparseArray<String> stringSparseArray;
    private static final int MONTH_NUM = 0;
    private static final int WEEK_NUM = 0;
    private static final int DAY_NUM = 0;
    MyCombinedLeftMarkerView myCombinedLeftMarkerView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String string = (String) msg.obj;
                    System.out.println("1:msg.obj:" + string);
                    float high = Float.parseFloat(shareEntry.getHigh());
                    float low = Float.parseFloat(shareEntry.getLow());
                    String diff = decimalFormat.format(high - low);
                    String current_str = decimalFormat.format(Float.parseFloat(shareEntry.getCurrent()));
                    String low_str = decimalFormat.format(Float.parseFloat(shareEntry.getLow()));
                    mData.parseMinutes(string, low_str, diff);
                    setMinuteData(mData);
                    break;
                case 2:
//                    initChart();
//                    setData(mData);
                    combinedchart.setAutoScaleMinMaxEnabled(true);
                    barChart.setAutoScaleMinMaxEnabled(true);
                    combinedchart.notifyDataSetChanged();
                    barChart.notifyDataSetChanged();
                    combinedchart.animateX(500);
                    barChart.animateX(1);
                    combinedchart.invalidate();
                    barChart.invalidate();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        yuce_colorbar = (ColorBar) findViewById(R.id.yuce_colorbar);
//        initLineChart();
        myCombinedLeftMarkerView = new MyCombinedLeftMarkerView(this, R.layout.mycombined_markerview);
        initStockData();
        initEvent();
        initChart();
        initMinuteLineChart();
        initMinuteBarChart();
        stringSparseArray = setXLabels();

        if (current_stock.equals("sh60")) {
            handler.postDelayed(runnable,0);
        } else {
            handler.postDelayed(runnable_typeone,0);
        }

    }

    private void initEvent() {
        ciriyuceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuceTv.setText("次日涨跌预测");
                ciriyuceTv.setBackgroundColor(getResources().getColor(R.color.yuce_tv_bg_color));
                ciriyuceTv.setTextColor(getResources().getColor(R.color.common_white));
                zuoriyuceTv.setTextColor(getResources().getColor(R.color.yuce_tv_bg_color));
                zuoriyuceTv.setBackgroundColor(getResources().getColor(R.color.common_white));
                initStockPrceditData(shareEntry.getQ_code(), "1", current_day_str);
            }
        });

        zuoriyuceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuceTv.setText("昨日涨跌预测");
                zuoriyuceTv.setTextColor(getResources().getColor(R.color.common_white));
                zuoriyuceTv.setBackgroundColor(getResources().getColor(R.color.yuce_tv_bg_color));
                ciriyuceTv.setBackgroundColor(getResources().getColor(R.color.common_white));
                ciriyuceTv.setTextColor(getResources().getColor(R.color.yuce_tv_bg_color));
                initStockPrceditData(shareEntry.getQ_code(), "1", before_day_str);
            }
        });
    }

    private void initStockPrceditData(String stocks, String country, String day) {
        DataManager.stockSpecDay(stocks, country, day, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<StockPredictItem> stockPredictItems = new Gson().fromJson(
                        response.optString("StockPredictItems"),
                        new TypeToken<List<StockPredictItem>>() {
                        }.getType());
                if (stockPredictItems != null && stockPredictItems.size() > 0) {
                    yuceBarRelative.setVisibility(View.VISIBLE);
                    ciriRelative.setVisibility(View.GONE);
                    yuceColorbar.setFirstPercent(Float.parseFloat(stockPredictItems.get(0).getProbRise()) / 100f);
                    yuceColorbar.setSecondPercent(Float.parseFloat(stockPredictItems.get(0).getProbSmooth()) / 100f);
                    yuceColorbar.setThirdPercent(Float.parseFloat(stockPredictItems.get(0).getProbFall()) / 100f);
                } else {
                    yuceBarRelative.setVisibility(View.GONE);
                    ciriRelative.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private boolean isAddZixuan(String string) {
        boolean isZixuanStock = false;
        String stocks = SPUtils.get(KLineActivity.this, "stockCodes", "").toString();
        String[] stockcode = stocks.split(",");
//        ArrayList<String> stockArray = new ArrayList<String>();
        for (int i = 0; i < stockcode.length; i++) {
            String str = stockcode[i];
//            stockArray.add(str);
            if (str.equals(string)) {
                isZixuanStock = true;
            }
        }
//        if (stockArray.contains(string)) {
//            isZixuanStock = true;
//        }else{
//            isZixuanStock = false;
//        }
        return isZixuanStock;
    }

    private Runnable runnable_stockData = new Runnable() {
        @Override
        public void run() {
            refreshStockData(shareEntry.getQ_code());
            handler.postDelayed(this,TIME);
        }
    };


    public void refreshStockData(String stockcode){
        DataManager.getInfo(stockcode, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                ShareEntry entry = entries.get(0);

                float current, close, change, change_rate, open,high,low;
                current = Float.parseFloat(entry.getCurrent());
                close = Float.parseFloat(entry.getClose());
                open = Float.parseFloat(entry.getOpen());
                high = Float.parseFloat(entry.getHigh());
                low = Float.parseFloat(entry.getLow());
                change = current - close;
                change_rate = change / close;

                if ((current - open) > 0) {
                    zhishuTv.setTextColor(getResources().getColor(R.color.red_color));
                } else if ((current - open) < 0) {
                    zhishuTv.setTextColor(getResources().getColor(R.color.price_down_color));
                }
                zhishuTv.setText(decimalFormat.format(Float.parseFloat(entry.getCurrent())));
                if (change > 0) {
                    zhiTv.setTextColor(getResources().getColor(R.color.red_color));
                    zhiTv.setText("+" + decimalFormat.format(change));
                } else if (change < 0) {
                    zhiTv.setTextColor(getResources().getColor(R.color.price_down_color));
                    zhiTv.setText(decimalFormat.format(change));
                } else if (change == 0) {
                    zhiTv.setTextColor(getResources().getColor(R.color.code_color));
                    zhiTv.setText("0.00");
                }

                if (change_rate > 0) {
                    rateTv.setTextColor(getResources().getColor(R.color.red_color));
                    rateTv.setText("+" + decimalFormat.format(change_rate * 100) + "%");
                } else if (change_rate < 0) {
                    rateTv.setTextColor(getResources().getColor(R.color.price_down_color));
                    rateTv.setText(decimalFormat.format(change_rate * 100) + "%");
                } else if (change_rate == 0) {
                    rateTv.setTextColor(getResources().getColor(R.color.code_color));
                    rateTv.setText("0.00%");
                }


                openTv.setText(decimalFormat.format(open));
                closeTv.setText(decimalFormat.format(close));
                if(high - close >= 0){
                    highTv.setTextColor(getResources().getColor(R.color.red_color));
                }else{
                    highTv.setTextColor(getResources().getColor(R.color.price_down_color));
                }
                highTv.setText(decimalFormat.format(high));
                if(low - close >= 0){
                    lowTv.setTextColor(getResources().getColor(R.color.red_color));
                }else{
                    lowTv.setTextColor(getResources().getColor(R.color.price_down_color));
                }
                lowTv.setText(decimalFormat.format(low));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private void initStockData() {
        shareEntry = (ShareEntry) getIntent().getSerializableExtra("shareInfo");
        current_stock = getIntent().getStringExtra("current_stock");
        shareName.setText(shareEntry.getName());
        shareCode.setText(shareEntry.getCode());
        System.out.println("shareEntry.getQ_code():" + shareEntry.getQ_code());
        System.out.println("isAddZixuan:" + isAddZixuan(shareEntry.getQ_code()));
        if (!isAddZixuan(shareEntry.getQ_code())) {
            addZixuanTv.setText("+自选");
        } else {
            addZixuanTv.setText("已添加");
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
        current_day_str = sDateFormat.format(new Date());
        before_day_str = sDateFormat.format(DateUtils.getLastDay(new Date()));
//        current_day_str = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
//        before_day_str = before_date.substring(0, 4) + before_date.substring(5, 7) + before_date.substring(8, 10);
        initStockPrceditData(shareEntry.getQ_code(), "1", before_day_str);

        float current, close, change, change_rate, open;
        current = Float.parseFloat(shareEntry.getCurrent());
        close = Float.parseFloat(shareEntry.getClose());
        open = Float.parseFloat(shareEntry.getOpen());
        change = current - close;
        change_rate = change / close;

        if ((current - open) > 0) {
            zhishuTv.setTextColor(getResources().getColor(R.color.red_color));
        } else if ((current - open) < 0) {
            zhishuTv.setTextColor(getResources().getColor(R.color.price_down_color));
        }
        zhishuTv.setText(decimalFormat.format(Float.parseFloat(shareEntry.getCurrent())));
        if (change > 0) {
            zhiTv.setTextColor(getResources().getColor(R.color.red_color));
            zhiTv.setText("+" + decimalFormat.format(change));
        } else if (change < 0) {
            zhiTv.setTextColor(getResources().getColor(R.color.price_down_color));
            zhiTv.setText(decimalFormat.format(change));
        } else if (change == 0) {
            zhiTv.setTextColor(getResources().getColor(R.color.code_color));
            zhiTv.setText("0.00");
        }

        if (change_rate > 0) {
            rateTv.setTextColor(getResources().getColor(R.color.red_color));
            rateTv.setText("+" + decimalFormat.format(change_rate * 100) + "%");
        } else if (change_rate < 0) {
            rateTv.setTextColor(getResources().getColor(R.color.price_down_color));
            rateTv.setText(decimalFormat.format(change_rate * 100) + "%");
        } else if (change_rate == 0) {
            rateTv.setTextColor(getResources().getColor(R.color.code_color));
            rateTv.setText("0.00%");
        }


        openTv.setText(decimalFormat.format(open));
        highTv.setTextColor(getResources().getColor(R.color.red_color));
        highTv.setText(decimalFormat.format(Float.parseFloat(shareEntry.getHigh())));
        closeTv.setText(decimalFormat.format(close));
        lowTv.setTextColor(getResources().getColor(R.color.price_down_color));
        lowTv.setText(decimalFormat.format(Float.parseFloat(shareEntry.getLow())));

        handler.postDelayed(runnable_stockData,0);
    }

    private SparseArray<String> setXLabels() {
        SparseArray<String> xLabels = new SparseArray<String>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(121, "11:30/13:00");
        xLabels.put(182, "14:00");
        xLabels.put(239, "15:00");
        return xLabels;
    }


    private void getData(String param, int type) {
        initChart();
        mData = new DataParse();
        switch (type) {
            case 0:
                DataManager.getKLineData(param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mData.parseKLine(response, DAY_NUM);
                        setData(mData);
                        setCombineData(mData);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                break;
            case 1:
                DataManager.getKWeekLine(param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mData.parseKLine(response, WEEK_NUM);
                        setData(mData);
                        setCombineData(mData);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                break;
            case 2:
                DataManager.getKMonthLine(param, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mData.parseKLine(response, MONTH_NUM);
                        setData(mData);
                        setCombineData(mData);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                break;
        }

    }

    private void getOffLineData() {
           /*方便测试，加入假数据*/
        mData = new DataParse();
        JSONObject object = null;
        try {
            object = new JSONObject(ConstantTest.MINUTESURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mData.parseMinutes(object);
        setMinuteData(mData);
    }

    public void onlineData(String code,int type,String start,String num){
        mData = new DataParse();
        String typeStr = null;
        if (type == 1) {
            typeStr = "szse";
        } else if (type == 2) {
            typeStr = "sse";
        }
        DataManager.minuteRequest(code, typeStr, start, num, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("minute response:"+response);
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = response;
                handler.sendMessage(message);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


    private void setMinuteData(DataParse mData) {
        setMarkerView(mData);
        setShowLabels(stringSparseArray);

        if (mData.getDatas().size() == 0) {
            lineChart.setNoDataText("暂无数据");
            return;
        }
//        System.out.println("mData.getMin():" + mData.getMin());
//        System.out.println("mData.getMax():" + mData.getMax());
//        System.out.println("mData.getPercentMin():" + mData.getPercentMin());
//        System.out.println("mData.getPercentMax():" + mData.getPercentMax());
//        System.out.println("mData.getVolmax():" + mData.getVolmax());
//        System.out.println("mData.getVolmax():" + mData.getVolmax());
        //设置y左右两轴最大最小值
        axisLeftLine_fenshi.setAxisMinValue(mData.getMin());
        axisLeftLine_fenshi.setAxisMaxValue(mData.getMax());
        axisRightLine_fenshi.setAxisMinValue(mData.getPercentMin());
        axisRightLine_fenshi.setAxisMaxValue(mData.getPercentMax());


        axisLeftBar_fenshi.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar_fenshi.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        axisLeftBar_fenshi.setShowMaxAndUnit(unit);
        axisLeftBar_fenshi.setDrawLabels(true);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
        axisLeftBar_fenshi.setShowOnlyMinMax(true);
        axisLeftBar_fenshi.setAxisMaxValue(mData.getVolmax());
        //   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
        //axisRightBar.setShowOnlyMinMax(true);

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(getResources().getColor(R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine_fenshi.addLimitLine(ll);
        axisRightLine_fenshi.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<Entry>();
        ArrayList<Entry> lineJJEntries = new ArrayList<Entry>();
        ArrayList<String> dateList = new ArrayList<String>();
        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();
        Log.e("##", Integer.toString(xVals.size()));
        System.out.println("mData.getDatas().size():" + mData.getDatas().size());
        for (int i = 0; i < mData.getDatas().size(); i++) {
           /* //避免数据重复，skip也能正常显示
            if (mData.getDatas().get(i).time.equals("13:30")) {
                continue;
            }*/
            MinutesBean t = mData.getDatas().get(i);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            lineCJEntries.add(new Entry(mData.getDatas().get(i).cjprice, i));
            lineJJEntries.add(new Entry(mData.getDatas().get(i).avprice, i));
            barEntries.add(new BarEntry(mData.getDatas().get(i).cjnum, i));
            // dateList.add(mData.getDatas().get(i).time);
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d1.setHighLightColor(Color.BLACK);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);


        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColors(new int[]{Color.RED, Color.GREEN});
//        barDataSet.setColor(Color.RED);
        List<Integer> list = new ArrayList<Integer>();
        list.add(Color.RED);
        list.add(Color.GREEN);
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(new String[240], sets);
//        LineData cd = new LineData(new String[mData.getDatas().size()], sets);
        System.out.println("mData.getDatas().size():"+mData.getDatas().size());
        lineChart.setData(cd);
        BarData barData = new BarData(new String[240], barDataSet);
//        BarData barData = new BarData(new String[mData.getDatas().size()], barDataSet);
        fenBarChart.setData(barData);

        setMinuteOffset();
        lineChart.setAutoScaleMinMaxEnabled(true);
        fenBarChart.setAutoScaleMinMaxEnabled(true);
        lineChart.notifyDataSetChanged();
        fenBarChart.notifyDataSetChanged();
        lineChart.invalidate();//刷新图
        fenBarChart.invalidate();

/*        handler.sendEmptyMessageDelayed(0, 300);*/
    }

    private void initMinuteLineChart() {
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderWidth(1);
        lineChart.setNoDataText("加载中...");
        lineChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        lineChart.setDescription("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);

        //x轴
        xAxisLine_fenshi = lineChart.getXAxis();
        xAxisLine_fenshi.setDrawLabels(true);
        xAxisLine_fenshi.setAvoidFirstLastClipping(true);
        xAxisLine_fenshi.setPosition(XAxis.XAxisPosition.BOTTOM);

        //左边y
        axisLeftLine_fenshi = lineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine_fenshi.setLabelCount(5, true);
        axisLeftLine_fenshi.setDrawLabels(true);
        axisLeftLine_fenshi.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine_fenshi.setDrawAxisLine(false);


        //右边y
        axisRightLine_fenshi = lineChart.getAxisRight();
        axisRightLine_fenshi.setLabelCount(2, true);
        axisRightLine_fenshi.setDrawLabels(true);
        axisRightLine_fenshi.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine_fenshi.setStartAtZero(false);
        axisRightLine_fenshi.setDrawGridLines(false);
        axisRightLine_fenshi.setDrawAxisLine(false);
        //背景线
        xAxisLine_fenshi.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine_fenshi.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine_fenshi.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine_fenshi.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine_fenshi.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine_fenshi.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine_fenshi.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                System.out.println("分时图dataSetIndex:" + dataSetIndex);
                System.out.println("分时图Highlight h.getXIndex():" + h.getXIndex());
                fenBarChart.highlightValues(new Highlight[]{h});
//                lineChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void initMinuteBarChart() {
        fenBarChart.setScaleEnabled(false);
        fenBarChart.setDrawBorders(true);
        fenBarChart.setNoDataText("加载中...");
        fenBarChart.setBorderWidth(1);
        fenBarChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        fenBarChart.setDescription("");
        fenBarChart.setHighlightPerDragEnabled(false);
        Legend barChartLegend = fenBarChart.getLegend();
        barChartLegend.setEnabled(false);

        //bar x y轴
        xAxisBar_fenshi = fenBarChart.getXAxis();
        xAxisBar_fenshi.setDrawLabels(false);
        xAxisBar_fenshi.setDrawGridLines(true);
        xAxisBar_fenshi.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar_fenshi.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftBar_fenshi = fenBarChart.getAxisLeft();
        axisLeftBar_fenshi.setAxisMinValue(0);
        axisLeftBar_fenshi.setDrawGridLines(false);
        axisLeftBar_fenshi.setDrawAxisLine(false);
        axisLeftBar_fenshi.setTextColor(getResources().getColor(R.color.minute_zhoutv));


        axisRightBar_fenshi = fenBarChart.getAxisRight();
        axisRightBar_fenshi.setDrawLabels(false);
        axisRightBar_fenshi.setDrawGridLines(false);
        axisRightBar_fenshi.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine_fenshi.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });

        fenBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //  barChart.highlightValues(new Highlight[]{h});
//               lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                // barChart.setHighlightValue(h);
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }


    private void initChart() {
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1);
        barChart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        barChart.setDescription("");
        barChart.setNoDataText("加载中...");
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(false);
//        barChart.animateX(2);

        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y轴
        xAxisBar = barChart.getXAxis();
        xAxisBar.setDrawLabels(true);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setAvoidFirstLastClipping(true);
        xAxisBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setSpaceTop(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        /****************************************************************/
        combinedchart.setDrawBorders(true);
//        combinedchart.setAutoScaleMinMaxEnabled(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(getResources().getColor(R.color.minute_grayLine));
        combinedchart.setDescription("");
        combinedchart.setNoDataText("加载中...");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);

        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(true);
        final int[] colors = {Color.GREEN, Color.GRAY, Color.YELLOW};
        String[] labels = {"MA5", "MA10", "MA30"};
        combinedchartLegend.setCustom(colors, labels);
        combinedchartLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        combinedchartLegend.setTextColor(Color.BLACK);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setAvoidFirstLastClipping(true);
        xAxisK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(getResources().getColor(R.color.minute_grayLine));

        axisLeftK = combinedchart.getAxisLeft();
        axisLeftK.setDrawGridLines(false);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.0");
                return mFormat.format(value);
            }
        });
        //  axisLeftK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(false);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        combinedchart.setDragDecelerationEnabled(true);
        barChart.setDragDecelerationEnabled(true);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);
        barChart.setDragDecelerationFrictionCoef(0.2f);
        combinedchart.setAutoScaleMinMaxEnabled(true);
        barChart.setAutoScaleMinMaxEnabled(true);

        // 将K线控的滑动事件传递给交易量控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));
        // 将交易量控件的滑动事件传递给K线控件

        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{combinedchart}));
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Log.e("%%%%", h.getXIndex() + "");
/*                lineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));*/
                combinedchart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
            }
        });
        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                barChart.highlightValues(new Highlight[]{h});
                myCombinedLeftMarkerView.setData(e.getVal());
                combinedchart.setMarkerView(myCombinedLeftMarkerView);
/*                barChart.highlightValue(new Highlight(h.getXIndex(), 0));*/
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
            }
        });


    }

    private float getSum(Integer a, Integer b) {

        for (int i = a; i <= b; i++) {
            sum += mData.getKLineDatas().get(i).close;
        }
        return sum;
    }

    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 150 * 5;
        return max;
    }

    private void setCombineData(DataParse mData) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
        ArrayList<Entry> line1Entries = new ArrayList<Entry>();
        ArrayList<Entry> line5Entries = new ArrayList<Entry>();
        ArrayList<Entry> line10Entries = new ArrayList<Entry>();
        ArrayList<Entry> line30Entries = new ArrayList<Entry>();

        for (int i = 0; i < mData.getKLineDatas().size(); i++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
//            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i));
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close));
            line1Entries.add(new Entry(mData.getKLineDatas().get(i).close, i));
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry(getSum(i - 4, i) / 5, i));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry(getSum(i - 9, i) / 10, i));
            }
            if (i >= 29) {
                sum = 0;
                line30Entries.add(new Entry(getSum(i - 29, i) / 30, i));
            }

        }

        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setDrawHorizontalHighlightIndicator(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(false);
        candleDataSet.setIncreasingColor(Color.RED);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setDecreasingColor(getResources().getColor(R.color.price_down_color));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setShadowWidth(0.5f);

        CandleData candleData = new CandleData(xVals, candleDataSet);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(setMaLine(1, xVals, line1Entries));
        sets.add(setMaLine(5, xVals, line5Entries));
        sets.add(setMaLine(10, xVals, line10Entries));
        sets.add(setMaLine(30, xVals, line30Entries));


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        final float xscaleCombin = 20;
        matrixCombin.postScale(xscaleCombin, 1f);

        combinedchart.moveViewToX(mData.getKLineDatas().size() - 1);
        barChart.moveViewToX(mData.getKLineDatas().size() - 1);


        setOffset();
        Message message = handler.obtainMessage();
        message.what = 2;
        handler.sendMessage(message);
    }

    private void setData(DataParse mData) {
        setKlineMarkerView(mData);
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<CandleEntry>();
        ArrayList<Entry> line1Entries = new ArrayList<Entry>();
        ArrayList<Entry> line5Entries = new ArrayList<Entry>();
        ArrayList<Entry> line10Entries = new ArrayList<Entry>();
        ArrayList<Entry> line30Entries = new ArrayList<Entry>();
        String unit = MyUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));

        for (int i = 0; i < mData.getKLineDatas().size(); i++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
//            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, i));
            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).vol, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close, i));
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high, mData.getKLineDatas().get(i).low, mData.getKLineDatas().get(i).open, mData.getKLineDatas().get(i).close));
            line1Entries.add(new Entry(mData.getKLineDatas().get(i).close, i));
            if (i >= 4) {
                sum = 0;
                line5Entries.add(new Entry(getSum(i - 4, i) / 5, i));
            }
            if (i >= 9) {
                sum = 0;
                line10Entries.add(new Entry(getSum(i - 9, i) / 10, i));
            }
            if (i >= 29) {
                sum = 0;
                line30Entries.add(new Entry(getSum(i - 29, i) / 30, i));
            }

        }
        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(25); //bar空隙
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setDrawValues(false);
        barDataSet.setColors(new int[]{Color.RED, Color.GREEN});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(xVals, dataSets);
        barChart.setData(barData);
        final ViewPortHandler viewPortHandlerBar = barChart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        final float xscale = 20;
        touchmatrix.postScale(xscale, 1f);

        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setDrawHorizontalHighlightIndicator(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(false);
        candleDataSet.setIncreasingColor(Color.RED);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setDecreasingColor(getResources().getColor(R.color.price_down_color));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setShadowWidth(0.5f);

        CandleData candleData = new CandleData(xVals, candleDataSet);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(setMaLine(1, xVals, line1Entries));
        sets.add(setMaLine(5, xVals, line5Entries));
        sets.add(setMaLine(10, xVals, line10Entries));
        sets.add(setMaLine(30, xVals, line30Entries));


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        final float xscaleCombin = 20;
        matrixCombin.postScale(xscaleCombin, 1f);

        combinedchart.moveViewToX(mData.getKLineDatas().size() - 1);
        barChart.moveViewToX(mData.getKLineDatas().size() - 1);


        setOffset();
        Message message = handler.obtainMessage();
        message.what = 2;
        handler.sendMessage(message);
//        combinedchart.setAutoScaleMinMaxEnabled(true);
//        barChart.setAutoScaleMinMaxEnabled(true);
//        combinedchart.notifyDataSetChanged();
//        barChart.notifyDataSetChanged();
//        combinedchart.animateX(1);
//        barChart.animateX(1);
//        combinedchart.invalidate();
//        barChart.invalidate();
    }

    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 1) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(true);
            lineDataSetMa.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(Color.GREEN);
        } else if (ma == 10) {
            lineDataSetMa.setColor(Color.GRAY);
        } else if (ma == 30) {
            lineDataSetMa.setColor(Color.YELLOW);
        } else if (ma == 1) {
            lineDataSetMa.setColor(Color.WHITE);
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float lineBottom = combinedchart.getViewPortHandler().offsetBottom();
        float lineTop = combinedchart.getViewPortHandler().offsetTop();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            transLeft = lineLeft;
        } else {
            transLeft = barLeft;
            combinedchart.setViewPortOffsets(barLeft, lineTop, lineRight, lineBottom);

        }
  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 15, transRight, barBottom);
    }

    private void setMinuteOffset() {
        float lineLeft = lineChart.getViewPortHandler().offsetLeft();
        float lineRight = lineChart.getViewPortHandler().offsetRight();
        float fenBarLeft = fenBarChart.getViewPortHandler().offsetLeft();
        float fenBarRight = fenBarChart.getViewPortHandler().offsetRight();
        float fenBarBottom = fenBarChart.getViewPortHandler().offsetBottom();
        float lineBottom = lineChart.getViewPortHandler().offsetBottom();
        float lineTop = lineChart.getViewPortHandler().offsetTop();

        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
        if (fenBarLeft < lineLeft) {
            transLeft = lineLeft;
        } else {
            transLeft = fenBarLeft;
            lineChart.setViewPortOffsets(fenBarLeft, lineTop, lineRight, lineBottom);
        }

        if (lineRight < fenBarRight) {
            offsetRight = Utils.convertPixelsToDp(fenBarRight);
            lineChart.setExtraRightOffset(offsetRight);
            transRight = fenBarRight;
        } else {
            transRight = lineRight;
        }
        fenBarChart.setViewPortOffsets(transLeft, 15, transRight, fenBarBottom);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            onlineData(shareEntry.getCode(), 2,"20160830000000", "600");
            handler.postDelayed(this,TIME);
        }
    };

    private Runnable runnable_typeone = new Runnable() {
        @Override
        public void run() {
            onlineData(shareEntry.getCode(), 1,"20160830000000", "600");
            handler.postDelayed(this,TIME);
        }
    };



    @OnClick({R.id.left_btn, R.id.fenshi, R.id.dayk_tv, R.id.weekk_tv, R.id.monthk_tv, R.id.add_zixuan_tv, R.id.left_btn_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn_view:
            case R.id.left_btn:
                finish();
                break;
            case R.id.fenshi:
                fenshiLinear.setVisibility(View.VISIBLE);
                klineLinear.setVisibility(View.GONE);
                fenshiView.setVisibility(View.VISIBLE);
                daykView.setVisibility(View.GONE);
                weekkView.setVisibility(View.GONE);
                monthkView.setVisibility(View.GONE);
                initMinuteLineChart();
                initMinuteBarChart();
                if (current_stock.equals("sh60")) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable,TIME);
//                    onlineData(shareEntry.getCode(), 2, "600");
                } else {
                    handler.removeCallbacks(runnable_typeone);
                    handler.postDelayed(runnable_typeone,TIME);
//                    onlineData(shareEntry.getCode(), 1, "600");
                }
                break;
            case R.id.dayk_tv:
                removeMinuteCall();
                fenshiView.setVisibility(View.GONE);
                daykView.setVisibility(View.VISIBLE);
                weekkView.setVisibility(View.GONE);
                monthkView.setVisibility(View.GONE);
                fenshiLinear.setVisibility(View.GONE);
                klineLinear.setVisibility(View.VISIBLE);
                getData(shareEntry.getQ_code(), 0);
                break;
            case R.id.weekk_tv:
                removeMinuteCall();
                fenshiView.setVisibility(View.GONE);
                daykView.setVisibility(View.GONE);
                weekkView.setVisibility(View.VISIBLE);
                monthkView.setVisibility(View.GONE);
                fenshiLinear.setVisibility(View.GONE);
                klineLinear.setVisibility(View.VISIBLE);
                getData(shareEntry.getQ_code(), 1);
                break;
            case R.id.monthk_tv:
                removeMinuteCall();
                fenshiView.setVisibility(View.GONE);
                daykView.setVisibility(View.GONE);
                weekkView.setVisibility(View.GONE);
                monthkView.setVisibility(View.VISIBLE);
                fenshiLinear.setVisibility(View.GONE);
                klineLinear.setVisibility(View.VISIBLE);
                getData(shareEntry.getQ_code(), 2);
                break;
            case R.id.add_zixuan_tv:
                System.out.println("!isAddZixuan(shareEntry.getQ_code())" + !isAddZixuan(shareEntry.getQ_code()));
                if (!isAddZixuan(shareEntry.getQ_code())) {
                    if(MyApplication.login_stauts){
                        addZixuanTv.setText("已添加");
                        MyApplication.addString(shareEntry.getQ_code());
                    }else{
                        Intent intent = new Intent();
                        intent.setClass(KLineActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    addZixuanTv.setText("+自选");
                    MyApplication.removeString(shareEntry.getQ_code());
//                    MyApplication.addString(shareEntry.getQ_code());
                }
                break;
        }
    }

    public void removeMinuteCall(){
      handler.removeCallbacks(runnable);
      handler.removeCallbacks(runnable_typeone);
    }

    private void setMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(KLineActivity.this, R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(KLineActivity.this, R.layout.mymarkerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, mData);
    }

    private void setKlineMarkerView(DataParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(KLineActivity.this, R.layout.mymarkerview);
        combinedchart.setMarker(leftMarkerView, mData);
    }

    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine_fenshi.setXLabels(labels);
        xAxisBar_fenshi.setXLabels(labels);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeMinuteCall();
        handler.removeCallbacks(runnable_stockData);
    }
}
