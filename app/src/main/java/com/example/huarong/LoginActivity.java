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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;

import HttpUtils.HttpUtils;
import extendForGame.FullScreenUtils;

public class LoginActivity extends AppCompatActivity {

    private QMUIRoundButton submitBtn;
    private EditText account;
    private EditText password;
    private int type;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String m = (String) msg.obj;
            try {
                m = URLDecoder.decode(m, "utf-8");
                JSONObject retObj = new JSONObject(m);
                String data = retObj.getString("data");
                Log.d("Data", data);
                if(!data.equals("error")) {
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
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        setContentView(R.layout.activity_login);
        submitBtn = findViewById(R.id.submitBtn);
        account = findViewById(R.id.account_input);
        password = findViewById(R.id.password_input);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"锐字真言体.ttf");
        submitBtn.setTypeface(typeface);
        account.setTypeface(typeface);
        password.setTypeface(typeface);
        if(type == 0)
            submitBtn.setText("登录");
        else
            submitBtn.setText("注册");
    }

    @Override
    protected void onResume() {
        FullScreenUtils.fullScreen(this);
        super.onResume();
    }

    public void submit(View view) {
        if(type == 0)
        {
            try {
                login();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                register();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void login() throws IOException {
        HttpUtils httpUtils = new HttpUtils();
        JSONObject json = new JSONObject();
        Log.d("Form", account.getText().toString() + password.getText().toString());
        try {
            json.put("username", account.getText().toString());
            json.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpUtils.post("http://120.79.195.46:8080/api/login", json.toString(), handler);
    }

    private void register() throws IOException {
        HttpUtils httpUtils = new HttpUtils();
        JSONObject json = new JSONObject();
        Log.d("Form", account.getText().toString() + password.getText().toString());
        try {
            json.put("username", account.getText().toString());
            json.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpUtils.post("http://120.79.195.46:8080/api/register", json.toString(), handler);
    }

    private void success(String data) {
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("token");
        editor.remove("username");
        editor.remove("password");
        editor.putString("username", account.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.putString("token", data);
        editor.apply();
        this.finish();
    }

    private void showSuccess(String data) {
        String actionName = "";
        Activity that = this;
        if(type == 0)
            actionName = "登录";
        else
            actionName = "注册";
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(actionName + "成功")
                .create();
        tipDialog.show();
        submitBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                success(data);
                tipDialog.dismiss();
                that.finish();
            }
        }, 1000);
    }

    private void showFail() {
        String actionName = "";
        if(type == 0)
            actionName = "登录";
        else
            actionName = "注册";
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(actionName + "失败")
                .create();
        tipDialog.show();
        submitBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1000);
    }
}
