package com.collge.afile;

import android.app.Application;

/**
 * Created by Siddhesh on 30-Jan-17.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
