package com.zhongruan.android.fingerprint_demo.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog;
import com.zhongruan.android.fingerprint_demo.dialog.SeekbarDialog;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.LogUtil;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import static com.zhongruan.android.fingerprint_demo.utils.FileUtils.delFolder;

/**
 * 基础设置
 * Created by LHJ on 2018/3/9.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout mLlDataBack;
    private TextView mTitleData;
    private TextView mTvMsChange;
    private LinearLayout mLlMsChange;
    private TextView mTvHyfs;
    private LinearLayout mLlHyfs;
    private TextView mTvFingerFz;
    private LinearLayout mLlFingerFz;
    private TextView mTvFingerCfcs;
    private AlertDialog.Builder builder;
    private LinearLayout mLlFingerCfcs;
    private TextView mTvFingerBdfw;
    private LinearLayout mLlFingerBdfw;
    private LinearLayout mLlSjql;
    private TextView mTvStorage;
    private TextView mTvStorageTip;
    private TextView mTvStorageTip2;
    private long size;
    private long total;
    private TextView mTvBgd;
    private LinearLayout mLlBgd;
    private TextView mTvSxt;
    private LinearLayout mLlSxt;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initViews() {
        mLlDataBack = findViewById(R.id.ll_data_back);
        mLlDataBack = findViewById(R.id.ll_data_back);
        mTitleData = findViewById(R.id.titleData);
        mTvMsChange = findViewById(R.id.tv_ms_change);
        mLlMsChange = findViewById(R.id.ll_ms_change);
        mTvHyfs = findViewById(R.id.tv_hyfs);
        mLlHyfs = findViewById(R.id.ll_hyfs);
        mTvFingerFz = findViewById(R.id.tv_finger_fz);
        mLlFingerFz = findViewById(R.id.ll_finger_fz);
        mTvFingerCfcs = findViewById(R.id.tv_finger_cfcs);
        mLlFingerCfcs = findViewById(R.id.ll_finger_cfcs);
        mTvFingerBdfw = findViewById(R.id.tv_finger_bdfw);
        mLlFingerBdfw = findViewById(R.id.ll_finger_bdfw);
        mLlSjql = findViewById(R.id.ll_sjql);
        mTvStorage = findViewById(R.id.tv_storage);
        mTvStorageTip = findViewById(R.id.tv_storage_tip);
        mTvStorageTip2 = findViewById(R.id.tv_storage_tip2);
        mTvBgd = findViewById(R.id.tv_bgd);
        mLlBgd = findViewById(R.id.ll_bgd);
        mTvSxt = findViewById(R.id.tv_sxt);
        mLlSxt = findViewById(R.id.ll_sxt);
    }

    @Override
    public void initListeners() {
        mLlSjql.setOnClickListener(this);
        mLlDataBack.setOnClickListener(this);
        mLlFingerFz.setOnClickListener(this);
        mLlMsChange.setOnClickListener(this);
        mLlFingerCfcs.setOnClickListener(this);
        mLlHyfs.setOnClickListener(this);
        mLlFingerBdfw.setOnClickListener(this);
        mLlBgd.setOnClickListener(this);
        mLlSxt.setOnClickListener(this);
    }

    @Override
    public void initData() {
        updateMemory();
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ms()) == 0) {
            mTvMsChange.setText("采集模式");
        } else {
            mTvMsChange.setText("认证模式");
        }
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 0) {
            mTvHyfs.setText("指纹+拍照");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 1) {
            mTvHyfs.setText("指纹+人脸比对");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 2) {
            mTvHyfs.setText("身份证+指纹+拍照");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 3) {
            mTvHyfs.setText("身份证+指纹+人脸比对");
        }
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_fz()) == 0) {
            mTvFingerFz.setText("低");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_fz()) == 1) {
            mTvFingerFz.setText("中");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_fz()) == 2) {
            mTvFingerFz.setText("高");
        }
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_cfcs()) == 0) {
            mTvFingerCfcs.setText("3次");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_cfcs()) == 1) {
            mTvFingerCfcs.setText("6次");
        }
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_bdfw()) == 0) {
            mTvFingerBdfw.setText("1 : 1");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_bdfw()) == 1) {
            mTvFingerBdfw.setText("1 : N");
        }
        mTvBgd.setText(ConfigApplication.getApplication().getCameraExposure());
        mTvSxt.setText(ConfigApplication.getApplication().getCameraDirectionStr());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_data_back:
                finish();
                break;
            case R.id.ll_ms_change:
                final String[] msArry = new String[]{"采集模式", "认证模式"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(msArry, Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ms()), new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvMsChange.setText(msArry[which]);
                        DbServices.getInstance(getBaseContext()).saveSbMs(which + "");
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.ll_hyfs:
                final String[] hyArry = new String[]{"指纹+拍照", "指纹+人脸比对", "身份证+指纹+拍照", "身份证+指纹+人脸比对"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(hyArry, Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()), new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvHyfs.setText(hyArry[which]);
                        DbServices.getInstance(getBaseContext()).saveSbHyfs(which + "");
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.ll_finger_fz:
                final String[] fzArry = new String[]{"低", "中", "高"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(fzArry, Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_fz()), new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvFingerFz.setText(fzArry[which]);
                        DbServices.getInstance(getBaseContext()).saveSbZwfz(which + "");
                        MyApplication.getYltFingerEngine().setSecurityLevel(which + 1);
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.ll_finger_cfcs:
                final String[] cfcsArry = new String[]{"3", "6"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(cfcsArry, Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_cfcs()), new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvFingerCfcs.setText(cfcsArry[which] + "次");
                        DbServices.getInstance(getBaseContext()).saveSbZwcfcs(which + "");
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.ll_finger_bdfw:
                final String[] bdfwArry = new String[]{"1 : 1", "1 : N"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(bdfwArry, Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_finger_bdfw()), new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvFingerBdfw.setText(bdfwArry[which]);
                        DbServices.getInstance(getBaseContext()).saveSbZwbdfw(which + "");
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
            case R.id.ll_sjql:
                new HintDialog(this, R.style.dialog, "是否清空以前数据？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            showProgressDialog(SettingActivity.this, "正在清空数据...", false);
                            ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                @Override
                                public Object callback() {
                                    if (deleteRecord()) {
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                            }, new ABLSynCallback.ForegroundCall() {
                                @Override
                                public void callback(Object obj) {
                                    if (((Boolean) obj).booleanValue()) {
                                        updateMemory();
                                        dismissProgressDialog();
                                        ShowHintDialog(SettingActivity.this, "清空数据完成", "提示", R.drawable.img_base_icon_correct, "知道了", false);
                                    }
                                }
                            });
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setTitle("U盘导入数据").setPositiveButton("是").setNegativeButton("否").show();
                break;
            case R.id.ll_bgd:
                new SeekbarDialog(this, R.style.dialog, new SeekbarDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, String str, boolean confirm) {
                        if (confirm) {
                            ConfigApplication.getApplication().setCameraExposure(str.trim());
                            mTvBgd.setText(str.trim());
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).show();
                break;
            case R.id.ll_sxt:
                final String[] sxtArry = new String[]{"正", "反"};
                builder = new AlertDialog.Builder(this);// 自定义对话框
                builder.setSingleChoiceItems(sxtArry, ConfigApplication.getApplication().getCameraDirectionStr().equals("正") ? 0 : 1, new DialogInterface.OnClickListener() {// 2默认的选中
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvSxt.setText(sxtArry[which]);
                        dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
                    }
                });
                builder.show();// 让弹出框显示
                break;
        }
    }

    /**
     * 删除历史数据记录
     */
    private boolean deleteRecord() {
        if (delFolder("DataTemp") && delFolder("bk_ksxp") && delFolder("sfrz_rzjl")) {
            DbServices.getInstance(SettingActivity.this).deleteAlltemp();
            DbServices.getInstance(SettingActivity.this).deleteAllxp();
            DbServices.getInstance(SettingActivity.this).deleteAllrzks();
            DbServices.getInstance(SettingActivity.this).deleteAllzw();
            DbServices.getInstance(SettingActivity.this).deleteAllcc();
            DbServices.getInstance(SettingActivity.this).deleteAllkc();
            DbServices.getInstance(SettingActivity.this).deleteAllkd();
            DbServices.getInstance(SettingActivity.this).deleteAllkm();
            DbServices.getInstance(SettingActivity.this).deleteAllbkks();
            DbServices.getInstance(SettingActivity.this).deleteAllrzjl();
            DbServices.getInstance(SettingActivity.this).deleteAllrzjg();
            Glide.get(this).clearDiskCache();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新剩余空间
     */
    private void updateMemory() {
        if (Utils.externalMemoryAvailable()) {
            size = Utils.getAvailableExternalMemorySize();
            total = Utils.getTotalExternalMemorySize();
            LogUtil.i("执行1:" + size + "   " + total);
        } else {
            LogUtil.i("执行2:" + size + "   " + total);
            size = Utils.getAvailableInternalMemorySize();
            total = Utils.getTotalInternalMemorySize();
        }
        mTvStorage.setText(Utils.formatFileSize(size) + " / " + Utils.formatFileSize(total));
        if (size < 104857600) {
            mTvStorageTip.setVisibility(View.VISIBLE);
        } else {
            mTvStorageTip.setVisibility(View.GONE);
        }
    }
}
