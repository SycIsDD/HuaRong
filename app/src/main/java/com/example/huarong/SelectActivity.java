package com.example.huarong;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import database.OnlineRecord;
import database.Steps;
import extendForGame.FullScreenUtils;

public class SelectActivity extends AppCompatActivity {

    private QMUIViewPager selectGame;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String record = "  当前记录:";
        super.onCreate(savedInstanceState);
        FullScreenUtils.fullScreen(this);
        setContentView(R.layout.activity_select);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"锐字真言体.ttf");
        ExpandingList expandingList = (ExpandingList) findViewById(R.id.expanding_list_main); //得到整个列表
        ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout); //子表项
        ((TextView) item.findViewById(R.id.title)).setText("初出茅庐");
        ((TextView) item.findViewById(R.id.title)).setTypeface(typeface);
        item.createSubItems(2);
        for(int i=0;i<2;i++) {
            View subItem = item.getSubItemView(i);
            final int index = i;
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(0, index);
                }
            });
            int id = getResources().getIdentifier("game0" + (i + 1), "string", this.getPackageName());
            String name = getResources().getString(id).split("-")[0];
            String rank = getOnlineRecord("0" + (i + 1));
            int step = Steps.getOneRecord("0" + (i + 1), this);
            if(step > 0) {
                name += (record + step);
            }
            ((TextView) subItem.findViewById(R.id.sub_title)).setText(name);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTypeface(typeface);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTextColor(Color.WHITE);
            ((TextView) subItem.findViewById(R.id.record_rank)).setText(rank);
            ((TextView) subItem.findViewById(R.id.record_rank)).setTypeface(typeface);
        }

        item.setIndicatorColorRes(R.color.testIcon);
        ((TextView) item.findViewById(R.id.title)).setTextColor(this.getResources().getColor(R.color.testIcon));
        item.setIndicatorIconRes(R.drawable.zhang);

        item = expandingList.createNewItem(R.layout.expanding_layout);
        ((TextView) item.findViewById(R.id.title)).setText("牛刀小试");
        ((TextView) item.findViewById(R.id.title)).setTypeface(typeface);
        item.createSubItems(6);
        for(int i=0;i<6;i++) {
            View subItem = item.getSubItemView(i);
            final int index = i;
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(1, index);
                }
            });
            int id = getResources().getIdentifier("game1" + (i + 1), "string", this.getPackageName());
            String name = getResources().getString(id).split("-")[0];
            String rank = getOnlineRecord("1" + (i + 1));
            int step = Steps.getOneRecord("1" + (i + 1), this);
            if(step > 0) {
                name += (record + step);
            }
            ((TextView) subItem.findViewById(R.id.sub_title)).setText(name);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTypeface(typeface);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTextColor(Color.WHITE);
            ((TextView) subItem.findViewById(R.id.record_rank)).setText(rank);
            ((TextView) subItem.findViewById(R.id.record_rank)).setTypeface(typeface);
        }

        item.setIndicatorColorRes(R.color.easyIcon);
        ((TextView) item.findViewById(R.id.title)).setTextColor(this.getResources().getColor(R.color.easyIcon));
        item.setIndicatorIconRes(R.drawable.guan);

        item = expandingList.createNewItem(R.layout.expanding_layout);
        ((TextView) item.findViewById(R.id.title)).setText("江湖高手");
        ((TextView) item.findViewById(R.id.title)).setTypeface(typeface);
        item.createSubItems(6);
        for(int i=0;i<6;i++) {
            View subItem = item.getSubItemView(i);
            final int index = i;
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(2, index);
                }
            });
            int id = getResources().getIdentifier("game2" + (i + 1), "string", this.getPackageName());
            String name = getResources().getString(id).split("-")[0];
            String rank = getOnlineRecord("2" + (i + 1));
            int step = Steps.getOneRecord("2" + (i + 1), this);
            if(step > 0) {
                name += (record + step);
            }
            ((TextView) subItem.findViewById(R.id.sub_title)).setText(name);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTypeface(typeface);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTextColor(Color.WHITE);
            ((TextView) subItem.findViewById(R.id.record_rank)).setText(rank);
            ((TextView) subItem.findViewById(R.id.record_rank)).setTypeface(typeface);
        }
        item.setIndicatorColorRes(R.color.midIcon);
        ((TextView) item.findViewById(R.id.title)).setTextColor(this.getResources().getColor(R.color.midIcon));
        item.setIndicatorIconRes(R.drawable.zhao);

        item = expandingList.createNewItem(R.layout.expanding_layout);
        ((TextView) item.findViewById(R.id.title)).setTextColor(this.getResources().getColor(R.color.midIcon));
        ((TextView) item.findViewById(R.id.title)).setText("世外高人");
        ((TextView) item.findViewById(R.id.title)).setTypeface(typeface);
        item.createSubItems(6);
        for(int i=0;i<6;i++) {
            View subItem = item.getSubItemView(i);
            final int index = i;
            subItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(3, index);
                }
            });
            int id = getResources().getIdentifier("game3" + (i + 1), "string", this.getPackageName());
            String name = getResources().getString(id).split("-")[0];
            String rank = getOnlineRecord("3" + (i + 1));
            int step = Steps.getOneRecord("3" + (i + 1), this);
            if(step > 0) {
                name += (record + step);
            }
            ((TextView) subItem.findViewById(R.id.sub_title)).setText(name);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTypeface(typeface);
            ((TextView) subItem.findViewById(R.id.sub_title)).setTextColor(Color.WHITE);
            ((TextView) subItem.findViewById(R.id.record_rank)).setText(rank);
            ((TextView) subItem.findViewById(R.id.record_rank)).setTypeface(typeface);
        }

        item.setIndicatorColorRes(R.color.highIcon);
        ((TextView) item.findViewById(R.id.title)).setTextColor(this.getResources().getColor(R.color.highIcon));
        item.setIndicatorIconRes(R.drawable.cao);
    }

    @Override
    public void onResume() {
        FullScreenUtils.fullScreen(this);
        super.onResume();
    }

    private void startGame(int level, int index) {
        try
        {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("level", level);
            intent.putExtra("index", index+1);
            startActivityForResult(intent, 1);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int type = data.getExtras().getInt("type", 0);
        if(type == 1) {
            this.finish();
        }
    }

    public String getOnlineRecord(String name) {
        OnlineRecord record = Steps.getOneOnlineRecord(name, this);
        if(record == null)
            return "暂无数据~ 待您完成后获取";
        else
            return "最佳纪录:" + record.getBest() +
                    "        排名:" + record.getRank() + "/" +record.getSum();
    }
}
