package com.stamp20.app.data;

import java.util.ArrayList;

public class DeviceStamp {

	// 用来存储不同价格的Stamp信息
	
	private String name;
	private int stampValue;
	private int price;
	private int taxableAmount;
	private String imageName;
	
	public DeviceStamp(String name, int stampValue, int price,String imageName) {
		super();
		this.name = name;
		this.stampValue = stampValue;
		this.price = price;
		this.taxableAmount = price - 20*stampValue;
		this.imageName = imageName;
	}
	
	public String getName() {
		return name;
	}

	public int getStampValue() {
		return stampValue;
	}

	public int getPrice() {
		return price;
	}

	public int getTaxableAmount() {
		return taxableAmount;
	}

	public String getImageName() {
		return imageName;
	}

	static public ArrayList<DeviceStamp> getDeviceStampList(){
		ArrayList<DeviceStamp> l = new ArrayList<DeviceStamp>();
		l.add(new DeviceStamp("Post Card",34,1900,"Icon_Postcard"));
		l.add(new DeviceStamp("1st Class Letter 1oz",34,1900,"Icon_PostCard"));
		l.add(new DeviceStamp("1st Class Letter 2oz or 1oz odd",70,1900,"Icon_Letter"));
		l.add(new DeviceStamp("1st Class Letter 3oz or 2oz odd",91,1900,"Icon_Letter"));
		l.add(new DeviceStamp("1st Class Letter 3oz odd",112,1900,"Icon_Letter"));
		l.add(new DeviceStamp("1st Class Large Envelope 1oz",98,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("1st Class Large Envelope 2oz",119,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("1st Class Large Envelope 3oz",140,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("1st Class Large Envelope 4oz",161,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("1st Class Large Envelope 5oz",182,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("1st Class Large Envelope 6oz",203,1900,"Icon_LargeEnvelope"));
		l.add(new DeviceStamp("Priority (up to 16 oz )",560,1900,"Icon_LargeEnvelope"));
		return l;
	}
	
}
