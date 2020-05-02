package com.example.restimplementation.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.restimplementation.ScannerAppNetworkSingltone;

public class VolleySinglton {
    private static VolleySinglton sIntance=null;
    private RequestQueue requestQueue;
    public VolleySinglton()
    {
        requestQueue=getRequestQueue();
    }
    public static VolleySinglton getInstance(){
        if(sIntance==null)
        {
            sIntance=new VolleySinglton();
        }
        return sIntance;
    }
    private RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(ScannerAppNetworkSingltone.getInstance());
        }
        return requestQueue;
    }
    public void addRequestQueue(Request request){
        requestQueue.add(request);
    }
}
