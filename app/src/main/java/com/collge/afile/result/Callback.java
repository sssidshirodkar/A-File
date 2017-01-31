package com.collge.afile.result;

/**
 * Created by Siddhesh on 30-Jan-17.
 */

public interface Callback {
    void onSuccess(Object object);
    void onError(Object object);
}
