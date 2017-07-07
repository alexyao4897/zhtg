package com.money.deep.tstock.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.money.deep.tstock.R;

/**
 * Created by fengxg on 2016/9/13.
 */
public class CaseSummaryFragment extends Fragment {

    private  static CaseSummaryFragment INSTANCE;
    public  static CaseSummaryFragment getInstance(){
        if(INSTANCE == null){
            INSTANCE = new CaseSummaryFragment();
        }
        return  INSTANCE;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.case_summary_fragment,container,false);
        return view;
    }
}
