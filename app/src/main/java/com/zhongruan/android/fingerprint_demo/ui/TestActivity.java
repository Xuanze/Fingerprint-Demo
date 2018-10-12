package com.zhongruan.android.fingerprint_demo.ui;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.config.ABLConfig;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.Ks_kcDao;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_cc;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;
import com.zhongruan.android.fingerprint_demo.db.entity.Sb_setting;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjg;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjl;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog2;
import com.zhongruan.android.fingerprint_demo.dialog.IPDialog;
import com.zhongruan.android.fingerprint_demo.dialog.JiuGonggeDialog;
import com.zhongruan.android.fingerprint_demo.service.MyService;
import com.zhongruan.android.fingerprint_demo.socket.SocketClient;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.DateUtil;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;
import com.zhongruan.android.fingerprint_demo.utils.LogUtil;
import com.zhongruan.android.fingerprint_demo.utils.MyTimeTask;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import rx.android.BuildConfig;

/**
 * 主页
 * Created by Administrator on 2017/8/1.
 */
public class TestActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llNowtime, llLocalip, llNetip, linearlayoutSfcj, linearlayoutCjjl, linearlayoutSfrz, linearlayoutKwdj, linearlayoutRzjl, linearlayoutSjgl, ll_test_xqkc, ll_test_jcsz, ll_test_cqqd;
    private TextView nowtimeTv, nowdayTv, localipTv, tvConnectState, netipTv, upload_app_tv, version_app_tv, mTvKd, mTvKc, mTvCc, mTvTs;
    private ImageView imgConnectState;
    private SocketClient client;
    private Map<String, Object> map;
    private List<Ks_cc> cc;
    private List<Ks_kc> kc;
    private boolean receive;
    private MyTimeTask timeTask;
    private Intent intent;
    private MyReceiver myReceiver;
    private boolean isRegister = false;
    private final int socketStr = 11111;
    private final int timeStr = 22222;
    private final int ipStr = 33333;
    private List<Ks_kc> ksKcList;
    private String kcmc;
    private final String TAG = "TestActivity";
    private Handler checkMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case socketStr:
                    startCheckMeesageFromKD();
                    break;
                case timeStr:
                    tvConnectState.setText(ConfigApplication.getApplication().getKDConnectState() ? "已连接校端" : "未连接校端");
                    imgConnectState.setBackgroundResource(ConfigApplication.getApplication().getKDConnectState() ? R.drawable.img_module_tab_footer_base_icon_connect : R.drawable.img_module_tab_footer_base_icon_disconnect);
                    break;
                case ipStr:
                    nowtimeTv.setText(DateUtil.getNowTimeNoDate());
                    nowdayTv.setText(DateUtil.getDateByFormat("yyyy年MM月dd日"));
                    localipTv.setText(ConfigApplication.getApplication().getDeviceIP());
                    break;
            }
        }
    };

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void initViews() {
        llNowtime = findViewById(R.id.ll_nowtime);
        nowtimeTv = findViewById(R.id.nowtime_tv);
        nowdayTv = findViewById(R.id.nowday_tv);
        llLocalip = findViewById(R.id.ll_localip);
        localipTv = findViewById(R.id.localip_tv);
        llNetip = findViewById(R.id.ll_netip);
        imgConnectState = findViewById(R.id.img_connect_state);
        tvConnectState = findViewById(R.id.tv_connect_state);
        netipTv = findViewById(R.id.netip_tv);
        upload_app_tv = findViewById(R.id.upload_app_tv);
        linearlayoutSfcj = findViewById(R.id.linearlayout_sfcj);
        linearlayoutCjjl = findViewById(R.id.linearlayout_cjjl);
        linearlayoutSfrz = findViewById(R.id.linearlayout_sfrz);
        linearlayoutKwdj = findViewById(R.id.linearlayout_kwdj);
        linearlayoutRzjl = findViewById(R.id.linearlayout_rzjl);
        linearlayoutSjgl = findViewById(R.id.linearlayout_sjgl);
        ll_test_xqkc = findViewById(R.id.ll_test_xqkc);
        ll_test_jcsz = findViewById(R.id.ll_test_jcsz);
        ll_test_cqqd = findViewById(R.id.ll_test_cqqd);
        version_app_tv = findViewById(R.id.version_app_tv);
        llNowtime = findViewById(R.id.ll_nowtime);
        mTvKd = findViewById(R.id.tv_kd);
        mTvKc = findViewById(R.id.tv_kc);
        mTvCc = findViewById(R.id.tv_cc);
        mTvTs = findViewById(R.id.tv_ts);
        intent = new Intent(this, MyService.class);
    }

    @Override
    public void initListeners() {
        llNowtime.setOnClickListener(this);
        llLocalip.setOnClickListener(this);
        llNetip.setOnClickListener(this);
        linearlayoutSfcj.setOnClickListener(this);
        linearlayoutCjjl.setOnClickListener(this);
        linearlayoutSfrz.setOnClickListener(this);
        linearlayoutKwdj.setOnClickListener(this);
        linearlayoutRzjl.setOnClickListener(this);
        linearlayoutSjgl.setOnClickListener(this);
        ll_test_xqkc.setOnClickListener(this);
        ll_test_jcsz.setOnClickListener(this);
        ll_test_cqqd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        netipTv.setText(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
        startCheckMeesageFromKD();
        setTimer();
    }

    private void setTimer() {
        timeTask = new MyTimeTask(1000, new TimerTask() {
            @Override
            public void run() {
                checkMessageHandler.sendEmptyMessage(ipStr);
                //或者发广播，启动服务都是可以的
            }
        });
        timeTask.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_localip:
                startActivity(new Intent("android.settings.AIRPLANE_MODE_SETTINGS"));
                break;
            case R.id.ll_netip:
                new IPDialog(this, R.style.dialog, new IPDialog.OnEditInputFinishedListener() {
                    @Override
                    public void editInputFinished(Dialog dialog, String ipStr, boolean confirm) {
                        if (confirm) {
                            netipTv.setText(ipStr);
                            DbServices.getInstance(getBaseContext()).saveSbip(ipStr);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setTitle("修改服务器地址").setStrip1(StringUtils.substringBefore(netipTv.getText().toString(), ".")).setStrip2(StringUtils.substringBefore(StringUtils.substringAfter(netipTv.getText().toString(), "."), ".")).setStrip3(StringUtils.substringAfterLast(StringUtils.substringBeforeLast(netipTv.getText().toString(), "."), ".")).setStrip4(StringUtils.substringAfterLast(netipTv.getText().toString(), ".")).show();
                break;
            case R.id.linearlayout_sfcj:
                startActivity(new Intent(this, CJActivity.class));
                break;
            case R.id.linearlayout_cjjl:
                startActivity(new Intent(this, CJJLActivity.class));
                break;
            case R.id.linearlayout_sfrz:
                kc = DbServices.getInstance(getBaseContext()).selectKC();
                cc = DbServices.getInstance(getBaseContext()).selectCC();
                LogUtil.i("kc", kc);
                LogUtil.i("cc", cc);
                if (kc.size() == 0) {
                    new HintDialog(this, R.style.dialog, "未发现数据,是否现在导入数据？", new HintDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                new JiuGonggeDialog(TestActivity.this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            startActivity(new Intent(TestActivity.this, DataActivity.class));
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }).setBackgroundResource(R.drawable.img_base_icon_info).setNOVisibility(true).setLLButtonVisibility(true).setTitle("未导入数据").setPositiveButton("确定").show();
                } else if (cc.size() == 0) {
                    Intent intent = new Intent(this, SelectKcCcActivity.class);
                    intent.putExtra("sfrz", "1");
                    startActivity(intent);
                } else {
                    if (!DateUtil.isTime(DateUtil.dateToLong(DateUtil.getNowTime_Millisecond3()), DateUtil.dateToLong(cc.get(0).getCc_kssj()), DateUtil.dateToLong(cc.get(0).getCc_jssj()))) {
                        new HintDialog2(this, R.style.dialog, "当前场次不在当前考试时间", "当前时间：" + DateUtil.getNowTimeChinese(), "所选场次：" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.get(0).getCc_kssj())) + "-" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.get(0).getCc_jssj())), new HintDialog2.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    Intent intent = new Intent(TestActivity.this, SelectKcCcActivity.class);
                                    intent.putExtra("sfrz", "1");
                                    startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    startActivity(new Intent(TestActivity.this, TimeActivity.class));
                                    dialog.dismiss();
                                }
                            }
                        }).setTitle("提示").setBackgroundResource(R.drawable.img_base_icon_question).setNegativeButton("重新选择场次").setPositiveButton("修改当前时间").show();
                    } else {
                        startActivity(new Intent(this, RZActivity.class));
                    }
                }
                break;
            case R.id.linearlayout_kwdj:
                kc = DbServices.getInstance(getBaseContext()).selectKC();
                cc = DbServices.getInstance(getBaseContext()).selectCC();
                if (kc.size() == 0) {
                    new HintDialog(this, R.style.dialog, "未发现数据,是否现在导入数据？", new HintDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                new JiuGonggeDialog(TestActivity.this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            startActivity(new Intent(TestActivity.this, DataActivity.class));
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }).setBackgroundResource(R.drawable.img_base_icon_info).setNOVisibility(true).setLLButtonVisibility(true).setTitle("未导入数据").setPositiveButton("确定").show();
                } else if (cc.size() == 0) {
                    Intent intent = new Intent(this, SelectKcCcActivity.class);
                    intent.putExtra("sfrz", "3");
                    startActivity(intent);
                } else {
                    if (!DateUtil.isTime(DateUtil.dateToLong(DateUtil.getNowTime_Millisecond3()), DateUtil.dateToLong(cc.get(0).getCc_kssj()), DateUtil.dateToLong(cc.get(0).getCc_jssj()))) {
                        new HintDialog2(this, R.style.dialog, "当前场次不在当前考试时间", "当前时间：" + DateUtil.getNowTimeChinese(), "所选场次：" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.get(0).getCc_kssj())) + "-" + DateUtil.getChineseTime(DateUtil.getStringToDate(cc.get(0).getCc_jssj())), new HintDialog2.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                if (confirm) {
                                    Intent intent = new Intent(TestActivity.this, SelectKcCcActivity.class);
                                    intent.putExtra("sfrz", "3");
                                    startActivity(intent);
                                    dialog.dismiss();
                                } else {
                                    startActivity(new Intent(TestActivity.this, TimeActivity.class));
                                    dialog.dismiss();
                                }
                            }
                        }).setTitle("提示").setBackgroundResource(R.drawable.img_base_icon_question).setNegativeButton("重新选择场次").setPositiveButton("修改当前时间").show();
                    } else {
                        startActivity(new Intent(this, KWDJActivity.class));
                    }
                }
                break;
            case R.id.linearlayout_rzjl:
                kc = DbServices.getInstance(getBaseContext()).selectKC();
                cc = DbServices.getInstance(getBaseContext()).selectCC();
                if (kc.size() == 0) {
                    new HintDialog(this, R.style.dialog, "未发现数据,是否现在导入数据？", new HintDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                new JiuGonggeDialog(TestActivity.this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            startActivity(new Intent(TestActivity.this, DataActivity.class));
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }).setBackgroundResource(R.drawable.img_base_icon_info).setNOVisibility(true).setLLButtonVisibility(true).setTitle("未导入数据").setPositiveButton("确定").show();
                } else if (cc.size() == 0) {
                    Intent intent = new Intent(this, SelectKcCcActivity.class);
                    intent.putExtra("sfrz", "2");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(this, RZDJJLActivity.class));
                }
                break;
            case R.id.linearlayout_sjgl:
                new JiuGonggeDialog(this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            startActivity(new Intent(TestActivity.this, DataActivity.class));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
                break;
            case R.id.ll_test_xqkc:
                kc = DbServices.getInstance(getBaseContext()).selectKC();
                if (kc.size() == 0) {
                    new HintDialog(this, R.style.dialog, "未发现数据,是否现在导入数据？", new HintDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                dialog.dismiss();
                                new JiuGonggeDialog(TestActivity.this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                                    @Override
                                    public void onClick(Dialog dialog, boolean confirm) {
                                        if (confirm) {
                                            startActivity(new Intent(TestActivity.this, DataActivity.class));
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).show();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }).setBackgroundResource(R.drawable.img_base_icon_info).setNOVisibility(true).setLLButtonVisibility(true).setTitle("未导入数据").setPositiveButton("确定").show();
                } else {
                    new HintDialog(this, R.style.dialog, "是否需要重新选择考场？", new HintDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                List<Ks_kc> kcList = DbServices.getInstance(getBaseContext()).selectKC();
                                ArrayList<String> list = new ArrayList<>();
                                for (int i = 0; i < kcList.size(); i++) {
                                    list.add(kcList.get(i).getKc_name());
                                }
                                MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Ks_kcDao.TABLENAME + " SET  kc_extract = 0");
                                dialog.dismiss();
                                Intent intent = new Intent(TestActivity.this, SelectKcCcActivity.class);
                                intent.putStringArrayListExtra("kcmc", list);
                                intent.putExtra("sfrz", "1");
                                startActivity(intent);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setTitle("提示").setPositiveButton("是").setNegativeButton("否").show();
                }
                break;
            case R.id.ll_test_jcsz:
                new JiuGonggeDialog(TestActivity.this, R.style.dialog, "123", new JiuGonggeDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            startActivity(new Intent(TestActivity.this, SettingActivity.class));
                        } else {
                            Toast.makeText(TestActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
                break;
            case R.id.ll_nowtime:
                startActivity(new Intent(this, TimeActivity.class));
                break;
            case R.id.ll_test_cqqd:
                new HintDialog(this, R.style.dialog, "是否需要重启驱动模块？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            MyApplication.getYltFingerEngine().freeEngine();
                            MyApplication.getYltIdCardEngine().freeEngine();
                            stopService(intent);
                            dialog.dismiss();
                            showProgressDialog(TestActivity.this, "正在重启驱动模块，请稍后...", false);
                            //启动服务
                            startService(new Intent(TestActivity.this, MyService.class));
                            //注册广播接收器
                            if (!isRegister) {
                                myReceiver = new MyReceiver();
                                IntentFilter filter = new IntentFilter();
                                filter.addAction("MyService");
                                TestActivity.this.registerReceiver(myReceiver, filter);
                                isRegister = true;
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setTitle("提示").setPositiveButton("是").setNegativeButton("否").show();
                break;
        }
    }

    private void downApp() {
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            @Override
            public Object callback() {
                new Runnable() {
                    @Override
                    public void run() {
                        SocketClient socketClient = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                        receive = socketClient.receiveUnLockField("", ABLConfig.DATAVERSION_DOWNLOAD, FileUtils.getSDCardPath() + "/Apk_App/", "1", checkMessageHandler);
                    }
                }.run();
                return receive;
            }
        }, new ABLSynCallback.ForegroundCall() {
            @Override
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    Toast.makeText(TestActivity.this, "正在下载中。。。", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TestActivity.this, "下载失败。。。", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void ChangeText(List<Ks_kc> kcStr, List<Ks_cc> ccStr) {
        if (ccStr.size() > 0) {
            int rzjg1 = DbServices.getInstance(getBaseContext()).selectWSBrzjgList(kcStr, ccStr.get(0).getCc_name(), "1").size();
            int rzjg2 = DbServices.getInstance(TestActivity.this).selectKCCCrzjgList(kcStr, ccStr.get(0).getCc_name()).size();
            if (rzjg2 > 0) {
                upload_app_tv.setText("上传 " + ((int) (100.0d * ((((double) rzjg1) * 1.0d) / (((double) rzjg2) * 1.0d)))) + "%（" + rzjg1 + " / " + rzjg2 + "）");
                EventBus.getDefault().post(666);
            } else {
                upload_app_tv.setText("暂无数据上传");
            }
        } else {
            upload_app_tv.setText("暂无数据上传");
        }
    }

    private void startCheckMeesageFromKD() {
        if (MyApplication.getApplication().isShouldStopUploadingData()) {
            checkAgain();
            return;
        }
        if (DbServices.getInstance(getBaseContext()).selectKC().size() > 0 && ConfigApplication.getApplication().getKDConnectState()) {
            kc = DbServices.getInstance(getBaseContext()).selectKC();
            cc = DbServices.getInstance(getBaseContext()).selectCC();
            ChangeText(kc, cc);
            if (DbServices.getInstance(TestActivity.this).loadAllrzjg().size() > 0 || DbServices.getInstance(TestActivity.this).loadAllrzjl().size() > 0) {
                List<Sfrz_rzjg> rzjgList = DbServices.getInstance(getBaseContext()).selectWSBrzjg("0");
                List<Sfrz_rzjl> rzjlList = DbServices.getInstance(getBaseContext()).selectWSBrzjl("0");
                if (rzjgList.size() > 0) {
                    for (int i = 0; i < rzjgList.size(); i++) {
                        uploadRzjg(rzjgList.get(i));
                    }
                }
                if (rzjlList.size() > 0) {
                    for (int i = 0; i < rzjlList.size(); i++) {
                        uploadRzjl(rzjlList.get(i));
                    }
                }
            }
        } else {
            upload_app_tv.setText("暂无数据上传");
        }
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                String kcno = BuildConfig.VERSION_NAME;
                List<Ks_kc> kcs = DbServices.getInstance(getBaseContext()).selectKC();
                if (kcs != null) {
                    for (int i = 0; i < kcs.size(); i++) {
                        String no = kcs.get(i).getKc_no();
                        if (Utils.stringIsEmpty(kcno)) {
                            kcno = no;
                        } else {
                            kcno = kcno + "&" + no;
                        }
                    }
                }
                LogUtil.i(TAG, kcno + " | 开始获取考点端消息");
                client = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                Map<String, Object> messages = client.receiveUnLockMessage(BuildConfig.VERSION_NAME, kcno);
                LogUtil.i(TAG, kcno + " | 获取到考点端消息：" + messages);
                client.closeSocket();
                return messages;
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (obj != null) {
                    ConfigApplication.getApplication().setKDConnectState(true);
                    checkMessageHandler.sendEmptyMessage(timeStr);
                    Map<String, Object> messages = (Map) obj;
                    String message = BuildConfig.VERSION_NAME;
                    if (messages.get("mess") != null) {
                        message = (String) messages.get("mess");
                    }
                    if (!Utils.stringIsEmpty(message) && message.length() > 1) {
                        Intent intent = new Intent();
                        intent.setAction("mess");
                        intent.putExtra("mess", message);
                        sendBroadcast(intent);
                    }
                    LogUtil.i("messages", messages + " |");
                    if (messages.get("time") != null) {
                        String time = (String) messages.get("time");
                        LogUtil.i("mess", time);
                        timeSynchronization(time);
                    }
                } else {
                    ConfigApplication.getApplication().setKDConnectState(false);
                    checkMessageHandler.sendEmptyMessage(timeStr);
                }
                checkAgain();
            }
        });
    }

    private void checkAgain() {
        this.checkMessageHandler.removeMessages(socketStr);
        Message msg = new Message();
        msg.what = socketStr;
        this.checkMessageHandler.sendMessageDelayed(msg, 10000);
    }

    private void uploadRzjg(final Sfrz_rzjg rzjg) {
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                new Runnable() {
                    @Override
                    public void run() {
                        SocketClient socketClient = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                        map = socketClient.sendString(ABLConfig.RZJG, rzjg.toString());
                        LogUtil.i("数据上报：uploadRzjg", map);
                    }
                }.run();
                return map.get("success");
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    DbServices.getInstance(TestActivity.this).saveRZJG(rzjg.getRzjg_time());
                    ChangeText(kc, cc);
                } else {
                    if (ConfigApplication.getApplication().getKDConnectState()) {
                        return;
                    } else {
                        uploadRzjg(rzjg);
                    }
                }
            }
        });
    }

    private void uploadRzjl(final Sfrz_rzjl rzjl) {
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                new Runnable() {
                    @Override
                    public void run() {
                        SocketClient socketClient = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                        if (rzjl.getRzjl_pith() != "") {
                            map = socketClient.sendFile(BuildConfig.VERSION_NAME, ABLConfig.RZJL, FileUtils.getAppSavePath() + "/" + rzjl.getRzjl_pith(), rzjl.toString());
                        } else {
                            map = socketClient.sendFile(BuildConfig.VERSION_NAME, ABLConfig.RZJL, "", rzjl.toString());
                        }
                        LogUtil.i("数据上报：uploadRzjl", map);
                    }
                }.run();
                return map.get("success");
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    DbServices.getInstance(TestActivity.this).saveRZJL(rzjl.getRzjl_time());
                } else {
                    if (ConfigApplication.getApplication().getKDConnectState()) {
                        return;
                    } else {
                        uploadRzjl(rzjl);
                    }
                }
            }
        });
    }

    public boolean timeSynchronization(String time) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setTime(c.getTimeInMillis());
            return true;
        } catch (Exception e) {
            LogUtil.i("出错了");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int a = bundle.getInt("a");
            int b = bundle.getInt("b");
            LogUtil.i("123", "a:   " + a + "   b:   " + b);
            if (a == 1 && b == 1) {
                dismissProgressDialog();
                ShowHintDialog(context, "重启驱动模块完成", "提示", R.drawable.img_base_icon_correct, "知道了", false);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getApplication().setShouldStopUploadingData(false);
        List<Sb_setting> settingList = DbServices.getInstance(getBaseContext()).loadAllSbSetting();
        if (DbServices.getInstance(getBaseContext()).loadAllkd().size() != 0) {
            mTvKd.setVisibility(View.VISIBLE);
            mTvKd.setSelected(true);
            mTvTs.setVisibility(View.GONE);
            mTvKd.setText(DbServices.getInstance(getBaseContext()).loadAllkd().get(0).getKd_name());
        } else {
            mTvKd.setVisibility(View.GONE);
            mTvTs.setVisibility(View.VISIBLE);
        }
        ksKcList = DbServices.getInstance(getBaseContext()).selectKC();
        if (ksKcList.size() != 0) {
            mTvKc.setSelected(true);
            mTvKc.setVisibility(View.VISIBLE);
            for (int i = 0; i < ksKcList.size(); i++) {
                if (i == 0) {
                    kcmc = ksKcList.get(i).getKc_name();
                } else {
                    kcmc = kcmc + " " + ksKcList.get(i).getKc_name();
                }
            }
            mTvKc.setText(kcmc);
        } else {
            mTvKc.setVisibility(View.GONE);
        }
        if (DbServices.getInstance(getBaseContext()).selectCC().size() != 0) {
            mTvCc.setVisibility(View.VISIBLE);
            mTvCc.setText(DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_name());
        } else {
            mTvCc.setVisibility(View.GONE);
        }
        if (settingList.get(0).getSb_ms().equals("0")) {
            linearlayoutSfcj.setVisibility(View.VISIBLE);
            linearlayoutCjjl.setVisibility(View.VISIBLE);
            ll_test_xqkc.setVisibility(View.GONE);
            linearlayoutSfrz.setVisibility(View.GONE);
            linearlayoutKwdj.setVisibility(View.GONE);
            linearlayoutRzjl.setVisibility(View.GONE);
            linearlayoutSjgl.setVisibility(View.GONE);
        } else {
            linearlayoutSfcj.setVisibility(View.GONE);
            linearlayoutCjjl.setVisibility(View.GONE);
            ll_test_xqkc.setVisibility(View.VISIBLE);
            linearlayoutSfrz.setVisibility(View.VISIBLE);
            linearlayoutKwdj.setVisibility(View.VISIBLE);
            linearlayoutRzjl.setVisibility(View.VISIBLE);
            linearlayoutSjgl.setVisibility(View.VISIBLE);
        }
        startCheckMeesageFromKD();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTask.stop();
        checkMessageHandler.removeMessages(socketStr);
        ConfigApplication.getApplication().setKDConnectState(false);
        MyApplication.getYltFingerEngine().freeEngine();
        MyApplication.getYltIdCardEngine().freeEngine();
        stopService(intent);
    }
}
