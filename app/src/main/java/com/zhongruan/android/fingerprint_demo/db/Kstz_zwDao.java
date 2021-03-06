package com.zhongruan.android.fingerprint_demo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhongruan.android.fingerprint_demo.db.entity.Kstz_zw;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "kstz_zw".
*/
public class Kstz_zwDao extends AbstractDao<Kstz_zw, Void> {

    public static final String TABLENAME = "kstz_zw";

    /**
     * Properties of entity Kstz_zw.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Ksno = new Property(0, String.class, "ksno", false, "ksno");
        public final static Property Xm = new Property(1, String.class, "xm", false, "xm");
        public final static Property Zjno = new Property(2, String.class, "zjno", false, "zjno");
        public final static Property Zw_position = new Property(3, String.class, "zw_position", false, "zw_position");
        public final static Property Zw_feature = new Property(4, String.class, "zw_feature", false, "zw_feature");
        public final static Property A = new Property(5, String.class, "a", false, "a");
        public final static Property B = new Property(6, String.class, "b", false, "b");
    }


    public Kstz_zwDao(DaoConfig config) {
        super(config);
    }
    
    public Kstz_zwDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"kstz_zw\" (" + //
                "\"ksno\" TEXT," + // 0: ksno
                "\"xm\" TEXT," + // 1: xm
                "\"zjno\" TEXT," + // 2: zjno
                "\"zw_position\" TEXT," + // 3: zw_position
                "\"zw_feature\" TEXT," + // 4: zw_feature
                "\"a\" TEXT," + // 5: a
                "\"b\" TEXT);"); // 6: b
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"kstz_zw\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Kstz_zw entity) {
        stmt.clearBindings();
 
        String ksno = entity.getKsno();
        if (ksno != null) {
            stmt.bindString(1, ksno);
        }
 
        String xm = entity.getXm();
        if (xm != null) {
            stmt.bindString(2, xm);
        }
 
        String zjno = entity.getZjno();
        if (zjno != null) {
            stmt.bindString(3, zjno);
        }
 
        String zw_position = entity.getZw_position();
        if (zw_position != null) {
            stmt.bindString(4, zw_position);
        }
 
        String zw_feature = entity.getZw_feature();
        if (zw_feature != null) {
            stmt.bindString(5, zw_feature);
        }
 
        String a = entity.getA();
        if (a != null) {
            stmt.bindString(6, a);
        }
 
        String b = entity.getB();
        if (b != null) {
            stmt.bindString(7, b);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Kstz_zw entity) {
        stmt.clearBindings();
 
        String ksno = entity.getKsno();
        if (ksno != null) {
            stmt.bindString(1, ksno);
        }
 
        String xm = entity.getXm();
        if (xm != null) {
            stmt.bindString(2, xm);
        }
 
        String zjno = entity.getZjno();
        if (zjno != null) {
            stmt.bindString(3, zjno);
        }
 
        String zw_position = entity.getZw_position();
        if (zw_position != null) {
            stmt.bindString(4, zw_position);
        }
 
        String zw_feature = entity.getZw_feature();
        if (zw_feature != null) {
            stmt.bindString(5, zw_feature);
        }
 
        String a = entity.getA();
        if (a != null) {
            stmt.bindString(6, a);
        }
 
        String b = entity.getB();
        if (b != null) {
            stmt.bindString(7, b);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Kstz_zw readEntity(Cursor cursor, int offset) {
        Kstz_zw entity = new Kstz_zw( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // ksno
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // xm
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // zjno
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // zw_position
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // zw_feature
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // a
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // b
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Kstz_zw entity, int offset) {
        entity.setKsno(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setXm(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setZjno(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setZw_position(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setZw_feature(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setA(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setB(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Kstz_zw entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Kstz_zw entity) {
        return null;
    }

    @Override
    public boolean hasKey(Kstz_zw entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
