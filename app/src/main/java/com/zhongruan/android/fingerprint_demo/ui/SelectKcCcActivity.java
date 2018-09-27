package com.zhongruan.android.fingerprint_demo.ui;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.adapter.SelectCcAdapter;
import com.zhongruan.android.fingerprint_demo.adapter.SelectKcMcAdapter;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.db.Bk_ksDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ks_tempDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ksxpDao;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.Ks_ccDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kcDao;
import com.zhongruan.android.fingerprint_demo.db.Kstz_zwDao;
import com.zhongruan.android.fingerprint_demo.db.Rz_ks_zwDao;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_cc;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog2;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.DateUtil;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import rx.android.BuildConfig;

/**
 * 选取考场场次
 * Created by Administrator on 2017/9/7.
 */
public class SelectKcCcActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLlCcBack;
    private TextView mTvEmpty, mTvSelectkc, rz_title;
    private GridView mGvContent;
    private SelectCcAdapter selectCcAdapter;
    private SelectKcMcAdapter selectKcMcAdapter;
    private List<Ks_cc> ksCcList;
    private List<Ks_kc> ksKcList;
    private Button mLlButtons;
    private Ks_cc cc;
    private List<Ks_kc> mXZksKcList;
    private List<Ks_cc> ccList;
    private ImageView mIvSelectall;
    private LinearLayout mLvSelectall;
    private List<Bk_ks> ksList;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_select_kccc);
    }

    @Override
    public void initViews() {
        mLlCcBack = findViewById(R.id.ll_cc_back);
        mTvEmpty = findViewById(R.id.tv_empty);
        mTvSelectkc = findViewById(R.id.tv_selectkc);
        mGvContent = findViewById(R.id.gv_content);
        mLlButtons = findViewById(R.id.ll_buttons);
        rz_title = findViewById(R.id.rz_title);
        mIvSelectall = findViewById(R.id.iv_selectall);
        mLvSelectall = findViewById(R.id.lv_selectall);
        mTvEmpty.setVisibility(View.GONE);
        ksKcList = new ArrayList<>();
        ksCcList = new ArrayList<>();
        MyApplication.getApplication().setShouldStopUploadingData(true);
    }

    @Override
    public void initListeners() {
        List<Ks_kc> ksKcList = DbServices.getInstance(SelectKcCcActivity.this).loadAllkc();
        List<Ks_cc> ksCcList = DbServices.getInstance(SelectKcCcActivity.this).loadAllcc();
        mLlCcBack.setOnClickListener(this);
        mLvSelectall.setOnClickListener(this);
        if (ksKcList.size() != 0 && ksCcList.size() != 0) {
            if (DbServices.getInstance(SelectKcCcActivity.this).queryKC("1")) {
                selectCC();
            } else {
                selectKC();
            }
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lv_selectall:
                this.selectKcMcAdapter.selectAll();
                resetKcText();
                break;
            case R.id.ll_cc_back:
                finish();
                break;
        }
    }

    private void selectKC() {
        rz_title.setText("请选择考场");
        mGvContent.setNumColumns(4);
        mLvSelectall.setVisibility(View.VISIBLE);
        Intent getIntent = getIntent();
        ArrayList<String> arrayList = getIntent.getStringArrayListExtra("kcmc");
        for (int i = 0; i < arrayList.size(); i++) {
            DbServices.getInstance(getBaseContext()).saveKsKc(arrayList.get(i));
        }
        ksKcList.addAll(DbServices.getInstance(SelectKcCcActivity.this).loadAllkc());
        selectKcMcAdapter = new SelectKcMcAdapter(SelectKcCcActivity.this, ksKcList);
        mGvContent.setAdapter(selectKcMcAdapter);
        resetKcText();
        mGvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectKcMcAdapter.choiceState(i);
                selectKcMcAdapter.checkSelectAll();
                resetKcText();
            }
        });

        mLlButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isFastClick()) {
                    mXZksKcList = selectKcMcAdapter.getChosenKcList();
                    if (mXZksKcList.size() > 0) {
                        MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Ks_kcDao.TABLENAME + " SET  kc_extract = 0");
                        MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Ks_ccDao.TABLENAME + " SET  cc_extract = 0");
                        for (int i = 0; i < mXZksKcList.size(); i++) {
                            DbServices.getInstance(getBaseContext()).saveKsKc(mXZksKcList.get(i).getKc_name());
                        }
                        selectCC();
                    }
                }
            }
        });
    }

    private void selectCC() {
        rz_title.setText("请选择场次");
        mGvContent.setNumColumns(2);
        mLvSelectall.setVisibility(View.GONE);
        mTvEmpty.setVisibility(View.VISIBLE);
        mTvSelectkc.setVisibility(View.GONE);
        ksCcList.addAll(DbServices.getInstance(SelectKcCcActivity.this).loadAllcc());
        selectCcAdapter = new SelectCcAdapter(SelectKcCcActivity.this, ksCcList);
        mGvContent.setAdapter(selectCcAdapter);
        mGvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectCcAdapter.choiceSingleState(i);
                resetCcText();
            }
        });
        mLlButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.isFastClick()) {
                    ccList = selectCcAdapter.getChosenCcList();
                    if (ccList.size() > 0) {
                        if (!DateUtil.isTime(DateUtil.dateToLong(DateUtil.getNowTime_Millisecond3()), DateUtil.dateToLong(cc.getCc_kssj()), DateUtil.dateToLong(cc.getCc_jssj()))) {
                            new HintDialog2(SelectKcCcActivity.this, R.style.dialog, "当前场次不在当前考试时间", "当前时间：" + DateUtil.getNowTimeChinese(), "所选场次：" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.getCc_kssj())) + "-" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.getCc_jssj())), new HintDialog2.OnCloseListener() {
                                @Override
                                public void onClick(Dialog dialog, boolean confirm) {
                                    if (confirm) {
                                        dialog.dismiss();
                                    } else {
                                        startActivity(new Intent(SelectKcCcActivity.this, TimeActivity.class));
                                        dialog.dismiss();
                                    }
                                }
                            }).setTitle("提示").setBackgroundResource(R.drawable.img_base_icon_question).setNegativeButton("重新选择场次").setPositiveButton("修改当前时间").show();
                        } else {
                            showProgressDialog(SelectKcCcActivity.this, "正在提取所选场次数据...", false);
                            DbServices.getInstance(getBaseContext()).deleteAllrzks();
                            if (selectCcAdapter.getChosenCcList().size() > 0) {
                                MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Ks_ccDao.TABLENAME + " SET  cc_extract = 0");
                                MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Ks_ccDao.TABLENAME + " SET  cc_extract = 1 WHERE cc_no = " + cc.getCc_no());
                                for (int i = 0; i < DbServices.getInstance(getBaseContext()).selectKC().size(); i++) {
                                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Rz_ks_zwDao.TABLENAME + " (ks_ksno,ks_xm,ks_xb,ks_zjno,ks_zwh,ks_kcno,ks_kcmc,ks_xp,zw_bs,zw_feature) " + " select a.ksno,a.xm,a.xb,a.zjno,a.zw,a.kcno,a.kcmc,b.xp_pic,c.zw_position,c.zw_feature from " + Bk_ks_tempDao.TABLENAME + " as a," + Bk_ksxpDao.TABLENAME + " as b , " + Kstz_zwDao.TABLENAME + " as c where  (a.kcno = '" + DbServices.getInstance(getBaseContext()).selectKC().get(i).getKc_no() + "' AND a.ccno = '" + cc.getCc_no() + "'AND a.zjno=b.zjno  AND a.zjno=c.zjno) ");
                                    if (DbServices.getInstance(getBaseContext()).queryBKKSList(DbServices.getInstance(getBaseContext()).selectKC().get(i).getKc_name(), cc.getCc_name()).size() == 0) {
                                        MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Bk_ksDao.TABLENAME + " (ks_ksno,ks_xm,ks_xb,ks_zjno,ks_zwh,ks_ccno,ks_ccmc,ks_kcno,ks_kcmc,ks_xp,isRZ,rzTime) " + " select a.ksno,a.xm,a.xb,a.zjno,a.zw,a.ccno,a.ccmc,a.kcno,a.kcmc,b.xp_pic,'0','' from " + Bk_ks_tempDao.TABLENAME + " as a," + Bk_ksxpDao.TABLENAME + " as b where  (a.kcno = '" + DbServices.getInstance(getBaseContext()).selectKC().get(i).getKc_no() + "' AND a.ccno = '" + cc.getCc_no() + "'AND a.zjno=b.zjno)");
                                    }
                                }
                                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                    @Override
                                    public Object callback() {
                                        if (DbServices.getInstance(getBaseContext()).loadAllrzkszw().size() > 0 && DbServices.getInstance(getBaseContext()).queryBKKSList(DbServices.getInstance(getBaseContext()).selectKC(), cc.getCc_name()).size() > 0) {
                                            return Boolean.valueOf(true);
                                        } else {
                                            return Boolean.valueOf(false);
                                        }
                                    }
                                }, new ABLSynCallback.ForegroundCall() {
                                    @Override
                                    public void callback(Object obj) {
                                        if (((Boolean) obj).booleanValue()) {
                                            showProgressDialog(SelectKcCcActivity.this, "正在提取所选场次数据完成", false);
                                            dismissProgressDialog();
                                            Intent getIntent = getIntent();
                                            String sfrz = getIntent.getStringExtra("sfrz");
                                            if (sfrz.equals("3")) {
                                                startActivity(new Intent(SelectKcCcActivity.this, KWDJActivity.class));
                                                finish();
                                            } else if (sfrz.equals("1")) {
                                                new HintDialog(SelectKcCcActivity.this, R.style.dialog, "提取指纹完成，共有" + DbServices.getInstance(getBaseContext()).queryBKKSList(DbServices.getInstance(getBaseContext()).selectKC(), cc.getCc_name()).size() + "个考生，有" + DbServices.getInstance(getBaseContext()).loadAllrzkszw().size() + "个指纹", new HintDialog.OnCloseListener() {
                                                    @Override
                                                    public void onClick(Dialog dialog, boolean confirm) {
                                                        if (confirm) {
                                                            dialog.dismiss();
                                                            startActivity(new Intent(SelectKcCcActivity.this, RZActivity.class));
                                                            finish();
                                                        }
                                                    }
                                                }).setBackgroundResource(R.drawable.img_base_check).setNOVisibility(false).setLLButtonVisibility(true).setTitle("提示").setPositiveButton("知道了").show();
                                            } else if (sfrz.equals("2")) {
                                                startActivity(new Intent(SelectKcCcActivity.this, RZDJJLActivity.class));
                                                finish();
                                            }
                                        } else {
                                            ShowHintDialog(SelectKcCcActivity.this, "提取考生指纹失败，请重新选择场次", "提示", R.drawable.img_base_icon_error, "知道了", false);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    private void resetKcText() {
        if (this.selectKcMcAdapter.isSelectAll()) {
            this.mIvSelectall.setBackgroundResource(R.drawable.img_module_tab_arrange_levelsubject_gkck_checked);
        } else {
            this.mIvSelectall.setBackgroundResource(R.drawable.img_module_tab_arrange_levelsubject_gkck_unchecked);
        }
        if (this.selectKcMcAdapter == null) {
            return;
        }
        if (this.selectKcMcAdapter.getChosenKcList().size() > 0) {
            this.mTvSelectkc.setText("【考场】" + this.selectKcMcAdapter.getChosenKcList().size() + "个");
            this.mTvSelectkc.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.GONE);
            return;
        }
        if (this.ksKcList != null && this.ksKcList.size() > 0) {
            this.mTvSelectkc.setText("【考场】" + this.ksKcList.size() + "个");
            this.mTvSelectkc.setVisibility(View.VISIBLE);
            this.mTvEmpty.setVisibility(View.GONE);
        }
        this.mTvSelectkc.setText(BuildConfig.VERSION_NAME);
        this.mTvSelectkc.setVisibility(View.GONE);
        this.mTvEmpty.setVisibility(View.VISIBLE);
    }

    private void resetCcText() {
        if (this.selectCcAdapter == null) {
            return;
        }
        if (this.selectCcAdapter.getChosenCcList().size() > 0) {
            this.cc = this.selectCcAdapter.getChosenCcList().get(0);
            this.mTvSelectkc.setText(cc.getCc_name());
            this.mLlButtons.setEnabled(true);
            this.mTvSelectkc.setVisibility(View.VISIBLE);
            this.mTvEmpty.setVisibility(View.GONE);
            return;
        }
        this.cc = null;
        this.mLlButtons.setEnabled(false);
        this.mTvSelectkc.setText(BuildConfig.VERSION_NAME);
        this.mTvSelectkc.setVisibility(View.GONE);
        this.mTvEmpty.setVisibility(View.VISIBLE);
    }
}
