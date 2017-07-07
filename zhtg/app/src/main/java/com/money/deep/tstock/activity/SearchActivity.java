package com.money.deep.tstock.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.money.deep.tstock.R;
import com.money.deep.tstock.app.MyApplication;
import com.money.deep.tstock.http.DataManager;
import com.money.deep.tstock.data.ParseData;
import com.money.deep.tstock.model.ShareEntry;
import com.money.deep.tstock.model.StockItem;
import com.money.deep.tstock.util.ActivityStackControlUtil;
import com.money.deep.tstock.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fengxg on 2016/9/8.
 */
public class SearchActivity extends Activity {
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.cancel_tv)
    TextView cancelTv;
    @Bind(R.id.search_result_list)
    ListView searchResultList;
    @Bind(R.id.cancel_imageview)
    ImageView cancelImageview;
    @Bind(R.id.no_result_layout)
    RelativeLayout noResultLayout;
    View view;
    private MyAdapter myAdapter;
    private MySearchStrAdapter mySearchStrAdapter;
    String results ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivityStackControlUtil.add(this);
        ButterKnife.bind(this);
        myAdapter = new MyAdapter(SearchActivity.this);
        searchResultList.setAdapter(myAdapter);
        initEvent();
        results = SPUtils.get(SearchActivity.this,"search_result","").toString();

    }

    private void initEvent() {
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    String search_str = searchEdit.getText().toString();
                    results += search_str+",";
                    SPUtils.put(SearchActivity.this,"search_result",results);
                    searchResultList.setVisibility(View.VISIBLE);
                    setSearchData(search_str);
                }
                return true;
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchResultList.setVisibility(View.VISIBLE);
                String input_str = s.toString();
                setSearchData(input_str);
            }
        });

        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultList.setVisibility(View.VISIBLE);
                noResultLayout.setVisibility(View.GONE);
                results = SPUtils.get(SearchActivity.this, "search_result", "").toString();
                ArrayList<String> result_list = getResultItem(results);
                mySearchStrAdapter = new MySearchStrAdapter(SearchActivity.this, result_list);
                searchResultList.setAdapter(mySearchStrAdapter);
                if (view == null) {
                    view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_result_clear_view, null);
                    searchResultList.addFooterView(view);
                }
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private ArrayList<String> getResultItem(String results){
        ArrayList<String> result_list = new ArrayList<String>();
        if(results != null && !results.equals("")){
            String[] stringArr = results.split(",");
            for(int i = 0;i < stringArr.length;i++){
                String str = stringArr[i];
                if(!result_list.contains(str)){
                    result_list.add(str);
                }
            }
        }
        return result_list;

    }

    private void setSearchData(String key) {
        DataManager.searchStock(key, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<StockItem> stockItems = (new Gson()).fromJson(
                        response.optString("StockItems"),
                        new TypeToken<List<StockItem>>() {
                        }.getType());
                if (stockItems != null && stockItems.size() > 0) {
                    searchResultList.setVisibility(View.VISIBLE);
                    noResultLayout.setVisibility(View.GONE);
                    if(view == null){
                        view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_result_clear_view, null);
                        searchResultList.addFooterView(view);
                    }
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SPUtils.put(SearchActivity.this,"search_result","");
                            searchResultList.setVisibility(View.GONE);
                        }
                    });
                    searchResultList.setAdapter(myAdapter);
                    myAdapter.refreshData(stockItems);
                }else{
                    searchResultList.setVisibility(View.GONE);
                    noResultLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @OnClick(R.id.cancel_imageview)
    public void onClick() {
        searchEdit.setText("");
        searchResultList.setVisibility(View.GONE);
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<StockItem> mData = new ArrayList<StockItem>();

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void refreshData(ArrayList<StockItem> data) {
            this.mData = data;
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
            final ViewHolder viewHolder;
            final StockItem stockItem = mData.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.search_result_list_item, null, false);
                viewHolder.stockItemName = (TextView) convertView.findViewById(R.id.stockitem_name);
                viewHolder.stockItemCode = (TextView) convertView.findViewById(R.id.stockitem_code);
                viewHolder.addImageView = (ImageView) convertView.findViewById(R.id.add_imgbtn);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.stockItemName.setText(stockItem.getStockName());
            viewHolder.stockItemCode.setText(stockItem.getStockCode());

            for(int i = 0;i< MyApplication.getArray().size();i++){
                String string = MyApplication.getArray().get(i);
                if(stockItem.getStockCode().equals(string)){
                    stockItem.setZixuan(true);
                }
            }
            viewHolder.addImageView.setBackgroundResource(stockItem.isZixuan() ? R.drawable.select_img : R.drawable.add_img);
            viewHolder.addImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stockItem.isZixuan()) {
                        stockItem.setZixuan(false);
                        String stockcode = stockItem.getStockCode();
                        MyApplication.removeString(stockcode);
                        viewHolder.addImageView.setBackgroundResource(R.drawable.add_img);
                    } else {
                        String loginstatus =  SPUtils.get(SearchActivity.this, "loginstatus", "").toString();
                        if (!loginstatus.equals("")) {
                            stockItem.setZixuan(true);
                            String stockcode = stockItem.getStockCode();
                            MyApplication.addString(stockcode);
                            viewHolder.addImageView.setBackgroundResource(R.drawable.select_img);
                        } else {
                            Intent intent = new Intent();
                            intent.setClass(SearchActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataManager.getInfo(stockItem.getStockCode(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null && !response.equals("")) {
                                ArrayList<ShareEntry> entries = new ParseData().parseArrayData(response);
                                if (entries != null && entries.size() > 0) {
                                        Intent intent = new Intent();
                                        intent.setClass(SearchActivity.this, KLineActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("shareInfo", entries.get(0));
                                        bundle.putString("current_stock", "sh60");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                }

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                }
            });
            return convertView;
        }
    }

    private class ViewHolder {
        TextView stockItemName;
        TextView stockItemCode;
        ImageView addImageView;
    }

    private class SearchViewHolder{
        TextView search_textview;
    }

    private class MySearchStrAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        private ArrayList<String> mData = new ArrayList<String>();
        public MySearchStrAdapter(Context context,ArrayList<String> data){
            layoutInflater = LayoutInflater.from(context);
            this.mData = data;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            SearchViewHolder searchViewHolder;
            if(convertView == null){
                searchViewHolder = new SearchViewHolder();
                convertView = layoutInflater.inflate(R.layout.search_str_list_item,null);
                searchViewHolder.search_textview = (TextView) convertView.findViewById(R.id.search_str_tv);
                convertView.setTag(searchViewHolder);
            }else {
                searchViewHolder = (SearchViewHolder) convertView.getTag();
            }
            searchViewHolder.search_textview.setText(mData.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSearchData(mData.get(position));
                }
            });
            return convertView;
        }
    }
}
