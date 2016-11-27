package com.example.zl.ihour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zl on 2016/11/20.
 */
public class SQLite extends SQLiteOpenHelper {
    private  final static int DATABASE_VERSION = 1;
    //数据库的文件名，文件扩展名可以任意取，也可以没有扩展名
    private final static String DATABASE_NAME = "1.db";

    //当读写数据库时会调用OnCreate方法建立scheduleList.db表
    //数据库由SQLiteOpenHelper系统自动创建
    @Override
    public void onCreate(SQLiteDatabase db){
        //创建表的SQL语句
        db.execSQL("create table [item] (" + "[_id] integer,"
                + "[name] varchar[50],"
                + "[importance] integer,"
                + "[plan] integer,"
                + "[amount] varchar[100],"
                + "[fromT] varchar[100],"
                + "[pattern] integer,"
                + "[flag] varchar[100],"
                + "PRIMARY KEY([_id]))");
        db.execSQL("create table [record] (" + "[_id] integer,"
                + "[name] varchar[100],"
                + "[date] varchar[100],"
                + "[time] varchar[100],"
                + "[pattern] integer,"
                + "PRIMARY KEY([_id]))");

    }

    public SQLite(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//    public SQLite(){
//        super(Context context,DATABASE_NAME,null,DATABASE_VERSION);
//    }

    //由于不打算对scheduleList.db进行升级，因此，在该方法中没有任何代码
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    //执行读操作
    public Cursor read(String sql, String[] args){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return  cursor;
    }
    //执行select语句
    public Cursor query(String sql, String[] args){
        //调用getRreadableDatabase方法的过程中,若不存在数据库，系统会调用onCreate方法
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return  cursor;
    }

    //执行写语句
    public void write(String sql, Object[] args){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("sql", sql);
        db.execSQL(sql, args);
    }

    //执行deleteAll语句
    public void deleteAllContactPerson(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from friendList";
        db.execSQL(sql);
    }
    public void deleteAllbody(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "delete from messageList";
        db.execSQL(sql);
    }
    public void delete(String table,String _id,String[] v){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table,_id,v);
    }

    public void update(String table,String time,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount",time);
        db.update(table,values,"_id=?",new String[]{id});
    }

    public void update2(String table,String v,String date,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("time",v);
        db.update(table,values,"date=? and name=?",new String[]{date,name});
    }
    public void updateFlag(String table,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("flag","t");
        db.update(table,values,"_id=?",new String[]{id});
    }
    public void excute(String sql,Object[] args){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql,args);
    }
}
