package com.example.qianyl.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //Don't show the notification anymore in the quick lanch
        manager.cancel(1);
    }
}
