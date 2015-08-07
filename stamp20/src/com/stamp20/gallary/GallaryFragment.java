package com.stamp20.gallary;

import android.support.v4.app.Fragment;

public class GallaryFragment extends Fragment {
    private boolean isFront = false;
    private final String name = getClass().getName();

    public boolean isFront() {
        return this.isFront;
    }

    public boolean onBackClick() {
        return false;
    }

    public void onBackground() {
        this.isFront = false;
    }

    public void onFront() {
        this.isFront = true;
    }
}
