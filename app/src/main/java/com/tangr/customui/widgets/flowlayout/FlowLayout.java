package com.tangr.customui.widgets.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tangr.customui.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tangr on 2016/6/16.
 */
public class FlowLayout extends ViewGroup implements View.OnClickListener, TagAdapter.OnDataChangedListener {
    public final static int MODE_SINGLE = 1;
    public final static int MODE_MULTI = 2;
    private final static int tag_prefix = 100;
    private TagAdapter adapter;
    private int mode = MODE_MULTI;
    private int max = -1;
    private List<Integer> mTags;
    private int curCount = 0;
    private int curIndex = -1;

    public FlowLayout(Context context) {
        super(context);
        initView();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        int click = (int) v.getTag() - tag_prefix;
        if(mode == MODE_SINGLE){
            if(click==curIndex){
                unselect();
                curIndex = -1;
            }else {
                unselect();
                curIndex = click;
                select();
            }
        }else {
            curIndex = click;
            if(mTags.contains(new Integer(click))){
                unselect();
            }else {
                select();
            }
        }
    }

    private void unselect() {
        if(mode == MODE_SINGLE){
            if(curIndex>-1){
                ((TagView)getChildAt(curIndex)).setChecked(false);
            }
        }else {
            if(curCount>0){
                curCount--;
                mTags.remove(new Integer(curIndex));
                ((TagView)getChildAt(curIndex)).setChecked(false);
            }
        }
    }

    private void select() {
        if(mode == MODE_SINGLE){
            ((TagView)getChildAt(curIndex)).setChecked(true);
            if (listener!=null)
                listener.onSelect(curIndex);
        }else {
            if (curCount<max){
                curCount++;
                mTags.add(new Integer(curIndex));
                ((TagView)getChildAt(curIndex)).setChecked(true);
                if (listener!=null)
                    listener.onSelect(curIndex);
            }else {
                if (listener!=null)
                    listener.onSelectFull(max);
            }
        }
    }

    private OnTagViewClickListener listener;

    public void setOnSelectListener(OnTagViewClickListener listener){
        this.listener = listener;
    }

    private void initCallback() {
        for (int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            child.setTag(tag_prefix+i);
            child.setOnClickListener(this);
        }
    }

    public void setAdapter(TagAdapter adapter){
        mTags = new ArrayList<Integer>();
        this.adapter = adapter;
        this.adapter.setOnDataChangedListener(this);
        changeView();
    }

    @Override
    public void onChanged() {
        changeView();
    }

    private void changeView() {
        removeAllViews();
        TagView tagView = null;
//        float density = getContext().getResources().getDisplayMetrics().density;
//        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        int margin = (int) (4*density);
//        lp.setMargins(margin,margin,margin,margin);
        for (int i=0;i<adapter.getCount();i++){
            tagView = (TagView) LayoutInflater.from(getContext()).inflate(R.layout.item_tag,this,false);
//            tagView = new TagView(getContext());
//            tagView.setBackgroundResource(R.drawable.item_flow_selector);
            tagView.setText(adapter.getItem(i));
//            tagView.setTextColor(Color.BLACK);
//            tagView.setClickable(true);
            addView(tagView);
        }
        initCallback();
    }

    public void setMode(int mode,int max) {
        if(mode==MODE_SINGLE){
            this.mode = mode;
            this.max = 1;
        }else {
            this.mode = MODE_MULTI;
            this.max = max >= -1 ? max : -1;
        }
    }

    public void setMode(int mode){
        if(mode==MODE_SINGLE){
            this.mode = mode;
            max = 1;
        }
        else {
            this.mode = MODE_MULTI;
            max = -1;
        }
    }

    public List<Integer> getSelected(){
        if(mode==MODE_SINGLE){
            List<Integer> list = new ArrayList<Integer>();
            list.add(curIndex);
            return list;
        }else {
            Collections.sort(mTags);
            return mTags;
        }
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
