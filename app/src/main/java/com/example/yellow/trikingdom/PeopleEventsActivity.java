package com.example.yellow.trikingdom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Yellow on 2017-11-18.
 */

public class PeopleEventsActivity extends AppCompatActivity {
    private String name;
    private String type;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_events);
        name=getIntent().getStringExtra("name");
        type=getIntent().getStringExtra("type");

        Toast.makeText(this,name,Toast.LENGTH_SHORT).show();

        initListView();
    }
    public void initListView(){
        MYSQL sql=new MYSQL(this);
        mListView= (ListView)findViewById(R.id.people_events_listview);
        if(type.equals("events")) mListView.setAdapter(sql.getnewsAdapter(this,name));
        else mListView.setAdapter(sql.getpersonAdapter(this,name));



    }

}
