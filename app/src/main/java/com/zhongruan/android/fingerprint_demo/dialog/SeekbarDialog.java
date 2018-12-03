package com.zhongruan.android.fingerprint_demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.ui.ConfigApplication;

public class SeekbarDialog extends Dialog implements View.OnClickListener {

    private TextView mText3;
    private TextView mText1;
    private SeekBar mSeekbar1;
    private TextView mText2;

    /**
     * 确定
     */
    private Button mPositiveButton;
    /**
     * 取消
     */
    private Button mNegativeButton;
    private OnCloseListener listener;


    public SeekbarDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pad_dialog_seekbar_layout);
        initView();
        String str = ConfigApplication.getApplication().getCameraExposure();
        mText1.setText("暗(-3)");
        mText2.setText("亮(3)");
        mSeekbar1.setMax(6);
        mText3.setText(str);
        mSeekbar1.setProgress(3 + objToInt(str, 0).intValue());
        mSeekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                mText3.setText(paramAnonymousInt - 3 + "");
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
            }
        });
    }

    private void initView() {
        mText3 = findViewById(R.id.text3);
        mText1 = findViewById(R.id.text1);
        mSeekbar1 = findViewById(R.id.seekbar1);
        mText2 = findViewById(R.id.text2);
        mPositiveButton = findViewById(R.id.positiveButton);
        mPositiveButton.setOnClickListener(this);
        mNegativeButton = findViewById(R.id.negativeButton);
        mNegativeButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.positiveButton:
                if (listener != null) {
                    listener.onClick(this, mText3.getText().toString(), true);
                }
                break;
            case R.id.negativeButton:
                if (listener != null) {
                    listener.onClick(this, "", false);
                }
                break;
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, String str, boolean confirm);
    }

    private Integer objToInt(Object paramObject, int paramInt) {
        if (paramObject == null)
            return Integer.valueOf(paramInt);
        try {
            Integer localInteger = Integer.valueOf(Integer.parseInt((paramObject + "").trim()));
            return localInteger;
        } catch (Exception localException) {
        }
        return Integer.valueOf(paramInt);
    }
}