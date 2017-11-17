package com.example.yellow.trikingdom;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ListView mListView=null;
    private EditText mEditText=null;
    private SeekBar mSeekBar=null;
    private int MinYear=190;
    private int MaxYear=280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSearch();
        initSeekBar();

    }

    public void initSearch(){
        mEditText=(EditText)findViewById(R.id.serach_edittext);
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
    }
    public void initSeekBar(){
        mSeekBar=(SeekBar)findViewById(R.id.year_seekbar);
        mSeekBar.setMax(90);//280-190=90
        mSeekBar.setProgress(0);
    }
    public void increaseYear(View view){
        //设置的值并非显示出来的值
        mSeekBar.setProgress(mSeekBar.getProgress()+1);
        TextView tv=(TextView)findViewById(R.id.year_event_tv);
        tv.setText("公元"+(mSeekBar.getProgress()+MinYear)+"年，群雄割据");
    }
    public void decreaseYear(View view){
        //设置的值并非显示出来的值
        mSeekBar.setProgress(mSeekBar.getProgress()-1);
        TextView tv=(TextView)findViewById(R.id.year_event_tv);
        tv.setText("公元"+(mSeekBar.getProgress()+MinYear)+"年，群雄割据");
    }

}
