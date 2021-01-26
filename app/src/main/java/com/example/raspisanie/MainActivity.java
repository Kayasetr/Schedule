package com.example.raspisanie;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static com.example.raspisanie.ExampleService.sPref;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public static String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
    public static String[] subjects = {"1","2","3","4","5"};
    public static EditText edit1, edit2, edit3, edit4, edit5;
    Button Savebtn, StopService;
    public static TextView text, para1bg, para2bg, para3bg, para4bg, para5bg, para1end, para2end, para3end, para4end, para5end;
    Boolean tag;
    public static String todayis, week, para;
    RadioButton radio0, radio1;
    //public static SharedPreferences sPref, prefTag;
    Date currentDate;
    public static int chosenday, currentday;
    Calendar calender = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentDate = new Date();
        calender.get(Calendar.WEEK_OF_YEAR);
        text = findViewById(R.id.selection);
        Spinner spinner = findViewById(R.id.daysofweek);
        edit1 = findViewById(R.id.EditText1);
        edit2 = findViewById(R.id.EditText2);
        edit3 = findViewById(R.id.EditText3);
        edit4 = findViewById(R.id.EditText4);
        edit5 = findViewById(R.id.EditText5);
        para1bg = findViewById(R.id.para1begin);
        para2bg = findViewById(R.id.para2begin);
        para3bg = findViewById(R.id.para3begin);
        para4bg = findViewById(R.id.para4begin);
        para5bg = findViewById(R.id.para5begin);
        para1end = findViewById(R.id.para1end);
        para2end = findViewById(R.id.para2end);
        para3end = findViewById(R.id.para3end);
        para4end = findViewById(R.id.para4end);
        para5end = findViewById(R.id.para5end);
        radio0 = findViewById(R.id.radioButton0);
        radio1 = findViewById(R.id.radioButton1);
        StopService = findViewById(R.id.StopService);
        Savebtn = findViewById(R.id.Button1);
        calender.get(Calendar.WEEK_OF_YEAR);
        if (calender.get(Calendar.WEEK_OF_YEAR)%2 == 0) {
            week = "Черная неделя";
            tag = true;
            radio1.setChecked(true);
        }else {
            tag = false;
            week = "Белая неделя";
            radio0.setChecked(true);
        }
        getDayOfWeek(currentDate);
        loadText();
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        //final Intent serviceIntent = new Intent(this, ExampleService.class);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton0:
                        {
                        week = radio0.getText().toString();
                        tag=false;
                        loadText();
                        radio0.setChecked(true);
                    }
                    break;
                    case R.id.radioButton1:
                        {
                        week = radio1.getText().toString();
                        tag=true;
                        loadText();
                        radio1.setChecked(true);
                    }
                    break;
                }
            }
        });
        //loadtag();

        /*if (tag) {
            week = radio1.getText().toString();

        }else {
            week = radio0.getText().toString();
            radio0.setChecked(true);
        }*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentday);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                chosenday = position;
                loadText();
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Загружено, " + days[chosenday]+", "+week, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        Calendar time = Calendar.getInstance();
        Intent intent = new Intent(MainActivity.this, ExampleService.class);
        PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        time.setTimeInMillis(System.currentTimeMillis());
        time.set(Calendar.HOUR_OF_DAY, 0);
        time.set(Calendar.MINUTE, 0);
        time.set(Calendar.SECOND, 10);
        alarm.setInexactRepeating(AlarmManager.RTC, time.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
        /*AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ExampleService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 0);
        time.set(Calendar.MINUTE, 0);
        alarmMgr.setRepeating(AlarmManager.RTC, time.getTimeInMillis(),AlarmManager.INTERVAL_HOUR, pendingIntent);*/
        Savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText();
                //savetag();
                Toast.makeText(getBaseContext(), "Сохранено, " + days[chosenday]+", "+week, Toast.LENGTH_SHORT).show();
            }
        });
        para1bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para1bg";
                TimePickerFragment.hour = Integer.valueOf(para1bg.getText().toString().substring(2,4));
                TimePickerFragment.minute = Integer.valueOf(para1bg.getText().toString().substring(5,7));
            }
        });
        para2bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para2bg";
                TimePickerFragment.hour = Integer.valueOf(para2bg.getText().toString().substring(2,4));
                TimePickerFragment.minute = Integer.valueOf(para2bg.getText().toString().substring(5,7));
            }
        });
        para3bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para3bg";
                TimePickerFragment.hour = Integer.valueOf(para3bg.getText().toString().substring(2,4));
                TimePickerFragment.minute = Integer.valueOf(para3bg.getText().toString().substring(5,7));
            }
        });
        para4bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para4bg";
                TimePickerFragment.hour = Integer.valueOf(para4bg.getText().toString().substring(2,4));
                TimePickerFragment.minute = Integer.valueOf(para4bg.getText().toString().substring(5,7));
            }
        });
        para5bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para5bg";
                TimePickerFragment.hour = Integer.valueOf(para5bg.getText().toString().substring(2,4));
                TimePickerFragment.minute = Integer.valueOf(para5bg.getText().toString().substring(5,7));
            }
        });
        para1end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para1end";
                TimePickerFragment.hour = Integer.valueOf(para1end.getText().toString().substring(3,5));
                TimePickerFragment.minute = Integer.valueOf(para1end.getText().toString().substring(6,8));
            }
        });
        para2end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para2end";
                TimePickerFragment.hour = Integer.valueOf(para2end.getText().toString().substring(3,5));
                TimePickerFragment.minute = Integer.valueOf(para2end.getText().toString().substring(6,8));
            }
        });
        para3end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para3end";
                TimePickerFragment.hour = Integer.valueOf(para3end.getText().toString().substring(3,5));
                TimePickerFragment.minute = Integer.valueOf(para3end.getText().toString().substring(6,8));
            }
        });
        para4end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para4end";
                TimePickerFragment.hour = Integer.valueOf(para4end.getText().toString().substring(3,5));
                TimePickerFragment.minute = Integer.valueOf(para4end.getText().toString().substring(6,8));
            }
        });
        para5end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
                para = "para5end";
                TimePickerFragment.hour = Integer.valueOf(para5end.getText().toString().substring(3,5));
                TimePickerFragment.minute = Integer.valueOf(para5end.getText().toString().substring(6,8));
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        /*Intent serviceIntent = new Intent(this, ExampleService.class);
        startService(serviceIntent);
        stopService(serviceIntent);
        startService(serviceIntent);*/
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (para) {
            case "para1bg":
                if (minute < 10 && hourOfDay < 10) {
                    para1bg.setText("С " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para1bg.setText("С " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para1bg.setText("С " +hourOfDay + ":" + "0" + minute);
                } else{
                    para1bg.setText("С " +hourOfDay + ":" + minute);
                }break;
            case "para2bg":
                if (minute < 10 && hourOfDay < 10) {
                    para2bg.setText("С " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para2bg.setText("С " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para2bg.setText("С " +hourOfDay + ":" + "0" + minute);
                } else{
                    para2bg.setText("С " +hourOfDay + ":" + minute);
                }break;
            case "para3bg":
                if (minute < 10 && hourOfDay < 10) {
                    para3bg.setText("С " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para3bg.setText("С " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para3bg.setText("С " +hourOfDay + ":" + "0" + minute);
                } else{
                    para3bg.setText("С " +hourOfDay + ":" + minute);
                }break;
            case "para4bg":
                if (minute < 10 && hourOfDay < 10) {
                    para4bg.setText("С " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para4bg.setText("С " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para4bg.setText("С " +hourOfDay + ":" + "0" + minute);
                } else{
                    para4bg.setText("С " +hourOfDay + ":" + minute);
                }break;
            case "para5bg":
                if (minute < 10 && hourOfDay < 10) {
                    para5bg.setText("С " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para5bg.setText("С " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para5bg.setText("С " +hourOfDay + ":" + "0" + minute);
                } else{
                    para5bg.setText("С " +hourOfDay + ":" + minute);
                }break;
            case "para1end":
                if (minute < 10 && hourOfDay < 10) {
                    para1end.setText("До " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para1end.setText("До " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para1end.setText("До " +hourOfDay + ":" + "0" + minute);
                } else{
                    para1end.setText("До " +hourOfDay + ":" + minute);
                }break;
            case "para2end":
                if (minute < 10 && hourOfDay < 10) {
                    para2end.setText("До " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para2end.setText("До " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para2end.setText("До " +hourOfDay + ":" + "0" + minute);
                } else{
                    para2end.setText("До " +hourOfDay + ":" + minute);
                }break;
            case "para3end":
                if (minute < 10 && hourOfDay < 10) {
                    para3end.setText("До " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para3end.setText("До " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para3end.setText("До " +hourOfDay + ":" + "0" + minute);
                } else{
                    para3end.setText("До " +hourOfDay + ":" + minute);
                }break;
            case "para4end":
                if (minute < 10 && hourOfDay < 10) {
                    para4end.setText("До " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para4end.setText("До " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para4end.setText("До " +hourOfDay + ":" + "0" + minute);
                } else{
                    para4end.setText("До " +hourOfDay + ":" + minute);
                }break;
            case "para5end":
                if (minute < 10 && hourOfDay < 10) {
                    para5end.setText("До " + "0" +hourOfDay + ":" + "0" + minute) ;
                } else if (minute > 10 && hourOfDay < 10){
                    para5end.setText("До " + "0" +hourOfDay + ":" + minute);
                } else if (minute < 10 && hourOfDay > 10){
                    para5end.setText("До " +hourOfDay + ":" + "0" + minute);
                } else{
                    para5end.setText("До " +hourOfDay + ":" + minute);
                }break;
        }
    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        startService(serviceIntent);
    }
    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
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
                todayis = "сегодня воскресенье";
                text.setText("Дни недели, "+ todayis + ", "+ week);
            }break;
            case 2: {
                todayis = "сегодня понедельник";
                text.setText("Дни недели, " + todayis+ ", "+ week);
            }break;
            case 3: {
                todayis = "сегодня вторник";
                text.setText("Дни недели, "+ todayis+ ", "+ week);
            }break;
            case 4: {
                todayis = "сегодня среда";
                text.setText("Дни недели, "+ todayis+ ", "+ week);
            }break;
            case 5: {
                todayis = "сегодня четверг";
                text.setText("Дни недели, "+ todayis+ ", "+ week);
            }break;
            case 6: {
                todayis = "сегодня пятница";
                text.setText("Дни недели, "+ todayis+ ", "+ week);
            }break;
            case 7: {
                todayis = "сегодня суббота";
                text.setText("Дни недели, "+ todayis+ ", "+ week);
            }break;
        }
    }

    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("para1bg", para1bg.getText().toString());
        ed.putString("para2bg", para2bg.getText().toString());
        ed.putString("para3bg", para3bg.getText().toString());
        ed.putString("para4bg", para4bg.getText().toString());
        ed.putString("para5bg", para5bg.getText().toString());
        ed.putString("para1end", para1end.getText().toString());
        ed.putString("para2end", para2end.getText().toString());
        ed.putString("para3end", para3end.getText().toString());
        ed.putString("para4end", para4end.getText().toString());
        ed.putString("para5end", para5end.getText().toString());
        switch (chosenday) {
            case 0: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
            case 1: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
            case 2: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
            case 3: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
            case 4: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
            case 5: {
                ed.putString(days[chosenday]+subjects[0]+week,edit1.getText().toString());
                ed.putString(days[chosenday]+subjects[1]+week,edit2.getText().toString());
                ed.putString(days[chosenday]+subjects[2]+week,edit3.getText().toString());
                ed.putString(days[chosenday]+subjects[3]+week,edit4.getText().toString());
                ed.putString(days[chosenday]+subjects[4]+week,edit5.getText().toString());
            }
            break;
        }
        ed.commit();

    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        para1bg.setText(sPref.getString("para1bg", "С (вр.)"));
        para2bg.setText(sPref.getString("para2bg", "С (вр.)"));
        para3bg.setText(sPref.getString("para3bg", "С (вр.)"));
        para4bg.setText(sPref.getString("para4bg", "С (вр.)"));
        para5bg.setText(sPref.getString("para5bg", "С (вр.)"));
        para1end.setText(sPref.getString("para1end", "До (вр.)"));
        para2end.setText(sPref.getString("para2end", "До (вр.)"));
        para3end.setText(sPref.getString("para3end", "До (вр.)"));
        para4end.setText(sPref.getString("para4end", "До (вр.)"));
        para5end.setText(sPref.getString("para5end", "До (вр.)"));
        switch (chosenday) {
            case 0:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
            case 1:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
            case 2:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
            case 3:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
            case 4:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
            case 5:{
                edit1.setText(sPref.getString(days[chosenday]+subjects[0]+week, ""));
                edit2.setText(sPref.getString(days[chosenday]+subjects[1]+week, ""));
                edit3.setText(sPref.getString(days[chosenday]+subjects[2]+week, ""));
                edit4.setText(sPref.getString(days[chosenday]+subjects[3]+week, ""));
                edit5.setText(sPref.getString(days[chosenday]+subjects[4]+week, ""));
            }break;
        }
      //  Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }

    /*void loadtag() {
        prefTag = getPreferences(MODE_PRIVATE);
        tag = prefTag.getBoolean("week",false);
    }

    void savetag() {
        prefTag = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edtag = prefTag.edit();
        edtag.putBoolean("week",tag);
        edtag.commit();
    }*/




    /*
    void saveText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(subjects[0], edit1.getText().toString());
        ed.commit();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(subjects[0], "");
        edit1.setText(savedText);
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
*/
}
