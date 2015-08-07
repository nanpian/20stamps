package com.stamp20.app.data;

import java.util.List;
import java.util.Random;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Design")
public class Design extends ParseObject {
    public static final String BitmapByte = "BitmapByte";
    private final static String Count = "Count";// the count of design

    public final static String Design = "Design";

    private final static String DesignIdLocal = "DesignIdLocal";// local random
                                                                // string

    private final static String DeviceId = "DeviceId";

    private final static String DeviceName = "DeviceName";

    private static Design mInstance;

    private final static String Options = "Options";// array of option values

    private final static String Orientation = "Orientation"; // orientation of

    // amount,
                                                                 // !=price when
                                                                 // it's stamp
    private final static String Rate = "Rate";

    private final static String Source2FileLocal = "Source2Local";

    private final static String Source2FileServer = "Source2Server";// parsefile

    private final static String Source2FileServerUrl = "Source2ServerUrl";

    private final static String Source3FileLocal = "Source3Local";

    private final static String Source3FileServer = "Source3Server";// parsefile

    private final static String Source3FileServerUrl = "SourceS3erverUrl";

    private final static String SourceFileLocal = "SourceLocal";

    private final static String SourceFileServer = "SourceServer";// parsefile

    private final static String SourceFileServerUrl = "SourceServerUrl";

    public static final String StampFileName = "Review.png";

    public static final String StampReview = "StampReview";

    private final static String TaxableAmount = "TaxableAmount"; // taxable

    private final static String ThumbFileOneLocal = "Thumb1Local";

    private final static String ThumbFileOneServer = "Thumb1Server";// parsefile

    private final static String ThumbFileOneServerUrl = "Thumb1ServerUrl";

    private final static String ThumbFileTwoLocal = "Thumb2Local";

    private final static String ThumbFileTwoServer = "Thumb2Server";// parsefile

    private final static String ThumbFileTwoServerUrl = "Thumb2ServerUrl";

    // single design
    private final static String TotalPrice = "TotalPrice";

    // stamps
    private final static String Type = "Type"; // type: stamp or card

    public static String TYPE_CARD = "type_card";

    public static String TYPE_STAMP = "type_stamp";

    private final static String UnitPrice = "UnitPrice";// price from device,

    private final static String UploadStatus = "UploadStatus";

    private final static String User = "User";// ParseUser object

    public static void clearInstance() {
        mInstance = null;
    }

    /**
     * 应使用该接口获取Design实例，以保证每次只有一个design，在进入到选照片页面的时候应将mInstance置空以重新获取实例
     * 
     * @return
     */
    public static Design getInstance() {
        if (mInstance != null) {
            return mInstance;
        } else {
            return getNewDesign();
        }
    }

    /*
     * constructor for creating an empty design with random localId
     */
    private static Design getNewDesign() {
        mInstance = new Design();
        Random rand = new Random();
        int randomNum = rand.nextInt(268435455);
        int randomNum2 = rand.nextInt(268435455);
        mInstance.setDesignIdLocal("D" + Integer.toHexString(randomNum) + Integer.toHexString(randomNum2));
        mInstance.setCount(1);
        return mInstance;
    }

    public static void upload() {
        getInstance().saveInBackground();
    }

    /**
     * empty constructor required by parse
     */
    public Design() {
    }

    // Count
    public int getCount() {
        return this.getInt(Count);
    }

    // Id local
    public String getDesignIdLocal() {
        return getString(DesignIdLocal);
    }

    // Id Parse
    public String getDesignIdParse() {
        return this.getObjectId();
    }

    // Device Id
    public String getDeviceId() {
        return getString(DeviceId);
    }

    // Device Name
    public String getDeviceName() {
        return getString(DeviceName);
    }

    // Options
    public List<String> getOptions() {
        return this.getList(Options);
    }

    public String getOptionString() {
        StringBuffer sb = new StringBuffer();
        if (getOptions() != null && !getOptions().isEmpty()) {
            for (String option : getOptions()) {
                sb.append(option);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Orientation "H" or "V"
    public String getOrientation() {
        return getString(Orientation);
    }

    // Rate
    public int getRate() {
        return this.getInt(Rate);
    }

    // review picture
    public byte[] getReview() {
        return this.getBytes(BitmapByte);
    }

    // Source 2
    public String getSource2FileLocal() {
        return getString(Source2FileLocal);
    }

    public ParseFile getSource2FileServer() {
        return getParseFile(Source2FileServer);
    }

    public String getSource2FileServerUrl() {
        return getString(Source2FileServerUrl);
    }

    // Source 3
    public String getSource3FileLocal() {
        return getString(Source3FileLocal);
    }

    public ParseFile getSource3FileServer() {
        return getParseFile(Source3FileServer);
    }

    public String getSource3FileServerUrl() {
        return getString(Source3FileServerUrl);
    }

    // Source
    public String getSourceFileLocal() {
        return getString(SourceFileLocal);
    }

    public ParseFile getSourceFileServer() {
        return getParseFile(SourceFileServer);
    }

    public String getSourceFileServerUrl() {
        return getString(SourceFileServerUrl);
    }

    // TaxableAmount
    public int getTaxableAmount() {
        return this.getInt(TaxableAmount);
    }

    // Thumb1 and Thumb2
    public String getThumbFileOneLocal() {
        return getString(ThumbFileOneLocal);
    }

    public ParseFile getThumbFileOneServer() {
        return this.getParseFile(ThumbFileOneServer);
    }

    public String getThumbFileOneServerUrl() {
        return getString(ThumbFileOneServerUrl);
    }

    public String getThumbFileTwoLocal() {
        return getString(ThumbFileTwoLocal);
    }

    public ParseFile getThumbFileTwoServer() {
        return getParseFile(ThumbFileTwoServer);
    }

    public String getThumbFileTwoServerUrl() {
        return getString(ThumbFileTwoServerUrl);
    }

    // Total Price
    public String getTotalPrice() {
        return this.getString(TotalPrice);
    }

    // type
    public String getType() {
        return this.getString(Type);
    }

    // string constants in database

    // Unit Price
    public int getUnitPrice() {
        return this.getInt(UnitPrice);
    }
    // UploadStatus
    public int getUploadStatus() {
        return getInt(UploadStatus);
    }

    public void setCount(int count) {
        put(Count, count);
    }
    public void setDesignIdLocal(String localDesignId) {
        put(DesignIdLocal, localDesignId);
    }

    public void setDesignIdParse(String parseDesignId) {
        this.setObjectId(parseDesignId);
    }
                                                                 public void setDeviceId(String mDeviceId) {
        put(DeviceId, mDeviceId);
    }
    public void setDeviceName(String mDeviceName) {
        put(DeviceName, mDeviceName);
    }
                                                        public void setOptions(List<String> options) {
        this.remove(Options);
        this.addAll(Options, options);
    }
    public void setOrientation(String mOrientation) {
        put(Orientation, mOrientation);
    }
    public void setRate(int rate) {
        put(Rate, rate);
    }
    public void setReview(byte[] s) {
        put(BitmapByte, s);
    }
    public void setSource2FileLocal(String mSource2FileLocal) {
        put(Source2FileLocal, mSource2FileLocal);
    }
                                                             public void setSource2FileServer(ParseFile mSource2FileServerParseFile) {
        put(Source2FileServer, mSource2FileServerParseFile);
    }

    public void setSource2FileServerUrl(String mSource2FileServerUrl) {
        put(Source2FileServerUrl, mSource2FileServerUrl);
    }
    public void setSource3FileLocal(String mSource3FileLocal) {
        put(Source3FileLocal, mSource3FileLocal);
    }
    public void setSource3FileServer(ParseFile mSource3FileServerParseFile) {
        put(Source3FileServer, mSource3FileServerParseFile);
    }
    public void setSource3FileServerUrl(String mSource3FileServerUrl) {
        put(Source3FileServerUrl, mSource3FileServerUrl);
    }
    public void setSourceFileLocal(String mSourceFileLocal) {
        put(SourceFileLocal, mSourceFileLocal);
    }
    public void setSourceFileServer(ParseFile mSourceFileServerParseFile) {
        put(SourceFileServer, mSourceFileServerParseFile);
    }

    public void setSourceFileServerUrl(String mSourceFileServerUrl) {
        put(SourceFileServerUrl, mSourceFileServerUrl);
    }
    public void setTaxableAmount(int mTaxableAmount) {
        put(TaxableAmount, mTaxableAmount);
    }
    public void setThumbFileOneLocal(String mThumbFileOneLocal) {
        put(ThumbFileOneLocal, mThumbFileOneLocal);
    }
    public void setThumbFileOneServer(ParseFile mThumbFileOneServerParseFile) {
        put(ThumbFileOneServer, mThumbFileOneServerParseFile);
    }
    public void setThumbFileOneServerUrl(String mThumbFileOneServerUrl) {
        put(ThumbFileOneServerUrl, mThumbFileOneServerUrl);
    }
    public void setThumbFileTwoLocal(String mThumbFileTwoLocal) {
        put(ThumbFileTwoLocal, mThumbFileTwoLocal);
    }
    public void setThumbFileTwoServer(ParseFile mThumbFileTwoServerParseFile) {
        put(ThumbFileTwoServer, mThumbFileTwoServerParseFile);
    }
    public void setThumbFileTwoServerUrl(String mThumbFileTwoServerUrl) {
        put(ThumbFileTwoServerUrl, mThumbFileTwoServerUrl);
    }
    public void setTotalPrice(String price) {
        put(TotalPrice, price);
    }
    public void setType(String t) {
        put(Type, t);
    }

    public void setUnitPrice(int mPrice) {
        put(UnitPrice, mPrice);
    }
    public void setUploadStatus(int mUploadStatus) {
        this.put(UploadStatus, mUploadStatus);
    }
    // User
    public void setUser(ParseUser user) {
        this.put(User, user);
    }

}
