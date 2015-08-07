package com.stamp20.app.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("OrderItem")
public class OrderItem extends ParseObject {

    private final static String Count = "Count";

    private final static String Customized = "Customized";// false for Ideas

    private final static String DesignIdLocal = "DesignIdLocal";

    private final static String DesignIdParse = "DesignIdParse";// parse object

    private final static String DeviceId = "DeviceId";

    // id for design
    private final static String DeviceName = "DeviceName";

    private final static String InCart = "InCart";

    private final static String Option = "Option";

    private final static String OrderIdParse = "OrderIdParse";// parse object id
                                                              // for order

    public final static String OrderItem = "OrderItem";

    private final static String Price = "Price";

    private final static String TaxableAmount = "TaxableAmount";

    private final static String ThumbFileOneLocal = "Thumb1Local";

    private final static String ThumbFileOneServer = "Thumb1Server";// server
                                                                    // url of
                                                                    // thumb
                                                                    // image

    private final static String User = "User";// parse user object

    public OrderItem() {
    }

    public OrderItem(Design design, int count) {
        setDesignIdLocal(design.getDesignIdLocal());
        setDesignIdParse("");
        setDeviceName(design.getDeviceName());
        setDeviceId(design.getDeviceId());
        setPrice(design.getUnitPrice());
        setTaxableAmount(design.getTaxableAmount());

        setThumbFileOneLocal(design.getThumbFileOneLocal());
        setThumbFileOneServerUrl("");
        setOption(design.getOptionString());

        setCount(count);
        setOrderIdParse("");
        setCustomized(true);
        setInCart(true);
    }

    public int getCount() {
        return getInt(Count);
    }

    // Customized
    public boolean getCustomized() {
        return getBoolean(Customized);
    }

    // Design Id local
    public String getDesignIdLocal() {
        return getString(DesignIdLocal);
    }

    // Design Id Parse
    public String getDesignIdParse() {
        return getString(DesignIdParse);
    }

    // Device Id
    public String getDeviceId() {
        return getString(DeviceId);
    }

    // Device Name
    public String getDeviceName() {
        return getString(DeviceName);
    }

    public String getOption() {
        return getString(Option);
    }

    // OrderIdParse (this is not known until checkout completes)
    public String getOrderIdParse() {
        return getString(OrderIdParse);
    }

    // Price
    public int getPrice() {
        return this.getInt(Price);
    }

    // TaxableAmount
    public int getTaxableAmount() {
        return this.getInt(TaxableAmount);
    }

    // Thumb1
    public String getThumbFileOneLocal() {
        return getString(ThumbFileOneLocal);
    };

    // constant strings used in database

    public String getThumbFileOneServerUrl() {
        return getString(ThumbFileOneServer);
    }
    // Count
    public void setCount(int mCount) {
        put(Count, mCount);
    }
    public void setCustomized(boolean mCustomized) {
        put(Customized, mCustomized);
    }
                                                                public void setDesignIdLocal(String localDesignId) {
        put(DesignIdLocal, localDesignId);
    }
    public void setDesignIdParse(String parseDesignId) {
        put(DesignIdParse, parseDesignId);
    }

    public void setDeviceId(String mDeviceId) {
        put(DeviceId, mDeviceId);
    }
    public void setDeviceName(String mDeviceName) {
        put(DeviceName, mDeviceName);
    }

    // InCart
    public void setInCart(boolean inCart) {
        put(InCart, inCart);
    }
    // Option string (not array)
    public void setOption(String mOption) {
        put(Option, mOption);
    }

    public void setOrderIdParse(String mOrderId) {
        put(OrderIdParse, mOrderId);
    }
    public void setPrice(int mPrice) {
        put(Price, mPrice);
    }
    public void setTaxableAmount(int mTaxableAmount) {
        put(TaxableAmount, mTaxableAmount);
    }

    public void setThumbFileOneLocal(String mThumbFileOneLocal) {
        put(ThumbFileOneLocal, mThumbFileOneLocal);
    }

    public void setThumbFileOneServerUrl(String mThumbFileOneServer) {
        put(ThumbFileOneServer, mThumbFileOneServer);
    }
    // User
    public void setUser(ParseUser owner) {
        put(User, owner);
    }

}
