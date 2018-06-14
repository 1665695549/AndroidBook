package com.example.qianyl.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice=(Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @Override
   public void onClick(View v){
        switch (v.getId()){
            case R.id.send_notice:
                Intent intent=new Intent(this,NotificationActivity.class);
                PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
                NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                Notification notification=new NotificationCompat.Builder(this)
                        .setContentText("Learn how to build notifications,send and sync data," +
                         "and use voice actions.Get the official Android IDE and " +
                        "developer tools to build apps for Android.")
                       .setContentTitle("This is content title")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        //.setVibrate(new long[] {0,1000,1000,1000})
                       // . setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg")))
                        //.setLights(Color.GREEN,1000,1000)//activity while the screen is black
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(1,notification);
                break;
            default:
                break;
        }
    }
}
