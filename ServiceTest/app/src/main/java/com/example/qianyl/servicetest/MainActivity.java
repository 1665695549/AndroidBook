package com.example.qianyl.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private MyService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
            @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (MyService.DownloadBinder)service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","onCreat");
        Button startService = (Button)findViewById(R.id.start_service);
        Button stopService = (Button)findViewById(R.id.stop_service);
        Button bindService = (Button)findViewById(R.id.bind_service);
        Button unbindService = (Button)findViewById(R.id.unbind_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unbindService.setOnClickListener(this);
        Button startIntentservice = (Button)findViewById(R.id.start_intent_service);
        startIntentservice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service:
                Log.d("MainActivity","start onClick");
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);//startup service
                break;
            case R.id.stop_service:
                Log.d("MainActivity","stop onClick");
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);//startup service
                break;
            case R.id.bind_service:
                Log.d("MainActivity","bind_service clicked");
                Intent bindIntent = new Intent(this,MyService.class);
                Log.d("MainActivity","bind_service clicked 1");
                bindService(bindIntent,connection,BIND_AUTO_CREATE);// bind service
                Log.d("MainActivity","bind_service clicked 2");
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
            case R.id.start_intent_service:
                //print main thread id
                Log.d("MainActivity","Thread id is "+ Thread.currentThread().getId());
                Intent intentService = new Intent(this,MyIntentService.class);
                startService(intentService);
                break;
            default:
                break;
        }
    }
}
