package com.money.deep.tstock.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.money.deep.tstock.R;

/**
 * Created by fengxg on 2016/9/7.
 */
public class ColorBar extends View{
    private int num;
    private float first_percent;
    private float second_percent;
    private float third_percent;
    private Paint mPaint;

    public ColorBar(Context context) {
        this(context,null);
    }

    public ColorBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ColorBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomColorView,defStyleAttr,0);
        try{
            num = typedArray.getInteger(R.styleable.CustomColorView_num,3);
            first_percent = typedArray.getFloat(R.styleable.CustomColorView_first_percent, 0.6f);
            second_percent = typedArray.getFloat(R.styleable.CustomColorView_second_percent,0.3f);
            third_percent = typedArray.getFloat(R.styleable.CustomColorView_third_percent,0.1f);
        }finally {
            typedArray.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


/*    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
//            width = getPaddingLeft() + getWidth() + getPaddingRight();
        }
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(getResources().getColor(R.color.kanzhang_color));
        canvas.drawRect(0, 0, getWidth() * first_percent, getHeight(), mPaint);
        mPaint.setColor(getResources().getColor(R.color.kanwen_color));
        canvas.drawRect(getWidth() * first_percent, 0, getWidth() * first_percent + getWidth() * second_percent, getHeight(), mPaint);
        mPaint.setColor(getResources().getColor(R.color.kandie_color));
        canvas.drawRect(getWidth() * first_percent + getWidth() * second_percent, 0,  getWidth() * first_percent + getWidth() * second_percent+getWidth()*third_percent,getHeight(),mPaint);
    }

    public void setFirstPercent(float first_percent){
        this.first_percent = first_percent;
        invalidate();
    }

    public void setSecondPercent(float second_percent){
        this.second_percent = second_percent;
        invalidate();
    }

    public void setThirdPercent(float third_percent){
        this.third_percent = third_percent;
        invalidate();
    }




}
