package com.example.zl.ihour;

import android.app.Activity;
import android.app.AlarmManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

public class RegistersActivity extends Activity {

    private EditText toStringRegisterAccount;
    private EditText toStringRegisterPassword;
    private EditText toStringRegisterPassword2;


    private AlarmManager mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registers);
        toStringRegisterAccount = (EditText)findViewById(R.id.id_et_reg_nickname);

        toStringRegisterPassword = (EditText)findViewById(R.id.id_et_reg_password);
        toStringRegisterPassword2 = (EditText)findViewById(R.id.id_et_reg_password_again);
        Button register = (Button)findViewById(R.id.id_btn_reg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = toStringRegisterAccount.getText().toString();
                final String password = toStringRegisterPassword.getText().toString();
                Log.d("account"+account,"password"+password);
                if(account.equals("")||password.equals("")){
                    Toast.makeText(RegistersActivity.this,"账号或密码不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    new Thread() {
                        @Override
                        public void run() {
                            String result = regist(account, password);
                            if (result.equals("0")) {
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            } else if (result.equals("2")) {
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            } else if (result.equals("1")) {
                                Message msg = new Message();
                                msg.what = 3;
                                handler.sendMessage(msg);
                                finish();
                            } else {
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                            }
                        }
                    }.start();
                }
            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(RegistersActivity.this, "服务器未响应！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(RegistersActivity.this, "账号已存在！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(RegistersActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(RegistersActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public String regist(String account, String password) {
        try {
            if (!ConServer.connection.isConnected()) {
                ConServer.connection.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(ConServer.connection.getServiceName());
        reg.setUsername(account);
        reg.setPassword(password);
        reg.addAttribute("android", "geolo_createUser_android");
        PacketFilter filter = new AndFilter(new PacketIDFilter(
                reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = ConServer.connection
                .createPacketCollector(filter);
        ConServer.connection.sendPacket(reg);
        IQ result = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        // Stop queuing results
        collector.cancel();// 停止请求results（是否成功的结果）
        if (result == null) {
            return "0";
        } else if (result.getType() == IQ.Type.RESULT) {
            return "1";
        } else { // if (result.getType() == IQ.Type.ERROR)
            if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                return "2";
            } else {
                return "3";
            }
        }
    }
}