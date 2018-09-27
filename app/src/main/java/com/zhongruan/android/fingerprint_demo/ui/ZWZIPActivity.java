package com.zhongruan.android.fingerprint_demo.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.base.BaseActivity;
import com.zhongruan.android.fingerprint_demo.fingerprintengine.FingerData;
import com.zhongruan.android.fingerprint_demo.utils.ABLSynCallback;
import com.zhongruan.android.fingerprint_demo.utils.FileUtils;
import com.zhongruan.android.fingerprint_demo.utils.LogUtil;
import com.zhongruan.android.fingerprint_demo.utils.Utils;
import com.zhongruan.android.fingerprint_demo.zip.archiver.ArchiverManager;
import com.zhongruan.android.fingerprint_demo.zip.archiver.IArchiverListener;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;

import static com.zhongruan.android.fingerprint_demo.utils.FileUtils.getAppSavePath;
import static com.zhongruan.android.fingerprint_demo.utils.Utils.getUSBPath;

/**
 * Created by LHJ on 2018/1/31.
 */

public class ZWZIPActivity extends BaseActivity {
    private Handler handler = new Handler();
    private FingerData fingerData;
    private ImageView mImageZip;
    private boolean isError;
    private List<String> tempList;
    private String NewPith = getAppSavePath() + "/DataTemp/";
    private String NewFilePath, NewFile, hint;
    private ProgressDialog dialog;
    private String zw = "G4kVyIAnAAgAAA0ECQQAEgsAFAAGCXIwQAgG7mzABwZ+fGAPCHikYAoFC8EADwfn2OAEB4fgwBgLhBVBEQYGYEEYFwaIYRkWgpGhEQsQxAEaEI0BQhcPbwhCCwdhDIIFCmlgogsNUryiBAiD3MIUCyfpYhwJThKDCAtLHCMCDB8iIxUXhzsjFByeTsMYET2PQwkMhY4jExxAmMMDCKmaYxotIMODEhAR1qMVEZX/IxMPGAIkEBRCPAQABhZohBIGOtgkAQpy4OQZFQIMeQMPgNFjGgEBMTcwsTOurDE0tqxJTLBGtrGwtbLKRLStSdPFr+mtwm52AZKfjZwlb29ybGVfXFddW01MSj1GOTA7JS8eOB00GhYnFxAtLRAcFxFAIUY+JCklpSifniEpo5kyNJksl56moayRH6+uAIKAt3Wzg9LeVNKqxapEhX6QiINzd2yAXl5OSVBBO1NkSl0lHktjHh8RRR5aBx8THSlqJGoiAS4=";
    private Button button_zip;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_zwzip);
    }

    @Override
    public void initViews() {
        mImageZip = findViewById(R.id.image_zip);
        button_zip = findViewById(R.id.button_zip);
        dialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("解压中，请稍候...");
        dialog.setTitle("解压文件");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    public void initListeners() {
        button_zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utils.isFastClick()) {
                    button_zip.setEnabled(false);
                    handler.postDelayed(runnable02, 500);// 间隔1秒
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private Runnable runnable02 = new Runnable() {
        public void run() {
            fingerData = MyApplication.getYltFingerEngine().fingerCollect();
            if (fingerData != null) {
                playBeep();
                ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
                    @Override
                    public Object callback() {
                        if (MyApplication.getYltFingerEngine().fingerVerify(Base64.decode(zw, 2), fingerData.getFingerFeatures()) == 1) {
                            return Boolean.valueOf(true);
                        } else {
                            return Boolean.valueOf(false);
                        }
                    }
                }, new ABLSynCallback.ForegroundCall() {
                    @Override
                    public void callback(Object obj) {
                        if (((Boolean) obj).booleanValue()) {
                            unzipfile();
                            handler.removeCallbacks(runnable02);
                        } else {
                            handler.postDelayed(runnable02, 500);// 间隔1秒
                        }
                    }
                });
            } else {
                handler.postDelayed(runnable02, 500);// 间隔1秒
            }
        }
    };

    private void playBeep() {
        SoundPool soundPool = new SoundPool(10, 3, 100);
        soundPool.load(ZWZIPActivity.this, R.raw.beep, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                soundPool.play(1,  //声音id
                        1, //左声道
                        1, //右声道
                        0, //优先级
                        0, // 0表示不循环，-1表示循环播放
                        1);//播放比率，0.5~2，一般为1
            }
        });
    }

    private void unzipfile() {
        isError = false;
        tempList = Utils.checkUSBZip(getUSBPath(), null);
        LogUtil.i("HNZR", tempList);
        if (!Utils.checkUSBInserted()) {
            isError = true;
            hint = "U盘未插入";
            LogUtil.i("U盘未插入");
            button_zip.setEnabled(true);
        } else {
            copyFile();
            button_zip.setEnabled(true);
        }
        if (isError) {
            ShowHintDialog(ZWZIPActivity.this, hint, "U盘导入数据", R.drawable.img_base_icon_error, "知道了", false);
        }
    }

    private void copyFile() {
        ABLSynCallback.call(new ABLSynCallback.BackgroundCall() {
            public Object callback() {
                if (tempList == null || tempList.size() <= 0) {
                    return Boolean.valueOf(false);
                }
                return Boolean.valueOf(FileUtils.copyFile(tempList));
            }
        }, new ABLSynCallback.ForegroundCall() {
            public void callback(Object obj) {
                if (((Boolean) obj).booleanValue()) {
                    NewFile = StringUtils.substringAfterLast(tempList.get(0), "/");
                    NewFilePath = StringUtils.substringBeforeLast(NewFile, ".");
                    unZipFile(NewPith + NewFile, NewPith + NewFilePath, "mst");
                } else {
                    isError = true;
                    hint = "复制文件失败！";
                    return;
                }
            }
        });
    }

    private void unZipFile(String srcfile, final String unrarPath, String password) {
        ArchiverManager.getInstance().doUnArchiver(srcfile, unrarPath, password, new IArchiverListener() {
            @Override
            public void onStartArchiver() {
                dialog.show();
            }

            @Override
            public void onProgressArchiver(int current, int total) {
                dialog.setMax(total);
                dialog.setProgress(current);
            }

            @Override
            public void onEndArchiver() {
                dialog.dismiss();
                listFileTxt(new File(unrarPath));
            }
        });
    }

    private void listFileTxt(File file) {
        File[] files = file.listFiles();
        try {
            for (File f : files) {
                if (!f.isDirectory()) {
                    if (f.getName().endsWith(".jpg")) {
                        LogUtil.i("HNZR", f.getPath());
                        Picasso.with(this).load("file://" + f.getPath()).into(mImageZip);
                    }
                } else if (f.isDirectory()) {
                    //如果是目录，迭代进入该目录
                    listFileTxt(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fingerData = null;
        handler.removeCallbacks(runnable02);
    }
}
