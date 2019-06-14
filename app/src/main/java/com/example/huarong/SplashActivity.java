package com.example.huarong;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import database.MySqliteHelper;
import database.Steps;

/**
 * 启动页面
 */
public class SplashActivity extends Activity {

    private static final int SHOW_TEXT1 = 0x01;// 显示图片1
    private static final int SHOW_TEXT2 = 0x02;// 显示图片2
    private static final int SHOW_BUTTON = 0x03;// 显示按钮
    private static final int DELAY_TIME = 1000;// 延时时间
    private QMUIRoundButton enterButton;
    private ImageView text1;
    private ImageView text2;

    MySqliteHelper mySQLiteHelper;  // 申明一个数据库管理助手对象
    SQLiteDatabase database;        // 申明一个数据库对象

    // 创建Handler对象，处理接收的消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_TEXT1:// 延时1秒跳转
                {
                    text1.setVisibility(View.VISIBLE);
                    showText2();
                    break;
                }
                case SHOW_TEXT2:// 延时1秒跳转
                {
                    text2.setVisibility(View.VISIBLE);
                    showButton();
                    break;
                }
                case SHOW_BUTTON:// 延时1秒跳转
                {
                    enterButton.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySQLiteHelper=new MySqliteHelper(this,"record",null,1);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"锐字真言体.ttf");
        enterButton = findViewById(R.id.enterGame);
        enterButton.setTypeface(typeface);
        text1 = findViewById(R.id.viewText1);
        text2 = findViewById(R.id.viewText2);
        // 调用handler的sendEmptyMessageDelayed方法
        handler.sendEmptyMessageDelayed(SHOW_TEXT1, (int)(0.5 * DELAY_TIME));
    }

    private void showText2() {
        handler.sendEmptyMessageDelayed(SHOW_TEXT2, DELAY_TIME);
    }

    private void showButton() {
        handler.sendEmptyMessageDelayed(SHOW_BUTTON, DELAY_TIME);
    }

    /**
     * 跳转到主页面
     */
    public void goHome(View view) {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();// 销毁当前活动界面
    }
}