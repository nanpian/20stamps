package com.stamp20.gallary;

public class Album {
    private String mContent;

    // Album Content
    private int mCount;
    // Album name
    private String mName;
    // Cover Picture
    private String mUri;

    private String mUri_High;
    private String mUri_Low;

    public Album(String name, String uri, int count, String content) {
        this.mName = name;
        this.mUri = uri;
        this.mCount = count;
        this.mContent = content;
    }

    public String getAlbumName() {
        return this.mName;
    }

    public String getContent() {
        return this.mContent;
    }

    public int getContentCount() {
        return this.mCount;
    }

    public String getCoverUri() {
        return this.mUri;
    }

    public String getHighQualityCoverUri() {
        return this.mUri_High;
    }

    public String getLowQualityCoverUri() {
        return this.mUri_Low;
    }

    public void setHighQualityCover(String uri) {
        this.mUri_High = uri;
    }

    public void setLowQualityCover(String uri) {
        this.mUri_Low = uri;
    }

}
