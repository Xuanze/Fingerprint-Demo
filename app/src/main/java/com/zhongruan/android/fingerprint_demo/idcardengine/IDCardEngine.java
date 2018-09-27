package com.zhongruan.android.fingerprint_demo.idcardengine;

import com.zhongruan.android.fingerprint_demo.fingerprintengine.IDetectable;

public abstract class IDCardEngine implements IDetectable {
    protected int error;
    protected int type;

    public abstract int freeEngine();

    public abstract int initEngine();

    public abstract IDCardData startScanIdCard();

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
