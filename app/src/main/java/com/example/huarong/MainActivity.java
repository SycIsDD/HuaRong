package com.example.huarong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUIVerticalTextView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;

import HttpUtils.HttpUtils;
import database.Steps;
import extendForGame.FullScreenUtils;

public class MainActivity extends AppCompatActivity {
    private QMUIRoundButton chooseGameBtn;
    private QMUIRoundButton resumeGameBtn;
    private QMUIRoundButton getRank;
    private QMUIVerticalTextView gameTitle;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String m = (String) msg.obj;
            try {
                m = URLDecoder.decode(m, "utf-8");
                JSONObject retObj = new JSONObject(m);
                String error = retObj.getString("error");
                String data = retObj.getString("data");
                if(error.equals("null")) {
                    showSuccess(data);
                }
                else
                    showFail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Response", m);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenUtils.fullScreen(this);
        setContentView(R.layout.activity_main);
        chooseGameBtn = findViewById(R.id.chooseGame);
        resumeGameBtn = findViewById(R.id.resumeGame);
        getRank = findViewById(R.id.getRank);
        gameTitle = findViewById(R.id.gameTitle);
        SharedPreferences sp = getSharedPreferences("saveState", Context.MODE_PRIVATE);
        String gameId = sp.getString("gameId", "00");
        Log.d("GetState", gameId);
        if(gameId == "00") {
            resumeGameBtn.setVisibility(View.GONE);
        }
        Typeface typeface=Typeface.createFromAsset(getAssets(),"锐字真言体.ttf");
        chooseGameBtn.setTypeface(typeface);
        resumeGameBtn.setTypeface(typeface);
        getRank.setTypeface(typeface);
        gameTitle.setTypeface(typeface);
    }

    @Override
    public void onResume() {
        SharedPreferences sp = getSharedPreferences("saveState", Context.MODE_PRIVATE);
        String gameId = sp.getString("gameId", "00");
        Log.d("GetState", gameId);
        if(gameId == "00") {
            resumeGameBtn.setVisibility(View.GONE);
        }
        else {
            resumeGameBtn.setVisibility(View.VISIBLE);
        }
        FullScreenUtils.fullScreen(this);
        super.onResume();
    }

    public void selectGame(View view) {
        try
        {
            Intent intent = new Intent(this, SelectActivity.class);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void resumeGame(View view) {
        try
        {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("level", 0);
            intent.putExtra("index", 0);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getRank(View view) {
        Activity that = this;
        if(checkLogin() == false) {
            new QMUIDialog.MessageDialogBuilder(this)
                    .setTitle("登录或注册")
                    .setMessage("您还未登录哟~")
                    .addAction("登录",
                            new QMUIDialogAction.ActionListener() {
                                @Override public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    loginAccount(0);
                                    FullScreenUtils.fullScreen(that);
                                }
                            })
                    .addAction("注册",
                            new QMUIDialogAction.ActionListener() {
                                @Override public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    loginAccount(1);
                                    FullScreenUtils.fullScreen(that);
                                }
                            })
                    .addAction("返回",
                            new QMUIDialogAction.ActionListener() {
                                @Override public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    FullScreenUtils.fullScreen(that);
                                }
                            })
                    .show();
        }
        else {
            try {
                synchronize();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void synchronize() throws IOException {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        HttpUtils httpUtils = new HttpUtils();
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i < 2; i++) {
                JSONObject jsonObject = new JSONObject();
                int step = Steps.getOneRecord("0" + (i + 1), this);
                if(step > 0) {
                    jsonObject.put("name", "0" + (i + 1));
                    jsonObject.put("step", step);
                    jsonArray.put(jsonObject);
                }
            }
            for(int i = 1; i <= 3; i++) {
                for(int j = 1; j <= 5; j++) {
                    JSONObject jsonObject = new JSONObject();
                    int step = Steps.getOneRecord(i + "" + j, this);
                    if(step > 0) {
                        jsonObject.put("name", i + "" + j);
                        jsonObject.put("step", step);
                        jsonArray.put(jsonObject);
                    }
                }
            }
            json.put("record", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("UploadJson", json.toString());
        httpUtils.post("http://120.79.195.46:8080/api/upload", json.toString(), handler);
    }

    public boolean checkLogin() {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        if(!token.equals(""))
            return true;
        return false;
    }

    public void loginAccount(int type) {
        try
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void success(String data) throws JSONException {
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonArray = jsonData.getJSONArray("record");
        for(int i=0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String name = object.getString("name");
            int best = object.getInt("best");
            int rank = object.getInt("rank");
            int sum = object.getInt("sum");
            Steps.saveOnlineRecord(name, best, sum, rank, this);
        }
        selectGame(chooseGameBtn);
    }

    private void showSuccess(String data) {
        Activity that = this;
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord("获取成功")
                .create();
        tipDialog.show();
        chooseGameBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    success(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tipDialog.dismiss();
                FullScreenUtils.fullScreen(that);
            }
        }, 1000);
    }

    private void showFail() {
        Activity that = this;
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord("获取失败")
                .create();
        tipDialog.show();
        chooseGameBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
                FullScreenUtils.fullScreen(that);
            }
        }, 1000);
    }
}
