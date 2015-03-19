package com.stamp20.gallary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class FeaturedAlbumFragment extends GallaryFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        return inflater.inflate(R.layout.tab_featured_album, container, false);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(this, "onCreate");
        super.onCreate(savedInstanceState);
    }

}
