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
    String size;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
