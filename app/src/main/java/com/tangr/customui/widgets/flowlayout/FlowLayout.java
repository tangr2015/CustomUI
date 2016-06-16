package com.tangr.customui.widgets.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tangr on 2016/6/16.
 */
public class FlowLayout extends ViewGroup{
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if(modeWidth==MeasureSpec.EXACTLY&&modeHeight==MeasureSpec.EXACTLY){
            setMeasuredDimension(sizeWidth,sizeHeight);
            return;
        }

        int width = 0;
        int height = 0;
        int currentW = 0;
        int maxH = 0;

        for (int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int occupyW = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int occupyH = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;

            if(currentW+occupyW>sizeWidth){
                height += maxH;
                width = Math.max(currentW,width);
                currentW = occupyW;
                maxH = occupyH;
            }else {
                currentW += occupyW;
                maxH = Math.max(maxH,occupyH);
            }

            if (i==getChildCount()-1){
                width = Math.max(currentW,width);
                height += maxH;
            }
        }
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int left = 0;
        int top = 0;

        int currentW = 0;
        int maxH = 0;

        for (int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams)child.getLayoutParams();
            int occupyW = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int occupyH = child.getMeasuredHeight()+lp.topMargin+lp.bottomMargin;

            if(currentW+occupyW>width){
                left = 0;
                top += maxH;
                currentW = occupyW;
                maxH = occupyH;
            }else {
                currentW += occupyW;
                maxH = Math.max(maxH,occupyH);
            }
            l = left + lp.leftMargin;
            r = l + child.getMeasuredWidth();
            t = top + lp.topMargin;
            b = t + child.getMeasuredHeight();
            child.layout(l,t,r,b);
            left += occupyW;
        }
    }
}
