package com.example.zl.ihour;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ItemActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private Button button1;
    private Button button2;
    private Button button3;
    private String pa = "";
    String id = "";
    String str1 = "";
    String str3 = "";
    String da = "";
    int time = 0;
    private LinearLayout curve;
    private ChartService mService;
    private GraphicalView mView;//图表

    private SQLite sqLite = new SQLite(this);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        init();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Log.i("gggggggggggggggg",id);
        final Cursor cursor = sqLite.query("select name,importance,amount,fromT,pattern" +
                " from item where _id=?",new String[]{id});
        cursor.moveToFirst();
        if(cursor != null && cursor.getCount() > 0){
            str1 = cursor.getString(cursor.getColumnIndex("name"));
            String str2 = cursor.getString(cursor.getColumnIndex("importance"));
            str3 = cursor.getString(cursor.getColumnIndex("amount"));
            String str4 = cursor.getString(cursor.getColumnIndex("fromT"));
            String str5 = cursor.getString(cursor.getColumnIndex("pattern"));

            Date date = null;
            try {
                date = sdf.parse(str4);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String current = sdf.format(curDate);
            int d = (int) ((curDate.getTime() - date.getTime())/86400000);
            da = String.valueOf(d);

            if(str5.equals("1")){
                pa = "懒人模式";
            }
            if(str5.equals("2")){
                pa = "正常模式";
            }
            if(str5.equals("3")){
                pa = "勤快模式";
            }

            textView1.setText(str1);
            textView2.setText(str4);
            textView3.setText(str3);
            textView4.setText(da);
            textView5.setText(str2);
            textView6.setText(pa);
        }

        Cursor cursor2 = sqLite.query("select date,time from record where name=?",new String[]{str1});
        Log.i("ssssssssssssss",str1);
        cursor2.moveToFirst();
        ArrayList<Double> doubles = new ArrayList<Double>();
        ArrayList<Date> dates = new ArrayList<Date>();
        if(cursor2!=null&&cursor2.getCount()>=0){
            Log.i("jjjjjjjjjjj","指针不为空");


            String string1 = cursor2.getString(cursor2.getColumnIndex("date"));
            Log.i("string1",string1);
            Date date = null;
            try {
                date = sdf.parse(string1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double d = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("time")));
            doubles.add(d);
            dates.add(date);
            while (cursor2.moveToNext()){
                String string2 = cursor2.getString(cursor2.getColumnIndex("date"));
                Log.i("string1",string2);
                Date date2 = null;
                try {
                    date2 = sdf.parse(string2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double d2 = Double.parseDouble(cursor2.getString(cursor2.getColumnIndex("time")));
                doubles.add(d2);
                dates.add(date2);
            }
            if(doubles!=null&&dates!=null){
                mService = new ChartService(ItemActivity.this);
                String[] titles = {"曲线分析"};
                int[] colors = {Color.CYAN};
                XYMultipleSeriesDataset dataset = mService.setXYMultipleSeriesDataset(titles,doubles);//标明曲线
                mService.buildBarRenderer(colors);
//                mService.setXYMultipleSeriesRenderer("时间", "日期", "时间",colors,styles,dates,0,100);
                mView = mService.getGraphicalView(dataset);
                //将图表添加到布局容器中
                curve.addView(mView, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                doubles.clear();
            }
        }else {

        }



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi,\t"+"分享我的一万小时项目详情\t"+"在"
                +str1+"上投入"+str3+"小时，坚持了"+da+"天。");

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                View view = (LinearLayout)getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("请添加时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double mHour1 = timePicker.getCurrentHour();
                        double mMinute1 = timePicker.getCurrentMinute();
                        String currentTime = textView3.getText().toString();
                        double g = Double.parseDouble(currentTime);
                        double d = g + mHour1 + mMinute1/60;
                        DecimalFormat df = new DecimalFormat("0.00");
                        final String db = df.format(d);
                        Log.i("ssssssssssssssssss",String.valueOf(d));
                        Log.i("hhhhhhhhhhhhhhhhhhhhh",String.valueOf(mHour1));
                        Log.i("hhhhhhhhhhhhhhhhhhhhh",String.valueOf(mMinute1));
                        double t = mHour1 + mMinute1/60;
                        final String sFt = df.format(t);

                        sqLite.update("item",db,id);
                        //获取当前时间
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String str = formatter.format(curDate);
                        Log.i("iiiiiiiiiiiiii",str);


                        Cursor cursor1 = sqLite.query("select time from record where date=? and name=?",new String[]{str,str1});
                        cursor1.moveToFirst();
                        //如果不存在今天的数据
                        if(cursor == null||cursor1.getCount() == 0){
                            sqLite.write("insert into record(name,date,time) values(?,?,?)",new Object[]{str1,str,sFt});
                        }else {
                            String timeBefore = cursor1.getString(cursor1.getColumnIndex("time"));
                            double timeB = Double.parseDouble(timeBefore);
                            double timeN = t + timeB;
                            String timeNow = String.valueOf(timeN);
//                            sqLite.update2("record",timeNow,str1,str);
                            sqLite.excute("update record set time=? where name=? and date=?",new Object[]{timeNow,str1,str});
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg = new Message();
                                msg.what = 1;
                                Bundle bundle = new Bundle();
                                bundle.putString("db",db);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                        }).start();

//                        //时间小于10的数字 前面补0 如01:12:00
//                        StringBuilder tF = new StringBuilder();
//                        tF.append(mHour1 < 10 ? "0" + mHour1 : mHour1).append(":")
//                                .append(mMinute1 < 10 ? "0" + mMinute1 : mMinute1).append(":00");
//                        button3.setText("闹钟时间"+tF);
//                        timeFrom = tF.toString();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
                builder.setTitle("确定要删除吗");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLite.delete("item","_id=?",new String[]{id});
                        finish();
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    private void init(){
        textView1 = (TextView) findViewById(R.id.title);
        textView2 = (TextView) findViewById(R.id.time_from);
        textView3 = (TextView) findViewById(R.id.time_total);
        textView4 = (TextView) findViewById(R.id.day);
        textView5 = (TextView) findViewById(R.id.impo);
        textView6 = (TextView) findViewById(R.id.patt);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.delete);
        curve = (LinearLayout) findViewById(R.id.curve);
    }

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle b = msg.getData();
                    String db = b.getString("db");
                    textView3.setText(db);
                    Toast.makeText(ItemActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
