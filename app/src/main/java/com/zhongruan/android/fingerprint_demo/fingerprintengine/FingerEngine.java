package com.zhongruan.android.fingerprint_demo.fingerprintengine;

public abstract class FingerEngine implements IDetectable {
    protected String collectType;

    public abstract boolean clear();

    public abstract FingerData fingerCollect();

    public abstract String fingerSearch(byte[] bArr);

    public abstract int freeEngine();

    public abstract int getEnrollCount();

    public abstract int importfinger(byte[] bArr, String str);

    public abstract int initEngine();

    public String getCollectType() {
        return this.collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }
}
