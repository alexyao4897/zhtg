package com.money.deep.tstock.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.money.deep.tstock.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CaseFragment extends Fragment {
    private GmFragment gmFragment;
    private CaseDetailFragment caseDetailFragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.case_fragment, container, false);
        setSelect(0);
        return view;
    }

    public void setSelect(int i){
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (i){
            case 0:
                if(gmFragment == null){
                    gmFragment = new GmFragment();
                    ft.add(R.id.case_fl_content,gmFragment);
                }
                ft.show(gmFragment);
                break;
            case 1:
                if(caseDetailFragment == null){
                    caseDetailFragment = new CaseDetailFragment();
                    ft.add(R.id.case_fl_content, caseDetailFragment);
                }
                ft.show(caseDetailFragment);
                break;
        }
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("caseFragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("caseFragment onPause");
    }


    public void hideFragment(FragmentTransaction ft) {
        if(gmFragment != null){
            ft.hide(gmFragment);
        }
        if(caseDetailFragment != null){
            ft.hide(caseDetailFragment);
        }
    }
}
