package com.collge.afile.pojo;

import android.graphics.Bitmap;

/**
 * Created by Siddhesh on 25-Jan-17.
 */

public class Item {
    public String file;
    public int icon;
    /**
     * 0 : Folder
     * 1 : File
     */
    public int type = 1;
    boolean isSelected = false;

    public Item(String file, Integer icon) {
        this.file = file;
        this.icon = icon;
    }

    public String getFile() {
        return file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
