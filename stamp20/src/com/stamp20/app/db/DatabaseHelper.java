package com.stamp20.app.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * TODO<数据库帮助类>
 * 
 * @author zhudewei
 * @data: 2014-12-31 下午3:38:32
 * @version: V1.0
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "stampdesign.db";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ShopInfoData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ShopInfoData.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // private Map<String, Dao> daos = new HashMap<String, Dao>();
    // private static DatabaseHelper instance;
    //
    // /**
    // * 单例获取该Helper
    // *
    // * @param context
    // * @return
    // */
    // public static synchronized DatabaseHelper getHelper(Context context) {
    // context = context.getApplicationContext();
    // if (instance == null) {
    // synchronized (DatabaseHelper.class) {
    // if (instance == null)
    // instance = new DatabaseHelper(context);
    // }
    // }
    //
    // return instance;
    // }
    //
    // public synchronized Dao getDao(Class clazz) throws SQLException {
    // Dao dao = null;
    // String className = clazz.getSimpleName();
    //
    // if (daos.containsKey(className)) {
    // dao = daos.get(className);
    // }
    // if (dao == null) {
    // dao = super.getDao(clazz);
    // daos.put(className, dao);
    // }
    // return dao;
    // }
    //
    // /**
    // * 释放资源
    // */
    // @Override
    // public void close() {
    // super.close();
    //
    // for (String key : daos.keySet()) {
    // Dao dao = daos.get(key);
    // dao = null;
    // }
    // }

}
