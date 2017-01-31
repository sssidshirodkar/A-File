package com.collge.afile.presenter;

import java.io.File;
import java.util.List;

/**
 * Created by Siddhesh on 30-Jan-17.
 */
public interface Presenter {

    void onLoad();

    void onResume();

    void onPause();

    void update(List<?> list, File path);
}
