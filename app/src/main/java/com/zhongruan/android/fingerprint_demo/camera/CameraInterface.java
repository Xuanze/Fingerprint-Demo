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
import com.zhongruan.android.fingerprint_demo.config.ABLConfig;
import com.zhongruan.android.fingerprint_demo.ui.ConfigApplication;
import com.zhongruan.android.fingerprint_demo.ui.MyApplication;
import com.zhongruan.android.fingerprint_demo.utils.CommonUtil;
import com.zhongruan.android.fingerprint_demo.utils.PreferenceUtils;
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
    private int cameraCondition;
    private String cameraFaceSys;
    private int camereFrontId = -1;
    private int camereBackId = -1;
    public boolean FACING_FRONT = false;
    public boolean FACING_BACK = false;
    public boolean isChange = false;

    private CameraInterface() {
    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    private void initCameraInfo() {
        cameraFaceSys = "FRONT";
        cameraRotateSys = CommonUtil.objToInt(PreferenceUtils.getInstance(MyApplication.getApplication()).getPrefString(ABLConfig.CAMERA_DIRECTION, "0")).intValue();
        if (cameraRotateSys == 0) {
            isChange = false;
        } else {
            isChange = true;
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();
        for (int camId = 0; camId < cameraCount; camId = camId + 0x1) {
            Camera.getCameraInfo(camId, cameraInfo);
            if (cameraInfo.facing == 1) {
                camereFrontId = camId;
            }
            if (cameraInfo.facing == 0) {
                camereBackId = camId;
            }
        }
        if ((camereFrontId != -1) && (camereBackId != -0x1)) {
            cameraCondition = 3;
        } else if ((camereFrontId == -1) && (camereBackId == -1)) {
            cameraCondition = 0;
        } else if (camereBackId == -1) {
            cameraCondition = 1;
        } else {
            cameraCondition = 2;
        }
        if ((cameraFaceSys != null) && (!cameraFaceSys.equals(""))) {
            switch (cameraCondition) {
                case 0: {
                    Log.i("CameraInterface", "设备找不到任何摄像头");
                    return;
                }
                case 1: {
                    mCamera = Camera.open(camereFrontId);
                    FACING_FRONT = true;
                    FACING_BACK = false;
                    return;
                }
                case 2: {
                    mCamera = Camera.open(camereBackId);
                    FACING_BACK = true;
                    FACING_FRONT = false;
                    return;
                }
                case 3: {
                    if (cameraFaceSys.equals("FRONT")) {
                        mCamera = Camera.open(camereBackId);
                        FACING_BACK = true;
                        FACING_FRONT = false;
                        return;
                    }
                    if (cameraFaceSys.equals("BACK")) {
                        mCamera = Camera.open(camereFrontId);
                        FACING_FRONT = true;
                        FACING_BACK = false;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 打开Camera
     */
    public void doOpenCamera(CameraInterface.CamOpenOverCallback callback) {
        if (mCamera == null) {
            initCameraInfo();
        }
        callback.cameraHasOpened();
    }

    public interface CamOpenOverCallback {
        void cameraHasOpened();
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
            mParams.setExposureCompensation(Integer.parseInt(str));
            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            try {
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

    public void cameraSwitch() {
        if ((isPreviewing) && (mCamera != null)) {
            if (cameraRotateSys < 180) {
                cameraRotateSys = (cameraRotateSys + 180);
            } else {
                cameraRotateSys = (cameraRotateSys - 180);
            }
            mCamera.stopPreview();
            mParams.setRotation(cameraRotateSys);
            mCamera.setDisplayOrientation(cameraRotateSys);
            mCamera.startPreview();
            PreferenceUtils.getInstance(MyApplication.getApplication()).setPrefString(ABLConfig.CAMERA_DIRECTION, cameraRotateSys + "");
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
