package com.stamp20.gallary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.stamp20.app.activities.CardEffect;
import com.stamp20.app.activities.MainEffect;
import com.stamp20.app.util.Image;
import com.stamp20.app.util.Log;
import com.stamp20.app.util.PhotoFromWhoRecorder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;

public class GallaryUtil {
    static public final String IMG_URI_EXTRA = "imageUri";
    private static Uri u;

    static public void goToEffectActivity(Context context, String uri) {
        u = Uri.parse(uri);

        String fromStampOrCard = PhotoFromWhoRecorder.readFromWhich(context);

        Intent intent = null;
        if (fromStampOrCard == null || fromStampOrCard.endsWith("stamp")) {
            intent = new Intent(context, MainEffect.class);
            intent.putExtra(IMG_URI_EXTRA, u);
        } else if (fromStampOrCard.equals("card")) {
            intent = new Intent(context, CardEffect.class);
            intent.putExtra(IMG_URI_EXTRA, u);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getPhotoUri() {
        return u;
    }

    static public void goToEffectAfterDownLoad(Context context, String url) {
        new DownloadImageTask(context).execute(url);
    }

    private static class DownloadImageTask extends
            AsyncTask<String, Void, String> {
        private Context mContext;
        Dialog progress;

        public DownloadImageTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progress = GallaryProgressDialog.show(mContext, true,
                    GallaryActivity.OUT_OF_TIME, false,
                    new GallaryProgressDialog.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onTimeCancel(DialogInterface dialog) {
                            // TODO Auto-generated method stub
                            Log.e("wangpeng",
                                    "download out time, need add response");
                        }
                    });
        }

        @Override
        protected String doInBackground(String... urls) {
            Bitmap tmp = Image.LoadImage(urls[0]);

            if (tmp == null)
                return null;

            File file = new File(mContext.getFilesDir(), "tmp.jpg");
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                tmp.compress(CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Log.d("wangpeng", "Image tmp file is saved");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Uri.fromFile(file).toString();
        }

        @Override
        protected void onPostExecute(String uri) {
            progress.dismiss();
            goToEffectActivity(mContext, uri);
        }

    }
}
