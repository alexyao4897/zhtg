package com.money.deep.tstock.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author RAW
 */
public class FlowLayout extends ViewGroup {
	private static int PAD_H = 3, PAD_V = 3; // Space between child views.
	private int mHeight;

	public void setPAD_H(int h) {
		PAD_H = h;
	}

	public void setPAD_V(int v) {
		PAD_V = v;
	}

	public FlowLayout(Context context) {
		super(context);
	}

	public FlowLayout(Context context,AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);
		final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		final int count = getChildCount();
		int xpos = getPaddingLeft();
		int ypos = getPaddingTop();
		int childHeightMeasureSpec;
		if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST)
			childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
		else
			childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		mHeight = 0;
		int maxheight = 0;
		for(int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if(child.getVisibility() != GONE) {
				child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
				final int childw = child.getMeasuredWidth();
				mHeight = child.getMeasuredHeight() + PAD_V;
				if(xpos + childw > width) {
					xpos = getPaddingLeft();
					if(i != 0)
						ypos += maxheight;
					else
						ypos += mHeight;
					maxheight = 0;
				}
				maxheight = mHeight > maxheight ? mHeight : maxheight;
				xpos += childw + PAD_H;
			}
		}
		if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
			height = ypos + maxheight;
		}else if(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			if(ypos + maxheight < height) {
				height = ypos + maxheight;
			}
		}
		// height += 5; // Fudge to avoid clipping bottom of last row.
		setMeasuredDimension(width + getPaddingLeft(), height + 5);
	} // end onMeasure()

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		int xpos = getPaddingLeft();
		int ypos = getPaddingTop();
		int maxheight = 0;
		for(int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			if(child.getVisibility() != GONE) {
				final int childw = child.getMeasuredWidth();
				final int childh = child.getMeasuredHeight();
				if(xpos + childw > width) {
					xpos = getPaddingLeft();
					if(i != 0)
						ypos += maxheight;
					else
						ypos += mHeight;
					maxheight = 0;
				}
				maxheight = childh + PAD_V > maxheight ? childh + PAD_V : maxheight;
				child.layout(xpos, ypos, xpos + childw, ypos + childh);
				xpos += childw + PAD_H;
			}
		}
	} // end onLayout()
}
