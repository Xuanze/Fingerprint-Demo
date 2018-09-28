package com.zhongruan.android.fingerprint_demo.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.adapter.SelectKsAdapter;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.camera.CameraInterface;
import com.zhongruan.android.fingerprint_demo.camera.CameraSurfaceView;
import com.zhongruan.android.fingerprint_demo.camera.util.DisplayUtil;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;
import com.zhongruan.android.fingerprint_demo.db.entity.Rz_ks_zw;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjg;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjl;
import com.zhongruan.android.fingerprint_demo.dialog.FaceDialog;
import com.zhongruan.android.fingerprint_demo.dialog.SfzhEditDialog;
import com.zhongruan.android.fingerprint_demo.fingerprintengine.FingerData;
import com.zhongruan.android.fingerprint_demo.idcardengine.IDCardData;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.Base64Util;
import com.zhongruan.android.fingerprint_demo.utils.DateUtil;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;
import com.zhongruan.android.fingerprint_demo.utils.IDCard;
import com.zhongruan.android.fingerprint_demo.utils.LogUtil;
import com.zhongruan.android.fingerprint_demo.utils.MyTimeTask;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.TimerTask;

import cn.com.aratek.dev.Terminal;

/**
 * 身份认证
 * Created by Administrator on 2017/9/8.
 */
public class RZActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llSwitch, llPhoto, mLlBack, mLlChangeCc, ll_kwdj, layout_view_finger, include_idcard, ll_inputIdCard, layout_view_face, layout_view_rz_face, state_camera, layout_view_kslist;
    private TextView mTvCountUnverified, mTvTitle, mTvCountVerified, mTvCountTotal, mTvTime, mTvDate, mTvTip, mTvKsName, mTvKsSeat, mKsResult, mTvKsSfzh, mTvKsKc, mTvKsno;
    private RelativeLayout rl_camera;
    private ImageView mIvKs;
    private FingerData fingerData;
    private Bitmap zwBitmap;
    private CameraSurfaceView surfaceView = null;
    private List<Rz_ks_zw> rz_ks_zw;
    private boolean isRzSucceed;
    private GridView gvKs;
    private SelectKsAdapter selectKsAdapter;
    private List<Bk_ks> bk_ks;
    private String zwid, kmno, kmmc, kcmc, kdno, ccmc, ccno, kcmcs = "";
    private List<Ks_kc> ksKcList;
    private int CS = 0;
    private Sfrz_rzjg sfrz_rzjg;
    private Sfrz_rzjl sfrz_rzjl;
    private String ZpFileName;
    private IDCardData idCardData;
    private Bk_ks bkKs;
    private int ksid;
    private RelativeLayout mRlBcpz;
    private MyTimeTask timeTask;
    private final int timeStr = 1111;
    private final int viewStr = 2222;
    private final int visibilityStr = 3333;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case timeStr:
                    mTvTime.setText(DateUtil.getNowTimeNoDate());
                    mTvDate.setText(DateUtil.getDateByFormat("yyyy年MM月dd日"));
                    break;
                case viewStr:
                    initViewParams();
                    break;
                case visibilityStr:
                    state_camera.setVisibility(View.GONE);
                    rl_camera.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_rz);
    }

    @Override
    public void initViews() {
        mLlBack = findViewById(R.id.llBack);
        mLlChangeCc = findViewById(R.id.ll_change_cc);
        ll_kwdj = findViewById(R.id.ll_kwdj);
        mTvTitle = findViewById(R.id.tvTitle);
        mTvTime = findViewById(R.id.tvTime);
        mTvDate = findViewById(R.id.tvDate);
        mTvTip = findViewById(R.id.tvTip);
        mTvCountTotal = findViewById(R.id.tvCountTotal);
        mTvCountVerified = findViewById(R.id.tvCountVerified);
        mTvCountUnverified = findViewById(R.id.tvCountUnverified);
        layout_view_kslist = findViewById(R.id.layout_view_kslist);
        layout_view_finger = findViewById(R.id.layout_view_finger);
        layout_view_face = findViewById(R.id.layout_view_face);
        layout_view_rz_face = findViewById(R.id.layout_view_rz_face);
        include_idcard = findViewById(R.id.include_idcard);
        ll_inputIdCard = findViewById(R.id.ll_inputIdCard);
        mIvKs = findViewById(R.id.ivKs);
        mTvKsName = findViewById(R.id.tvKsName);
        mTvKsSeat = findViewById(R.id.tvKsSeat);
        mKsResult = findViewById(R.id.ks_result);
        mTvKsSfzh = findViewById(R.id.tvKsSfzh);
        mTvKsKc = findViewById(R.id.tvKsKc);
        mTvKsno = findViewById(R.id.tvKsno);
        surfaceView = findViewById(R.id.sf_face);
        llPhoto = findViewById(R.id.llPhoto);
        llSwitch = findViewById(R.id.llSwitch);
        state_camera = findViewById(R.id.state_camera);
        rl_camera = findViewById(R.id.rl_camera);
        gvKs = findViewById(R.id.gvKs);
        mRlBcpz = findViewById(R.id.rl_bcpz);
        MyApplication.getApplication().setShouldStopUploadingData(false);
        ccno = DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_no();
        ccmc = DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_name();
        ksKcList = DbServices.getInstance(getBaseContext()).selectKC();
        kmmc = DbServices.getInstance(getBaseContext()).selectCC().get(0).getKm_name();
        kmno = DbServices.getInstance(getBaseContext()).selectCC().get(0).getKm_no();
        kdno = DbServices.getInstance(getBaseContext()).loadAllkd().get(0).getKd_no();
        for (int i = 0; i < ksKcList.size(); i++) {
            if (i == 0) {
                kcmc = ksKcList.get(i).getKc_name();
            } else {
                kcmc = kcmc + " " + ksKcList.get(i).getKc_name();
            }
        }
        mTvTitle.setText(ccmc + " " + kcmc + " " + kmmc);
        mTvCountTotal.setText(DbServices.getInstance(getBaseContext()).queryBKKSList(ksKcList, ccmc).size() + "");
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 0) {
            showProgressDialog(RZActivity.this, "正在加载数据...", false);
            handler.postDelayed(runnable, 1000);
            gvKs.setVisibility(View.GONE);
            bk_ks = DbServices.getInstance(getBaseContext()).queryBKKSList(ksKcList, ccmc);
            selectKsAdapter = new SelectKsAdapter(this, bk_ks);
            gvKs.setAdapter(selectKsAdapter);
            gvKs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ksid = i;
                    layout_view_kslist.setVisibility(View.GONE);
                    layout_view_face.setVisibility(View.VISIBLE);
                    layout_view_finger.setVisibility(View.VISIBLE);
                    rz_ks_zw = DbServices.getInstance(getBaseContext()).selectBkKs(bk_ks.get(i).getKs_zjno());
                    KsZW();
                }
            });
        }
        xzMS();
    }

    @Override
    public void initListeners() {
        mLlBack.setOnClickListener(this);
        mLlChangeCc.setOnClickListener(this);
        llPhoto.setOnClickListener(this);
        llSwitch.setOnClickListener(this);
        ll_kwdj.setOnClickListener(this);
        ll_inputIdCard.setOnClickListener(this);
        mRlBcpz.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvTitle.setSelected(true);
        setTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                if (isRzSucceed) {
                    xzMS();
                } else {
                    finish();
                }
                break;
            case R.id.ll_change_cc:
                Intent intent = new Intent(this, SelectKcCcActivity.class);
                intent.putExtra("sfrz", "1");
                startActivity(intent);
                finish();
                break;
            case R.id.llPhoto:
                doTakePicture();
                break;
            case R.id.llSwitch:
                CameraInterface.getInstance().cameraSwitch(surfaceView);
                break;
            case R.id.ll_kwdj:
                startActivity(new Intent(this, KWDJActivity.class));
                finish();
                break;
            case R.id.ll_inputIdCard:
                idCardData = null;
                handler.removeCallbacks(runnable04); //停止刷新
                new SfzhEditDialog(RZActivity.this, R.style.dialog, new SfzhEditDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm, String Str) {
                        if (confirm) {
                            dialog.dismiss();
                            IDCard idCard = new IDCard();
                            if (idCard.validate_effective(Str.toLowerCase()).equals(Str.toLowerCase())) {
                                bkKs = DbServices.getInstance(getBaseContext()).selectBKKSs(ksKcList, ccno, Str);
                                if (bkKs != null) {
                                    layout_view_kslist.setVisibility(View.GONE);
                                    layout_view_rz_face.setVisibility(View.GONE);
                                    layout_view_face.setVisibility(View.VISIBLE);
                                    layout_view_finger.setVisibility(View.VISIBLE);
                                    include_idcard.setVisibility(View.GONE);
                                    rz_ks_zw = DbServices.getInstance(getBaseContext()).selectBkKs(bkKs.getKs_zjno());
                                    fingerData = null;
                                    KsZW();
                                } else {
                                    ShowToast("未查找到" + Str + "，请重试");
                                    handler.postDelayed(runnable04, 100);// 间隔1秒
                                }
                            } else {
                                ShowToast("输入身份证号有误！");
                            }
                        } else {
                            dialog.dismiss();
                            handler.postDelayed(runnable04, 100);
                        }
                    }
                }).show();
                break;
            case R.id.rl_bcpz:
                CS = 0;
                KsPZ();
                break;
        }
    }

    /**
     * 表
     */
    private void setTimer() {
        timeTask = new MyTimeTask(1000, new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(timeStr);
                //或者发广播，启动服务都是可以的
            }
        });
        timeTask.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            gvKs.setVisibility(View.VISIBLE);
            dismissProgressDialog();
        }
    };
    /**
     * 指纹认证
     */
    private Runnable runnable02 = new Runnable() {
        public void run() {
            fingerData = MyApplication.getYltFingerEngine().fingerCollect();
            if (fingerData != null) {
                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                    @Override
                    public Object callback() {
                        CS++;
                        soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
                        return MyApplication.getYltFingerEngine().fingerSearch(fingerData.getFingerFeatures());
                    }
                }, new ABLSynCallback.ForegroundCall() {
                    @Override
                    public void callback(Object obj) {
                        zwid = (String) obj;
                        if (Utils.stringIsEmpty(zwid) && CS != 3) {
                            zwid = "";
                            fingerData = null;
                            handler.postDelayed(runnable02, 500);// 间隔1秒
                        } else {
                            CS = 0;
                            LogUtil.i(zwid);
                            KsPZ();
                        }
                    }
                });
            } else {
                handler.postDelayed(runnable02, 500);// 间隔1秒
            }
        }
    };
    /**
     * 身份证信息采集
     */
    private Runnable runnable04 = new Runnable() {
        public void run() {
            idCardData = MyApplication.getYltIdCardEngine().startScanIdCard();
            if (idCardData != null) {
                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                    @Override
                    public Object callback() {
                        soundPool.play(musicId.get(1), 1, 1, 0, 0, 1);
                        return DbServices.getInstance(getBaseContext()).selectBKKSs(ksKcList, ccno, idCardData.getSfzh());
                    }
                }, new ABLSynCallback.ForegroundCall() {
                    @Override
                    public void callback(Object obj) {
                        bkKs = (Bk_ks) obj;
                        if (bkKs != null) {
                            layout_view_kslist.setVisibility(View.GONE);
                            layout_view_rz_face.setVisibility(View.GONE);
                            layout_view_face.setVisibility(View.VISIBLE);
                            layout_view_finger.setVisibility(View.VISIBLE);
                            include_idcard.setVisibility(View.GONE);
                            rz_ks_zw = DbServices.getInstance(getBaseContext()).selectBkKs(bkKs.getKs_zjno());
                            fingerData = null;
                            handler.removeCallbacks(runnable04); //停止刷新
                            KsZW();
                        } else {
                            ShowToast("未查找到" + idCardData.getSfzh() + "，请重试");
                            handler.postDelayed(runnable04, 100);// 间隔1秒
                        }
                    }
                });
            } else {
                handler.postDelayed(runnable04, 500);// 间隔1秒
            }
        }
    };

    private void xzMS() {
        zwid = "";
        ZpFileName = null;
        isRzSucceed = false;
        fingerData = null;
        sfrz_rzjg = null;
        sfrz_rzjl = null;
        mLlChangeCc.setEnabled(true);
        ll_kwdj.setEnabled(true);
        layout_view_face.setVisibility(View.GONE);
        layout_view_finger.setVisibility(View.GONE);
        layout_view_rz_face.setVisibility(View.GONE);
        rl_camera.setVisibility(View.GONE);
        handler.removeCallbacks(runnable02);
        state_camera.setVisibility(View.VISIBLE);
        mTvCountVerified.setText(DbServices.getInstance(getBaseContext()).queryBkKsWTG(ksKcList, ccmc, "0") + "");
        mTvCountUnverified.setText(DbServices.getInstance(getBaseContext()).queryBkKsIsTG(ksKcList, ccmc, "0") + "");
        if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 0) {
            layout_view_kslist.setVisibility(View.VISIBLE);
            include_idcard.setVisibility(View.GONE);
            mTvTip.setText("请选择考生");
        } else if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 1) {
            layout_view_kslist.setVisibility(View.GONE);
            include_idcard.setVisibility(View.VISIBLE);
            mTvTip.setText("请刷身份证");
            soundPool.play(musicId.get(3), 1, 1, 0, 0, 1);
            handler.post(runnable04);
        }
    }

    private void KsZW() {
        isRzSucceed = true;
        mLlChangeCc.setEnabled(false);
        ll_kwdj.setEnabled(false);
        Glide.with(this).load(new File(FileUtils.getAppSavePath() + "/" + rz_ks_zw.get(0).getKs_xp())).into(mIvKs);
        mTvKsName.setText(rz_ks_zw.get(0).getKs_xm() + "|" + (rz_ks_zw.get(0).getKs_xb().equals("1") ? "男" : "女"));
        mTvKsSeat.setText(rz_ks_zw.get(0).getKs_zwh());
        mTvKsSfzh.setText(rz_ks_zw.get(0).getKs_zjno());
        mTvKsKc.setText(rz_ks_zw.get(0).getKs_kcmc());
        mTvKsno.setText(rz_ks_zw.get(0).getKs_ksno());
        kcmcs = rz_ks_zw.get(0).getKs_kcmc();
        mKsResult.setText("指纹比对中");
        mKsResult.setTextColor(getResources().getColor(R.color.red));
        MyApplication.getYltFingerEngine().clear();
        for (int i = 0; i < rz_ks_zw.size(); i++) {
            byte[] bytes = Base64.decode(rz_ks_zw.get(i).getZw_feature(), 2);
            MyApplication.getYltFingerEngine().importfinger(bytes, rz_ks_zw.get(i).getKsid() + "");
        }
        mTvTip.setText("请按手指");
        soundPool.play(musicId.get(2), 1, 1, 0, 0, 1);
        handler.post(runnable02);
    }

    private void KsPZ() {
        handler.removeCallbacks(runnable02); //停止刷新
        layout_view_rz_face.setVisibility(View.VISIBLE);
        layout_view_finger.setVisibility(View.GONE);
        if (!Utils.stringIsEmpty(zwid)) {
            mKsResult.setText("指纹比对通过");
            mKsResult.setTextColor(getResources().getColor(R.color.green));
            sfrz_rzjg = saveRzjg("21", rz_ks_zw.get(0).getKs_ksno(), kmno, kdno, rz_ks_zw.get(0).getKs_kcno(), rz_ks_zw.get(0).getKs_zwh(), Terminal.getSN(), "", "0");
            sfrz_rzjl = saveRzjl("8003", rz_ks_zw.get(0).getKs_ksno(), kmno, kdno, rz_ks_zw.get(0).getKs_kcno(), rz_ks_zw.get(0).getKs_zwh(), Terminal.getSN(), "1", DateUtil.getNowTime(), Base64Util.encode(fingerData.getFingerFeatures()), "sfrz_rzjl/kstz_a_zw/" + rz_ks_zw.get(0).getKs_ksno() + "_" + DateUtil.getNowTime_Millisecond4() + ".jpg", sfrz_rzjg.getRzjgid(), "0");
        } else {
            mKsResult.setText("指纹比对不通过");
            mKsResult.setTextColor(getResources().getColor(R.color.collect_yellow));
            sfrz_rzjg = saveRzjg("22", rz_ks_zw.get(0).getKs_ksno(), kmno, kdno, rz_ks_zw.get(0).getKs_kcno(), rz_ks_zw.get(0).getKs_zwh(), Terminal.getSN(), "", "0");
        }
        mTvTip.setText("请拍照");
        soundPool.play(musicId.get(4), 1, 1, 0, 0, 1);
        handler.sendEmptyMessage(viewStr);
        handler.sendEmptyMessageDelayed(visibilityStr, 1000);
    }

    /**
     * 拍照
     */
    public void doTakePicture() {
        if (CameraInterface.getInstance().isPreviewing && (CameraInterface.getInstance().getCameraDevice() != null)) {
            CameraInterface.getInstance().getCameraDevice().takePicture(mShutterCallback, null, mJpegPictureCallback);
        }
    }

    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
        public void onShutter() {
        }
    };

    Camera.PictureCallback mJpegPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                int x = b.getWidth() / 4;
                int y = 0;
                int DST_RECT_WIDTH = b.getWidth() / 2;
                int DST_RECT_HEIGHT = b.getHeight();
                final Bitmap bitmap = Bitmap.createBitmap(b, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);
                CameraInterface.getInstance().rectBitmap = ThumbnailUtils.extractThumbnail(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight()), 168, 240);
                new FaceDialog(RZActivity.this, R.style.MyDialogStyle, new FaceDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            ZpFileName = "sfrz_rzjl/kstz_a_pz/" + rz_ks_zw.get(0).getKs_ksno() + "_" + DateUtil.getNowTime_Millisecond4() + ".jpg";
                            FileUtils.saveBitmap2(CameraInterface.getInstance().rectBitmap, "sfrz_rzjl/kstz_a_pz/", ZpFileName);
                            sfrz_rzjg.setRzjg_time(DateUtil.getNowTime());
                            DbServices.getInstance(getBaseContext()).saveRzjg(sfrz_rzjg);
                            ShowHintDialog(RZActivity.this, rz_ks_zw.get(0).getKs_xm() + " 验证通过", "提示", R.drawable.img_base_icon_correct, 800, false);
                            ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                @Override
                                public Object callback() {
                                    if (DbServices.getInstance(getBaseContext()).selectBKKS(ccno, rz_ks_zw.get(0).getKs_zjno()).getIsRZ().equals("1") || !Utils.stringIsEmpty(zwid)) {
                                        return Boolean.valueOf(true);
                                    } else {
                                        return Boolean.valueOf(false);
                                    }
                                }
                            }, new ABLSynCallback.ForegroundCall() {
                                @Override
                                public void callback(Object obj) {
                                    if (((Boolean) obj).booleanValue()) {
                                        if (fingerData != null) {
                                            byte[] bytes = fingerData.getFingerImage();
                                            zwBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //生成位图
                                            FileUtils.saveBitmap2(zwBitmap, "sfrz_rzjl/kstz_a_zw/", sfrz_rzjl.getRzjl_pith());
                                            DbServices.getInstance(getBaseContext()).saveRzjl(sfrz_rzjl);
                                        }
                                        DbServices.getInstance(getBaseContext()).saveRzjl("8007", rz_ks_zw.get(0).getKs_ksno(), kmno, kdno, rz_ks_zw.get(0).getKs_kcno(), rz_ks_zw.get(0).getKs_zwh(), Terminal.getSN(), "1", DateUtil.getNowTime(), "", ZpFileName, sfrz_rzjg.getRzjgid(), "0");
                                    } else {
                                        DbServices.getInstance(getBaseContext()).saveRzjl("8006", rz_ks_zw.get(0).getKs_ksno(), kmno, kdno, rz_ks_zw.get(0).getKs_kcno(), rz_ks_zw.get(0).getKs_zwh(), Terminal.getSN(), "0", DateUtil.getNowTime(), "", ZpFileName, sfrz_rzjg.getRzjgid(), "0");
                                    }
                                    if (sfrz_rzjg.getRzjg_ztid().equals("21")) {
                                        DbServices.getInstance(getBaseContext()).saveBkKs(kcmcs, ccno, rz_ks_zw.get(0).getKs_zjno());
                                    } else if (sfrz_rzjg.getRzjg_ztid().equals("22")) {
                                        DbServices.getInstance(getBaseContext()).saveBkKs3(kcmcs, ccno, rz_ks_zw.get(0).getKs_zjno());
                                    }
                                    if (Integer.parseInt(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_hyfs()) == 0) {
                                        updateSingle(ksid);
                                    }
                                    xzMS();
                                }
                            });
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setFaceBitmap(CameraInterface.getInstance().rectBitmap).show();
            }
        }
    };

    private void initViewParams() {
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        surfaceView.setLayoutParams(params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isRzSucceed) {
            xzMS();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 局部更新GridView
     *
     * @param position
     */
    private void updateSingle(int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = gvKs.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = gvKs.getLastVisiblePosition();
        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = gvKs.getChildAt(position - firstVisiblePosition);
            LinearLayout linearLayout = view.findViewById(R.id.ll_kslist);
            linearLayout.setBackgroundColor(ContextCompat.getColor(RZActivity.this, R.color.auth_kslist_hasauth));
        }
    }

    public Sfrz_rzjl saveRzjl(String rzjl_rzfsno, String rzjl_ksno, String rzjl_kmbh, String rzjl_kdbh, String rzjl_kcbh, String rzjl_zwh, String rzjl_device, String rzjl_verify_result, String rzjl_time, String rzjl_Features, String rzjl_pith, String rzjl_rzjgid, String rzjl_sb) {
        Sfrz_rzjl sfrzRzjl = new Sfrz_rzjl();
        sfrzRzjl.setRzjl_rzfsno(rzjl_rzfsno);
        sfrzRzjl.setRzjl_ksno(rzjl_ksno);
        sfrzRzjl.setRzjl_kmbh(rzjl_kmbh);
        sfrzRzjl.setRzjl_kdbh(rzjl_kdbh);
        sfrzRzjl.setRzjl_kcbh(rzjl_kcbh);
        sfrzRzjl.setRzjl_zwh(rzjl_zwh);
        sfrzRzjl.setRzjl_device(rzjl_device);
        sfrzRzjl.setRzjl_verify_result(rzjl_verify_result);
        sfrzRzjl.setRzjl_time(rzjl_time);
        sfrzRzjl.setRzjl_Features(rzjl_Features);
        sfrzRzjl.setRzjl_pith(rzjl_pith);
        sfrzRzjl.setRzjl_rzjgid(rzjl_rzjgid);
        sfrzRzjl.setRzjl_sb(rzjl_sb);
        return sfrzRzjl;
    }

    public Sfrz_rzjg saveRzjg(String rzjg_ztid, String rzjg_ksno, String rzjg_kmno, String rzjg_kdno, String rzjg_kcno, String rzjg_zwh, String rzjg_device, String rzjg_time, String rzjg_sb) {
        Sfrz_rzjg sfrzRzjg = new Sfrz_rzjg();
        sfrzRzjg.setRzjg_ztid(rzjg_ztid);
        sfrzRzjg.setRzjg_ksno(rzjg_ksno);
        sfrzRzjg.setRzjg_kmno(rzjg_kmno);
        sfrzRzjg.setRzjg_kdno(rzjg_kdno);
        sfrzRzjg.setRzjg_kcno(rzjg_kcno);
        sfrzRzjg.setRzjg_zwh(rzjg_zwh);
        sfrzRzjg.setRzjg_device(rzjg_device);
        sfrzRzjg.setRzjg_time(rzjg_time);
        sfrzRzjg.setRzjg_a(" ");
        sfrzRzjg.setRzjg_b(" ");
        sfrzRzjg.setRzjg_sb(rzjg_sb);
        return sfrzRzjg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTask.stop();
        handler.removeCallbacks(runnable02);
        handler.removeCallbacks(runnable04);
        handler = null;
        fingerData = null;
        CameraInterface.getInstance().doStopCamera();
    }
}
