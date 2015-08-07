package com.stamp20.app.db;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;

/**
 * TODO<邮票数据list Dao>
 * 
 * @author zhudw3
 * @data: 2014-11-30 下午16:25:18
 * @version: V1.0
 */
public class ShopInfoDao {

    private Dao<ShopInfoData, Integer> dao;
    private com.stamp20.app.db.DatabaseHelper helper;

    public ShopInfoDao(Context mContext) {
        try {
            helper = new DatabaseHelper(mContext);
            // helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(ShopInfoData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条数据
     * 
     * @param user
     */
    public void add(ShopInfoData dataInfo) {

        if (dataInfo == null)
            return;

        try {
            dao.create(dataInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        try {
            TableUtils.clearTable(helper.getConnectionSource(), ShopInfoData.class);
        } catch (SQLException e) {
            // TODO: handle exception
        }
    }

    /**
     * 删除某条购物数据
     * 
     * @param user_id
     * @param others_id
     */
    public void deleteById(String shop_id) {
        try {
            DeleteBuilder<ShopInfoData, Integer> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq("shop_uid", shop_id);
            int count = deleteBuilder.delete();
            Log.e("delete count", "count == " + count);
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<ShopInfoData> queryAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            // TODO: handle exception
            return null;
        }
    }

    /**
     * 更新一条数据
     * 
     * @param user
     */
    public void update(ShopInfoData dataInfo) {

        if (dataInfo == null)
            return;

        try {
            dao.update(dataInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
