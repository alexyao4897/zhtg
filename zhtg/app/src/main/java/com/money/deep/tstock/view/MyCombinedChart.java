package com.money.deep.tstock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.money.deep.tstock.data.DataParse;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;


public class MyCombinedChart extends CombinedChart {
    private MyLeftMarkerView myMarkerViewLeft;
//    private MyRightMarkerView myMarkerViewRight;
    private DataParse minuteHelper;

    public MyCombinedChart(Context context) {
        super(context);
    }

    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        /*此两处不能重新示例*/
//        mXAxis = new MyXAxis();
        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);
//        mXAxisRenderer = new XAxisRenderer(mViewPortHandler, (XAxis) mXAxis, mLeftAxisTransformer, this);

//        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
//        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);

    }

    @Override
    public MyYAxis getAxisLeft() {
        return (MyYAxis) super.getAxisLeft();
    }

    /*返回转型后的左右轴*/
//    @Override
//    public MyYAxis getAxisLeft() {
//        return (MyYAxis) super.getAxisLeft();
//    }
//
//    @Override
//    public MyXAxis getXAxis() {
//        return (MyXAxis) super.getXAxis();
//    }


//    @Override
//    public MyYAxis getAxisRight() {
//        return (MyYAxis) super.getAxisRight();
//    }

    public void setMarker(MyLeftMarkerView markerLeft, DataParse minuteHelper) {
        this.myMarkerViewLeft = markerLeft;
//        this.myMarkerViewRight = markerRight;
        this.minuteHelper = minuteHelper;
    }

    public void setHighlightValue(Highlight h) {
        if (mData == null)
            mIndicesToHighlight = null;
        else {
            mIndicesToHighlight = new Highlight[]{
                    h};
        }
        invalidate();
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkerViews || !valuesToHighlight())
            return;
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            int xIndex = mIndicesToHighlight[i].getXIndex();
            int dataSetIndex = mIndicesToHighlight[i].getDataSetIndex();
            float deltaX = ((mData == null ? 0.f : mData.getXValCount()) - 1.f);
            if (xIndex <= deltaX && xIndex <= deltaX * mAnimator.getPhaseX()) {
                Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
                // make sure entry not null
                if (e == null || e.getXIndex() != mIndicesToHighlight[i].getXIndex())
                    continue;
                float[] pos = getMarkerPosition(e, highlight);
                // check bounds
                if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                    continue;

                float yValForXIndex1 = minuteHelper.getKLineDatas().get(mIndicesToHighlight[i].getXIndex()).close;
                myMarkerViewLeft.setData(yValForXIndex1);
                myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                /*修复bug*/
                // invalidate();
                /*重新计算大小*/
                myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                        myMarkerViewLeft.getMeasuredHeight());


                myMarkerViewLeft.draw(canvas, mViewPortHandler.contentLeft() - myMarkerViewLeft.getWidth(), pos[1] - myMarkerViewLeft.getHeight() / 2);
            }
        }
    }
}
