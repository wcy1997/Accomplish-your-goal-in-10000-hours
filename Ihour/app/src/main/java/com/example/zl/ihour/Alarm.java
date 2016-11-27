package com.example.zl.ihour;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zl on 2016/11/23.
 */
public class Alarm extends Activity {
    MediaPlayer mediaPlayer;
    private SQLite sqLite = new SQLite(this);
    private int IdNum;
    private Calendar calendar;
    private double command = 0;
    private double days = 0;
    ArrayList<Double> doubles = new ArrayList<Double>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = MediaPlayer.create(this,R.raw.alarm);

        String id = getIntent().getStringExtra("id");
        Cursor cursor = sqLite.query("select name,pattern,plan from item where _id=?",new String[]{id});
        cursor.moveToFirst();
        int pattren = 0;
        int plan = 0;
        String name = "";
        if(cursor!=null&&cursor.getCount()>0){

            pattren = cursor.getInt(cursor.getColumnIndex("pattern"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            plan = cursor.getInt(cursor.getColumnIndex("plan"));
        }
        Log.i("pattern:",String.valueOf(pattren));
        Cursor cursor1 = sqLite.query("select date,time from record where name=?",new String[]{name});
        cursor1.moveToFirst();
        if(cursor1 != null && cursor1.getCount()>0){
            String str = cursor1.getString(cursor1.getColumnIndex("time"));
            double dou = Double.parseDouble(str);
            doubles.add(dou);
            while ((cursor1.moveToNext())){
                String str1 = cursor1.getString(cursor1.getColumnIndex("time"));
                double dou1 = Double.parseDouble(str1);
                doubles.add(dou1);
            }
        }

        double len = doubles.size();
        int length = doubles.size();
        if(pattren == 1){
            //懒人模式
            Log.i("sssssssssssssssss","懒人");
            double to1 = 0;
            if(len < 10){
                double total = 0;
                double to2 = 0;
                for(int i = 0;i<len;i++){
                    //求平方和
                    to2 = to2 + doubles.get(i)*doubles.get(i);

                }
                for (int i = 0;i<len;i++){
                    double num = doubles.get(i);
                    if(to2>0){
                        total = total + num*num*num/to2;
                    }else {
                        total = 1;
                    }

                    Log.i("ddddddddddddddddddd",String.valueOf(num));
                }

                command = total;
                Log.i("ccccccccccccccc",String.valueOf(total));
            }else {
                //大于等于10
                double total = 0;
                double to2 = 0;
                for(int i = 0;i<10;i++){
                    //求平方和
                    to2 = to2 +doubles.get(length-i)*doubles.get(length-i);
                }
                for(int i=0;i<10;i++){
                    if(to2>0){
                        total = total + doubles.get(length-i)*doubles.get(length-i)*doubles.get(length-i)/to2;
                    }else {
                        total = 1;
                    }

                }
                command = total;
                Log.i("ccccccccccccccc",String.valueOf(command));
            }

            for(int i = 0;i<len;i++){
                to1 = to1+doubles.get(i);
            }
            if(to1>0){
                days = (plan - to1)/to1*len;
            }else if(to1 ==0){
                days = plan/1;
            }



        }
        if(pattren == 2){
            //正常模式
            Log.i("sssssssssssssssss","正常");
            double total = 0;
            if(len == 1 && doubles.get(0)==0){
                command = 2;
            }else {
                for (int i = 0;i < len;i++){
                    total = total+doubles.get(i);
                }
                command = total / len;
            }
            days = (plan - total)/command;
            Log.i("ccccccccccccccc",String.valueOf(command));
        }
        if(pattren ==3){
            command = 2;//勤奋模式2小时每天

            double total = 0;
            if(len > 0) {
                for (int i = 0;i < len;i++){
                    total = total+doubles.get(i);
                }
            }else {
                total = 0;
            }
            days = (plan - total) / 2;
        }

        //算法


        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        new AlertDialog.Builder(Alarm.this).setTitle("闹钟")
                .setMessage("该执行"+name+"了。\n"+"推荐时间为："+command+"。\n"+
                "预计"+days+"之后完成任务。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        Alarm.this.finish();
                    }
                })
                .show();

    }
}
