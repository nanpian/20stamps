package com.stamp20.app.db; 

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * TODO<数据库帮助类>
 * 
 * @author zhudewei
 * @data: 2014-12-31 下午3:38:32
 * @version: V1.0
 */

@DatabaseTable(tableName = "db_shopInfo")
public class ShopInfoData  implements Serializable {
	@DatabaseField
	  private String shop_uid;
	@DatabaseField
	  private String shop_imageurl;
	@DatabaseField
	  private String shop_num;
	/**
	 * @return the shop_uid
	 */
	public String getShop_uid() {
		return shop_uid;
	}
	/**
	 * @param shop_uid the shop_uid to set
	 */
	public void setShop_uid(String shop_uid) {
		this.shop_uid = shop_uid;
	}
	/**
	 * @return the shop_imageurl
	 */
	public String getShop_imageurl() {
		return shop_imageurl;
	}
	/**
	 * @param shop_imageurl the shop_imageurl to set
	 */
	public void setShop_imageurl(String shop_imageurl) {
		this.shop_imageurl = shop_imageurl;
	}
	/**
	 * @return the shop_num
	 */
	public String getShop_num() {
		return shop_num;
	}
	/**
	 * @param shop_num the shop_num to set
	 */
	public void setShop_num(String shop_num) {
		this.shop_num = shop_num;
	}
}
 