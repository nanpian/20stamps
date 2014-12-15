package com.stamp20.app.imageloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stamp20.app.R;
import com.stamp20.app.util.Log;

public class FaceBookAlbumFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(this, "onCreateView");
        return inflater.inflate(R.layout.tab_fb_album, container, false);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(this, "onCreate");
        super.onCreate(savedInstanceState);
    }

}