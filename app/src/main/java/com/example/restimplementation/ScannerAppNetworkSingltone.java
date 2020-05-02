package com.example.restimplementation;

import android.app.Application;
import android.content.Context;

public class ScannerAppNetworkSingltone extends Application {
    private static ScannerAppNetworkSingltone sInstant;
    private Context getApplicationContext;

    public void onCreate() {
        super.onCreate();
        sInstant=this;
    }
    public static ScannerAppNetworkSingltone getInstance()
    {
        return sInstant;
    }
    public static Context getAppContext()
    {
        return sInstant.getApplicationContext;
    }
}
