package com.zhongruan.android.fingerprint_demo.db;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ksxp;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks_cjxx;
import com.zhongruan.android.fingerprint_demo.db.entity.Bk_ks_temp;
import com.zhongruan.android.fingerprint_demo.db.entity.Kstz_zw;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_cc;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kc;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_kd;
import com.zhongruan.android.fingerprint_demo.db.entity.Ks_km;
import com.zhongruan.android.fingerprint_demo.db.entity.Rz_ks_zw;
import com.zhongruan.android.fingerprint_demo.db.entity.Sb_setting;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzfs;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjg;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjl;
import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzzt;

import com.zhongruan.android.fingerprint_demo.db.Bk_ksDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ksxpDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ks_cjxxDao;
import com.zhongruan.android.fingerprint_demo.db.Bk_ks_tempDao;
import com.zhongruan.android.fingerprint_demo.db.Kstz_zwDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_ccDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kcDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kdDao;
import com.zhongruan.android.fingerprint_demo.db.Ks_kmDao;
import com.zhongruan.android.fingerprint_demo.db.Rz_ks_zwDao;
import com.zhongruan.android.fingerprint_demo.db.Sb_settingDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzfsDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzjgDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzjlDao;
import com.zhongruan.android.fingerprint_demo.db.Sfrz_rzztDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bk_ksDaoConfig;
    private final DaoConfig bk_ksxpDaoConfig;
    private final DaoConfig bk_ks_cjxxDaoConfig;
    private final DaoConfig bk_ks_tempDaoConfig;
    private final DaoConfig kstz_zwDaoConfig;
    private final DaoConfig ks_ccDaoConfig;
    private final DaoConfig ks_kcDaoConfig;
    private final DaoConfig ks_kdDaoConfig;
    private final DaoConfig ks_kmDaoConfig;
    private final DaoConfig rz_ks_zwDaoConfig;
    private final DaoConfig sb_settingDaoConfig;
    private final DaoConfig sfrz_rzfsDaoConfig;
    private final DaoConfig sfrz_rzjgDaoConfig;
    private final DaoConfig sfrz_rzjlDaoConfig;
    private final DaoConfig sfrz_rzztDaoConfig;

    private final Bk_ksDao bk_ksDao;
    private final Bk_ksxpDao bk_ksxpDao;
    private final Bk_ks_cjxxDao bk_ks_cjxxDao;
    private final Bk_ks_tempDao bk_ks_tempDao;
    private final Kstz_zwDao kstz_zwDao;
    private final Ks_ccDao ks_ccDao;
    private final Ks_kcDao ks_kcDao;
    private final Ks_kdDao ks_kdDao;
    private final Ks_kmDao ks_kmDao;
    private final Rz_ks_zwDao rz_ks_zwDao;
    private final Sb_settingDao sb_settingDao;
    private final Sfrz_rzfsDao sfrz_rzfsDao;
    private final Sfrz_rzjgDao sfrz_rzjgDao;
    private final Sfrz_rzjlDao sfrz_rzjlDao;
    private final Sfrz_rzztDao sfrz_rzztDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bk_ksDaoConfig = daoConfigMap.get(Bk_ksDao.class).clone();
        bk_ksDaoConfig.initIdentityScope(type);

        bk_ksxpDaoConfig = daoConfigMap.get(Bk_ksxpDao.class).clone();
        bk_ksxpDaoConfig.initIdentityScope(type);

        bk_ks_cjxxDaoConfig = daoConfigMap.get(Bk_ks_cjxxDao.class).clone();
        bk_ks_cjxxDaoConfig.initIdentityScope(type);

        bk_ks_tempDaoConfig = daoConfigMap.get(Bk_ks_tempDao.class).clone();
        bk_ks_tempDaoConfig.initIdentityScope(type);

        kstz_zwDaoConfig = daoConfigMap.get(Kstz_zwDao.class).clone();
        kstz_zwDaoConfig.initIdentityScope(type);

        ks_ccDaoConfig = daoConfigMap.get(Ks_ccDao.class).clone();
        ks_ccDaoConfig.initIdentityScope(type);

        ks_kcDaoConfig = daoConfigMap.get(Ks_kcDao.class).clone();
        ks_kcDaoConfig.initIdentityScope(type);

        ks_kdDaoConfig = daoConfigMap.get(Ks_kdDao.class).clone();
        ks_kdDaoConfig.initIdentityScope(type);

        ks_kmDaoConfig = daoConfigMap.get(Ks_kmDao.class).clone();
        ks_kmDaoConfig.initIdentityScope(type);

        rz_ks_zwDaoConfig = daoConfigMap.get(Rz_ks_zwDao.class).clone();
        rz_ks_zwDaoConfig.initIdentityScope(type);

        sb_settingDaoConfig = daoConfigMap.get(Sb_settingDao.class).clone();
        sb_settingDaoConfig.initIdentityScope(type);

        sfrz_rzfsDaoConfig = daoConfigMap.get(Sfrz_rzfsDao.class).clone();
        sfrz_rzfsDaoConfig.initIdentityScope(type);

        sfrz_rzjgDaoConfig = daoConfigMap.get(Sfrz_rzjgDao.class).clone();
        sfrz_rzjgDaoConfig.initIdentityScope(type);

        sfrz_rzjlDaoConfig = daoConfigMap.get(Sfrz_rzjlDao.class).clone();
        sfrz_rzjlDaoConfig.initIdentityScope(type);

        sfrz_rzztDaoConfig = daoConfigMap.get(Sfrz_rzztDao.class).clone();
        sfrz_rzztDaoConfig.initIdentityScope(type);

        bk_ksDao = new Bk_ksDao(bk_ksDaoConfig, this);
        bk_ksxpDao = new Bk_ksxpDao(bk_ksxpDaoConfig, this);
        bk_ks_cjxxDao = new Bk_ks_cjxxDao(bk_ks_cjxxDaoConfig, this);
        bk_ks_tempDao = new Bk_ks_tempDao(bk_ks_tempDaoConfig, this);
        kstz_zwDao = new Kstz_zwDao(kstz_zwDaoConfig, this);
        ks_ccDao = new Ks_ccDao(ks_ccDaoConfig, this);
        ks_kcDao = new Ks_kcDao(ks_kcDaoConfig, this);
        ks_kdDao = new Ks_kdDao(ks_kdDaoConfig, this);
        ks_kmDao = new Ks_kmDao(ks_kmDaoConfig, this);
        rz_ks_zwDao = new Rz_ks_zwDao(rz_ks_zwDaoConfig, this);
        sb_settingDao = new Sb_settingDao(sb_settingDaoConfig, this);
        sfrz_rzfsDao = new Sfrz_rzfsDao(sfrz_rzfsDaoConfig, this);
        sfrz_rzjgDao = new Sfrz_rzjgDao(sfrz_rzjgDaoConfig, this);
        sfrz_rzjlDao = new Sfrz_rzjlDao(sfrz_rzjlDaoConfig, this);
        sfrz_rzztDao = new Sfrz_rzztDao(sfrz_rzztDaoConfig, this);

        registerDao(Bk_ks.class, bk_ksDao);
        registerDao(Bk_ksxp.class, bk_ksxpDao);
        registerDao(Bk_ks_cjxx.class, bk_ks_cjxxDao);
        registerDao(Bk_ks_temp.class, bk_ks_tempDao);
        registerDao(Kstz_zw.class, kstz_zwDao);
        registerDao(Ks_cc.class, ks_ccDao);
        registerDao(Ks_kc.class, ks_kcDao);
        registerDao(Ks_kd.class, ks_kdDao);
        registerDao(Ks_km.class, ks_kmDao);
        registerDao(Rz_ks_zw.class, rz_ks_zwDao);
        registerDao(Sb_setting.class, sb_settingDao);
        registerDao(Sfrz_rzfs.class, sfrz_rzfsDao);
        registerDao(Sfrz_rzjg.class, sfrz_rzjgDao);
        registerDao(Sfrz_rzjl.class, sfrz_rzjlDao);
        registerDao(Sfrz_rzzt.class, sfrz_rzztDao);
    }
    
    public void clear() {
        bk_ksDaoConfig.clearIdentityScope();
        bk_ksxpDaoConfig.clearIdentityScope();
        bk_ks_cjxxDaoConfig.clearIdentityScope();
        bk_ks_tempDaoConfig.clearIdentityScope();
        kstz_zwDaoConfig.clearIdentityScope();
        ks_ccDaoConfig.clearIdentityScope();
        ks_kcDaoConfig.clearIdentityScope();
        ks_kdDaoConfig.clearIdentityScope();
        ks_kmDaoConfig.clearIdentityScope();
        rz_ks_zwDaoConfig.clearIdentityScope();
        sb_settingDaoConfig.clearIdentityScope();
        sfrz_rzfsDaoConfig.clearIdentityScope();
        sfrz_rzjgDaoConfig.clearIdentityScope();
        sfrz_rzjlDaoConfig.clearIdentityScope();
        sfrz_rzztDaoConfig.clearIdentityScope();
    }

    public Bk_ksDao getBk_ksDao() {
        return bk_ksDao;
    }

    public Bk_ksxpDao getBk_ksxpDao() {
        return bk_ksxpDao;
    }

    public Bk_ks_cjxxDao getBk_ks_cjxxDao() {
        return bk_ks_cjxxDao;
    }

    public Bk_ks_tempDao getBk_ks_tempDao() {
        return bk_ks_tempDao;
    }

    public Kstz_zwDao getKstz_zwDao() {
        return kstz_zwDao;
    }

    public Ks_ccDao getKs_ccDao() {
        return ks_ccDao;
    }

    public Ks_kcDao getKs_kcDao() {
        return ks_kcDao;
    }

    public Ks_kdDao getKs_kdDao() {
        return ks_kdDao;
    }

    public Ks_kmDao getKs_kmDao() {
        return ks_kmDao;
    }

    public Rz_ks_zwDao getRz_ks_zwDao() {
        return rz_ks_zwDao;
    }

    public Sb_settingDao getSb_settingDao() {
        return sb_settingDao;
    }

    public Sfrz_rzfsDao getSfrz_rzfsDao() {
        return sfrz_rzfsDao;
    }

    public Sfrz_rzjgDao getSfrz_rzjgDao() {
        return sfrz_rzjgDao;
    }

    public Sfrz_rzjlDao getSfrz_rzjlDao() {
        return sfrz_rzjlDao;
    }

    public Sfrz_rzztDao getSfrz_rzztDao() {
        return sfrz_rzztDao;
    }

}
