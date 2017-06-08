package com.collge.afile.util;

import java.io.File;

/**
 * Created by Siddhesh on 23-02-2017.
 */

public class FileUtil {

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String calculateSizeOfFile(String path) {
        String value;
        long Filesize = getFolderSize(new File(path)) / 1024;//call function and convert bytes into Kb
        if (Filesize >= 1024)
            value = Filesize / 1024 + " Mb";
        else
            value = Filesize + " Kb";

        return value;
    }

}
