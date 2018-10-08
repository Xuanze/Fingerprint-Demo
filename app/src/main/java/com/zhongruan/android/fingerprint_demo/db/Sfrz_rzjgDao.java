package com.zhongruan.android.fingerprint_demo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhongruan.android.fingerprint_demo.db.entity.Sfrz_rzjg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "sfrz_rzjg".
*/
public class Sfrz_rzjgDao extends AbstractDao<Sfrz_rzjg, Long> {

    public static final String TABLENAME = "sfrz_rzjg";

    /**
     * Properties of entity Sfrz_rzjg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Jgid = new Property(0, Long.class, "jgid", true, "jgid");
        public final static Property Rzjgid = new Property(1, String.class, "rzjgid", false, "rzjgid");
        public final static Property Rzjg_ztid = new Property(2, String.class, "rzjg_ztid", false, "rzjg_ztid");
        public final static Property Rzjg_ksno = new Property(3, String.class, "rzjg_ksno", false, "rzjg_ksno");
        public final static Property Rzjg_kmno = new Property(4, String.class, "rzjg_kmno", false, "rzjg_kmno");
        public final static Property Rzjg_kdno = new Property(5, String.class, "rzjg_kdno", false, "rzjg_kdno");
        public final static Property Rzjg_kcno = new Property(6, String.class, "rzjg_kcno", false, "rzjg_kcno");
        public final static Property Rzjg_zwh = new Property(7, String.class, "rzjg_zwh", false, "rzjg_zwh");
        public final static Property Rzjg_device = new Property(8, String.class, "rzjg_device", false, "rzjg_device");
        public final static Property Rzjg_time = new Property(9, String.class, "rzjg_time", false, "rzjg_time");
        public final static Property Rzjg_a = new Property(10, String.class, "rzjg_a", false, "rzjg_a");
        public final static Property Rzjg_b = new Property(11, String.class, "rzjg_b", false, "rzjg_b");
        public final static Property Rzjg_sb = new Property(12, String.class, "rzjg_sb", false, "rzjg_sb");
    }


    public Sfrz_rzjgDao(DaoConfig config) {
        super(config);
    }
    
    public Sfrz_rzjgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"sfrz_rzjg\" (" + //
                "\"jgid\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: jgid
                "\"rzjgid\" TEXT," + // 1: rzjgid
                "\"rzjg_ztid\" TEXT," + // 2: rzjg_ztid
                "\"rzjg_ksno\" TEXT," + // 3: rzjg_ksno
                "\"rzjg_kmno\" TEXT," + // 4: rzjg_kmno
                "\"rzjg_kdno\" TEXT," + // 5: rzjg_kdno
                "\"rzjg_kcno\" TEXT," + // 6: rzjg_kcno
                "\"rzjg_zwh\" TEXT," + // 7: rzjg_zwh
                "\"rzjg_device\" TEXT," + // 8: rzjg_device
                "\"rzjg_time\" TEXT," + // 9: rzjg_time
                "\"rzjg_a\" TEXT," + // 10: rzjg_a
                "\"rzjg_b\" TEXT," + // 11: rzjg_b
                "\"rzjg_sb\" TEXT);"); // 12: rzjg_sb
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"sfrz_rzjg\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Sfrz_rzjg entity) {
        stmt.clearBindings();
 
        Long jgid = entity.getJgid();
        if (jgid != null) {
            stmt.bindLong(1, jgid);
        }
 
        String rzjgid = entity.getRzjgid();
        if (rzjgid != null) {
            stmt.bindString(2, rzjgid);
        }
 
        String rzjg_ztid = entity.getRzjg_ztid();
        if (rzjg_ztid != null) {
            stmt.bindString(3, rzjg_ztid);
        }
 
        String rzjg_ksno = entity.getRzjg_ksno();
        if (rzjg_ksno != null) {
            stmt.bindString(4, rzjg_ksno);
        }
 
        String rzjg_kmno = entity.getRzjg_kmno();
        if (rzjg_kmno != null) {
            stmt.bindString(5, rzjg_kmno);
        }
 
        String rzjg_kdno = entity.getRzjg_kdno();
        if (rzjg_kdno != null) {
            stmt.bindString(6, rzjg_kdno);
        }
 
        String rzjg_kcno = entity.getRzjg_kcno();
        if (rzjg_kcno != null) {
            stmt.bindString(7, rzjg_kcno);
        }
 
        String rzjg_zwh = entity.getRzjg_zwh();
        if (rzjg_zwh != null) {
            stmt.bindString(8, rzjg_zwh);
        }
 
        String rzjg_device = entity.getRzjg_device();
        if (rzjg_device != null) {
            stmt.bindString(9, rzjg_device);
        }
 
        String rzjg_time = entity.getRzjg_time();
        if (rzjg_time != null) {
            stmt.bindString(10, rzjg_time);
        }
 
        String rzjg_a = entity.getRzjg_a();
        if (rzjg_a != null) {
            stmt.bindString(11, rzjg_a);
        }
 
        String rzjg_b = entity.getRzjg_b();
        if (rzjg_b != null) {
            stmt.bindString(12, rzjg_b);
        }
 
        String rzjg_sb = entity.getRzjg_sb();
        if (rzjg_sb != null) {
            stmt.bindString(13, rzjg_sb);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Sfrz_rzjg entity) {
        stmt.clearBindings();
 
        Long jgid = entity.getJgid();
        if (jgid != null) {
            stmt.bindLong(1, jgid);
        }
 
        String rzjgid = entity.getRzjgid();
        if (rzjgid != null) {
            stmt.bindString(2, rzjgid);
        }
 
        String rzjg_ztid = entity.getRzjg_ztid();
        if (rzjg_ztid != null) {
            stmt.bindString(3, rzjg_ztid);
        }
 
        String rzjg_ksno = entity.getRzjg_ksno();
        if (rzjg_ksno != null) {
            stmt.bindString(4, rzjg_ksno);
        }
 
        String rzjg_kmno = entity.getRzjg_kmno();
        if (rzjg_kmno != null) {
            stmt.bindString(5, rzjg_kmno);
        }
 
        String rzjg_kdno = entity.getRzjg_kdno();
        if (rzjg_kdno != null) {
            stmt.bindString(6, rzjg_kdno);
        }
 
        String rzjg_kcno = entity.getRzjg_kcno();
        if (rzjg_kcno != null) {
            stmt.bindString(7, rzjg_kcno);
        }
 
        String rzjg_zwh = entity.getRzjg_zwh();
        if (rzjg_zwh != null) {
            stmt.bindString(8, rzjg_zwh);
        }
 
        String rzjg_device = entity.getRzjg_device();
        if (rzjg_device != null) {
            stmt.bindString(9, rzjg_device);
        }
 
        String rzjg_time = entity.getRzjg_time();
        if (rzjg_time != null) {
            stmt.bindString(10, rzjg_time);
        }
 
        String rzjg_a = entity.getRzjg_a();
        if (rzjg_a != null) {
            stmt.bindString(11, rzjg_a);
        }
 
        String rzjg_b = entity.getRzjg_b();
        if (rzjg_b != null) {
            stmt.bindString(12, rzjg_b);
        }
 
        String rzjg_sb = entity.getRzjg_sb();
        if (rzjg_sb != null) {
            stmt.bindString(13, rzjg_sb);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Sfrz_rzjg readEntity(Cursor cursor, int offset) {
        Sfrz_rzjg entity = new Sfrz_rzjg( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // jgid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // rzjgid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // rzjg_ztid
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // rzjg_ksno
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // rzjg_kmno
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // rzjg_kdno
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // rzjg_kcno
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // rzjg_zwh
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // rzjg_device
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // rzjg_time
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // rzjg_a
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // rzjg_b
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // rzjg_sb
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Sfrz_rzjg entity, int offset) {
        entity.setJgid(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRzjgid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRzjg_ztid(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRzjg_ksno(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRzjg_kmno(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRzjg_kdno(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRzjg_kcno(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setRzjg_zwh(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRzjg_device(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRzjg_time(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRzjg_a(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRzjg_b(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRzjg_sb(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Sfrz_rzjg entity, long rowId) {
        entity.setJgid(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Sfrz_rzjg entity) {
        if(entity != null) {
            return entity.getJgid();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Sfrz_rzjg entity) {
        return entity.getJgid() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
