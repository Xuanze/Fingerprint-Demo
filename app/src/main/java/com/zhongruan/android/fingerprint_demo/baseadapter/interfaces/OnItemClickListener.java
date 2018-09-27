package com.zhongruan.android.fingerprint_demo.baseadapter.interfaces;

import com.zhongruan.android.fingerprint_demo.baseadapter.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
