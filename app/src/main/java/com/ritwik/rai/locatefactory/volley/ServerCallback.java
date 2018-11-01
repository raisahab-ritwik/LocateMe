package com.ritwik.rai.locatefactory.volley;

import com.android.volley.VolleyError;

public interface ServerCallback {

    public void onSuccess(String resultJsonObject);


    /**
     * If there occurs any error while communicating with server
     *
     * @param error
     */

    public void ErrorMsg(VolleyError error);
}
