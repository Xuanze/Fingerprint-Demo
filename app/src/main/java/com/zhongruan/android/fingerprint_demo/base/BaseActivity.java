package com.zhongruan.android.fingerprint_demo.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.dialog.HintDialog;

import java.util.HashMap;

/**
 * 基类
 */
public abstract class BaseActivity extends FragmentActivity {
    private Toast mToast;
    private HintDialog hintDialog;
    private boolean isExit;
    private ProgressDialog progressDialog;
    private BaseReceiver baseReceiver;
    public SoundPool soundPool;
    //定义一个HashMap用于存放音频流的ID
    public HashMap<Integer, Integer> musicId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        baseReceiver = new BaseReceiver();
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
        //通过load方法加载指定音频流，并将返回的音频ID放入musicId中
        musicId.put(1, soundPool.load(this, R.raw.beep, 1));
        musicId.put(2, soundPool.load(this, R.raw.finger, 1));
        musicId.put(3, soundPool.load(this, R.raw.idcard, 1));
        musicId.put(4, soundPool.load(this, R.raw.identify_face, 1));
        musicId.put(5, soundPool.load(this, R.raw.identify_face_failed, 1));
        musicId.put(6, soundPool.load(this, R.raw.identify_succeeded, 1));
        setContentView();
        initViews();
        initListeners();
        initData();
    }

    /**
     * 设置布局文件
     */
    public abstract void setContentView();

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initViews();

    /**
     * 初始化控件的监听
     */
    public abstract void initListeners();

    /**
     * 进行数据初始化
     * initData
     */
    public abstract void initData();

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("mess");
        registerReceiver(baseReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(baseReceiver);
        super.onPause();
    }
    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class BaseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess = intent.getExtras().getString("mess");
            ShowHintDialog(context, mess.substring(2, mess.length()).toString(), "温馨提示", R.drawable.img_base_check, "知道了", false);
        }
    }

    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
                case 1:
                    hintDialog.dismiss();
                    break;
            }
        }
    };

    public void ShowHintDialog(Context context, String hint, String title, int image, String buttonText, boolean isVisibility) {
        new HintDialog(context, R.style.dialog, hint, new HintDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                if (confirm) {
                    dialog.dismiss();
                }
            }
        }).setBackgroundResource(image).setNOVisibility(isVisibility).setLLButtonVisibility(true).setTitle(title).setPositiveButton(buttonText).show();
    }

    public void ShowHintDialog(Context context, String hint, String title, int image, int time, boolean isVisibility) {
        hintDialog = new HintDialog(context, R.style.dialog, hint).setBackgroundResource(image).setNOVisibility(isVisibility).setLLButtonVisibility(isVisibility).setTitle(title);
        hintDialog.show();
        mHandler.sendEmptyMessageDelayed(1, time);
    }

    public void showProgressDialog(Context context, Object message, boolean cancelable) {
        try {
            if (this.progressDialog == null) {
                this.progressDialog = new ProgressDialog(context, 0);
                if (message instanceof String) {
                    this.progressDialog.setMessage((String) message);
                } else if (message instanceof Integer) {
                    this.progressDialog.setMessage(getApplication().getResources().getString(((Integer) message).intValue()));
                } else {
                    this.progressDialog.setMessage(message.toString());
                }
                this.progressDialog.setCancelable(cancelable);
                this.progressDialog.setCanceledOnTouchOutside(cancelable);
                if (cancelable) {
                    progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == 4 && event.getAction() == 1) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            return true;
                        }
                    });
                }
                this.progressDialog.show();
            } else if (message instanceof String) {
                this.progressDialog.setMessage((String) message);
            } else if (message instanceof Integer) {
                this.progressDialog.setMessage(getApplication().getResources().getString(((Integer) message).intValue()));
            } else {
                this.progressDialog.setMessage(message.toString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
                this.progressDialog = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 点击两次退出程序
     */
    public void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            //参数用作状态码；根据惯例，非 0 的状态码表示异常终止。
            System.exit(0);
        }
    }
}
