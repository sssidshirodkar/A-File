package com.collge.afile.pojo;

import android.graphics.Bitmap;

/**
 * Created by Siddhesh on 25-Jan-17.
 */

public class Item {
    public String file;
    public int icon;
    Bitmap thumbnail = null;

    public Item(String file, Integer icon) {
        this.file = file;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return file;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
