package com.stamp20.app.data;

import java.util.ArrayList;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/*
 * 利用Parse Local Store 功能实现一个简单的本地存储. 使用方法
 * 1. 每次产生一个新Design的时候, addDesign, 然后记住这个Design的LocalId, 在之后的activities里面传递
 * 2. 每次需要对一个Design做改动的时候, 使用LocalId获取这个Design,然后做改动(不需要再call save之类的, 本地数据库会实时保持一致)
 * 3. 在checkout的时候, 上传每一个Design,上传成功后把UploadStatus改成100
 * 4. checkout结束,或者用户从cart里删除的时候, 把需要删除的 call removeDesign
 * */

public class DesignProvider {

    private static final String DESIGN_LABEL = "localDesigns";
    private static final String LogTag = "20stamps";

    /*
     * Add one design to local store
     */
    public void addDesign(Design design) {
        design.pinInBackground(DESIGN_LABEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("LogTag", "Error addDesign to local store " + e.getMessage());
                }
            }
        });
    }

    // may take long time. Don't run on UI thread
    public ArrayList<Design> getAllDesign() {
        ParseQuery<Design> query = ParseQuery.getQuery(Design.Design);
        query.fromPin(DESIGN_LABEL);
        ArrayList<Design> result = new ArrayList<Design>();
        try {
            result.addAll(query.find());
        } catch (ParseException e) {
            Log.e("LogTag", "Error query designs from local store");
            e.printStackTrace();
        }
        return result;
    }

    public Design getDesignByLocalId(String designLocalId) {
        ParseQuery<Design> query = ParseQuery.getQuery(Design.Design);
        query.whereEqualTo("DesignIdLocal", designLocalId);
        query.fromLocalDatastore();
        Design result = null;
        try {
            result = query.getFirst();
        } catch (ParseException e) {
            Log.e("LogTag", "Error query design " + designLocalId + " from local store");
            e.printStackTrace();
        }
        return result;
    }

    /*
     * Remove one design from local store
     */
    public void removeDesign(Design design) {
        design.unpinInBackground(DESIGN_LABEL, new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("LogTag", "Error remove design from local store " + e.getMessage());
                }
            }
        });
    }

}
