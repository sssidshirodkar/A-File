package com.collge.afile.interactor;

import android.util.Log;

import com.collge.afile.App;
import com.collge.afile.R;
import com.collge.afile.pojo.Item;
import com.collge.afile.result.Callback;
import com.collge.afile.util.FileType;
import com.collge.afile.util.FileUtil;
import com.collge.afile.util.StorageUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;


/**
 * Created by Siddhesh on 30-Jan-17.
 */

public class FileInteractor implements UIInteractor {

    private File path;
    private List<Item> fileList;
    private Callback callback;
    private boolean firstLevel;

    public void setLevel(boolean firstLevel) {
        this.firstLevel = firstLevel;
    }

    public FileInteractor(File path, List<Item> fileList) {
        this.path = path;
        this.fileList = fileList;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        loadFileList();
    }

    private void loadFileList() {

        if (firstLevel) {
            String[] parentList = StorageUtil.getStorageDirectories(App.getInstance());
            for (int i = 0; i < parentList.length; i++) {
                fileList.add(i, new Item(parentList[i], R.mipmap.folder_empty));
                fileList.get(i).type = FileType.FOLDER;
            }
        } else {

            try {
                path.mkdirs();
            } catch (SecurityException e) {
                callback.onError("unable to write on the sd card ");
            }

            // Checks whether path exists
            if (path.exists()) {
                FilenameFilter filter = new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        File sel = new File(dir, filename);
                        // Filters based on whether the file is hidden or not
                        return (sel.isFile() || sel.isDirectory())
                                && !sel.isHidden();

                    }
                };

                String[] fList = path.list(filter);
//            fileList = new ArrayList<>(fList.length);
                for (int i = 0; i < fList.length; i++) {
                    fileList.add(i, new Item(fList[i], R.mipmap.file));
                    // Convert into file path
                    File sel = new File(path, fList[i]);

//                fileList.get(i).setSize(FileUtil.calculateSizeOfFile(sel.getAbsolutePath()));
//                Log.d("SIZE", fileList.get(i).getSize());

                    // Set drawables
                    if (sel.isDirectory()) {
                        fileList.get(i).icon = R.mipmap.folder_empty;
                        fileList.get(i).type = FileType.FOLDER;
                        Log.d("DIRECTORY", fileList.get(i).file);
                    } else {
                        Log.d("FILE", fileList.get(i).file);
                    }
                }

//            if (!firstLevel) {
//                /**
//                 * this indicates its not the first level so we show up button
//                 */
//                fileList.add(0, new Item("Up", R.drawable.directory_up));
//            }
            } else {
                callback.onError("path does not exist");
            }
        }
        callback.onSuccess(fileList);
    }

}
