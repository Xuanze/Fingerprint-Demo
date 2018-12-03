package com.zhongruan.android.fingerprint_demo.camera;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhongruan.android.fingerprint_demo.camera.util.CamParaUtil;
import com.zhongruan.android.fingerprint_demo.camera.util.Util;
import com.zhongruan.android.fingerprint_demo.ui.ConfigApplication;
import com.zhongruan.android.fingerprint_demo.utils.Utils;

import java.io.IOException;
import java.util.List;

public class CameraInterface {
    private static final String TAG = "HNZR";
    private Camera mCamera;
    private Camera.Parameters mParams;
    public boolean isPreviewing = false;
    private int mCameraId = -1;
    private static CameraInterface mCameraInterface;
    public Bitmap rectBitmap;
    public int cameraRotateSys;

    private CameraInterface() {
    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * 打开Camera
     */
    public void doOpenCamera(int cameraId) {
        Log.i(TAG, "Camera open....");
        mCamera = Camera.open(cameraId);
        mCameraId = cameraId;
    }

    /**
     * 开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate, int cameraRotateSys) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);//设置拍照后存储的图片格式
            CamParaUtil.getInstance().printSupportPictureSize(mParams);
            CamParaUtil.getInstance().printSupportPreviewSize(mParams);
            //设置PreviewSize和PictureSize
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setDisplayOrientation(cameraRotateSys);
            CamParaUtil.getInstance().printSupportFocusMode(mParams);
            String str = ConfigApplication.getApplication().getCameraExposure();
            mParams.setExposureCompensation(0);
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            try {
                mParams.setExposureCompensation(Integer.parseInt(str));
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();//开启预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isPreviewing = true;
            mParams = mCamera.getParameters(); //重新get一次
            Log.i(TAG, "最终设置:PreviewSize--With = " + mParams.getPreviewSize().width + "Height = " + mParams.getPreviewSize().height);
            Log.i(TAG, "最终设置:PictureSize--With = " + mParams.getPictureSize().width + "Height = " + mParams.getPictureSize().height);
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    public void cameraSwitch(SurfaceView surfaceView) {
        if (Build.MODEL.equals(Utils.DEVICETYPE_YLT1)) {
            int newId = (CameraInterface.getInstance().getCameraId() + 1) % 2;
            CameraInterface.getInstance().doStopCamera();
            CameraInterface.getInstance().doOpenCamera(newId);
            CameraInterface.getInstance().doStartPreview(surfaceView.getHolder(), 1.333f, 0);
        } else if (Build.MODEL.equals(Utils.DEVICETYPE_YLT2)) {
            if (this.isPreviewing && this.mCamera != null) {
                if (this.cameraRotateSys < 180) {
                    this.cameraRotateSys += 180;
                    this.mCameraId = 1;
                } else {
                    this.mCameraId = 0;
                    this.cameraRotateSys -= 180;
                }
                this.mCamera.stopPreview();
                this.mParams.setRotation(this.cameraRotateSys);
                this.mCamera.setDisplayOrientation(this.cameraRotateSys);
                this.mCamera.startPreview();
            }
        }
    }

    /**
     * 获取Camera.Parameters
     *
     * @return
     */
    public Camera.Parameters getCameraParams() {
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            return mParams;
        }
        return null;
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    public Camera getCameraDevice() {
        return mCamera;
    }

    public int getCameraId() {
        return mCameraId;
    }
}
