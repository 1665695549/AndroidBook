package com.example.qianyl.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by qianyl on 2018/4/11.
 */

public class MyIntentService extends IntentService{
    public MyIntentService(){super("MyIntentService");//call the constructor of the parent class
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //print current thread ID
        Log.d("MyIntentService","Thread id is "+Thread.currentThread().getId());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyIntentService","onDestroy executed");
    }
}

