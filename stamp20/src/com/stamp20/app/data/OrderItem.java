package com.stamp20.app.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("OrderItem")
public class OrderItem extends ParseObject {

	
	public OrderItem(){
	}

	public OrderItem(Design design, int count){
		setDesignIdLocal(design.getDesignIdLocal());
		setDesignIdParse("");
		setDeviceName(design.getDeviceName());
		setDeviceId(design.getDeviceId());
		setPrice(design.getPrice());
		setTaxableAmount(design.getTaxableAmount());
		
		setThumbFileOneLocal(design.getThumbFileOneLocal());
		setThumbFileOneServerUrl("");
		setOption(design.getOptionString());
		
		setCount(count);
		setOrderIdParse("");
		setCustomized(true);
		setInCart(true);
	}
	
	//Design Id local
	public String getDesignIdLocal(){
		return getString(DesignIdLocal);
	}
	public void setDesignIdLocal(String localDesignId){
		put(DesignIdLocal, localDesignId);
	}
	
	//Design Id Parse
	public String getDesignIdParse(){
		return getString(DesignIdParse);
	}
	public void setDesignIdParse(String parseDesignId){
		put(DesignIdParse, parseDesignId);
	}

	//Device Name
	public String getDeviceName() {
		return getString(DeviceName);
	}
	public void setDeviceName(String mDeviceName) {
		put(DeviceName, mDeviceName);
	}

	//Device Id
	public String getDeviceId() {
		return getString(DeviceId);
	}
	public void setDeviceId(String mDeviceId) {
		put(DeviceId, mDeviceId);
	}

	//TaxableAmount
	public int getTaxableAmount() {
		return this.getInt(TaxableAmount);
	}
	public void setTaxableAmount(int mTaxableAmount) {
		put(TaxableAmount, mTaxableAmount);
	}

	//Price
	public int getPrice() {
		return this.getInt(Price);
	}
	public void setPrice(int mPrice) {
		put(Price, mPrice);
	}

	//Thumb1
	public String getThumbFileOneLocal() {
		return getString(ThumbFileOneLocal);
	}

	public String getThumbFileOneServerUrl() {
		return getString(ThumbFileOneServer);
	}
	
	public void setThumbFileOneLocal(String mThumbFileOneLocal) {
		put(ThumbFileOneLocal, mThumbFileOneLocal);
	}

	public void setThumbFileOneServerUrl(String mThumbFileOneServer) {
		put(ThumbFileOneServer,mThumbFileOneServer);
	}
	
	//Option string (not array)
	public void setOption(String mOption) {
		put(Option, mOption);
	}
	public String getOption() {
		return getString(Option);
	}
	
	//User
	public void setUser(ParseUser owner){
		put(User,owner);
	}
	//OrderIdParse (this is not known until checkout completes)
	public String getOrderIdParse() {
		return getString(OrderIdParse);
	}
	public void setOrderIdParse(String mOrderId) {
		put(OrderIdParse, mOrderId);
	}
	
	//Customized
	public boolean getCustomized(){
		return getBoolean(Customized);
	}

	public void setCustomized(boolean mCustomized){
		put(Customized, mCustomized);
	}

	//Count
	public void setCount(int mCount) {
		put(Count, mCount);
	}
	public int getCount() {
		return getInt(Count);
	}

	//InCart
	public void setInCart(boolean inCart){
		put(InCart,inCart);
	};
	
	//constant strings used in database
	
	public final static String OrderItem="OrderItem";
	private final static String DesignIdLocal = "DesignIdLocal";
	private final static String DesignIdParse="DesignIdParse";//parse object id for design
	private final static String DeviceName  = "DeviceName";
	private final static String DeviceId  = "DeviceId";
	
	private final static String Price = "Price";
	private final static String TaxableAmount = "TaxableAmount";
	
	private final static String ThumbFileOneLocal = "Thumb1Local";
	private final static String ThumbFileOneServer = "Thumb1Server";//server url of thumb image
	
	private final static String Option = "Option";
	private final static String User = "User";//parse user object
	private final static String OrderIdParse="OrderIdParse";//parse object id for order
	
	private final static String Customized = "Customized";//false for Ideas
	
	private final static String Count = "Count";
	private final static String InCart = "InCart";

}
