package com.example.zl.ihour;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.util.StringUtils;

public class LoginActivity extends Activity {
    private EditText toStringLoginAccount;
    private EditText toStringLoginPassword;
    private Button registerButton;
    private Button loginButton;

    //用来记录是否登录
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE); //私有数据
        editor = sharedPreferences.edit();//获取编辑器


        if(sharedPreferences.getBoolean("login",false)){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            toStringLoginAccount = (EditText) findViewById(R.id.id_et_login_tel);
            toStringLoginPassword = (EditText) findViewById(R.id.id_et_password);
            registerButton = (Button) findViewById(R.id.id_btn_go_reg);
            loginButton = (Button) findViewById(R.id.id_btn_login);
            // 跳转到注册界面
            registerButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, RegistersActivity.class);
                    startActivity(intent);
                }
            });
            loginButton.setOnClickListener(new loginListener());


        }


    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //如果登录成功
                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("login",true);
                    editor.commit();
                    break;
                case 2:
                    //如果登录失败
                    Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    class loginListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            final String account = toStringLoginAccount.getText().toString();
            final String password = toStringLoginPassword.getText().toString();
            Log.i("iiiiiiiiiiiiiiiii",account);
            Log.i("iiiiiiiiiiiiiiiii",password);
            if (account.equals("") || password.equals("")) {
                Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
            } else {
                new Thread() {
                    @Override
                    public void run(){
                        ConServer.getInstance();
                        boolean b = login(account, password);
                        if(b) {
                            ConServer.useraccount = account;
                            ConServer.userpassword = password;

                            Message msg = new Message();
                            msg.what = 1;

//                            Bundle bd = new Bundle();
//                            bd.putString("account", account);

                            handler.sendMessage(msg);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }
                }.start();
            }
        }
    }

    public boolean login(String a, String p) {
        try {
            if (!ConServer.connection.isConnected()) {
                ConServer.connection.connect();
            }
            /** 登录 */
            ConServer.connection.login(a, p);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }



        return true;
    }

//    private int count = 1;
//    @Override
//    public void finish(){
//        if(count == 1) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("确定要退出吗");
//            builder.setNegativeButton("取消", null);
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    count++;
//                    finish();
//                }
//            });
//            builder.create();
//            builder.show();
//        }
//        else{
//            super.finish();
//        }
//    }

}