package com.example.yellow.trikingdom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yellow on 2017-11-18.
 */

public class PeopleEventsActivity extends AppCompatActivity {
    private String name;
    private String type;
    private ListView mListView;
    private String sex;
    private String place;
    private String year;
    private String country;
    private String detail;
    private int imgid;
    private android.graphics.drawable.Drawable img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_events);
        name=getIntent().getStringExtra("name");
        type=getIntent().getStringExtra("type");
        if(name.equals("添加人物")||name.equals("添加事件"))
        {
            Intent intent =new Intent(this,EditActivity.class);
            intent.putExtra("type",type);
            intent.putExtra("name",name);
            startActivity(intent);
            this.finish();
        }
        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

        initListView();
    }
    public void initListView(){
        MYSQL sql=new MYSQL(this);
        mListView= (ListView)findViewById(R.id.people_events_listview);
        mListView.setItemsCanFocus(true);
        if(type.equals("events")) mListView.setAdapter(sql.getnewsAdapter(this,name));
        else mListView.setAdapter(sql.getpersonAdapter(this,name));
    }

    public class PeopleActivity extends AppCompatActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.people_detail_layout);

            getAll();
        }
        public void getAll(){
            TextView tv=(TextView)findViewById(R.id.people_year);
            year=tv.getText().toString();
            tv=(TextView)findViewById(R.id.people_sex);
            sex=tv.getText().toString();
            tv=(TextView)findViewById(R.id.people_place);
            place=tv.getText().toString();
            tv=(TextView)findViewById(R.id.lead_by_who);
            country=tv.getText().toString();
            tv=(TextView)findViewById(R.id.people_introduction);
            detail=tv.getText().toString();
            ImageView imgbtn=(ImageView)findViewById(R.id.people_avatar);
            img=imgbtn.getBackground();
        }

    }

    public class EventsActivity extends AppCompatActivity{
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.events_detail_layout);

            getAll();
        }

        public void getAll(){
            TextView tv=(TextView)findViewById(R.id.event_name);
            detail=tv.getText().toString();
            ImageView imgv=(ImageView)findViewById(R.id.event_img);
            img=imgv.getBackground();
        }

    }

    public void backToList(View view){
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        this.finish();
    }
    public void Edit(View view){
        Intent intent =new Intent(this,EditActivity.class);
        DataShare ds=((DataShare)getApplicationContext());
        intent.putExtra("type",type);
        intent.putExtra("name",name);
        if(type.equals("people")){
            PeopleActivity pa=new PeopleActivity();
            //pa.getAll();
            ds.setDetailImage(img);
            ds.setCountry(country);
            ds.setDetail(detail);
            ds.setName(name);
            ds.setPlace(place);
            ds.setSex(sex);
            ds.setYear(year);
        }
        else if(type.equals("events")){
            EventsActivity ea=new EventsActivity();
            //ea.getAll();
            ds.setDetailImage(img);
            ds.setName(name);
            ds.setDetail(detail);
        }
        startActivity(intent);
        this.finish();
    }



}
