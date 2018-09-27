package com.zhongruan.android.fingerprint_demo.ui;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.adapter.GridListAdapter;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.baseadapter.interfaces.OnLoadMoreListener;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class KWDJActivity2 extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLlRzjlBack;
    private TextView mTvInputTip;
    private List<Bk_ks> bk_ks;
    private String ccmc, kcmc;
    private RecyclerView mRecyclerview;
    private GridListAdapter mAdapter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_kwdj2);
    }

    @Override
    public void initViews() {
        mLlRzjlBack = findViewById(R.id.ll_kwdj_back);
        mTvInputTip = findViewById(R.id.tv_inputTip);
        mRecyclerview = findViewById(R.id.recyclerview);
        kcmc = DbServices.getInstance(getBaseContext()).selectKC().get(0).getKc_name();
        ccmc = DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_name();
        mTvInputTip.setText(ccmc + " " + kcmc + " " + DbServices.getInstance(getBaseContext()).selectCC().get(0).getKm_name());
        //初始化adapter
        mAdapter = new GridListAdapter(this, null, true);
        //初始化EmptyView
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mRecyclerview.getParent(), false);
        mAdapter.setEmptyView(emptyView);
        //初始化 开始加载更多的loading View
        mAdapter.setLoadingView(R.layout.load_loading_layout);
        //加载失败，更新footer view提示
        mAdapter.setLoadFailedView(R.layout.load_failed_layout);
        //加载完成，更新footer view提示
        mAdapter.setLoadEndView(R.layout.load_end_layout);
        //设置加载更多触发的事件监听
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(gridLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        //延时3s刷新列表
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bk_ks = DbServices.getInstance(getBaseContext()).queryBKKSList(kcmc, ccmc);
                //刷新数据
                mAdapter.setNewData(bk_ks);
            }
        }, 1500);
    }

    @Override
    public void initListeners() {
        mLlRzjlBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_kwdj_back:
                finish();
                break;
        }
    }
}
