package com.example.yellow.trikingdom;

import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yellow on 2017-11-20.
 */

public class EditActivity extends AppCompatActivity {
    private String type;
    private String name;
    private MYSQL sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name=getIntent().getStringExtra("name");
        type=getIntent().getStringExtra("type");
        changeView();

        initViewData();

    }

    public void changeView(){
        ConstraintLayout clp=(ConstraintLayout)findViewById(R.id.people_edit_container);
        ConstraintLayout cle=(ConstraintLayout)findViewById(R.id.event_edit_container);
        if(type.equals("people")){
            clp.setVisibility(View.VISIBLE);
            cle.setVisibility(View.GONE);
        }
        else if(type.equals("events")){
            clp.setVisibility(View.GONE);
            cle.setVisibility(View.VISIBLE);
        }
    }
    public void initViewData(){
        sql=new MYSQL(this);
        DataShare ds=((DataShare)getApplicationContext());
        if(type.equals("people")){
            ImageView imgv=(ImageView)findViewById(R.id.people_avatar);
            EditText nameet=(EditText) findViewById(R.id.people_name_edit);
            EditText yearet=(EditText)findViewById(R.id.people_year_edit);
            EditText placeet=(EditText)findViewById(R.id.people_place_edit);
            EditText leadet=(EditText)findViewById(R.id.people_lead_edit);
            EditText detailet=(EditText)findViewById(R.id.people_introduction_edit);
            /*imgv.setBackground(ds.getDetailImage());
            nameet.setText(ds.getName());
            yearet.setText(ds.getYear());
            placeet.setText(ds.getPlace());
            leadet.setText(ds.getCountry());
            detailet.setText(ds.getDetail());
            Toast.makeText(this,ds.getYear(),Toast.LENGTH_SHORT).show();*/

            Cursor cur=sql.getpersonAdapter(this,name).getCursor();//2"name",3/1"ppic",4"sex",5"szny",6"jg",7"zxsl",8"pdata"
            cur.moveToFirst();                                              //name,ppic,sex ,szny ,jg ,zxsl ,pdata
            nameet.setText(cur.getString(1));
            imgv.setBackgroundResource(cur.getInt(0));
            yearet.setText(cur.getString(4));//4
            placeet.setText(cur.getString(5));//5
            leadet.setText(cur.getString(6));//6
            detailet.setText(cur.getString(7));//7
        }
        else if(type.equals("events")){

        }
    }


}
