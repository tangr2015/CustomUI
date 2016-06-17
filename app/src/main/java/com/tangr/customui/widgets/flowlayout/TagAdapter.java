package com.tangr.customui.widgets.flowlayout;

import java.util.List;

/**
 * Created by tangr on 2016/6/17.
 */
public class TagAdapter {
    private List<String> mDatas;
    private OnDataChangedListener mOnDataChangedListener;
    static interface OnDataChangedListener
    {
        void onChanged();
    }

    public TagAdapter(List<String> datas)
    {
        mDatas = datas;
    }

    void setOnDataChangedListener(OnDataChangedListener listener)
    {
        mOnDataChangedListener = listener;
    }

    public void notifyDataChanged()
    {
        mOnDataChangedListener.onChanged();
    }

    public int getCount()
    {
        return mDatas == null ? 0 : mDatas.size();
    }

    public String getItem(int position)
    {
        return mDatas.get(position);
    }
}
