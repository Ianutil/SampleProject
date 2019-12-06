package com.function.ianchang.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.function.ianchang.simplegreendao.ActivityInfo.PageInfoConverter;
import com.function.ianchang.simplegreendao.ActivityInfo.ScheduleInfoConverter;
import java.util.List;

import com.function.ianchang.simplegreendao.ActivityInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACTIVITY_INFO".
*/
public class ActivityInfoDao extends AbstractDao<ActivityInfo, Void> {

    public static final String TABLENAME = "ACTIVITY_INFO";

    /**
     * Properties of entity ActivityInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ActivityCode = new Property(0, String.class, "activityCode", false, "ACTIVITY_CODE");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property ScheduleInfos = new Property(2, String.class, "scheduleInfos", false, "SCHEDULE_INFOS");
        public final static Property PageInfos = new Property(3, String.class, "pageInfos", false, "PAGE_INFOS");
    }

    private final ScheduleInfoConverter scheduleInfosConverter = new ScheduleInfoConverter();
    private final PageInfoConverter pageInfosConverter = new PageInfoConverter();

    public ActivityInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ActivityInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACTIVITY_INFO\" (" + //
                "\"ACTIVITY_CODE\" TEXT UNIQUE ," + // 0: activityCode
                "\"NAME\" TEXT," + // 1: name
                "\"SCHEDULE_INFOS\" TEXT," + // 2: scheduleInfos
                "\"PAGE_INFOS\" TEXT);"); // 3: pageInfos
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACTIVITY_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ActivityInfo entity) {
        stmt.clearBindings();
 
        String activityCode = entity.getActivityCode();
        if (activityCode != null) {
            stmt.bindString(1, activityCode);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        List scheduleInfos = entity.getScheduleInfos();
        if (scheduleInfos != null) {
            stmt.bindString(3, scheduleInfosConverter.convertToDatabaseValue(scheduleInfos));
        }
 
        List pageInfos = entity.getPageInfos();
        if (pageInfos != null) {
            stmt.bindString(4, pageInfosConverter.convertToDatabaseValue(pageInfos));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ActivityInfo entity) {
        stmt.clearBindings();
 
        String activityCode = entity.getActivityCode();
        if (activityCode != null) {
            stmt.bindString(1, activityCode);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        List scheduleInfos = entity.getScheduleInfos();
        if (scheduleInfos != null) {
            stmt.bindString(3, scheduleInfosConverter.convertToDatabaseValue(scheduleInfos));
        }
 
        List pageInfos = entity.getPageInfos();
        if (pageInfos != null) {
            stmt.bindString(4, pageInfosConverter.convertToDatabaseValue(pageInfos));
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ActivityInfo readEntity(Cursor cursor, int offset) {
        ActivityInfo entity = new ActivityInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // activityCode
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : scheduleInfosConverter.convertToEntityProperty(cursor.getString(offset + 2)), // scheduleInfos
            cursor.isNull(offset + 3) ? null : pageInfosConverter.convertToEntityProperty(cursor.getString(offset + 3)) // pageInfos
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ActivityInfo entity, int offset) {
        entity.setActivityCode(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setScheduleInfos(cursor.isNull(offset + 2) ? null : scheduleInfosConverter.convertToEntityProperty(cursor.getString(offset + 2)));
        entity.setPageInfos(cursor.isNull(offset + 3) ? null : pageInfosConverter.convertToEntityProperty(cursor.getString(offset + 3)));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ActivityInfo entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ActivityInfo entity) {
        return null;
    }

    @Override
    public boolean hasKey(ActivityInfo entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
