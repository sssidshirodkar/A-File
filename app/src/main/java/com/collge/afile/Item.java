package com.collge.afile;

/**
 * Created by Siddhesh on 25-Jan-17.
 */

public class Item {
    public String file;
    public int icon;

    public Item(String file, Integer icon) {
        this.file = file;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return file;
    }
}
