package com.example.raspisanie;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import static com.example.raspisanie.App.CHANNEL_ID;
//import static com.example.raspisanie.MainActivity.chosenday;
//import static com.example.raspisanie.MainActivity.currentday;
//import static com.example.raspisanie.MainActivity.days;
//import static com.example.raspisanie.MainActivity.sPref;
//import static com.example.raspisanie.MainActivity.todayis;
import static com.example.raspisanie.MainActivity.week;
import static com.example.raspisanie.MainActivity.subjects;

public class
ExampleService extends Service {
    public String todayis;
    public static SharedPreferences sPref;
    public static int currentday;

    //  String input;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void getDayOfWeek(Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        if (dow == 1) {
            currentday = dow-1;
        }else {
            currentday = dow-2;
        }
        switch (dow) {
            case 1: {
                todayis = "Воскресенье";
            }break;
            case 2: {
                todayis = "Понедельник";
            }break;
            case 3: {
                todayis = "Вторник";
            }break;
            case 4: {
                todayis = "Среда";
            }break;
            case 5: {
                todayis = "Четверг";
            }break;
            case 6: {
                todayis = "Пятница";
            }break;
            case 7: {
                todayis = "Суббота";
            }break;
        }
    }
   @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
       Context context = this;
       Date currentDate = new Date();
       getDayOfWeek(currentDate);
       AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
       RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_widget);
       ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
       appWidgetManager.updateAppWidget(thisWidget, remoteViews);
       ExampleAppWidgetProvider.onClick1(remoteViews);
       PendingIntent pendingIntent = PendingIntent.getActivity(this,
               0, notificationIntent, 0);
       Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID )
                .setContentTitle(todayis+ ", " + week).setColor(getColor(R.color.colorPrimary))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(sPref.getString("para1bg", "С (вр.)") + " " + sPref.getString("para1end", "До (вр.)")+ " " +sPref.getString(todayis+subjects[0]+week, ""))
                        .addLine(sPref.getString("para2bg", "С (вр.)") + " " + sPref.getString("para2end", "До (вр.)")+ " " +sPref.getString(todayis+subjects[1]+week, ""))
                        .addLine(sPref.getString("para3bg", "С (вр.)") + " " + sPref.getString("para3end", "До (вр.)")+ " " +sPref.getString(todayis+subjects[2]+week, ""))
                        .addLine(sPref.getString("para4bg", "С (вр.)") + " " + sPref.getString("para4end", "До (вр.)")+ " " +sPref.getString(todayis+subjects[3]+week, ""))
                        .addLine(sPref.getString("para5bg", "С (вр.)") + " " + sPref.getString("para5end", "До (вр.)")+ " " +sPref.getString(todayis+subjects[4]+week, "")))
                .setSmallIcon(R.drawable.ic_event_note)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
