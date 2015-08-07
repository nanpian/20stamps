package com.stamp20.gallary;

import com.stamp20.app.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class GallaryProgressDialog extends ProgressDialog {
    private final Builder mBuilder;
    private final Handler mHandler;
    private Runnable mTimeOverHandle;

    public GallaryProgressDialog(Context context) {
        this(context, new Builder(context));
    }

    public GallaryProgressDialog(Context context, Builder builder) {
        super(context, R.style.CustomProgressDialog);
        this.mBuilder = builder;
        mHandler = new Handler(context.getMainLooper());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallary_progress_dialog);

        this.setCancelable(mBuilder.mCancelable);
        this.setOnCancelListener(mBuilder.mOnCancelListener);

        if (mBuilder.mCancelTime > 0) {
            mTimeOverHandle = new Runnable() {

                @Override
                public void run() {
                    if (mBuilder.mTimeOverListener != null) {
                        mBuilder.mTimeOverListener.onTimeCancel(GallaryProgressDialog.this);
                    }
                    dismiss();
                }
            };

            mHandler.postDelayed(mTimeOverHandle, mBuilder.mCancelTime);
        }

        if (mBuilder.mMessage != null && !TextUtils.isEmpty(mBuilder.mMessage)) {
            TextView tv = (TextView) (this.findViewById(R.id.progresstext));
            tv.setText(mBuilder.mMessage);
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismiss() {
        if (mTimeOverHandle != null) {
            mHandler.removeCallbacks(mTimeOverHandle);
        }
        super.dismiss();
    }

    public static GallaryProgressDialog show(Context context, boolean indeterminate, long overtime, boolean cancelable,
            DialogInterface.OnCancelListener cancelListener) {
        GallaryProgressDialog dialog = new Builder(context).setCancelTime(overtime).setCancelable(cancelable).setCancelListener(cancelListener).build();

        if (indeterminate) {
            dialog.show();
        }
        return dialog;
    }

    public static class Builder {
        private Context mContext;
        private String mMessage;
        private long mCancelTime;
        private boolean mCancelable;
        private GallaryProgressDialog.OnCancelListener mTimeOverListener;
        private DialogInterface.OnCancelListener mOnCancelListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessage(String m) {
            this.mMessage = m;
            return this;
        }

        /**
         * set the time that dialog will be shown.
         * 
         * @param time
         *            after time (in milliseconds),dialog will be dismissed;
         * 
         */
        public Builder setCancelTime(long time) {
            this.mCancelTime = time;
            return this;
        }

        public Builder setCancelable(boolean cancel) {
            this.mCancelable = cancel;
            return this;
        }

        public Builder setCancelListener(DialogInterface.OnCancelListener listener) {
            if (listener instanceof GallaryProgressDialog.OnCancelListener)
                this.mTimeOverListener = (GallaryProgressDialog.OnCancelListener) listener;
            this.mOnCancelListener = listener;
            return this;
        }

        public GallaryProgressDialog build() {
            return new GallaryProgressDialog(this.mContext, this);
        }
    }

    interface OnCancelListener extends DialogInterface.OnCancelListener {
        /**
         * This method will be invoked when the dialog is canceled because time
         * over.
         * 
         * @param dialog
         *            The dialog that was canceled will be passed into the
         *            method.
         */
        public void onTimeCancel(DialogInterface dialog);
    }
}
