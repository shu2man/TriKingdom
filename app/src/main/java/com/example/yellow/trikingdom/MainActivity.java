package com.example.yellow.trikingdom;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.StringPrepParseException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDataBase();
        /*if(getIntent().getStringExtra("type")!=null){
            if(getIntent().getStringExtra("type").equals("events")){
                changeView(3);
            }
            else if(getIntent().getStringExtra("type").equals("people")){
                changeView(2);
            }
        }*/

        initSearch();
        initSeekBar();

        initPeopleGridView();
        initEventsGridView();
    }

    public void initSearch(){
        mEditText=(EditText)findViewById(R.id.serach_edittext);
        SearchResultShow=(ListView) findViewById(R.id.search_result);
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

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ImageButton ibtn=(ImageButton)findViewById(R.id.map_btn);
                DataShare ds=((DataShare)getApplicationContext());
                ibtn.setBackgroundResource(ds.getIcon(mSeekBar.getProgress()));
                TextView tv=(TextView)findViewById(R.id.year_event_tv);
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
        /*sql=new MYSQL(this);
        sql.recover();

        String[] eventname = new String[9];
        String[] eventpic = new String[9];
        String[] eventbrf = new String[9];

        eventname[0] = "貂蝉连环计";
        eventname[1] = "单刀赴会";
        eventname[2] = "刮骨疗毒";
        eventname[3] = "空城计";
        eventname[4] = "七擒孟获";
        eventname[5] = "三顾茅庐";
        eventname[6] = "三英战吕布";
        eventname[7] = "桃园三结义";
        eventname[8] = "三气周瑜";

        eventpic[0] = Integer.toString(R.drawable.diaochanlianhuanji);
        eventpic[1] = Integer.toString(R.drawable.dandaofuhui);
        eventpic[2] = Integer.toString(R.drawable.guaguliaodu);
        eventpic[3] = Integer.toString(R.drawable.kongchengji);
        eventpic[4] = Integer.toString(R.drawable.qiqinmenghuo);
        eventpic[5] = Integer.toString(R.drawable.sangumaolu);
        eventpic[6] = Integer.toString(R.drawable.sanyingzhanlvbu);
        eventpic[7] = Integer.toString(R.drawable.taoyuansanjieyi);
        eventpic[8] = Integer.toString(R.drawable.sanqizhouyu);

        eventbrf[0] = getString(R.string.diaochanlianhuanji);
        eventbrf[1] = getString(R.string.dandaofuhui);
        eventbrf[2] = getString(R.string.guaguliaodu);
        eventbrf[3] = getString(R.string.kongchengji);
        eventbrf[4] = getString(R.string.qiqinmenghuo);
        eventbrf[5] = getString(R.string.sangumaolu);
        eventbrf[6] = getString(R.string.sanyingzhanlvbu);
        eventbrf[7] = getString(R.string.taoyuansanjieyi);
        eventbrf[8] = getString(R.string.sanqizhouyu);

        for(int i=0;i<9;i++){
            sql.setnews(eventname[i],eventpic[i],eventbrf[i]);
        }

        String[] personname = new String[15];
        String[] personpic = new String[15];
        String[] personsex = new String[15];
        String[] personbirth = new String[15];
        String[] personhome = new String[15];
        String[] personfrom = new String[15];
        String[] personbrf = new String[15];

        personname[0] = "曹操";
        personname[1] = "貂蝉";
        personname[2] = "董卓";
        personname[3] = "关羽";
        personname[4] = "华佗";
        personname[5] = "刘备";
        personname[6] = "鲁肃";
        personname[7] = "吕布";
        personname[8] = "孟获";
        personname[9] = "司马懿";
        personname[10] = "孙权";
        personname[11] = "张飞";
        personname[12] = "赵云";
        personname[13] = "周瑜";
        personname[14] = "诸葛亮";

        personpic[0] = Integer.toString(R.drawable.cao_cao);
        personpic[1] = Integer.toString(R.drawable.diao_chan);
        personpic[2] = Integer.toString(R.drawable.dong_zhuo);
        personpic[3] = Integer.toString(R.drawable.guan_yu);
        personpic[4] = Integer.toString(R.drawable.hua_tuo);
        personpic[5] = Integer.toString(R.drawable.liu_bei);
        personpic[6] = Integer.toString(R.drawable.lu_su);
        personpic[7] = Integer.toString(R.drawable.lv_bu);
        personpic[8] = Integer.toString(R.drawable.meng_huo);
        personpic[9] = Integer.toString(R.drawable.sima_yi);
        personpic[10] = Integer.toString(R.drawable.sun_quan);
        personpic[11] = Integer.toString(R.drawable.zhang_fei);
        personpic[12] = Integer.toString(R.drawable.zhao_yun);
        personpic[13] = Integer.toString(R.drawable.zhou_yu);
        personpic[14] = Integer.toString(R.drawable.zhuge_liang);

        personsex[0] = "男";
        personsex[1] = "女";
        personsex[2] = "男";
        personsex[3] = "男";
        personsex[4] = "男";
        personsex[5] = "男";
        personsex[6] = "男";
        personsex[7] = "男";
        personsex[8] = "男";
        personsex[9] = "男";
        personsex[10] = "男";
        personsex[11] = "男";
        personsex[12] = "男";
        personsex[13] = "男";
        personsex[14] = "男";

        personbirth[0] = "（150-220）";
        personbirth[1] = "（？-？）";
        personbirth[2] = "（？-192）";
        personbirth[3] = "（161-220）";
        personbirth[4] = "（145-208）";
        personbirth[5] = "（161-223）";
        personbirth[6] = "（172-217）";
        personbirth[7] = "（？-198）";
        personbirth[8] = "（？-？）";
        personbirth[9] = "（179-251）";
        personbirth[10] = "（182-252）";
        personbirth[11] = "（？-221）";
        personbirth[12] = "（？-229）";
        personbirth[13] = "（175-210）";
        personbirth[14] = "（181-234）";

        personhome[0] = "沛国谯县人";
        personhome[1] = "关西临洮人";
        personhome[2] = "陇西临洮人";
        personhome[3] = "河东解良人";
        personhome[4] = "沛国谯人";
        personhome[5] = "幽州涿郡人";
        personhome[6] = "临淮郡东城县人";
        personhome[7] = "并州五原郡九原人";
        personhome[8] = "益州益州郡人";
        personhome[9] = "司州河内郡人";
        personhome[10] = "扬州吴郡富春人";
        personhome[11] = "幽州涿郡人";
        personhome[12] = "冀州常山国真定人";
        personhome[13] = "扬州庐江郡舒人";
        personhome[14] = "徐州琅邪国阳都人";

        personfrom[0] = "所属： 魏国";
        personfrom[1] = "所属： 群雄";
        personfrom[2] = "所属： 群雄";
        personfrom[3] = "所属： 蜀国";
        personfrom[4] = "所属： 群雄";
        personfrom[5] = "所属： 蜀国";
        personfrom[6] = "所属： 吴国";
        personfrom[7] = "所属： 群雄";
        personfrom[8] = "所属： 吴国";
        personfrom[9] = "所属： 魏国";
        personfrom[10] = "所属： 吴国";
        personfrom[11] = "所属： 蜀国";
        personfrom[12] = "所属： 蜀国";
        personfrom[13] = "所属： 吴国";
        personfrom[14] = "所属： 蜀国";

        personbrf[0] = getString(R.string.caocao_brf);
        personbrf[1] = getString(R.string.diaochan_brf);
        personbrf[2] = getString(R.string.dongzhuo_brf);
        personbrf[3] = getString(R.string.guanyu_brf);
        personbrf[4] = getString(R.string.huatuo_brf);
        personbrf[5] = getString(R.string.liubei_brf);
        personbrf[6] = getString(R.string.lusu_brf);
        personbrf[7] = getString(R.string.lvbu_brf);
        personbrf[8] = getString(R.string.menghuo_brf);
        personbrf[9] = getString(R.string.simayi_brf);
        personbrf[10] = getString(R.string.sunquan_brf);
        personbrf[11] = getString(R.string.zhangfei_brf);
        personbrf[12] = getString(R.string.zhaoyun_brf);
        personbrf[13] = getString(R.string.zhouyu_brf);
        personbrf[14] = getString(R.string.zhugeliang_brf);

        for(int i=0;i<15;i++){
            sql.setperson(personname[i],personpic[i],personsex[i],personbirth[i],personhome[i],personfrom[i],personbrf[i]);
        }*/

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
            CLMap.setVisibility(View.VISIBLE);
            CLEvents.setVisibility(View.GONE);
            CLPeople.setVisibility(View.GONE);

            mapbtn.setImageResource(R.drawable.map_on);
            eventsbtn.setImageResource(R.drawable.events_off);
            peoplebtn.setImageResource(R.drawable.people_off);
        }
        else if(i==2){
            CLMap.setVisibility(View.GONE);
            CLEvents.setVisibility(View.GONE);
            CLPeople.setVisibility(View.VISIBLE);

            mapbtn.setImageResource(R.drawable.map_off);
            eventsbtn.setImageResource(R.drawable.events_off);
            peoplebtn.setImageResource(R.drawable.people_on);
        }
        else if(i==3){
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
    }


}
