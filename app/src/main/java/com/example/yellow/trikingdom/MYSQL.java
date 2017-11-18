package com.example.yellow.trikingdom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLDataException;

/**
 * Created by asus2 on 2017/11/18.
 */

public class MYSQL{
    private SQLiteDatabase db;
    public MYSQL(Context c){
        openSQL(c);
        createtable();
    }
    public void openSQL(Context c){
        db = SQLiteDatabase.openOrCreateDatabase(c.getFilesDir().toString()+"/mysql.db3",null);
    }
    public void closeSQL(){
        if(db != null && db.isOpen()){
            db.close();
        }
    }
    public void useSQL(String s){
        db.execSQL(s);
    }
    public void createtable(){
        try{
            db.execSQL("create table person(_id integer primary key autoincrement,pname varchar(10),ppic integer,sex varchar(4),szny varchar(40),jg varchar(10),zxsl varchar(10),pdata integer)");
            db.execSQL("create table news(_id integer primary key autoincrement,nname varchar(20),npic integer,data integer)");
        }
        catch (SQLiteException e) {}
    }
    public void setperson(String pname,String ppic,String sex,String szny,String jg,String zxsl,String pdata){
        try{
            db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{ppic,pname,ppic,sex,szny,jg,zxsl,pdata});
        }
        catch (SQLiteException e) {}
    }
    public void setnews(String nname,String npic,String data){
        try{
            db.execSQL("insert into news values(?,?,?,?)",new String[]{npic,nname,npic,data});
        }
        catch (SQLiteException e) {}
    }
    public SimpleCursorAdapter getpersonAdapter(Context ct,String name){
        return new SimpleCursorAdapter(ct,R.layout.people_detail_layout,selectp(name),new String[]{"pname","ppic","sex","szny","jg","zxsl","pdata"},
                new int[]{R.id.people_name,R.id.people_avatar,R.id.people_sex,R.id.people_year,R.id.people_place,R.id.lead_by_who,R.id.people_introduction});//**
    }
    public SimpleCursorAdapter getnewsAdapter(Context ct,String name){
        return new SimpleCursorAdapter(ct,R.layout.events_detail_layout,selectn(name),new String[]{"nname","npic","data"},
                new int[]{R.id.event_name,R.id.event_img,R.id.event_detail});
    }
    public Cursor selectp(String c)
    {
        return db.rawQuery("select * from person where pname = ?",new String[]{c});
    }
    public Cursor selectn(String c)
    {
        return db.rawQuery("select * from news where nname = ?",new String[]{c});
    }
	/*public Cursor selectall(String c)
	{
		return db.rawQuery("(select * from news where nname like _?)",new String[]{c});
	}*/
    public void deletetable()
    {
        db.execSQL("drop table preson");
        db.execSQL("drop table news");
    }
}
