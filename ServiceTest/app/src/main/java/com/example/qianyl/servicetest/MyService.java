package com.example.qianyl.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    public MyService() {
    }
    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder{

        public void startDownload(){
            Log.d("MyService","startDownload execute");
        }
        public int getProgress(){
            Log.d("MyService","getProgress execute");
            return 0;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MyService","onBind executed");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d("MyServer","onCreate executed");
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        Log.d("MyServer","startForeground");
        startForeground(1,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyServer","onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyServer","onDestroy executed");
    }
}
