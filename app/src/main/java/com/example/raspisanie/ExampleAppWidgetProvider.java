package com.example.raspisanie;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

import static com.example.raspisanie.ExampleService.sPref;
//import static com.example.raspisanie.MainActivity.chosenday;
//import static com.example.raspisanie.MainActivity.currentday;
//import static com.example.raspisanie.MainActivity.days;
//import static com.example.raspisanie.MainActivity.sPref;
import static com.example.raspisanie.MainActivity.subjects;
//import static com.example.raspisanie.MainActivity.todayis;
import static com.example.raspisanie.MainActivity.week;


public class ExampleAppWidgetProvider extends AppWidgetProvider {
    public static RemoteViews views;
    Date currentDate;
    public static int currentday;
    public static String todayis;
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        currentDate = new Date();
        getDayOfWeek(currentDate);
        onClick1(views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
            currentDate = new Date();
            getDayOfWeek(currentDate);
            onClick1(views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.buttonWidget, pendingIntent);
        }
    }
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        resizeWidget(newOptions, views);
        currentDate = new Date();
        getDayOfWeek(currentDate);
        onClick1(views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views) {
      //int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
      //int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
      //int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if (maxHeight < 100) {
            views.setTextViewTextSize(R.id.text1, 2, 12);
            views.setTextViewTextSize(R.id.text2, 2, 12);
            views.setTextViewTextSize(R.id.text3, 2, 12);
            views.setTextViewTextSize(R.id.text4, 2, 12);
            //views.setTextViewTextSize(R.id.text5, 2, 12);
            views.setViewVisibility(R.id.text0, View.GONE);
            views.setViewVisibility(R.id.text5, View.GONE);
        } else {
            views.setViewVisibility(R.id.text0, View.VISIBLE);
            views.setViewVisibility(R.id.text5, View.VISIBLE);
            views.setTextViewTextSize(R.id.text1, 2, 14);
            views.setTextViewTextSize(R.id.text2, 2, 14);
            views.setTextViewTextSize(R.id.text3, 2, 14);
            views.setTextViewTextSize(R.id.text4, 2, 14);
            //views.setTextViewTextSize(R.id.text5, 2, 14);
        }
    }

    public static void onClick1 (RemoteViews views) {
        views.setCharSequence(R.id.text0, "setText", todayis/*.substring(0, 1).toUpperCase()+todayis.substring(1)*/+", " + week);
        views.setCharSequence(R.id.text1, "setText", sPref.getString("para1bg", "С (вр.)") + " " + sPref.getString("para1end", "До (вр.)") + " " +sPref.getString(todayis + subjects[0] + week, "")); //MainActivity.edit1.getText().toString());
        views.setCharSequence(R.id.text2, "setText", sPref.getString("para2bg", "С (вр.)") + " " + sPref.getString("para2end", "До (вр.)") + " " +sPref.getString(todayis + subjects[1] + week, "")); //MainActivity.edit2.getText().toString());
        views.setCharSequence(R.id.text3, "setText", sPref.getString("para3bg", "С (вр.)") + " " + sPref.getString("para3end", "До (вр.)") + " " +sPref.getString(todayis + subjects[2] + week, "")); //MainActivity.edit3.getText().toString());
        views.setCharSequence(R.id.text4, "setText", sPref.getString("para4bg", "С (вр.)") + " " + sPref.getString("para4end", "До (вр.)") + " " +sPref.getString(todayis + subjects[3] + week, "")); //MainActivity.edit4.getText().toString());
        views.setCharSequence(R.id.text5, "setText", sPref.getString("para5bg", "С (вр.)") + " " + sPref.getString("para5end", "До (вр.)") + " " +sPref.getString(todayis + subjects[4] + week, "")); //MainActivity.edit5.getText().toString());
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
}
