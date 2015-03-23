package com.stamp20.app.util;

import android.graphics.Bitmap;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import com.stamp20.app.data.Design;

public class UploadToParse {
    
    private boolean isSuccess;
    private Bitmap bitmap;
    private Design mDesign = Design.getInstance();
    private int retryTimes = 10;
    
    public UploadToParse(Bitmap bitmap){
        this.bitmap = bitmap;
        mDesign.upload();
    }

    public void uploadImage(){
      byte[] data = BitmapUtils.Bitmap2Bytes(bitmap);
      final ParseFile file = new ParseFile("review.png", data);
      Log.d(this, "file.saveInBackground...");
      file.saveInBackground(new ProgressCallback() {
          
          @Override
          public void done(Integer arg0) {
              Log.d(this, "file.saveInBackground, progress:" + arg0);
              if (arg0 == 100) {
                  // upload success
                  mDesign.put("reviewImage", file);
                  mDesign.saveInBackground(new SaveCallback() {
    
                      @Override
                      public void done(ParseException e) {
                          if(e == null){
                              Log.d(this, "upload review image success!");
                              retryTimes = 10;
                          } else {
                              Log.d(this, "upload fail, need to retry!");
                              while(retryTimes -- >= 0){
                                  uploadImage();
                              }
                          }
                      }
                  });
              }
          }
      });
    }
    
//  final Design mDesign = Design.getNewDesign();
//  byte[] data = BitmapUtils.Bitmap2Bytes(mCache.get());
//  final ParseFile file = new ParseFile("review.png", data);
//  Log.d(this, "file.saveInBackground...");
//  file.saveInBackground(new ProgressCallback() {
//      
//      @Override
//      public void done(Integer arg0) {
//          Log.d(this, "file.saveInBackground, progress:" + arg0);
//          if (arg0 == 100) {
//              // upload success
//              mDesign.put("reviewImage", file);
//              mDesign.saveInBackground(new SaveCallback() {
//
//                  @Override
//                  public void done(ParseException e) {
//                      if(e == null){
//                          Log.d(this, "upload review image success!");
//                      } else {
//                          Log.d(this, "upload fail, need to retry!");
//                      }
//                  }
//              });
//          }
//      }
//  });
}
