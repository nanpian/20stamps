package com.stamp20.app.util;

import java.util.List;

import android.graphics.Bitmap;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.stamp20.app.data.Design;

public class ParseUtil {

    private Bitmap bitmap;
    private Design mDesign = Design.getInstance();

    public ParseUtil(Bitmap bitmap) {
        this.bitmap = bitmap;
        mDesign.pinInBackground();
        Design.upload();
    }

    public void uploadImage() {
        byte[] data = BitmapUtils.Bitmap2Bytes(bitmap);
        final ParseFile file = new ParseFile(Design.StampFileName, data);
        String bitmapByte = new String(data);
        // 将bitmap以String的方式存储在Design里
        mDesign.put(Design.BitmapByte, bitmapByte);
        Log.d(this, "file.saveInBackground...");
        file.saveInBackground(new ProgressCallback() {

            @Override
            public void done(Integer arg0) {
                Log.d(this, "file.saveInBackground, progress:" + arg0);
                if (arg0 == 100) {
                    // upload success
                    mDesign.put(Design.StampReview, file);
                    mDesign.pinInBackground();// 保存Design至本地
                    mDesign.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d(this, "upload review image success!");
                                mDesign.setUploadStatus(100);
                            } else {
                                Log.d(this, "upload fail, need to retry!");
                            }
                        }
                    });
                }
            }
        });
    }

    public static List<Design> getMyLocalDesign() {
        ParseQuery<Design> query = ParseQuery.getQuery("Design");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Design>() {
            public void done(List<Design> objects, ParseException e) {
                if (e == null) {
                    Log.d(this, "getMyLocalDesign: success...");
                    // objectsWereRetrievedSuccessfully(objects);
                } else {
                    Log.d(this, "getMyLocalDesign: failed...");
                }
            }

        });
        return null;
    }
}
