package com.stamp20.app.data;

import java.util.ArrayList;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/*
 * 利用Parse Local Store 功能实现一个简单的本地存储. 使用方法
 * 1. 每次add to cart时, addOrderItem
 * 2. 每次需要对一个OrderItem做改动的时候(比如加减数量), 使用DesignLocalId获取这个Order Item,然后做改动(不需要再call save之类的, 本地数据库会实时保持一致)
 * 3. 显示cart的时候, getAllOrderItemInCart
 * 4. checkout结束,或者用户从cart里删除的时候, 把需要删除的 call removeDesign
 * */
public class OrderItemProvider {

    private static final String LogTag = "20stamps";
    private static final String CART_LABEL = "loalCart";

    /*
     * Add one orderItem to local store
     */
    public void addOrderItemToCart(OrderItem orderItem) {
        orderItem.pinInBackground(CART_LABEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("LogTag", "Error addOrderItemToCart to local store "
                            + e.getMessage());
                }
            }
        });
    }

    /*
     * Remove one orderItem from local store
     */
    public void removeOrderItemFromCart(OrderItem orderItem) {
        orderItem.unpinInBackground(CART_LABEL, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("LogTag",
                            "Error removeOrderItemFromCart local store "
                                    + e.getMessage());
                }
            }
        });
    }

    public OrderItem getOrderItemByDesignLocalId(String designLocalId) {
        ParseQuery<OrderItem> query = ParseQuery.getQuery(OrderItem.OrderItem);
        query.whereEqualTo("DesignIdLocal", designLocalId);
        query.fromLocalDatastore();
        OrderItem result = null;
        try {
            result = query.getFirst();
        } catch (ParseException e) {
            Log.e("LogTag", "Error query orderItem " + designLocalId
                    + " from local store");
            e.printStackTrace();
        }
        return result;
    }

    // may take long time. Don't run on UI thread
    public ArrayList<OrderItem> getAllOrderItemInCart() {
        ParseQuery<OrderItem> query = ParseQuery.getQuery(OrderItem.OrderItem);
        query.fromPin(CART_LABEL);
        ArrayList<OrderItem> result = new ArrayList<OrderItem>();
        try {
            result.addAll(query.find());
        } catch (ParseException e) {
            Log.e("LogTag", "Error query order items in cart from local store");
            e.printStackTrace();
        }
        return result;
    }

}
