package com.example.zl.ihour;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLite sqLite = new SQLite(this);
    private Cursor cursor;
    private ListView listView;

    //用来记录是否登录
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.listview);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE); //私有数据
        editor = sharedPreferences.edit();//获取编辑器

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.text1);
                String i = textView.getText().toString();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,ItemActivity.class);
                intent.putExtra("id",i);
                startActivity(intent);
            }
        });
        //长按
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //删除
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否删除?");
                builder.setTitle("提示");
                TextView textView = (TextView) view.findViewById(R.id.text1);
                final String t = textView.getText().toString();

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sqLite.delete("item","_id=?",new String[]{t});
                        updataAdapter();
                        Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,AddActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("确定要注销吗");
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            if (LogOff()) {
                                finish();
                                Message msg = new Message();
                                msg.what = 1;
                                handler2.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 2;
                                handler2.sendMessage(msg);
                            }
                        }
                    }.start();
                }
            });
            builder.create();
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static boolean LogOff()
    {
        try {
            if (!ConServer.connection.isConnected()) {
                ConServer.connection.connect();
            }
            ConServer.connection.disconnect();

            //ConServer.connection.getChatManager().removeChatListener(chatManagerListener);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.this, "已退出！", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("login",false);
                    editor.commit();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "未响应！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        updataAdapter();
    }

    public void updataAdapter(){
        if (cursor != null && !cursor.isClosed())
            cursor.close();

        cursor = sqLite.query("select _id,name,fromT,amount from item",null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {
            /**
             * 参数1：上下文对象
             * 参数2：每个itme的布局
             * 参数3：cursor对象
             * 参数4：cursor查询的字段数组
             * 参数5：布局里控件的id
             * 参数6：标记
             */
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    MainActivity.this, R.layout.item, cursor, new String[] {
                    "_id","fromT", "name","amount"}, new int[] { R.id.text1,
                    R.id.text2,R.id.text3,R.id.time},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listView.setAdapter(adapter);
        }
    }
}
