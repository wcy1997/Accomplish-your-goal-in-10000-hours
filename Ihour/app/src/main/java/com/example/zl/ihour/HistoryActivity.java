package com.example.zl.ihour;

import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private SQLite sqLite = new SQLite(this);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView) findViewById(R.id.listview);
        Cursor cursor1 = sqLite.query("select _id,plan,amount from item where flag=?",new String[]{"f"});
        cursor1.moveToFirst();
        if(cursor1!=null&&cursor1.getCount()>0){
            Log.i("cursor1",String.valueOf(cursor1.getCount()));
            //比较大小，看是否完成任务
            String amount = cursor1.getString(cursor1.getColumnIndex("amount"));
            String id = cursor1.getString(cursor1.getColumnIndex("_id"));
            double am = Double.parseDouble(amount);
            int plan = cursor1.getInt(cursor1.getColumnIndex("amount"));
            if(am > plan){
                sqLite.updateFlag("item",id);
            }
            while ((cursor1.moveToNext())){
                String amount2 = cursor1.getString(cursor1.getColumnIndex("amount"));
                String id2 = cursor1.getString(cursor1.getColumnIndex("_id"));
                double am2 = Double.parseDouble(amount);
                int plan2 = cursor1.getInt(cursor1.getColumnIndex("amount"));
                if(am2 > plan2){
                    sqLite.updateFlag("item",id2);
                }
            }

        }
        updataAdapter();
    }

    public void updataAdapter(){
        if (cursor != null && !cursor.isClosed())
            cursor.close();

        cursor = sqLite.query("select _id,name,fromT,amount from item where flag=?",new String[]{"t"});
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
                    HistoryActivity.this, R.layout.item, cursor, new String[] {
                    "_id","fromT", "name","amount"}, new int[] { R.id.text1,
                    R.id.text2,R.id.text3,R.id.time},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            listView.setAdapter(adapter);
        }
    }
}
