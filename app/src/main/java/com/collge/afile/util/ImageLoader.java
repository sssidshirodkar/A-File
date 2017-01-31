package com.collge.afile.util;

import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.collge.afile.App;

/**
 * Created by Siddhesh on 30-Jan-17.
 */

public class ImageLoader {

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null)
            instance = new ImageLoader();
        return instance;
    }

    public void loadImage(Uri uri, int placeHolder, int errorHolder, ImageView imageView) {
        Glide.with(App.getInstance())
                .load(uri)
                .placeholder(placeHolder)
                .error(errorHolder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView);
    }

    public void loadImage(Uri uri, ImageView imageView) {
        Glide.with(App.getInstance()).load(uri).into(imageView);
    }

    public void loadImage(int uri, ImageView imageView) {
        Glide.with(App.getInstance()).load(uri).into(imageView);
    }

}
