package com.stamp20.gallary.facebook;

import android.os.Bundle;

public class FbPhoto {
    public static FbPhoto fromBundle(Bundle b) {
        return new FbPhoto(b.getString("id"), b.getString("sourceImageUrl"), b.getString("thumbnailUrl"),
                b.getInt("width"), b.getInt("height"));
    }
    private int height;
    private String id;
    private String sourceImageUrl;
    private String thumbnailUrl;

    private int width;

    public FbPhoto(String id, String sourceImageUrl, String thumbnailUrl, int width, int height) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.sourceImageUrl = sourceImageUrl;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public String getId() {
        return id;
    }

    public String getSourceImageUrl() {
        return sourceImageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSourceImageUrl(String sourceImageUrl) {
        this.sourceImageUrl = sourceImageUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("sourceImageUrl", sourceImageUrl);
        b.putString("thumbnailUrl", thumbnailUrl);
        b.putInt("width", width);
        b.putInt("height", height);
        return b;
    }
}
