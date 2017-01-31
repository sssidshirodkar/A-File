package com.collge.afile.presenter;

import com.collge.afile.pojo.Item;
import com.collge.afile.executor.ExecutorModule;
import com.collge.afile.interactor.FileInteractor;
import com.collge.afile.result.Callback;

import java.io.File;
import java.util.List;
import java.util.TimerTask;


/**
 * Created by Siddhesh on 30-Jan-17.
 */

public class FilePresenter implements Presenter {

    View view;
    File currentPath;

    public void setView(View view) {
        this.view = view;
    }


    @Override
    public void onLoad() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void update(List<?> list, File path) {
        if (view.isReady())
            view.update(list, path);
    }

    public void loadFileList(File path, List<Item> fileList) {
        currentPath = path;
        FileInteractor fileInteractor = new FileInteractor(path, fileList);
        fileInteractor.setCallback(new Callback() {
            @Override
            public void onSuccess(final Object object) {
                ExecutorModule.provideExecutor().runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                         update((List<Item>) object, currentPath);
                    }
                });
            }

            @Override
            public void onError(Object object) {

            }
        });
        ExecutorModule.provideExecutor().submitTask(fileInteractor);
    }

    public interface View {
        void update(List<?> list, File path);

        boolean isReady();
    }

}
