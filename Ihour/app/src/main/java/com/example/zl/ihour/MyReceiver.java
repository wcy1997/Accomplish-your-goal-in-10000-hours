package com.example.zl.ihour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zl on 2016/11/23.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String id = intent.getStringExtra("id");
        Intent i = new Intent(context, Alarm.class);
        i.putExtra("id",id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//这一句很重要，不然会出错
        context.startActivity(i);
        Log.i("aaaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaa");
    }
}
