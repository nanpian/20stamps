package lenovo.jni;

import android.graphics.Bitmap;
import com.stamp20.app.util.Log;

public final class ImageUtils {
    static {
        System.loadLibrary("GaussBlur");
    }

    public static Bitmap fastBlur(Bitmap src, int degree) {
        Log.d("20stamp", "fastBlur");
        Bitmap dest = null;
        int w = src.getWidth();
        int h = src.getHeight();

        if (degree <= 0) {
            return src;
        }

        dest = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        fastBlur(src, dest, w, h, degree);

        return dest;
    }

    private static native void fastBlur(Bitmap src, Bitmap store, int width, int height, int degree);
}
