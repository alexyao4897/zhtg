package com.money.deep.tstock.listener;

/**
 * Created by Administrator on 2016/9/13.
 */
public class FragmentListener {
    public static interface GMListener {
        public void onClickItem(int position);
    }

    public static interface CaseDetailListener {
        public void onLeftBtnClick();
    }

    public static interface HomeToPredictListener{
        public void goPredictClick();
    }

    public static interface HomeToNextDayListener{
        public void goNextDayView();
    }

    public static interface HomeToCaseViewListener{
        public void goCaseView();
    }

    public static interface LoginToMainListener{
        public void goMainView();
    }

}
