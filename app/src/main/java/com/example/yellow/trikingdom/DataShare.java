package com.example.yellow.trikingdom;

import android.app.Application;

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
}
