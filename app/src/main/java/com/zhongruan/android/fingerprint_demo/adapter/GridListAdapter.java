package com.zhongruan.android.fingerprint_demo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.baseadapter.ViewHolder;
import com.zhongruan.android.fingerprint_demo.baseadapter.base.CommonBaseAdapter;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;

import java.io.File;
import java.util.List;

public class GridListAdapter extends CommonBaseAdapter<Bk_ks> {
    public GridListAdapter(Context context, List<Bk_ks> datas, boolean isLoadMore) {
        super(context, datas, isLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, final Bk_ks data, int position) {
        holder.setBackgroundImage(mContext, R.id.tvSeat, new File(FileUtils.getAppSavePath() + "/" + DbServices.getInstance(mContext).selectBkKs(data.getKs_zjno()).get(0).getKs_xp()));
        holder.setText(R.id.tvName, data.getKs_xm());
        holder.setText(R.id.tvKsno, data.getKs_ksno());
        if (data.getIsRZ().equals("0")) {
            holder.setBgColor(R.id.ll_kslist, ContextCompat.getColor(this.mContext, R.color.auth_kslist_hastauth));
        } else if (data.getIsRZ().equals("1")) {
            holder.setBgColor(R.id.ll_kslist, ContextCompat.getColor(this.mContext, R.color.auth_kslist_hasauth));
        } else if (data.getIsRZ().equals("2")) {
            holder.setBgColor(R.id.ll_kslist, ContextCompat.getColor(this.mContext, R.color.button_wjdj));
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.pad_adapter_kslist2;
    }
}
