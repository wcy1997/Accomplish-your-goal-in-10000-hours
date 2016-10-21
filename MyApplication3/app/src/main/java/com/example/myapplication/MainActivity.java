package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import static com.example.myapplication.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(activity_main);
        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"已退出" ,Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.id_action_history:
                Toast.makeText(this,"你点击了<历史纪录>",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_action_items:
                Toast.makeText(this,"你点击了<所有项目>",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_action_culculate:
                Toast.makeText(this,"你点击了<我的统计>",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_action_background:
                Toast.makeText(this,"你点击了<背景设置>",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;

    }
}

