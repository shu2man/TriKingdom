package com.example.yellow.trikingdom;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Yellow on 2017-11-18.
 */

public class DataShare extends Application {
    private List<String> Year;
    private List<String> Events;
    private int[] Icon={R.drawable.map_all,R.drawable.map190,R.drawable.map191,R.drawable.map192,R.drawable.map193,R.drawable.map194,
            R.drawable.map196,R.drawable.map197,R.drawable.map198,R.drawable.map199,R.drawable.map200,R.drawable.map201,
            R.drawable.map202,R.drawable.map203,R.drawable.map204,R.drawable.map205,R.drawable.map206,R.drawable.map207,
            R.drawable.map208,R.drawable.map209,R.drawable.map210,R.drawable.map211,R.drawable.map212,R.drawable.map213,
            R.drawable.map214,R.drawable.map215,R.drawable.map216,R.drawable.map217,R.drawable.map218,R.drawable.map219_1,
            R.drawable.map219_2,R.drawable.map220,R.drawable.map221,R.drawable.map222,R.drawable.map223,R.drawable.map225,
            R.drawable.map226,R.drawable.map228,R.drawable.map229,R.drawable.map230,R.drawable.map231,R.drawable.map232,
            R.drawable.map233,R.drawable.map234,R.drawable.map235,R.drawable.map236,R.drawable.map237,R.drawable.map238,
            R.drawable.map239,R.drawable.map241,R.drawable.map242,R.drawable.map243,R.drawable.map244,R.drawable.map245,
            R.drawable.map280 };

    private List<String> SearchEvent;
    private List<String> SearchName;

    private android.graphics.drawable.Drawable DetailImage;
    private String name;
    private String year;
    private String sex;
    private String place;
    private String country;
    private String detail;

    public DataShare(){
        Year=new ArrayList<String>();
        Events=new ArrayList<String>();
        SearchEvent=new ArrayList<String>();
        SearchName=new ArrayList<String>();

        String[] name={"刘备","曹操","关羽","张飞","赵云","貂蝉","孙权","周瑜","诸葛亮",
                "黄忠","吕布","鲁肃","陆逊","马超","甘宁","乐进","徐晃","董卓"};
        String[] year={"","190","191","192","193","194","196","197","198","199","200",
                "201","202","203","204","205","206","207","208","209","210","211","212","213",
                "214","215","216","217","218","219-1","219-2","220","221","222","223","225",
                "226","228","229","230","231","232","233","234","235","236","237","238","239",
                "241","242","243","244","245","280"};
        String[] events={"群雄并起，逐鹿中原","讨伐董卓","群雄割据","帝出长安","曹操围徐州","三让徐州","迁都许昌",
                "袁术称帝","曹操斩吕布","曹袁不和","官渡之战","刘备驻新野","袁家之争","袁家内战",
                "曹操破邺","幽冀降曹","曹并平州","三顾茅庐","赤壁之战","孙权躲南郡","刘备借荆州",
                "刘备入蜀","刘备攻川","曹操封公","刘备收蜀","孙刘分荆州","曹操称王","鲁肃之死",
                "刘备攻汉中","定军山之战","关羽失荆州","曹丕篡汉","刘备称帝","夷陵之战","刘备去世",
                "南征孟获","曹丕之死","两出祁山","孙权称帝","吴求夷州","四出祁山","吴俑辽东","辽东之争",
                "五丈原","蒋琬治蜀","辽东再争","辽东称王","魏平辽东","曹睿之死","吴魏相贡","吴讨珠雅",
                "魏受倭贡","魏攻汉中","陆逊之死","天下归晋"};

        for (int i=0;i<55;i++){
            Year.add(year[i]);
            Events.add(events[i]);
        }
        for(int i=0;i<18;i++){
            SearchName.add(name[i]);
        }
    }
    public String  getYear(int i){
        return Year.get(i);
    }
    public String getEvents(int i){
        return Events.get(i);
    }
    public int getIcon(int i){
        if (i<55) return Icon[i];
        else return Icon[0];
    }
    public List<String> getSearchEvents(){
        return Events;
    }
    public List<String> getSearchName(){
        return SearchName;
    }

    public void initDataBase(){
        MYSQL sql=new MYSQL(this);
        sql.recover();

        String[] eventname = new String[9];
        int[] eventpic = new int[9];
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

        eventpic[0] = R.drawable.diaochanlianhuanji;
        eventpic[1] = R.drawable.dandaofuhui;
        eventpic[2] = R.drawable.guaguliaodu;
        eventpic[3] = R.drawable.kongchengji;
        eventpic[4] = R.drawable.qiqinmenghuo;
        eventpic[5] = R.drawable.sangumaolu;
        eventpic[6] = R.drawable.sanyingzhanlvbu;
        eventpic[7] = R.drawable.taoyuansanjieyi;
        eventpic[8] = R.drawable.sanqizhouyu;

        eventbrf[0] = getString(R.string.diaochanlianhuanji);
        eventbrf[1] = getString(R.string.dandaofuhui);
        eventbrf[2] = getString(R.string.guaguliaodu);
        eventbrf[3] = getString(R.string.kongchengji);
        eventbrf[4] = getString(R.string.qiqinmenghuo);
        eventbrf[5] = getString(R.string.sangumaolu);
        eventbrf[6] = getString(R.string.sanyingzhanlvbu);
        eventbrf[7] = getString(R.string.taoyuansanjieyi);
        eventbrf[8] = getString(R.string.sanqizhouyu);
        Cursor event_Cursor=sql.selectn("添加事件");
        event_Cursor.moveToFirst();
        if(event_Cursor.getInt(0)==1)
            for(int i=0;i<9;i++){
                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + getResources().getResourcePackageName(eventpic[i]) + "/"
                        + getResources().getResourceTypeName(eventpic[i]) + "/"
                        + getResources().getResourceEntryName(eventpic[i]));
                sql.setnews(eventname[i],uri.toString(),eventbrf[i]);
            }

        String[] personname = new String[15];
        int[] personpic = new int[15];
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

        personpic[0] = R.drawable.cao_cao;
        personpic[1] = R.drawable.diao_chan;
        personpic[2] = R.drawable.dong_zhuo;
        personpic[3] = R.drawable.guan_yu;
        personpic[4] = R.drawable.hua_tuo;
        personpic[5] = R.drawable.liu_bei;
        personpic[6] = R.drawable.lu_su;
        personpic[7] = R.drawable.lv_bu;
        personpic[8] = R.drawable.meng_huo;
        personpic[9] = R.drawable.sima_yi;
        personpic[10] = R.drawable.sun_quan;
        personpic[11] = R.drawable.zhang_fei;
        personpic[12] = R.drawable.zhao_yun;
        personpic[13] = R.drawable.zhou_yu;
        personpic[14] = R.drawable.zhuge_liang;

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
        Cursor event_People=sql.selectp("添加人物");
        event_People.moveToFirst();
        if(event_People.getInt(0)==1)
            for(int i=0;i<15;i++){
                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + getResources().getResourcePackageName(personpic[i]) + "/"
                + getResources().getResourceTypeName(personpic[i]) + "/"
                + getResources().getResourceEntryName(personpic[i]));
                sql.setperson(personname[i],uri.toString(),personsex[i],personbirth[i],personhome[i],personfrom[i],personbrf[i]);
            }
    }
    public void setDetailImage(android.graphics.drawable.Drawable img){
        DetailImage=img;
    }
    public void setDetail(String d){
        detail=d;
    }
    public void setYear(String y){
        year=y;
    }
    public void setCountry(String c){
        country=c;
    }
    public void setPlace(String p){
        place=p;
    }
    public void setName(String n){
        name=n;
    }
    public void setSex(String s){
        sex=s;
    }
    public android.graphics.drawable.Drawable getDetailImage(){
        return DetailImage;
    }
    public String getName(){
        return name;
    }
    public String getYear(){
        return year;
    }
    public String getSex(){
        return sex;
    }
    public String getPlace(){
        return place;
    }
    public String getCountry(){
        return country;
    }
    public String getDetail(){
        return detail;
    }
}
