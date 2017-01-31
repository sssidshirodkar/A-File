package com.collge.afile.result;

/**
 * respresent a result
 * <p/>
 * Created by saghayam on 2/24/2016.
 */
public interface Result {

    void onSuccess(OnSuccess success);

    void onError(OnError error);

    void onException(OnException exception);

}
