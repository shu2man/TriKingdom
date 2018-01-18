package com.example.yellow.trikingdom;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.StringPrepParseException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /*@Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        super.onTouchEvent(motionEvent);
        return true;
    }*/
    public ServiceConnection sc;
    public IBinder ib;
    private ListView mListView=null;
    private EditText mEditText=null;
    private SeekBar mSeekBar=null;
    private GridView mGridViewE=null;
    private GridView mGridViewP=null;
    private int MinYear=190;
    private int MaxYear=280;
    private int NumofEvents=55;
    private ListView SearchResultShow;
    private SimpleCursorAdapter mCursorAdapter;
    private MYSQL sql;
    private MediaPlayer mMediaPlayer;
    private GestureDetector gestureDetector;
    private int whichview=1;
    private boolean isplaying=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        if(getIntent().getStringExtra("type")!=null){
            if(getIntent().getStringExtra("type").equals("events")){
                changeView(3);
            }
            else if(getIntent().getStringExtra("type").equals("people")){
                changeView(2);
            }
        }

        initSearch();
        initSeekBar();
        initMusic();
        initPeopleGridView();
        initEventsGridView();
    }

    public void initMusic()
    {
        //final FloatingActionButton stop_start=(FloatingActionButton) findViewById(R.id.start_or_stop_music);
        final ImageButton ibtn=(ImageButton)findViewById(R.id.music_ibtn);
        Intent intent = new Intent(this,MyMusicService.class);
        startService(intent);
        sc=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ib = service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sc=null;
            }
        };
        bindService(intent,sc, Context.BIND_AUTO_CREATE);
        ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int code = 101;
                    Parcel data =Parcel.obtain();
                    Parcel reply =Parcel.obtain();
                    ib.transact(code,data,reply,0);
                    if (isplaying) {
                        ibtn.setImageResource(R.drawable.music_on);
                        isplaying=false;
                    }
                    else{
                        ibtn.setImageResource(R.drawable.music_off);
                        isplaying=true;
                    }
                }
                catch (RemoteException e){}
            }
        });
    }

    public void initSearch(){
        mEditText=(EditText)findViewById(R.id.serach_edittext);
        SearchResultShow=(ListView) findViewById(R.id.search_result);
        SearchResultShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view.findViewById(R.id.search_result_name)).getText().toString();
                Intent intent=new Intent(MainActivity.this,PeopleEventsActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("type",sql.gettype(MainActivity.this,name));
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        final ConstraintLayout CL=(ConstraintLayout)findViewById(R.id.container_map);
        CL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CL.requestFocus();
                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);//
                mEditText.setCursorVisible(false);
                return false;
            }
        });
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setCursorVisible(true);
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=mEditText.getText().toString();
                SearchResultShow.setAdapter(sql.searchAdapter(MainActivity.this,text));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mEditText.getText().length()==0) SearchResultShow.setVisibility(View.GONE);
                else SearchResultShow.setVisibility(View.VISIBLE);
            }
        });

    }
    public void initSeekBar(){
        mSeekBar=(SeekBar)findViewById(R.id.year_seekbar);
        mSeekBar.setMax(NumofEvents-1);//共有54件事
        mSeekBar.setProgress(0);
        final ImageButton ibtn=(ImageButton)findViewById(R.id.map_btn);
        final DataShare ds=((DataShare)getApplicationContext());
        final TextView tv=(TextView)findViewById(R.id.year_event_tv);
        gestureDetector=new GestureDetector(MainActivity.this,new mapGestureListener());
        ibtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ibtn.setBackgroundResource(ds.getIcon(mSeekBar.getProgress()));
                if(mSeekBar.getProgress()==0) tv.setText(ds.getEvents(mSeekBar.getProgress()));
                else {
                    tv.setText("公元" + ds.getYear(mSeekBar.getProgress()) + "年，" +
                            ds.getEvents(mSeekBar.getProgress()));
                }
            }
        });
    }

    public void initDataBase(){
        DataShare ds=((DataShare)getApplicationContext());
        ds.initDataBase();
        sql=new MYSQL(this);

/*        sql=new MYSQL(this);
        sql.recover();
        sql.setperson("曹操",Integer.toString(R.drawable.cao_cao),"男","(155~220)","沛国谯县人","魏国统领",getString(R.string.caocao));
        sql.setnews("空城计",Integer.toString(R.drawable.kongchengji),getString(R.string.kongchengji));*/

        //sql.deletetable();
        //sql.closeSQL();
    }

    public void initPeopleGridView(){
        mGridViewP=(GridView)findViewById(R.id.people_list_gridview);
        /*List<Map<String,Object>> data_list;
        data_list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<23;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("img",R.drawable.cao_cao);
            map.put("name","曹操");
            data_list.add(map);
        }
        String[] from={"img","name"};
        int[] to={R.id.img_people_item,R.id.name_people_item};
        SimpleAdapter mAdapter=new SimpleAdapter(this,data_list,R.layout.item_gridview_people,from,to);*/
        mGridViewP.setAdapter(sql.poeplelistAdapter(this));
        mGridViewP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)view.findViewById(R.id.name_people_item);
                goToDetail(tv.getText().toString(),"people");
            }
        });
        mGridViewP.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView tv=(TextView)view.findViewById(R.id.name_people_item);
                if(!tv.getText().toString().equals("添加人物"))
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                    ab.setTitle("移除人物");
                    ab.setNegativeButton("取消",null);
                    ab.setMessage("是否移除人物:"+tv.getText().toString());
                    ab.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i)
                        {
                            sql.deletep(tv.getText().toString());
                            mGridViewP.setAdapter(sql.poeplelistAdapter(MainActivity.this));
                        }
                    });
                    ab.create();
                    ab.show();
                }
                return true;
            }
        });
        /*mGridViewP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new GestureDetector(MainActivity.this,new peopleviewGestureListener()).onTouchEvent(event);
                return true;
            }
        });*/
    }
    public void initEventsGridView(){
        mGridViewE=(GridView)findViewById(R.id.events_list_gridview);
        /*List<Map<String,Object>> data_list;
        data_list=new ArrayList<Map<String,Object>>();
        for(int i=0;i<14;i++){
            Map<String,Object> map=new HashMap<String,Object>();
            map.put("img",R.drawable.kongchengji);
            map.put("name","空城计");
            data_list.add(map);
        }
        String[] from={"img","name"};
        int[] to={R.id.img_event_item,R.id.name_event_item};
        SimpleAdapter mAdapter=new SimpleAdapter(this,data_list,R.layout.item_gridview_event,from,to);*/
        mGridViewE.setAdapter(sql.newslistAdapter(this));
        mGridViewE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)view.findViewById(R.id.name_event_item);
                goToDetail(tv.getText().toString(),"events");
            }
        });
        mGridViewE.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView tv=(TextView)view.findViewById(R.id.name_event_item);
                if(!tv.getText().toString().equals("添加事件"))
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                    ab.setTitle("移除事件");
                    ab.setNegativeButton("取消",null);
                    ab.setMessage("是否移除事件:"+tv.getText().toString());
                    ab.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i)
                        {
                            sql.deleten(tv.getText().toString());
                            mGridViewE.setAdapter(sql.newslistAdapter(MainActivity.this));
                        }
                    });
                    ab.create();
                    ab.show();
                }
                return true;
            }
        });
        /*mGridViewE.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new GestureDetector(MainActivity.this,new eventviewGestureListener()).onTouchEvent(event);
                return true;
            }
        });*/
    }

    public void increaseYear(View view){
        //设置的值并非显示出来的值
        changeMapText(1);
    }
    public void decreaseYear(View view){
        changeMapText(2);
    }
    public void changeMapText(int i){
        if(i==1) mSeekBar.setProgress(mSeekBar.getProgress()+1);
        else mSeekBar.setProgress(mSeekBar.getProgress()-1);

        DataShare ds=((DataShare)getApplicationContext());
        TextView tv=(TextView)findViewById(R.id.year_event_tv);
        if(mSeekBar.getProgress()==0) tv.setText(ds.getEvents(mSeekBar.getProgress()));
        else {
            tv.setText("公元" + ds.getYear(mSeekBar.getProgress()) + "年，" +
                    ds.getEvents(mSeekBar.getProgress()));
        }
        ImageButton ibtn=(ImageButton)findViewById(R.id.map_btn);
        ibtn.setBackgroundResource(ds.getIcon(mSeekBar.getProgress()));
    }


    public void showMap(View view){
        changeView(1);
    }
    public void showPeopleList(View view){
        changeView(2);
    }
    public void showEventList(View view){
        changeView(3);
    }

    public void changeView(int i){
        ConstraintLayout CLMap=(ConstraintLayout)findViewById(R.id.container_map);
        ConstraintLayout CLEvents=(ConstraintLayout)findViewById(R.id.container_events);
        ConstraintLayout CLPeople=(ConstraintLayout)findViewById(R.id.container_people);
        ImageButton mapbtn=(ImageButton)findViewById(R.id.map_ibtn);
        ImageButton peoplebtn=(ImageButton)findViewById(R.id.people_ibtn);
        ImageButton eventsbtn=(ImageButton)findViewById(R.id.events_ibtn);
        if(i==1){
            whichview=1;
            CLMap.setVisibility(View.VISIBLE);
            CLEvents.setVisibility(View.GONE);
            CLPeople.setVisibility(View.GONE);

            mapbtn.setImageResource(R.drawable.map_on);
            eventsbtn.setImageResource(R.drawable.events_off);
            peoplebtn.setImageResource(R.drawable.people_off);
        }
        else if(i==2){
            whichview=2;
            CLMap.setVisibility(View.GONE);
            CLEvents.setVisibility(View.GONE);
            CLPeople.setVisibility(View.VISIBLE);

            mapbtn.setImageResource(R.drawable.map_off);
            eventsbtn.setImageResource(R.drawable.events_off);
            peoplebtn.setImageResource(R.drawable.people_on);
        }
        else if(i==3){
            whichview=3;
            CLMap.setVisibility(View.GONE);
            CLEvents.setVisibility(View.VISIBLE);
            CLPeople.setVisibility(View.GONE);

            mapbtn.setImageResource(R.drawable.map_off);
            eventsbtn.setImageResource(R.drawable.events_on);
            peoplebtn.setImageResource(R.drawable.people_off);
        }
    }

    public void goToDetail(String name,String type){
        Intent intent=new Intent(this,PeopleEventsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("type",type);
        startActivity(intent);
        MainActivity.this.finish();
    }
    class mapGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final ImageButton ibtn=(ImageButton)findViewById(R.id.map_btn);
            final DataShare ds=((DataShare)getApplicationContext());
            final TextView tv=(TextView)findViewById(R.id.year_event_tv);
            if(velocityX<-100)
            {
                if(mSeekBar.getProgress()<mSeekBar.getMax())
                    mSeekBar.setProgress(mSeekBar.getProgress()+1);
                ibtn.setBackgroundResource(ds.getIcon(mSeekBar.getProgress()));
                if(mSeekBar.getProgress()==0) tv.setText(ds.getEvents(mSeekBar.getProgress()));
                else {
                    tv.setText("公元" + ds.getYear(mSeekBar.getProgress()) + "年，" +
                            ds.getEvents(mSeekBar.getProgress()));
                }
            }
            else if(velocityX>100)
            {
                if(mSeekBar.getProgress()>0)
                    mSeekBar.setProgress(mSeekBar.getProgress()-1);
                ibtn.setBackgroundResource(ds.getIcon(mSeekBar.getProgress()));
                if(mSeekBar.getProgress()==0) tv.setText(ds.getEvents(mSeekBar.getProgress()));
                else {
                    tv.setText("公元" + ds.getYear(mSeekBar.getProgress()) + "年，" +
                            ds.getEvents(mSeekBar.getProgress()));
                }
            }
            return false;
        }
    }
    /*class peopleviewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(velocityX<-100)
                changeView(3);
            else if(velocityX>100)
                changeView(1);
            return false;
        }
    }
    class eventviewGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(velocityX<-100)
                changeView(1);
            else if(velocityX>100)
                changeView(2);
            return false;
        }
    }*/

}
