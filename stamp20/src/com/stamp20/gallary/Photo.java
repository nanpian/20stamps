package com.stamp20.gallary;

public class Photo {
	public static int PHOTO_NET_TYPE = 1;
	public static int PHOTO_LOC_TYPE = 2;
	public static int PHOTO_UNKNOW_TYPE = 0;
	
	private String mName;
	private String mUri;
	private String mThumnailUri;
	private int mType;

	public Photo(String name, String uri, int type){
		this.mName = name;
		this.mUri = uri;
		this.mType = type;
	}
	
	public Photo(String name, String sUri, String tUri, int type){
		this(name, sUri, type);
		this.mThumnailUri = tUri;
	}
	
	public String getName(){
		return this.mName;
	}
	
	public String getUri(){
		return this.mUri;
	}
	
	public String getThumnailUri(){
		return this.mThumnailUri;
	}
	
	public int getType(){
		return this.mType;
	}
}
