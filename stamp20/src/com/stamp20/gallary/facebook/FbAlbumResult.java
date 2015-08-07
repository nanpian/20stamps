package com.stamp20.gallary.facebook;

import java.util.ArrayList;

public class FbAlbumResult extends ArrayList<FbAlbum> {

    private static final long serialVersionUID = -4248716785672144806L;

    private String nextAlbumsUrl = null;

    public FbAlbumResult(int cap) {
        super(cap);
    }

    public String getNextAlbumsUrl() {
        return nextAlbumsUrl;
    }

    public void setNextAlbumsUrl(String nextAlbumsUrl) {
        this.nextAlbumsUrl = nextAlbumsUrl;
    }

    public void appendMoreFbAlbums(FbAlbumResult more) {
        this.addAll(more);
        setNextAlbumsUrl(more.getNextAlbumsUrl());
    }

}
