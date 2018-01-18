package com.example.yellow.trikingdom;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private Uri imguri;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK&&data!=null){//people_img
            final ImageView imgv=(ImageView)findViewById(R.id.people_avatar);
            Uri imageData = data.getData();
            imguri=imageData;
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                imgv.setImageBitmap(bitmap);
            }catch (Exception e){}
        }else if(requestCode==200&&resultCode==RESULT_OK&&data!=null){//event_img
            final ImageView imgv=(ImageView)findViewById(R.id.event_img);
            Uri imageData = data.getData();
            imguri=imageData;
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                imgv.setImageBitmap(bitmap);
            }catch (Exception e){}
        }
    }
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
        final Button finish = (Button)findViewById(R.id.finish_edit);
        final Button cancel = (Button)findViewById(R.id.cancel_edit);
        if(type.equals("people")){
            final ImageView imgv=(ImageView)findViewById(R.id.people_avatar);
            final EditText nameet=(EditText) findViewById(R.id.people_name_edit);
            final EditText yearet=(EditText)findViewById(R.id.people_year_edit);
            final EditText placeet=(EditText)findViewById(R.id.people_place_edit);
            final EditText leadet=(EditText)findViewById(R.id.people_lead_edit);
            final EditText detailet=(EditText)findViewById(R.id.people_introduction_edit);
            final RadioButton sex=(RadioButton)findViewById(R.id.male_btn);
            final ImageButton people_pic=(ImageButton) findViewById(R.id.people_avatar);
            /*imgv.setBackground(ds.getDetailImage());
            nameet.setText(ds.getName());
            yearet.setText(ds.getYear());
            placeet.setText(ds.getPlace());
            leadet.setText(ds.getCountry());
            detailet.setText(ds.getDetail());
            Toast.makeText(this,ds.getYear(),Toast.LENGTH_SHORT).show();*/
            final Cursor cur=sql.getpersonAdapter(this,name).getCursor();//2"name",3/1"ppic",4"sex",5"szny",6"jg",7"zxsl",8"pdata"
            cur.moveToFirst();//name,ppic,sex ,szny ,jg ,zxsl ,pdata
            if(!cur.getString(1).equals("添加人物"))nameet.setText(cur.getString(1));
            imguri=Uri.parse(cur.getString(2));
            imgv.setImageURI(imguri);
            yearet.setText(cur.getString(4));//4
            placeet.setText(cur.getString(5));//5
            leadet.setText(cur.getString(6));//6
            detailet.setText(cur.getString(7));//7
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Cursor cur=sql.getpersonAdapter(EditActivity.this,name).getCursor();
                    String newsex;
                    if(!sex.isChecked()) newsex="女";
                    else newsex = "男";
                    if(cur.getString(1).equals("添加人物"))
                        sql.setperson(nameet.getText().toString(),imguri.toString(),newsex,yearet.getText().toString()
                                ,placeet.getText().toString(),leadet.getText().toString(),detailet.getText().toString());
                    else sql.changeperson(cur.getString(1),nameet.getText().toString(),imguri.toString(),newsex,yearet.getText().toString()
                            ,placeet.getText().toString(),leadet.getText().toString(),detailet.getText().toString());
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    EditActivity.this.finish();
                }
            });
            people_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,100);
                }
            });
        }
        else if(type.equals("events")){
            final ImageView imgv=(ImageView)findViewById(R.id.event_img);
            final EditText nameet=(EditText) findViewById(R.id.event_name_edit);
            final EditText detailet=(EditText)findViewById(R.id.event_detail);
            final Cursor cur=sql.getnewsAdapter(this,name).getCursor();
            final ImageButton event_pic = (ImageButton) findViewById(R.id.event_img) ;
            cur.moveToFirst();
            if(!cur.getString(1).equals("添加事件"))nameet.setText(cur.getString(1));
            imguri=Uri.parse(cur.getString(2));
            imgv.setImageURI(imguri);
            detailet.setText(cur.getString(3));
            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Cursor cur=sql.getpersonAdapter(EditActivity.this,name).getCursor();
                    if(cur.getString(1).equals("添加事件"))
                        sql.setnews(nameet.getText().toString(),imguri.toString(),detailet.getText().toString());
                    else  sql.changenews(cur.getString(1),nameet.getText().toString(),imguri.toString(),detailet.getText().toString());
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    EditActivity.this.finish();
                }
            });
            event_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,200);
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditActivity.this,MainActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
                EditActivity.this.finish();
            }
        });

    }
}
