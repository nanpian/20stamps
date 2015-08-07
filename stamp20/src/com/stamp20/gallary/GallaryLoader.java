package com.stamp20.gallary;

import java.util.List;

public interface GallaryLoader {
    List<Album> getAlbums();

    List<Photo> getPhotos(Album a);
}
