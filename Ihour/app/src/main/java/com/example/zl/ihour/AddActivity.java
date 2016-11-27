package com.example.zl.ihour;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private EditText editText;
    private RatingBar ratingBar;
    private Button button1;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Button button3;
    private Button button4;
    private EditText editText2;

    private int valueOfSpinner2;
    private int valueOfSpinner3;
    private int num = 0;

    private ArrayAdapter adapter;
    private ArrayAdapter adapter2;
    private ArrayAdapter adapter3;

    private Calendar alarmCalendar;

    private AlarmManager mAlarm;

    private SQLite sqLite = new SQLite(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();

        alarmCalendar = Calendar.getInstance();

////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(AddActivity.this, WheelViewPasswordActivity.class);
//                startActivity(intent);
//            }
//        });

////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////////////////////
        //将可选内容与ArrayAdapter连接起来
        adapter2 = ArrayAdapter.createFromResource(AddActivity.this, R.array.pattern, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner2.setAdapter(adapter2);
        //添加事件Spinner事件监听
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t = (String) adapter2.getItem(position);
                if (t.equals("懒人模式")) {
                    valueOfSpinner2 = 1;
                }
                if (t.equals("正常模式")) {
                    valueOfSpinner2 = 2;
                }
                if (t.equals("勤快模式")) {
                    valueOfSpinner2 = 3;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //设置默认值
        spinner2.setVisibility(View.VISIBLE);
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
        //将可选内容与ArrayAdapter连接起来
        adapter3 = ArrayAdapter.createFromResource(AddActivity.this, R.array.alarm, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner3.setAdapter(adapter3);
        //添加事件Spinner事件监听
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String t = (String) adapter3.getItem(position);
                if (t.equals("不提醒")) {
                    valueOfSpinner3 = 0;
                }
                if (t.equals("每天")) {
                    valueOfSpinner3 = 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //设置默认值
        spinner3.setVisibility(View.VISIBLE);
////////////////////////////////////////////////////////////////////////////////////////////////////
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                View view = (LinearLayout)getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                timePicker.setIs24HourView(true);
                timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("设置闹钟时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();

                        alarmCalendar.set(Calendar.HOUR_OF_DAY,
                                timePicker.getCurrentHour());
                        alarmCalendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
                        alarmCalendar.set(Calendar.SECOND, 0);
                        alarmCalendar.set(Calendar.MILLISECOND, 0);
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
////////////////////////////////////////////////////////////////////////////////////////////////////
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating = ratingBar.getRating();
                String title = editText.getText().toString();
                String p = editText2.getText().toString();
                int plan;
                if(p.equals("")){
                    plan = 0;
                }else {
                    plan = Integer.parseInt(editText2.getText().toString());
                }

                if(title.equals("")){
                    Toast.makeText(AddActivity.this,"请填写项目信息",Toast.LENGTH_SHORT).show();
                }else {
                    //获取当前时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String str = formatter.format(curDate);
                    Log.i("iiiiiiiiiiiiii",str);

                    sqLite.write("insert into item(name,importance,plan,amount,fromT,pattern,flag)" +
                            "values(?,?,?,?,?,?,?)",new Object[]{title,rating,plan,"0",str,valueOfSpinner2,"f"});
                    sqLite.write("insert into record(name,date,time,pattern) values(?,?,?,?)",
                            new Object[]{title,str,"0",valueOfSpinner2});

                    Cursor cursor = sqLite.query("select _id from item where name=?",new String[]{title});
                    cursor.moveToFirst();
                    if(cursor!=null&&cursor.getCount()>=0){
                        num = cursor.getInt(cursor.getColumnIndex("_id"));
                    }
                    Log.i("nnnnnnnnnnnnnn",String.valueOf(num));
                    //每天提醒
                    if(valueOfSpinner3 == 1){
                        /* 获取闹钟管理的实例 */
                        mAlarm = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                        Intent intent = new Intent(AddActivity.this,
                                MyReceiver.class);
                        //传id过去
                        intent.putExtra("id",String.valueOf(num));
                        PendingIntent pendingIntent = PendingIntent
                                .getBroadcast(AddActivity.this,
                                        num, intent, 0);// 第二个参数为区别不同闹铃的
                        //设置闹钟
                        mAlarm.set(AlarmManager.RTC_WAKEUP,
                                alarmCalendar.getTimeInMillis(),
                                pendingIntent);
                        mAlarm.setRepeating(AlarmManager.RTC_WAKEUP,//闹钟每天重复
                                alarmCalendar.getTimeInMillis(),
                                (1000 * 60 * 60 * 24),
                                pendingIntent);
                    }
                    finish();
                }

            }
        });
    }


    private void init(){
        editText = (EditText) findViewById(R.id.name);
        ratingBar = (RatingBar) findViewById(R.id.importance);
//        button1 = (Button) findViewById(R.id.pick);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.sure);
        editText2 = (EditText) findViewById(R.id.edit);

    }
}
