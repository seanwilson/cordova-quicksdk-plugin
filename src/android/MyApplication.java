package com.undergroundcreative.QuickSDK;

//import android.app.Application;
import com.quicksdk.QuickSdkApplication;
import android.util.Log;

public class MyApplication extends QuickSdkApplication {
    @Override
    public void onCreate() {
        Log.d("MyApplication", "onCreate");
        super.onCreate();
    }
}