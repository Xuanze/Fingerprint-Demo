package com.zhongruan.android.fingerprint_demo.camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public SurfaceHolder mSurfaceHolder;
    Context mContext;

    public CameraSurfaceView(Context context, SurfaceView surfaceView) {
        super(context);
        this.mContext = context;
        this.mSurfaceHolder = surfaceView.getHolder();
        this.mSurfaceHolder.setFormat(-2);
        this.mSurfaceHolder.setType(3);
        this.mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraInterface.getInstance().doStartPreview(mSurfaceHolder, 1.333f, 0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraInterface.getInstance().doStopCamera();
    }
}
