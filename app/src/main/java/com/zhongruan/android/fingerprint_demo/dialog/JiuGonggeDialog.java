package com.zhongruan.android.fingerprint_demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.view.MyJiugonggeView;

public class JiuGonggeDialog extends Dialog implements MyJiugonggeView.OnPatternChangeListener {
    private Context mContext;
    private String pawsswordStr;
    private MyJiugonggeView jiuggongge_view;
    private OnCloseListener listener;

    public JiuGonggeDialog(Context context, int themeResId, String pawsswordStr, OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.pawsswordStr = pawsswordStr;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.pad_dialog_jiugongge, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        jiuggongge_view = findViewById(R.id.jiuggongge_view);
        jiuggongge_view.setOnPatternChangeListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onPatternChange(String patternPassword) {
        if (listener != null) {
            if (patternPassword.equals(pawsswordStr)) {
                listener.onClick(this, true);
            } else {
                listener.onClick(this, false);
            }
        }
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
