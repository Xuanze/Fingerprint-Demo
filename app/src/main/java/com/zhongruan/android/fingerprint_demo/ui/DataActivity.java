package com.zhongruan.android.fingerprint_demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.adapter.FileBrowserAdapter;
import com.zhongruan.android.fingerprint_demo.adapter.SelectKcMcAdapter;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.db.Bk_ks_tempDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ksxpDao;
import com.zhongruan.android.fingerprint_demo.db.DbServices;
import com.zhongruan.android.fingerprint_demo.db.Ks_ccDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kcDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kdDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kmDao;
import com.zhongruan.android.fingerprint_demo.db.Kstz_zwDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzjgDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzjlDao;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks_cjxx;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks_temp;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjg;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjl;
import com.zhongruan.android.fingerprint_demo.dialog.EditDialog;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog;
import com.zhongruan.android.fingerprint_demo.dialog.MultiProgressDialog;
import com.zhongruan.android.fingerprint_demo.dialog.ZipListDialog;
import com.zhongruan.android.fingerprint_demo.socket.SocketClient;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.DateUtil;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;
import com.zhongruan.android.fingerprint_demo.utils.LogUtil;
import com.zhongruan.android.fingerprint_demo.utils.Utils;
import com.zhongruan.android.fingerprint_demo.zip.archiver.ArchiverManager;
import com.zhongruan.android.fingerprint_demo.zip.archiver.IArchiverListener;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.BuildConfig;

import static com.zhongruan.android.fingerprint_demo.utils.FileUtils.delFolder;
import static com.zhongruan.android.fingerprint_demo.utils.FileUtils.getAppSavePath;
import static com.zhongruan.android.fingerprint_demo.utils.Utils.getUSBPath;
import static com.zhongruan.android.fingerprint_demo.utils.Utils.stringIsEmpty;


/**
 * 数据管理
 * Created by Administrator on 2017/8/15.
 */
public class DataActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llDataBack, llUsbDr, llNetDr, llUsbDc, llNetSb, data_ll, ll_cj_usb_dc, select_kc_rl;
    private GridView gridView;
    private SelectKcMcAdapter selectKcMcAdapter;
    private String hint, pwd;
    private FileBrowserAdapter adapter;
    private Button ll_buttons;
    private List<String> tempList, fileList;
    private TextView tvUsbDr, tvNetDr, tv_empty, tv_selectkc, titleData, tv_rz_usb_dc, tv_cj_usb_dc;
    private boolean isError;
    private MyHandler handler;
    private HashMap<Integer, String> progressMap;
    private HashMap<Integer, Boolean> finishMap;
    private String NewPith = getAppSavePath() + "/DataTemp/";
    private String NewFilePath, NewFile;
    private MultiProgressDialog multiProgressDialog;
    private int progressSize, number = 0;
    private boolean isUSB = true;
    private List<Ks_kc> ksKcList;
    private int isReceive;
    private File zipFile, cjFile;
    private boolean isWC1, isWC2, isWC3, isCG = false;
    private List<Sfrz_rzjl> listRzjl;
    private List<Sfrz_rzjg> listRzjg;
    private List<Bk_ks_temp> bkKsTempList;
    private ImageView mIvSelectall;
    private LinearLayout mLvSelectall;
    private List<Ks_kc> mXZksKcList;
    private final String TAG = "DataActivity";
    private TextView mTvRzNetDc;
    private LinearLayout mLlNetDc;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_setting_data);
    }

    @Override
    public void initViews() {
        llDataBack = findViewById(R.id.ll_data_back);
        llUsbDr = findViewById(R.id.ll_usb_dr);
        llNetDr = findViewById(R.id.ll_net_dr);
        llUsbDc = findViewById(R.id.ll_usb_dc);
        llNetSb = findViewById(R.id.ll_net_sb);
        tvUsbDr = findViewById(R.id.tv_usb_dr);
        tvNetDr = findViewById(R.id.tv_net_dr);
        gridView = findViewById(R.id.gv_content);
        tv_empty = findViewById(R.id.tv_empty);
        tv_selectkc = findViewById(R.id.tv_selectkc);
        titleData = findViewById(R.id.titleData);
        data_ll = findViewById(R.id.data_ll);
        select_kc_rl = findViewById(R.id.select_kc_rl);
        ll_buttons = findViewById(R.id.ll_buttons);
        tv_rz_usb_dc = findViewById(R.id.tv_rz_usb_dc);
        ll_cj_usb_dc = findViewById(R.id.ll_cj_usb_dc);
        tv_cj_usb_dc = findViewById(R.id.tv_cj_usb_dc);
        mIvSelectall = findViewById(R.id.iv_selectall);
        mLvSelectall = findViewById(R.id.lv_selectall);
        mTvRzNetDc = findViewById(R.id.tv_rz_net_dc);
        mLlNetDc = findViewById(R.id.ll_net_dc);
        handler = new MyHandler();
        if (!stringIsEmpty(ConfigApplication.getApplication().getUsbImportTime())) {
            tvUsbDr.setText("最近导入：" + ConfigApplication.getApplication().getUsbImportTime());
        }
        if (!stringIsEmpty(ConfigApplication.getApplication().getNetImportTime())) {
            tvNetDr.setText("最近导入：" + ConfigApplication.getApplication().getNetImportTime());
        }
        if (!stringIsEmpty(ConfigApplication.getApplication().getUsbExportTime())) {
            tv_rz_usb_dc.setText("最近导入：" + ConfigApplication.getApplication().getUsbExportTime());
        }
        if (!stringIsEmpty(ConfigApplication.getApplication().getNetExportTime())) {
            mTvRzNetDc.setText("最近导入：" + ConfigApplication.getApplication().getNetExportTime());
        }
        if (!stringIsEmpty(ConfigApplication.getApplication().getCJExportTime())) {
            tv_cj_usb_dc.setText("最近导入：" + ConfigApplication.getApplication().getCJExportTime());
        }
    }

    @Override
    public void initListeners() {
        mLlNetDc.setOnClickListener(this);
        llDataBack.setOnClickListener(this);
        llUsbDr.setOnClickListener(this);
        llNetDr.setOnClickListener(this);
        llUsbDc.setOnClickListener(this);
        llNetSb.setOnClickListener(this);
        ll_cj_usb_dc.setOnClickListener(this);
        mLvSelectall.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            int current = msg.arg1;
            int i = msg.arg2;
            switch (what) {
                case 0:
                    finishMap.put(0, false);
                    progressMap.put(0, "开始复制文件");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 1:
                    finishMap.put(0, true);
                    progressMap.put(0, "文件复制完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 2:
                    finishMap.put(1, false);
                    progressMap.put(1, "解压进度：" + current + "%");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 3:
                    finishMap.put(1, true);
                    progressMap.put(1, "解压完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 4:
                    finishMap.put(2, false);
                    progressMap.put(2, "清空数据库中");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 5:
                    finishMap.put(2, true);
                    progressMap.put(2, "清空数据完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 6:
                    finishMap.put(3, false);
                    progressMap.put(3, "导入考生报名照片");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 7:
                    finishMap.put(3, true);
                    progressMap.put(3, "导入考生报名照片完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 8:
                    finishMap.put(4, false);
                    progressMap.put(4, "正在导入第" + i + "个考生编排信息");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 9:
                    finishMap.put(4, true);
                    progressMap.put(4, "导入考生编排信息完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 10:
                    finishMap.put(5, false);
                    progressMap.put(5, "正在导入第" + i + "个考生报名照片路径信息");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 11:
                    finishMap.put(5, true);
                    progressMap.put(5, "导入考生报名照片路径信息完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 12:
                    finishMap.put(6, false);
                    progressMap.put(6, "正在导入第" + i + "个考生指纹信息");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 13:
                    finishMap.put(6, true);
                    progressMap.put(6, "导入考生指纹信息完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 14:
                    finishMap.put(7, false);
                    progressMap.put(7, "插入考点/考场/场次/科目/考生/编排");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 15:
                    finishMap.put(7, true);
                    progressMap.put(7, "插入考点/考场/场次/科目/考生/编排完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 16:
                    finishMap.put(0, false);
                    progressMap.put(0, "正在下载数据：" + current + "%");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
                case 17:
                    finishMap.put(0, true);
                    progressMap.put(0, "数据下载完成");
                    showImportProgressDialog(DataActivity.this, progressMap, finishMap);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_data_back:
                finish();
                break;
            case R.id.ll_usb_dr:
                new HintDialog(this, R.style.dialog, "导入数据前会清空以前数据，\r\n是否继续？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            isCG = false;
                            isError = false;
                            if (Utils.checkUSBInserted()) {
                                initMap();
                                tempList = Utils.checkUSBZip(getUSBPath(), null);
                                LogUtil.i(tempList);
                                if (tempList == null || BuildConfig.VERSION_NAME.equals(tempList.get(0))) {
                                    isError = true;
                                    hint = "U盘根目录不存在ZIP包";
                                    LogUtil.i(TAG, "U盘根目录不存在ZIP包");
                                } else if (tempList == null || BuildConfig.VERSION_NAME.equals(tempList.get(2)) || BuildConfig.VERSION_NAME.equals(tempList.get(3)) || tempList.size() <= 4) {
                                    copyFile();
                                    LogUtil.i(TAG, "U盘根目录只存在一个ZIP包");
                                } else {
                                    LogUtil.i(TAG, "U盘根目录存在多个ZIP包");
                                    fileList = new ArrayList();
                                    fileList.addAll(tempList);
                                    fileList.remove(0);
                                    fileList.remove(0);
                                    fileList.remove(0);
                                    fileList.remove(0);
                                    final ListView listView = new ListView(DataActivity.this);
                                    adapter = new FileBrowserAdapter(DataActivity.this, fileList);
                                    new ZipListDialog(DataActivity.this, R.style.dialog, listView, new ZipListDialog.OnCloseListener() {
                                        @Override
                                        public void onClick(Dialog dialog, int p, boolean confirm) {
                                            if (confirm) {
                                                tempList.set(0, fileList.get(p));
                                                LogUtil.i(tempList.get(0));
                                                copyFile();
                                                dialog.dismiss();
                                            } else {
                                                dialog.dismiss();
                                            }
                                        }
                                    }).setTitle("请选择要导入的U盘数据包").setAdapter(adapter).show();
                                }
                            } else {
                                isError = true;
                                hint = "U盘未插入";
                                LogUtil.i(TAG, "U盘未插入");
                            }
                        } else {
                            dialog.dismiss();
                        }
                        if (isError) {
                            ShowHintDialog(DataActivity.this, hint, "U盘导入数据", R.drawable.img_base_icon_error, "知道了", false);
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setTitle("U盘导入数据").setPositiveButton("是").setNegativeButton("否").show();
                break;
            case R.id.ll_net_dr:
                new HintDialog(this, R.style.dialog, "导入数据前会清空以前数据\r\n是否继续？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            isCG = false;
                            if (ConfigApplication.getApplication().getKDConnectState()) {
                                initMap();
                                MyApplication.getApplication().setShouldStopUploadingData(true);
                                showProgressDialog(DataActivity.this, "正在清空数据...", false);
                                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                    public Object callback() {
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                if (deleteRecord()) {
                                                    isUSB = false;
                                                    String path = getAppSavePath() + "/DataTemp/";
                                                    File file = new File(path);
                                                    if (!file.exists()) {
                                                        file.mkdirs();
                                                    }
                                                    SocketClient client = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                                                    isReceive = client.receiveUnLockField(BuildConfig.VERSION_NAME, 105, path, handler);
                                                    dismissProgressDialog();
                                                } else {
                                                    ShowHintDialog(DataActivity.this, "清空数据失败！", "网络导入数据", R.drawable.img_base_icon_error, "知道了", false);
                                                    return;
                                                }
                                                LogUtil.i(TAG, "数据包下载：" + isReceive);
                                            }
                                        }.run();
                                        return isReceive;
                                    }
                                }, new ABLSynCallback.ForegroundCall() {
                                    public void callback(Object obj) {
                                        if ((int) obj == 1) {
                                            Message message1 = new Message();
                                            message1.what = 17;
                                            handler.sendMessage(message1);
                                            File tempFile = new File(NewPith.trim());
                                            File[] array = tempFile.listFiles();
                                            LogUtil.i("tempList", array[0]);
                                            NewFile = StringUtils.substringAfterLast(array[0].getPath(), "/");
                                            NewFilePath = StringUtils.substringBeforeLast(NewFile, ".");
                                            LogUtil.i("网络下载数据解压", "array[0].getPath():" + array[0].getPath() + " StringUtils.substringBeforeLast():" + StringUtils.substringBeforeLast(array[0].getPath(), "."));
                                            unZipFile(array[0].getPath(), StringUtils.substringBeforeLast(array[0].getPath(), "."), "mst");
                                        } else if ((int) obj == 0) {
                                            dismissProgressDialog();
                                            ShowHintDialog(DataActivity.this, "暂无数据，请检查服务器是否生成数据包！", "网络导入数据", R.drawable.img_base_icon_error, "知道了", false);
                                        } else if ((int) obj == -1) {
                                            dismissProgressDialog();
                                            multiProgressDialog.dismiss();
                                            ShowHintDialog(DataActivity.this, "网络通讯异常，请检查网络是否连接服务器！", "网络导入数据", R.drawable.img_base_icon_error, "知道了", false);
                                        }
                                    }
                                });
                            } else {
                                ShowHintDialog(DataActivity.this, "网络通讯异常，请检查网络是否连接服务器！", "网络导入数据", R.drawable.img_base_icon_error, "知道了", false);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setTitle("网络导入数据").setPositiveButton("是").setNegativeButton("否").show();
                break;
            case R.id.ll_usb_dc:
                new HintDialog(this, R.style.dialog, "是否导出认证数据包到U盘？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            if (Utils.checkUSBInserted()) {
                                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                    @Override
                                    public Object callback() {
                                        listRzjl = DbServices.getInstance(DataActivity.this).loadAllrzjl();
                                        listRzjg = DbServices.getInstance(DataActivity.this).loadAllrzjg();
                                        bkKsTempList = DbServices.getInstance(DataActivity.this).selectDOWNBKKS(DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_no());
                                        if (listRzjl.size() > 0 && listRzjg.size() > 0) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }, new ABLSynCallback.ForegroundCall() {
                                    @Override
                                    public void callback(Object obj) {
                                        if (((Boolean) obj).booleanValue()) {
                                            showProgressDialog(DataActivity.this, "正在导出数据库数据...", false);
                                            ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                                @Override
                                                public Object callback() {
                                                    if (delFolder("ExportData") && delFolder("TempZIP")) {
                                                        showProgressDialog(DataActivity.this, "正在生成TXT文件...", false);
                                                        for (int i = 0; i < listRzjl.size(); i++) {
                                                            FileUtils.writeTxtToFile(listRzjl.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "sfrz_rzjl.txt");
                                                        }
                                                        for (int i = 0; i < listRzjg.size(); i++) {
                                                            FileUtils.writeTxtToFile(listRzjg.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "sfrz_rzjg.txt");
                                                        }
                                                        for (int i = 0; i < bkKsTempList.size(); i++) {
                                                            FileUtils.writeTxtToFile(bkKsTempList.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "bk_ks.txt");
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
                                                        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                                            @Override
                                                            public Object callback() {
                                                                if (FileUtils.copyFolder(getAppSavePath() + "/sfrz_rzjl/", getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/sfrz_rzjl/")) {
                                                                    return true;
                                                                } else {
                                                                    return false;
                                                                }
                                                            }
                                                        }, new ABLSynCallback.ForegroundCall() {
                                                            @Override
                                                            public void callback(Object obj) {
                                                                if (((Boolean) obj).booleanValue()) {
                                                                    File file1 = new File(getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/");
                                                                    zipFile = new File(getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_" + DateUtil.getNowTime2());
                                                                    if (file1.renameTo(zipFile)) {
                                                                        ArchiverManager.getInstance().doArchiver(getAppSavePath() + "/ExportData/" + zipFile.getName(), getAppSavePath() + "/TempZIP/", true, "mst", ArchiverManager.ArchiverType._ZIP, new IArchiverListener() {
                                                                            @Override
                                                                            public void onStartArchiver() {
                                                                                LogUtil.i("时间：" + System.currentTimeMillis());
                                                                                showProgressDialog(DataActivity.this, "开始压缩文件...", false);
                                                                                showProgressDialog(DataActivity.this, "正在压缩文件...", false);
                                                                            }

                                                                            @Override
                                                                            public void onProgressArchiver(int current, int total) {
                                                                            }

                                                                            @Override
                                                                            public void onEndArchiver() {
                                                                                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                                                                    @Override
                                                                                    public Object callback() {
                                                                                        showProgressDialog(DataActivity.this, "压缩完成，校验压缩包完整性...", false);
                                                                                        File tempFolder = new File(getAppSavePath() + "/TempZIP/" + zipFile.getName() + ".zip");
                                                                                        showProgressDialog(DataActivity.this, "正在复制压缩文件到U盘...", false);
                                                                                        String USBPath = FileUtils.copyFileToUSB(tempFolder);
                                                                                        if (!stringIsEmpty(USBPath)) {
                                                                                            return true;
                                                                                        } else {
                                                                                            return false;
                                                                                        }
                                                                                    }
                                                                                }, new ABLSynCallback.ForegroundCall() {
                                                                                    @Override
                                                                                    public void callback(Object obj) {
                                                                                        dismissProgressDialog();
                                                                                        if (((Boolean) obj).booleanValue()) {

                                                                                            ShowHintDialog(DataActivity.this, "认证数据导出成功", "U盘导出认证数据", R.drawable.img_base_check, "知道了", false);
                                                                                            ConfigApplication.getApplication().setUsbExportTime(DateUtil.getNowTime());
                                                                                            tv_rz_usb_dc.setText("最近导入：" + ConfigApplication.getApplication().getUsbExportTime());
                                                                                        } else {
                                                                                            ShowHintDialog(DataActivity.this, "认证数据导出失败", "U盘导出认证数据", R.drawable.img_base_icon_error, "知道了", false);
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                } else {
                                                                    ShowHintDialog(DataActivity.this, "认证数据导出失败", "U盘导出认证数据", R.drawable.img_base_icon_error, "知道了", false);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        ShowHintDialog(DataActivity.this, "认证数据导出失败", "U盘导出认证数据", R.drawable.img_base_icon_error, "知道了", false);
                                                    }
                                                }
                                            });
                                        } else {
                                            ShowHintDialog(DataActivity.this, "暂无认证数据", "U盘导出认证数据", R.drawable.img_base_icon_error, "知道了", false);
                                        }
                                    }
                                });
                            } else {
                                ShowHintDialog(DataActivity.this, "U盘未插入", "U盘导出认证数据", R.drawable.img_base_icon_error, "知道了", false);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setLLButtonVisibility(true).setTitle("U盘导出认证数据").setPositiveButton("是").setNegativeButton("否").show();
                break;
            case R.id.ll_cj_usb_dc:
                new HintDialog(this, R.style.dialog, "是否导出采集数据包到U盘？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            if (Utils.checkUSBInserted()) {
                                List<Bk_ks_cjxx> cjxxList = DbServices.getInstance(DataActivity.this).loadAllNote();
                                if (cjxxList.size() > 0) {
                                    showProgressDialog(DataActivity.this, "正在导出数据库数据...", false);
                                    LogUtil.i("导出认证数据包到U盘", "正在导出数据库数据...");
                                    if (delFolder("ImportData") && delFolder("TempZIP")) {
                                        showProgressDialog(DataActivity.this, "正在生成TXT文件...", false);
                                        for (int i = 0; i < cjxxList.size(); i++) {
                                            FileUtils.writeTxtToFile(cjxxList.get(i).toString(), getAppSavePath() + "/ImportData/XXCJ/", "bk_ks_cjxx.txt");
                                        }
                                        if (FileUtils.copyFolder(getAppSavePath() + "/kstz_c_sfz/", getAppSavePath() + "/ImportData/XXCJ/") && FileUtils.copyFolder(getAppSavePath() + "/kstz_c_xp/", getAppSavePath() + "/ImportData/XXCJ/") && FileUtils.copyFolder(getAppSavePath() + "/kstz_c_zw/", getAppSavePath() + "/ImportData/XXCJ/")) {
                                            cjFile = new File(getAppSavePath() + "/ImportData/XXCJ" + DateUtil.getNowTime2());
                                            if (new File(getAppSavePath() + "/ImportData/XXCJ").renameTo(cjFile)) {
                                                ArchiverManager.getInstance().doArchiver(getAppSavePath() + "/ImportData/" + cjFile.getName(), getAppSavePath() + "/TempZIP/", true, "mst", ArchiverManager.ArchiverType._ZIP, new IArchiverListener() {
                                                    @Override
                                                    public void onStartArchiver() {
                                                        showProgressDialog(DataActivity.this, "开始压缩文件...", false);
                                                        showProgressDialog(DataActivity.this, "正在压缩文件...", false);
                                                    }

                                                    @Override
                                                    public void onProgressArchiver(int current, int total) {
                                                    }

                                                    @Override
                                                    public void onEndArchiver() {
                                                        showProgressDialog(DataActivity.this, "压缩完成，校验压缩包完整性...", false);
                                                        showProgressDialog(DataActivity.this, "正在复制压缩文件到U盘...", false);
                                                        File tempFolder = new File(getAppSavePath() + "/TempZIP/" + cjFile.getName() + ".zip");
                                                        String USBPath = FileUtils.copyFileToUSB(tempFolder);
                                                        LogUtil.i(TAG, "USBPath:" + USBPath);
                                                        dismissProgressDialog();
                                                        if (!stringIsEmpty(USBPath)) {
                                                            ShowHintDialog(DataActivity.this, "采集数据导出成功", "U盘导出采集数据", R.drawable.img_base_check, "知道了", false);
                                                            ConfigApplication.getApplication().setCJExportTime(DateUtil.getNowTime());
                                                            tv_cj_usb_dc.setText("最近导入：" + ConfigApplication.getApplication().getCJExportTime());
                                                        } else {
                                                            ShowHintDialog(DataActivity.this, "采集数据导出失败", "U盘导出采集数据", R.drawable.img_base_icon_error, "知道了", false);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                } else {
                                    ShowHintDialog(DataActivity.this, "暂无采集数据", "U盘导出采集数据", R.drawable.img_base_icon_error, "知道了", false);
                                }
                            } else {
                                ShowHintDialog(DataActivity.this, "U盘未插入", "U盘导出采集数据", R.drawable.img_base_icon_error, "知道了", false);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setLLButtonVisibility(true).setTitle("U盘导出采集数据").setPositiveButton("是").setNegativeButton("否").show();
                break;
            case R.id.ll_net_sb:
                new HintDialog(this, R.style.dialog, "后台上报服务已开启，请稍等", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            new Runnable() {
                                @Override
                                public void run() {
                                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Sfrz_rzjgDao.TABLENAME + " SET  rzjg_sb = 0");
                                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("UPDATE " + Sfrz_rzjlDao.TABLENAME + " SET  rzjl_sb = 0");
                                }
                            };
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_check).setNOVisibility(false).setLLButtonVisibility(true).setTitle("提示").setPositiveButton("知道了").show();
                break;
            case R.id.lv_selectall:
                this.selectKcMcAdapter.selectAll();
                resetKcText();
                break;
            case R.id.ll_net_dc:
                new HintDialog(this, R.style.dialog, "是否导出认证数据包到服务器？", new HintDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            showProgressDialog(DataActivity.this, "正在导出数据库数据...", false);
                            listRzjl = DbServices.getInstance(DataActivity.this).loadAllrzjl();
                            listRzjg = DbServices.getInstance(DataActivity.this).loadAllrzjg();
                            bkKsTempList = DbServices.getInstance(DataActivity.this).selectDOWNBKKS(DbServices.getInstance(getBaseContext()).selectCC().get(0).getCc_no());
                            if (listRzjl.size() > 0 && listRzjg.size() > 0) {
                                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                    @Override
                                    public Object callback() {
                                        if (delFolder("ExportData") && delFolder("TempZIP")) {
                                            showProgressDialog(DataActivity.this, "正在生成TXT文件...", false);
                                            for (int i = 0; i < listRzjl.size(); i++) {
                                                FileUtils.writeTxtToFile(listRzjl.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "sfrz_rzjl.txt");
                                            }
                                            for (int i = 0; i < listRzjg.size(); i++) {
                                                FileUtils.writeTxtToFile(listRzjg.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "sfrz_rzjg.txt");
                                            }
                                            for (int i = 0; i < bkKsTempList.size(); i++) {
                                                FileUtils.writeTxtToFile(bkKsTempList.get(i).toString(), getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/", "bk_ks.txt");
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
                                            ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                                @Override
                                                public Object callback() {
                                                    if (FileUtils.copyFolder(getAppSavePath() + "/sfrz_rzjl/", getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/sfrz_rzjl/")) {
                                                        return true;
                                                    } else {
                                                        return false;
                                                    }
                                                }
                                            }, new ABLSynCallback.ForegroundCall() {
                                                @Override
                                                public void callback(Object obj) {
                                                    if (((Boolean) obj).booleanValue()) {
                                                        File file1 = new File(getAppSavePath() + "/ExportData/" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_/");
                                                        zipFile = new File(getAppSavePath() + "/ExportData/" + "mst_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_no() + "_" + DbServices.getInstance(DataActivity.this).loadAllkd().get(0).getKd_name() + "_" + DateUtil.getNowTime2());
                                                        if (file1.renameTo(zipFile)) {
                                                            ArchiverManager.getInstance().doArchiver(getAppSavePath() + "/ExportData/" + zipFile.getName(), getAppSavePath() + "/TempZIP/", true, "mst", ArchiverManager.ArchiverType._ZIP, new IArchiverListener() {
                                                                @Override
                                                                public void onStartArchiver() {
                                                                    LogUtil.i("时间：" + System.currentTimeMillis());
                                                                    showProgressDialog(DataActivity.this, "开始压缩文件...", false);
                                                                    showProgressDialog(DataActivity.this, "正在压缩文件...", false);
                                                                }

                                                                @Override
                                                                public void onProgressArchiver(int current, int total) {
                                                                }

                                                                @Override
                                                                public void onEndArchiver() {
                                                                    ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                                                                        @Override
                                                                        public Object callback() {
                                                                            showProgressDialog(DataActivity.this, "压缩完成，校验压缩包完整性...", false);
                                                                            showProgressDialog(DataActivity.this, "正在上传数据到考点服务器...", false);
                                                                            SocketClient socketClient = new SocketClient(DbServices.getInstance(getBaseContext()).loadAllSbSetting().get(0).getSb_ip());
                                                                            return socketClient.sendFile("", 307, getAppSavePath() + "/TempZIP/" + zipFile.getName() + ".zip", zipFile.getName() + ".zip").get("success");
                                                                        }
                                                                    }, new ABLSynCallback.ForegroundCall() {
                                                                        @Override
                                                                        public void callback(Object obj) {
                                                                            dismissProgressDialog();
                                                                            if (((Boolean) obj).booleanValue()) {
                                                                                ShowHintDialog(DataActivity.this, "上传到考点服务器成功", "提示", R.drawable.img_base_check, "知道了", false);
                                                                                ConfigApplication.getApplication().setNetExportTime(DateUtil.getNowTime());
                                                                                mTvRzNetDc.setText("最近导入：" + ConfigApplication.getApplication().getNetExportTime());
                                                                            } else {
                                                                                ShowHintDialog(DataActivity.this, "上传到考点服务器失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        ShowHintDialog(DataActivity.this, "上传到考点服务器失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
                                                    }
                                                }
                                            });
                                        } else {
                                            ShowHintDialog(DataActivity.this, "上传到考点服务器失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
                                        }
                                    }
                                });
                            } else {
                                ShowHintDialog(DataActivity.this, "暂无认证数据", "提示", R.drawable.img_base_icon_error, "知道了", false);
                            }
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setBackgroundResource(R.drawable.img_base_icon_question).setNOVisibility(true).setLLButtonVisibility(true).setLLButtonVisibility(true).setTitle("提示").setPositiveButton("是").setNegativeButton("否").show();

                break;
        }
    }

    /**
     * 复制U盘压缩包到本地
     */
    private void copyFile() {
        showProgressDialog(DataActivity.this, "正在清空数据...", false);
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                if (deleteRecord()) {
                    dismissProgressDialog();
                }
                MyApplication.getApplication().setShouldStopUploadingData(true);
                isUSB = true;
                Message message1 = new Message();
                message1.what = 0;
                handler.sendMessage(message1);
                if (tempList == null || tempList.size() <= 0) {
                    return false;
                } else {
                    if (FileUtils.copyFile(tempList)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    if (!stringIsEmpty(tempList.get(1))) {
                        pwd = Utils.checkPwd(NewPith);
                        LogUtil.i("密码：" + pwd);
                    }
                    Message message2 = new Message();
                    message2.what = 1;
                    handler.sendMessage(message2);
                    isShouldPwd();
                } else {
                    multiProgressDialog.dismiss();
                    isError = true;
                    hint = "复制文件失败！";
                    return;
                }
            }
        });
    }

    /**
     * 判断是否需要密码
     */
    private void isShouldPwd() {
        try {
            NewFile = StringUtils.substringAfterLast(tempList.get(0), "/");
            NewFilePath = StringUtils.substringBeforeLast(NewFile, ".");
            LogUtil.i("NewFile:" + NewPith + NewFile);
            ZipFile zFile = new ZipFile(NewPith + NewFile);
            if (zFile.isEncrypted() && stringIsEmpty(tempList.get(1))) {
                multiProgressDialog.dismiss();
                new EditDialog(DataActivity.this, R.style.dialog, new EditDialog.OnEditInputFinishedListener() {
                    @Override
                    public void editInputFinished(Dialog dialog, String password, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            multiProgressDialog.show();
                            Message message3 = new Message();
                            message3.what = 1;
                            handler.sendMessage(message3);
                            unZipFile(NewPith + NewFile, NewPith + NewFilePath, password);
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).setTitle("请输入解压密码").setHint("请输入解压密码").setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD).show();
            } else {
                unZipFile(NewPith + NewFile, NewPith + NewFilePath, pwd);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩包解压
     *
     * @param srcfile   文件名
     * @param unrarPath 文件解压存放路径
     * @param password  解压密码
     */
    private void unZipFile(String srcfile, String unrarPath, String password) {
        ArchiverManager.getInstance().doUnArchiver(srcfile, unrarPath, password, new IArchiverListener() {
            @Override
            public void onStartArchiver() {
            }

            @Override
            public void onProgressArchiver(int current, int total) {
                Message message4 = new Message();
                message4.what = 2;
                message4.arg1 = (current * 100 / total);
                handler.sendMessage(message4);
            }

            @Override
            public void onEndArchiver() {
                Message message5 = new Message();
                message5.what = 3;
                handler.sendMessage(message5);
                deleteData();
            }
        });
    }

    /**
     * 清空历史数据
     */
    private void deleteData() {
        Message message6 = new Message();
        message6.what = 4;
        handler.sendMessage(message6);
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                DbServices.getInstance(DataActivity.this).deleteAlltemp();
                DbServices.getInstance(DataActivity.this).deleteAllxp();
                DbServices.getInstance(DataActivity.this).deleteAllrzks();
                DbServices.getInstance(DataActivity.this).deleteAllzw();
                DbServices.getInstance(DataActivity.this).deleteAllcc();
                DbServices.getInstance(DataActivity.this).deleteAllkc();
                DbServices.getInstance(DataActivity.this).deleteAllkd();
                DbServices.getInstance(DataActivity.this).deleteAllkm();
                DbServices.getInstance(DataActivity.this).deleteAllbkks();
                DbServices.getInstance(DataActivity.this).deleteAllrzjl();
                DbServices.getInstance(DataActivity.this).deleteAllrzjg();
                if (DbServices.getInstance(DataActivity.this).loadAlltemp().size() == 0) {
                    return Boolean.valueOf(true);
                }
                return Boolean.valueOf(false);
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    Message message7 = new Message();
                    message7.what = 5;
                    handler.sendMessage(message7);
                    renameFile();
                } else {
                    multiProgressDialog.dismiss();
                    ShowHintDialog(DataActivity.this, "数据库数据清除失败", "U盘导入数据", R.drawable.img_base_icon_error, "知道了", false);
                }
            }
        });
    }

    /**
     * 文件重命名
     */
    private void renameFile() {
        Message message8 = new Message();
        message8.what = 6;
        handler.sendMessage(message8);
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                File files = new File(NewPith + NewFilePath);
                LogUtil.i("NewFilePath:" + NewFilePath);
                LogUtil.i("files:" + files.getPath());
                File[] file = files.listFiles();
                for (int i = 0; i < file.length; i++) {
                    LogUtil.i("getFileName", file[i].getPath());
                    FileUtils.renameFile(file[i].getPath());
                    if (i == (file.length - 1)) {
                        return Boolean.valueOf(true);
                    }
                }
                return Boolean.valueOf(false);
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    Message message9 = new Message();
                    message9.what = 7;
                    handler.sendMessage(message9);
                    if (!new File(NewPith + NewFilePath + "/bk_ksxp.txt").exists()) {
                        multiProgressDialog.dismiss();
                        ShowHintDialog(DataActivity.this, "数据导入失败，请检查是否导入考生照片!", "提示", R.drawable.img_base_icon_error, "知道了", false);
                        return;
                    } else {
                        importData("bk_ks.txt", Bk_ks_tempDao.TABLENAME, 8, true, false, false);
                        importData("bk_ksxp.txt", Bk_ksxpDao.TABLENAME, 10, false, true, false);
                        importData("kstz_zw.txt", Kstz_zwDao.TABLENAME, 12, false, false, true);
                    }
                } else {
                    multiProgressDialog.dismiss();
                    ShowHintDialog(DataActivity.this, "报名照片复制失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
                }
            }
        });
    }

    /**
     * 删除历史数据记录
     */
    private boolean deleteRecord() {
        if (delFolder("DataTemp") && delFolder("bk_ksxp") && delFolder("sfrz_rzjl")) {
            DbServices.getInstance(DataActivity.this).deleteAlltemp();
            DbServices.getInstance(DataActivity.this).deleteAllxp();
            DbServices.getInstance(DataActivity.this).deleteAllrzks();
            DbServices.getInstance(DataActivity.this).deleteAllzw();
            DbServices.getInstance(DataActivity.this).deleteAllcc();
            DbServices.getInstance(DataActivity.this).deleteAllkc();
            DbServices.getInstance(DataActivity.this).deleteAllkd();
            DbServices.getInstance(DataActivity.this).deleteAllkm();
            DbServices.getInstance(DataActivity.this).deleteAllbkks();
            DbServices.getInstance(DataActivity.this).deleteAllrzjl();
            DbServices.getInstance(DataActivity.this).deleteAllrzjg();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取TXT文件内容写入数据库
     *
     * @param fileName      文件名
     * @param tabName       表名
     * @param mag           Message
     * @param isOneFinish   判断第一个是否完成
     * @param isTwoFinish   判断第二个是否完成
     * @param isThirdFinish 判断第三个是否完成
     */
    private void importData(final String fileName, final String tabName, final int mag, final boolean isOneFinish, final boolean isTwoFinish, final boolean isThirdFinish) {
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                File files = new File(NewPith + NewFilePath + "/" + fileName);
                if (getFileContent(files, tabName, mag)) {
                    LogUtil.i("成功", "成功" + isOneFinish + isTwoFinish + isThirdFinish);
                    return Boolean.valueOf(true);
                } else {
                    LogUtil.i("成功", "失败" + isOneFinish + isTwoFinish + isThirdFinish);
                    return Boolean.valueOf(false);
                }
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (obj != null && ((Boolean) obj).booleanValue()) {
                    Message message10 = new Message();
                    message10.what = mag + 1;
                    handler.sendMessage(message10);
                    if (isOneFinish) {
                        isWC1 = true;
                    }
                    if (isTwoFinish) {
                        isWC2 = true;
                    }
                    if (isThirdFinish) {
                        isWC3 = true;
                    }
                    if (isWC1 && isWC2 && isWC3) {
                        isWC1 = false;
                        isWC2 = false;
                        isWC3 = false;
                        if (isUSB) {
                            ConfigApplication.getApplication().setUsbImportTime(DateUtil.getNowTime());
                            ConfigApplication.getApplication().setNetImportTime("");
                            tvUsbDr.setText("最近导入：" + ConfigApplication.getApplication().getUsbImportTime());
                        } else {
                            ConfigApplication.getApplication().setNetImportTime(DateUtil.getNowTime());
                            ConfigApplication.getApplication().setUsbImportTime("");
                            tvNetDr.setText("最近导入：" + ConfigApplication.getApplication().getNetImportTime());
                        }
                        importXX();
                    }
                } else {
                    if (!isCG) {
                        isCG = true;
                        multiProgressDialog.dismiss();
                        ShowHintDialog(DataActivity.this, "考生编排信息导入失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
                    }
                }
            }
        });
    }

    /**
     * 读取.txt内容
     */
    private boolean getFileContent(File file, String tabName, int mag) {
        int numbers = 0;
        if (file != null) {    // 先判断目录是否为空，否则会报空指针
            if (!file.isDirectory()) {
                if (file.getName().endsWith(".txt")) {//格式为txt文件
                    try {
                        InputStream instream = new FileInputStream(file);
                        if (instream != null) {
                            InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                            BufferedReader buffreader = new BufferedReader(inputreader);
                            String line = "";
                            String lines = "";
                            //分行读取
                            while ((line = buffreader.readLine()) != null) {
                                if (tabName == "bk_ksxp") {
                                    lines = (line.replaceAll("\\\\", "/")).replaceAll("//", "/").replaceAll(",", "','");
                                    number++;
                                } else {
                                    lines = line.replaceAll(",", "','");
                                }
                                LogUtil.i("正在导入考生编排信息");
                                LogUtil.i(TAG, "整行提取txt信息" + lines);
                                Message message11 = new Message();
                                message11.what = mag;
                                message11.arg2 = numbers;
                                handler.sendMessage(message11);
                                MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + tabName + " VALUES ('" + lines + "')");
                                numbers++;
                            }
                            instream.close();
                            return true;
                        }
                    } catch (FileNotFoundException e) {
                        Log.d("TestFile", "The File doesn't not exist.");
                        return false;
                    } catch (IOException e) {
                        Log.d("TestFile", e.getMessage());
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 插入考点/考场/场次/科目/考生/编排
     */
    private void importXX() {
        ksKcList = new ArrayList<>();
        Message message13 = new Message();
        message13.what = 14;
        handler.sendMessage(message13);
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                LogUtil.i(DbServices.getInstance(DataActivity.this).loadAllcc().size());
                LogUtil.i(DbServices.getInstance(DataActivity.this).loadAllkc().size());
                LogUtil.i(DbServices.getInstance(DataActivity.this).loadAllkd().size());
                LogUtil.i(DbServices.getInstance(DataActivity.this).loadAllkm().size());
                if (DbServices.getInstance(DataActivity.this).loadAllcc().size() == 0 && DbServices.getInstance(DataActivity.this).loadAllkc().size() == 0 && DbServices.getInstance(DataActivity.this).loadAllkd().size() == 0 && DbServices.getInstance(DataActivity.this).loadAllkm().size() == 0) {
                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Ks_ccDao.TABLENAME + " (cc_no, cc_name,km_no,km_name,cc_kssj,cc_jssj,cc_extract) " + " select distinct ccno,ccmc,kmno,kmmc,kssj,jssj,'0' from " + Bk_ks_tempDao.TABLENAME);
                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Ks_kmDao.TABLENAME + " (km_no, km_name) " + " select distinct kmno,kmmc from " + Bk_ks_tempDao.TABLENAME);
                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Ks_kcDao.TABLENAME + " (kc_no, kc_name,kc_extract) " + " select distinct kcno,kcmc,'0' from " + Bk_ks_tempDao.TABLENAME);
                    MyApplication.getDaoInstant(getBaseContext()).getDatabase().execSQL("INSERT INTO " + Ks_kdDao.TABLENAME + " (kd_no, kd_name) " + " select distinct kdno,kdmc from " + Bk_ks_tempDao.TABLENAME);
                    ksKcList.addAll(DbServices.getInstance(DataActivity.this).loadAllkc());
                    return Boolean.valueOf(true);
                } else {
                    return Boolean.valueOf(false);
                }
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (obj != null && ((Boolean) obj).booleanValue()) {
                    Message message12 = new Message();
                    message12.what = 15;
                    handler.sendMessage(message12);
                    if (ksKcList.size() > 0) {
                        multiProgressDialog.dismiss();
                        select_kc_rl.setVisibility(View.VISIBLE);
                        data_ll.setVisibility(View.GONE);
                        titleData.setText("选择考场");
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_selectkc.setVisibility(View.GONE);
                        llDataBack.setEnabled(false);
                        for (int i = 0; i < ksKcList.size(); i++) {
                            LogUtil.i(ksKcList.get(i).getKc_name());
                        }
                        selectKcMcAdapter = new SelectKcMcAdapter(DataActivity.this, ksKcList);
                        gridView.setAdapter(selectKcMcAdapter);
                        resetKcText();
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                selectKcMcAdapter.choiceState(i);
                                selectKcMcAdapter.checkSelectAll();
                                resetKcText();
                            }
                        });
                        ll_buttons.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                titleData.setText("数据管理");
                                mXZksKcList = selectKcMcAdapter.getChosenKcList();
                                if (mXZksKcList.size() > 0) {
                                    for (int i = 0; i < mXZksKcList.size(); i++) {
                                        DbServices.getInstance(getBaseContext()).saveKsKc(mXZksKcList.get(i).getKc_name());
                                    }
                                    select_kc_rl.setVisibility(View.GONE);
                                    data_ll.setVisibility(View.VISIBLE);
                                    llDataBack.setEnabled(true);
                                    ShowHintDialog(DataActivity.this, "成功导入" + number + "位考生的编排数据", isUSB ? "U盘导入数据" : "网络导入数据", R.drawable.img_base_check, "知道了", false);
                                    MyApplication.getApplication().setShouldStopUploadingData(false);
                                }
                            }
                        });
                    }
                } else {
                    multiProgressDialog.dismiss();
                    ShowHintDialog(DataActivity.this, "考生编排信息导入失败", "提示", R.drawable.img_base_icon_error, "知道了", false);
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
            this.tv_selectkc.setText("【考场】" + this.selectKcMcAdapter.getChosenKcList().size() + "个");
            this.tv_selectkc.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
            return;
        }
        if (this.ksKcList != null && this.ksKcList.size() > 0) {
            this.tv_selectkc.setText("【考场】" + this.ksKcList.size() + "个");
            this.tv_selectkc.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        }
        this.tv_selectkc.setText(BuildConfig.VERSION_NAME);
        this.tv_selectkc.setVisibility(View.GONE);
        tv_empty.setVisibility(View.VISIBLE);
    }

    private void initMap() {
        progressMap = new HashMap<>();
        finishMap = new HashMap<>();
        number = 0;
        progressSize = 0;
        multiProgressDialog = null;
    }

    public void showImportProgressDialog(Context paramContext, HashMap<Integer, String> paramHashMap, HashMap<Integer, Boolean> paramHashMap1) {
        if (this.multiProgressDialog == null) {
            this.multiProgressDialog = new MultiProgressDialog(paramContext).createDialog();
            this.multiProgressDialog.setTitile("导入进度");
            this.multiProgressDialog.setProgressMap(paramHashMap);
            this.multiProgressDialog.setFinishMap(paramHashMap1);
            this.multiProgressDialog.setCancelable(false);
            this.multiProgressDialog.show();
            WindowManager.LayoutParams localLayoutParams = this.multiProgressDialog.getWindow().getAttributes();
            localLayoutParams.height = 420;
            localLayoutParams.width = 800;
            this.multiProgressDialog.getWindow().setAttributes(localLayoutParams);
            return;
        }
        if (this.progressSize <= paramHashMap.size())
            this.multiProgressDialog.setLastPosition();
        this.progressSize = paramHashMap.size();
        this.multiProgressDialog.setProgressMap(paramHashMap).setFinishMap(paramHashMap1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getApplication().setShouldStopUploadingData(false);
        handler.removeMessages(0);
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(3);
        handler.removeMessages(4);
        handler.removeMessages(5);
        handler.removeMessages(6);
        handler.removeMessages(7);
        handler.removeMessages(8);
        handler.removeMessages(9);
        handler.removeMessages(10);
        handler.removeMessages(11);
        handler.removeMessages(12);
        handler.removeMessages(13);
        handler.removeMessages(14);
        handler.removeMessages(15);
        handler.removeMessages(16);
        handler.removeMessages(17);
    }
}
