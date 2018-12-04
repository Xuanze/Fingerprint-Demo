package com.zhongruan.android.fingerprint_demo.utils;

import android.app.Application;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.zhongruan.android.fingerprint_demo.R;
import com.zhongruan.android.fingerprint_demo.ui.MyApplication;


public class PlaySoundUtils {
    public static final int SOUND_AUTH_OK = 101;
    public static final int SOUND_AUTH_SUCCESS = 102;
    public static final int SOUND_AUTH_WAIT = 103;
    public static final int SOUND_BEEP = 104;
    public static final int SOUND_BEEP_FAIL = 105;
    public static final int SOUND_BEEP_SUCCESS = 106;
    public static final int SOUND_COLLECT_OK = 107;
    public static final int SOUND_FINGER = 108;
    public static final int SOUND_FINGER_AGAIN = 114;
    public static final int SOUND_FINGER_CHANGE = 109;
    public static final int SOUND_FINGER_LEFT = 110;
    public static final int SOUND_FINGER_RIGHT = 111;
    public static final int SOUND_IDCARD = 112;
    public static final int SOUND_MANUAL_VERIFICATION = 115;
    public static final int SOUND_SUCCESS_TIP = 116;
    public static final int SOUND_TAKE_PICTURE = 113;
    private static SoundPool soundPool;
    private static SparseIntArray soundPoolMap;

    private static void addSound() {
        soundPool = new SoundPool(10, 3, 100);
        soundPoolMap = new SparseIntArray();
        soundPoolMap.put(101, soundPool.load(MyApplication.getApplication(), R.raw.beep, 1));
        soundPoolMap.put(102, soundPool.load(MyApplication.getApplication(), R.raw.finger, 1));
        soundPoolMap.put(103, soundPool.load(MyApplication.getApplication(), R.raw.idcard, 1));
        soundPoolMap.put(104, soundPool.load(MyApplication.getApplication(), R.raw.identify_face, 1));
        soundPoolMap.put(105, soundPool.load(MyApplication.getApplication(), R.raw.identify_face_failed, 1));
        soundPoolMap.put(106, soundPool.load(MyApplication.getApplication(), R.raw.identify_succeeded, 1));
    }

    public static void init() {
        addSound();
    }

    public static void playSound(int paramInt) {
        if ((soundPool == null) || (soundPoolMap == null))
            init();
        soundPool.play(soundPoolMap.get(paramInt), 1.0F, 1.0F, 0, 0, 1.0F);
    }

    public static void playSoundWithState(int paramInt1, int paramInt2, int paramInt3) {
        if (paramInt3 <= paramInt2) {
            if ((soundPool == null) || (soundPoolMap == null)){
                init();
            }
            soundPool.play(soundPoolMap.get(paramInt1), 1.0F, 1.0F, 0, 0, 1.0F);
        }
    }
}