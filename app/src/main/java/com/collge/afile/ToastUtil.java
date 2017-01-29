package com.collge.afile;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Siddhesh on 25-Jan-17.
 */

public class ToastUtil {
    public static void showShortToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
