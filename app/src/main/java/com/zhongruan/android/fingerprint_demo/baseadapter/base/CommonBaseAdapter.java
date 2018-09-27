package com.zhongruan.android.fingerprint_demo.baseadapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhongruan.android.fingerprint_demo.baseadapter.ViewHolder;
import com.zhongruan.android.fingerprint_demo.baseadapter.interfaces.OnItemChildClickListener;
import com.zhongruan.android.fingerprint_demo.baseadapter.interfaces.OnItemClickListener;
import com.zhongruan.android.fingerprint_demo.baseadapter.interfaces.OnSwipeMenuClickListener;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Othershe
 * Time: 2016/9/9 15:52
 */
public abstract class CommonBaseAdapter<T> extends com.zhongruan.android.fingerprint_demo.baseadapter.base.BaseAdapter<T> {
    private OnItemClickListener<T> mItemClickListener;
    private ArrayList<Integer> mViewId = new ArrayList<>();
    private ArrayList<OnSwipeMenuClickListener<T>> mListener = new ArrayList<>();
    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    private ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public CommonBaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

    protected abstract int getItemLayoutId();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return ViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position - getHeaderCount());
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, getAllData().get(position), position);
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isFastClick()) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(viewHolder, getAllData().get(position), position);
                    }
                }
            }
        });
        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (viewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                viewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Utils.isFastClick()) {
                            mItemChildListeners.get(tempI).onItemChildClick(viewHolder, getAllData().get(position), position);
                        }
                    }
                });
            }
        }
        if (mViewId.size() > 0 && mListener.size() > 0 && viewHolder.getSwipeView() != null) {
            ViewGroup swipeView = (ViewGroup) viewHolder.getSwipeView();
            for (int i = 0; i < mViewId.size(); i++) {
                final int tempI = i;
                swipeView.findViewById(mViewId.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Utils.isFastClick()) {
                            mListener.get(tempI).onSwipMenuClick(viewHolder, getAllData().get(position), position);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    public void setOnItemClickListener(OnItemClickListener<Bk_ks> itemClickListener) {
        mItemClickListener = (OnItemClickListener<T>) itemClickListener;
    }

    public void setOnSwipMenuClickListener(int viewId, OnSwipeMenuClickListener<T> swipeMenuClickListener) {
        mViewId.add(viewId);
        mListener.add(swipeMenuClickListener);
    }

    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }
}
