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
            db.execSQL("create table if not exists person(_id integer primary key autoincrement,name varchar(10),ppic integer,sex varchar(4),szny varchar(40),jg varchar(10),zxsl varchar(10),pdata integer)");
            db.execSQL("create table if not exists news(_id integer primary key autoincrement,name varchar(10),npic integer,data integer)");
            db.execSQL("create table if not exists allname(_id integer primary key autoincrement,name varchar(10),pic)");
            db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{"1","添加人物",Integer.toString(R.drawable.jiahao),null,null,null,null,null});
            db.execSQL("insert into news values(?,?,?,?)",new String[]{"1","添加事件",Integer.toString(R.drawable.jiahao),null});
            /*for(int i = 0 ; i< 15;i++)
                setperson(peoplename[i],peoplepic[i],peoplesex[i],peopleszny[i],peoplejg[i],peoplezxsl[i],peopledata[i]);
            for(int i = 0 ; i< 15;i++)
                setnews(newsname[i],newspic[i],newsdata[i]);*/
        }
        catch (SQLiteException e) {}
    }
    public void setperson(String name,String ppic,String sex,String szny,String jg,String zxsl,String pdata){
        try{
            int temp;
            Cursor n=selectp(name);
            Cursor c=selectp("添加人物");
            if(n.moveToFirst())
            {
                temp=n.getInt(0);
                deletep(name);
                db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{Integer.toString(temp),name,ppic,sex,szny,jg,zxsl,pdata});
                db.execSQL("insert into allname values(?,?,?)",new String[]{null,name,ppic});
            }
            else{
                c.moveToFirst();
                temp=c.getInt(0);
                deletep("添加人物");
                db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{Integer.toString(temp),name,ppic,sex,szny,jg,zxsl,pdata});
                db.execSQL("insert into allname values(?,?,?)",new String[]{null,name,ppic});
                db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{Integer.toString(temp+1),"添加人物",Integer.toString(R.drawable.jiahao),null,null,null,null,null});
            }

        }
        catch (SQLiteException e) {}
    }
    public void changeperson(String name,String newname ,String ppic,String sex,String szny,String jg,String zxsl,String pdata){
        try{
            int temp;
            Cursor n=selectp(name);
            n.moveToFirst();
            temp=n.getInt(0);
            deletep(name);
            db.execSQL("insert into person values(?,?,?,?,?,?,?,?)",new String[]{Integer.toString(temp),newname,ppic,sex,szny,jg,zxsl,pdata});
            db.execSQL("insert into allname values(?,?,?)",new String[]{null,newname,ppic});
        }
        catch (SQLiteException e) {}
    }
    public void setnews(String name,String npic,String data){
        try{
            int temps;
            Cursor n=selectn(name);
            Cursor c=selectn("添加事件");
            if(n.moveToFirst())
            {
                temps=n.getInt(0);
                deleten(name);
                db.execSQL("insert into news values(?,?,?,?)",new String[]{Integer.toString(temps),name,npic,data});
                db.execSQL("insert into allname values(?,?,?)",new String[]{null,name,npic});
            }
            else{
                c.moveToFirst();
                temps=c.getInt(0);
                deleten("添加事件");
                db.execSQL("insert into news values(?,?,?,?)",new String[]{Integer.toString(temps),name,npic,data});
                db.execSQL("insert into allname values(?,?,?)",new String[]{null,name,npic});
                db.execSQL("insert into news values(?,?,?,?)",new String[]{Integer.toString(temps+1),"添加事件",Integer.toString(R.drawable.jiahao),null});
            }

        }
        catch (SQLiteException e) {}
    }
    public void changenews(String name,String newname,String npic,String data){
        try{
            int temps;
            Cursor n=selectn(name);
            n.moveToFirst();
            temps=n.getInt(0);
            deleten(name);
            db.execSQL("insert into news values(?,?,?,?)",new String[]{Integer.toString(temps),newname,npic,data});
            db.execSQL("insert into allname values(?,?,?)",new String[]{null,newname,npic});
        }
        catch (SQLiteException e) {}
    }
    public void deletep(String c)
    {
        try{
            db.execSQL("delete from person where name = ?",new String[]{c});
            db.execSQL("delete from allname where name = ?",new String[]{c});
        }
        catch (SQLiteException e) {}
    }
    public void deleten(String c)
    {
        try{
            db.execSQL("delete from news where name = ?",new String[]{c});
            db.execSQL("delete from allname where name = ?",new String[]{c});
        }
        catch (SQLiteException e) {}
    }
    public SimpleCursorAdapter getpersonAdapter(Context ct,String name){
        return new SimpleCursorAdapter(ct,R.layout.people_detail_layout,selectp(name),new String[]{"name","ppic","sex","szny","jg","zxsl","pdata"},
                new int[]{R.id.people_name,R.id.people_avatar,R.id.people_sex,R.id.people_year,R.id.people_place,R.id.lead_by_who,R.id.people_introduction});
    }
    public SimpleCursorAdapter getnewsAdapter(Context ct,String name){
        return new SimpleCursorAdapter(ct,R.layout.events_detail_layout,selectn(name),new String[]{"name","npic","data"},
                new int[]{R.id.event_name,R.id.event_img,R.id.event_detail});
    }
    public SimpleCursorAdapter poeplelistAdapter(Context ct){
        return new SimpleCursorAdapter(ct,R.layout.item_gridview_people,allp(),new String[]{"name","ppic"},
                new int[]{R.id.name_people_item,R.id.img_people_item});
    }
    public SimpleCursorAdapter newslistAdapter(Context ct){
        return new SimpleCursorAdapter(ct,R.layout.item_gridview_event,alln(),new String[]{"name","npic"},
                new int[]{R.id.name_event_item,R.id.img_event_item});
    }
    public SimpleCursorAdapter searchAdapter(Context ct,String name){
        return new SimpleCursorAdapter(ct,R.layout.item_listview_search_result,selectall(name),new String[]{"name"},
                new int[]{R.id.search_result_name});
    }
    public SimpleCursorAdapter Adapterpus(Context ct,String name){
        Cursor c=selectp(name);
        Cursor cc=selectn(name);
        if(c.moveToPosition(0))//selectp(name).moveToPosition(0)
            return new SimpleCursorAdapter(ct,R.layout.people_detail_layout,c,new String[]{"name","ppic","sex","szny","jg","zxsl","pdata"},
                    new int[]{R.id.people_name,R.id.people_avatar,R.id.people_sex,R.id.people_year,R.id.people_place,R.id.lead_by_who,R.id.people_introduction});
        else if(cc.moveToPosition(0))
            return new SimpleCursorAdapter(ct,R.layout.events_detail_layout,cc,new String[]{"name","npic","data"},
                    new int[]{R.id.event_name,R.id.event_img,R.id.event_detail});
        else return null;
    }
    public String gettype(Context ct,String name){
        Cursor c=selectp(name);
        Cursor cc=selectn(name);
        if(c.moveToPosition(0))//selectp(name).moveToPosition(0)
            return "people";
        else if(cc.moveToPosition(0))
            return "events";
        else return null;
    }
    public Cursor selectp(String c)
    {
        return db.rawQuery("select * from person where name = ?",new String[]{c});
    }
    public Cursor selectn(String c)
    {
        return db.rawQuery("select * from news where name = ?",new String[]{c});
    }
    public Cursor allp()
    {
        return db.rawQuery("select * from person",null);
    }
    public Cursor alln()
    {
        return db.rawQuery("select * from news",null);
    }
    public Cursor selectall(String c)
    {
        //return db.rawQuery("(select * from person where name like ? and rownum <= 4)union(select * from person where name like ? and rownum <= 4)union(select * from news where name like ? and rownum <= 4)union(select * from news where name like ? and rownum <= 4)",new String[]{"%"+c+"%","%"+c+"%","%["+c+"]%","%["+c+"]%"});
        return db.rawQuery(" select * from allname where name like ?",new String[]{"%"+c+"%"});
    }
    public void deletetable()
    {
        try{
            db.execSQL("drop table preson");
            db.execSQL("drop table news");
            db.execSQL("drop table allname");
        }
        catch (SQLiteException e){}
    }
    public void recover()
    {
        deletetable();
        createtable();
    }

}