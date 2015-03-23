package com.stamp20.app.data;

import java.util.List;
import java.util.Random;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("Design")
public class Design extends ParseObject {
    
    private static Design mInstance;

	/**
	 * empty constructor required by parse 
	 */
	public Design(){
	}
	
	/*
	 * constructor for creating an empty design with random localId
	 * */
	private static Design getNewDesign()
	{
		mInstance = new Design();
		Random rand = new Random();
		int randomNum = rand.nextInt(268435455);
		int randomNum2 = rand.nextInt(268435455);
		mInstance.setDesignIdLocal("D"+Integer.toHexString(randomNum)+Integer.toHexString(randomNum2));
		return mInstance;
	}
	
	/**
	 * 应使用该接口获取Design实例，以保证每次只有一个design，在进入到选照片页面的时候应将mInstance置空以重新获取实例
	 * @return
	 */
	public static Design getInstance(){
	    if(mInstance != null){
	        return mInstance;
	    } else {
	        return getNewDesign();
	    }
	}
	
	public static void clearInstance(){
	    mInstance = null;
	}
	
	//Id local
	public String getDesignIdLocal(){
		return getString(DesignIdLocal);
	}
	public void setDesignIdLocal(String localDesignId){
		put(DesignIdLocal, localDesignId);
	}
	
	//Id Parse
	public String getDesignIdParse(){
		return this.getObjectId();
	}
	public void setDesignIdParse(String parseDesignId){
		this.setObjectId(parseDesignId);
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

	//Options
	public List<String> getOptions() {
		return this.getList(Options);
	}
	public void setOptions(List<String> options) {
	  this.remove(Options);
	  this.addAll(Options, options);
	}
	
	//User
	public void setUser(ParseUser user){
		  this.put(User, user);
	}
	
	//Orientation "H" or "V"
	public String getOrientation() {
		return getString(Orientation);
	}
	public void setOrientation(String mOrientation) {
		put(Orientation, mOrientation);
	}
	
	//UploadStatus
	public int getUploadStatus() {
		return getInt(UploadStatus);
	}
	public void setUploadStatus(int mUploadStatus) {
		this.put(UploadStatus, mUploadStatus);
	}

	//Thumb1 and Thumb2
	public String getThumbFileOneLocal() {
		return getString(ThumbFileOneLocal);
	}
	public void setThumbFileOneLocal(String mThumbFileOneLocal) {
		put(ThumbFileOneLocal, mThumbFileOneLocal);
	}

	public ParseFile getThumbFileOneServer() {
		return this.getParseFile(ThumbFileOneServer);
	}
	public void setThumbFileOneServer(ParseFile mThumbFileOneServerParseFile) {
		put(ThumbFileOneServer, mThumbFileOneServerParseFile);
	}

	public String getThumbFileOneServerUrl() {
		return getString(ThumbFileOneServerUrl);
	}
	public void setThumbFileOneServerUrl(String mThumbFileOneServerUrl) {
		put(ThumbFileOneServerUrl, mThumbFileOneServerUrl);
	}

	public String getThumbFileTwoLocal() {
		return getString(ThumbFileTwoLocal);
	}
	public void setThumbFileTwoLocal(String mThumbFileTwoLocal) {
		put(ThumbFileTwoLocal, mThumbFileTwoLocal);
	}

	public ParseFile getThumbFileTwoServer() {
		return getParseFile(ThumbFileTwoServer);
	}
	public void setThumbFileTwoServer(ParseFile mThumbFileTwoServerParseFile) {
		put(ThumbFileTwoServer, mThumbFileTwoServerParseFile);
	}

	public String getThumbFileTwoServerUrl() {
		return getString(ThumbFileTwoServerUrl);
	}
	public void setThumbFileTwoServerUrl(String mThumbFileTwoServerUrl) {
		put(ThumbFileTwoServerUrl, mThumbFileTwoServerUrl);
	}
	
	//Source 
	public String getSourceFileLocal() {
		return getString(SourceFileLocal);
	}
	public void setSourceFileLocal(String mSourceFileLocal) {
		put(SourceFileLocal,mSourceFileLocal);
	}

	public ParseFile getSourceFileServer() {
		return getParseFile(SourceFileServer);
	}
	public void setSourceFileServer(ParseFile mSourceFileServerParseFile) {
		put(SourceFileServer, mSourceFileServerParseFile);
	}

	public String getSourceFileServerUrl() {
		return getString(SourceFileServerUrl);
	}
	public void setSourceFileServerUrl(String mSourceFileServerUrl) {
		put(SourceFileServerUrl, mSourceFileServerUrl);
	}
	//Source 2
	public String getSource2FileLocal() {
		return getString(Source2FileLocal);
	}
	public void setSource2FileLocal(String mSource2FileLocal) {
		put(Source2FileLocal,mSource2FileLocal);
	}

	public ParseFile getSource2FileServer() {
		return getParseFile(Source2FileServer);
	}
	public void setSource2FileServer(ParseFile mSource2FileServerParseFile) {
		put(Source2FileServer, mSource2FileServerParseFile);
	}

	public String getSource2FileServerUrl() {
		return getString(Source2FileServerUrl);
	}
	public void setSource2FileServerUrl(String mSource2FileServerUrl) {
		put(Source2FileServerUrl, mSource2FileServerUrl);
	}

	//Source 3
	public String getSource3FileLocal() {
		return getString(Source3FileLocal);
	}
	public void setSource3FileLocal(String mSource3FileLocal) {
		put(Source3FileLocal,mSource3FileLocal);
	}

	public ParseFile getSource3FileServer() {
		return getParseFile(Source3FileServer);
	}
	public void setSource3FileServer(ParseFile mSource3FileServerParseFile) {
		put(Source3FileServer, mSource3FileServerParseFile);
	}

	public String getSource3FileServerUrl() {
		return getString(Source3FileServerUrl);
	}
	public void setSource3FileServerUrl(String mSource3FileServerUrl) {
		put(Source3FileServerUrl, mSource3FileServerUrl);
	}

	public String getOptionString(){
		StringBuffer sb = new StringBuffer();
		if (getOptions()!=null && !getOptions().isEmpty()){
		  for (String option:getOptions()){
			sb.append(option);
			sb.append(" ");
		  }
		}
		return sb.toString();
	}
	
	public static void upload(){
	    getInstance().saveInBackground();
	}
	
	// string constants in database
	
	public final static String Design = "Design";
	private final static String DesignIdLocal = "DesignIdLocal";//local random string
	
	private final static String DeviceName  = "DeviceName";
	private final static String DeviceId  = "DeviceId";
	
	private final static String TaxableAmount = "TaxableAmount"; //taxable amount, !=price when it's stamp
	private final static String Price = "Price";//price from device
	private final static String Options ="Options";//array of option values
	private final static String User = "User";//ParseUser object
	private final static String Orientation ="Orientation"; //orientation of stamps
	
	private final static String ThumbFileOneLocal = "Thumb1Local";
	private final static String ThumbFileOneServer = "Thumb1Server";//parsefile
	private final static String ThumbFileOneServerUrl = "Thumb1ServerUrl";
	private final static String ThumbFileTwoLocal = "Thumb2Local";
	private final static String ThumbFileTwoServer = "Thumb2Server";//parsefile
	private final static String ThumbFileTwoServerUrl = "Thumb2ServerUrl";
	
	private final static String SourceFileLocal = "SourceLocal";
	private final static String SourceFileServer = "SourceServer";//parsefile
	private final static String SourceFileServerUrl = "SourceServerUrl";
	private final static String Source2FileLocal = "Source2Local";
	private final static String Source2FileServer = "Source2Server";//parsefile
	private final static String Source2FileServerUrl = "Source2ServerUrl";
	private final static String Source3FileLocal = "Source3Local";
	private final static String Source3FileServer = "Source3Server";//parsefile
	private final static String Source3FileServerUrl = "SourceS3erverUrl";
	
	private final static String UploadStatus="UploadStatus";

}
