package com.stamp20.gallary.facebook;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonSyntaxException;
import com.parse.ParseFacebookUtils;

public class FbAlbumStore {
    // private final static JsonParser parser = new JsonParser();

    @SuppressWarnings("deprecation")
    public static FbAlbumResult getAlbums(JSONObject response) {

        FbAlbumResult result = new FbAlbumResult(100);

        try {
            if (response != null) {
                if (response.has("error")) {
                    Log.e("cases", "getAlbums error " + response.toString());
                } else {
                    if (response.has("data")) {
                        JSONArray albums = response.getJSONArray("data");
                        for (int i = 0; i < albums.length(); i++) {
                            JSONObject saObj = albums.getJSONObject(i);
                            if (saObj.has("count") && saObj.has("cover_photo") && saObj.has("name")) {
                                result.add(new FbAlbum(saObj.getString("id"), saObj.getString("name"), saObj
                                        .getInt("count"), saObj.getString("cover_photo"), null, null));
                            }
                        }
                    }

                    // adding next page if existing
                    if (response.has("paging")) {
                        JSONObject pagingObject = response.getJSONObject("paging");
                        if (pagingObject.has("next")) {
                            result.setNextAlbumsUrl(pagingObject.getString("next").substring(27));
                        }
                    }
                    // retrieving thumbnail cover url
                    /*
                     * for (FbAlbum fa: result){ FbPhoto coverPhoto =
                     * getFbPhoto(fa.getCoverId()); if (coverPhoto!=null){
                     * fa.setCoverThumbnailUrl(coverPhoto.getThumbnailUrl()); }
                     * }
                     */
                }

            }
        } catch (JSONException e) {
            Log.e("cases", "FbAlbumStore parse error in getAlbums", e);
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static FbAlbumResult getAlbums(String p) {
        JSONObject response = null;
        FbAlbumResult result = new FbAlbumResult(100);
        String para = p == null ? "me/albums" : p;
        try {
            response = new JSONObject(ParseFacebookUtils.getFacebook().request(para));
            if (response != null) {
                if (response.has("error")) {
                    Log.e("cases", "getAlbums error " + response.toString());
                } else {
                    if (response.has("data")) {
                        JSONArray albums = response.getJSONArray("data");
                        for (int i = 0; i < albums.length(); i++) {
                            JSONObject saObj = albums.getJSONObject(i);
                            if (saObj.has("count") && saObj.has("cover_photo") && saObj.has("name")) {
                                result.add(new FbAlbum(saObj.getString("id"), saObj.getString("name"), saObj
                                        .getInt("count"), saObj.getString("cover_photo"), null, null));
                            }
                        }
                    }

                    // adding next page if existing
                    if (response.has("paging")) {
                        JSONObject pagingObject = response.getJSONObject("paging");
                        if (pagingObject.has("next")) {
                            result.setNextAlbumsUrl(pagingObject.getString("next").substring(27));
                        }
                    }
                    // retrieving thumbnail cover url
                    for (FbAlbum fa : result) {
                        FbPhoto coverPhoto = getFbPhoto(fa.getCoverId());
                        if (coverPhoto != null) {
                            fa.setCoverThumbnailUrl(coverPhoto.getThumbnailUrl());
                            fa.setCoverSourceUrl(coverPhoto.getSourceImageUrl());
                        }
                    }
                }

            }
        } catch (JSONException e) {
            Log.e("cases", "FbAlbumStore parse error in getAlbums", e);
        } catch (MalformedURLException e) {
            Log.e("cases", "FbAlbumStore MalformedURL error in getAlbums", e);
        } catch (IOException e) {
            Log.e("cases", "FbAlbumStore IO error in getAlbums", e);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static FbPhoto getFbPhoto(final String photoId) {
        JSONObject response = null;
        FbPhoto result = null;
        try {
            response = new JSONObject(ParseFacebookUtils.getFacebook().request(photoId));
            if (response != null) {
                if (response.has("error")) {
                    Log.e("cases", "getFbPhoto error " + response.toString());
                } else {
                    result = new FbPhoto(response.getString("id"), response.getString("source"),
                            response.getString("picture"), response.getInt("width"), response.getInt("height"));
                }
            }
        } catch (JSONException e) {
            Log.e("cases", "FbAlbumStore parse error in getFbPhoto", e);
        } catch (MalformedURLException e) {
            Log.e("cases", "FbAlbumStore MalformedURL error in getFbPhoto", e);
        } catch (IOException e) {
            Log.e("cases", "FbAlbumStore IO error in getFbPhoto", e);
        }
        return result;
    }

    // String para = (p==null)? albumId+"/photos":p;
    @SuppressWarnings("deprecation")
    public static FbPhotoResult getPhotos(final String para) {
        JSONObject response = null;
        FbPhotoResult result = new FbPhotoResult(100);
        try {
            response = new JSONObject(ParseFacebookUtils.getFacebook().request(para));
            if (response != null) {
                if (response.has("error")) {
                    Log.e("cases", "getPhotos error " + response.toString());
                } else {
                    if (response.has("data")) {
                        JSONArray photos = response.getJSONArray("data");
                        for (int i = 0; i < photos.length(); i++) {
                            JSONObject spObj = photos.getJSONObject(i);
                            result.add(new FbPhoto(spObj.getString("id"), spObj.getString("source"), spObj
                                    .getString("picture"), spObj.getInt("width"), spObj.getInt("height")));
                        }
                    }
                    // adding next page if existing
                    if (response.has("paging")) {
                        JSONObject pagingObject = response.getJSONObject("paging");
                        if (pagingObject.has("next")) {
                            result.setNextPhotosUrl(pagingObject.getString("next").substring(27));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("cases", "FbAlbumStore parse error in getPhotos", e);
        } catch (MalformedURLException e) {
            Log.e("cases", "FbAlbumStore MalformedURL error in getPhotos", e);
        } catch (IOException e) {
            Log.e("cases", "FbAlbumStore IO error in getPhotos", e);
        }
        return result;
    }

}
