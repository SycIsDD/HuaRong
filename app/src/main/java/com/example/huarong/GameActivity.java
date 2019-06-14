package com.example.huarong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import extendForGame.DrawView;
import extendForGame.FullScreenUtils;

public class GameActivity extends AppCompatActivity {

    private DrawView pictureView;
    private TextView stepView;
    private QMUIRoundButton backStepButton;
    private QMUIRoundButton goBackButton;
    private TextView nameView;
    private String game = "game";
    public String gameId;
    private int step = 0;
    private boolean loaded = false;
    private static final String TAG = "GameActivity";

    public void setName(String name)
    {
        nameView.setText(name);
    }

    public void addStep() {
        step++;
        stepView.setText("步数:" + step);
    }

    public void setStep() {
        stepView.setText("步数:" + step);
    }

    public int getStep() {
        return step;
    }

    public void subtractStep() {
        step--;
        stepView.setText("步数:" + step);
    }

    public void backStep(View view) {
        pictureView.backStep();
    }

    public void goBack(View view) {
        this.onBackPressed();
    }

    public void saveState() {
        String state = pictureView.saveFigure();
        int step = this.step;
        SharedPreferences sp = getSharedPreferences("saveState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("state");
        editor.remove("gameId");
        editor.remove("step");
        editor.putString("state", state);
        editor.putString("gameId", gameId);
        editor.putInt("step", step);
        Log.d("SaveState", state);
        Log.d("SaveState", gameId);
        editor.apply();
    }

    public void clearState() {
        SharedPreferences sp = getSharedPreferences("saveState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("state");
        editor.remove("gameId");
        editor.remove("step");
        editor.apply();
    }

    private void setReturnType(int type) {
        Intent intent = new Intent();
        intent.putExtra("type", type);
        this.setResult(RESULT_OK, intent);
    }

    @Override
    public void onBackPressed() {
        final Activity that = this;
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("确认离开？")
                .setMessage("您将失去未保存的游戏记录")
                .addAction("直接离开",
                        new QMUIDialogAction.ActionListener() {
                            @Override public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                setReturnType(0);
                                GameActivity.super.onBackPressed();
                            }
                        })
                .addAction("保存存档",
                        new QMUIDialogAction.ActionListener() {
                            @Override public void onClick(QMUIDialog dialog, int index) {
                                saveState();
                                dialog.dismiss();
                                setReturnType(1);
                                GameActivity.super.onBackPressed();
                            }
                        })
                .addAction("继续游戏",
                        new QMUIDialogAction.ActionListener() {
                            @Override public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                FullScreenUtils.fullScreen(that);
                            }
                        })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FullScreenUtils.fullScreen(this);
        Intent intent = getIntent();
        gameId = new String();
        gameId += intent.getIntExtra("level", 0);
        gameId += intent.getIntExtra("index", 1);
        game += gameId;
        setContentView(R.layout.activity_game);
        pictureView = findViewById(R.id.PictureView);
        stepView = findViewById(R.id.stepView);
        nameView = findViewById(R.id.nameView);
        backStepButton = findViewById(R.id.backStepButton);
        goBackButton = findViewById(R.id.goBackButton);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"锐字真言体.ttf");
        stepView.setTypeface(typeface);
        nameView.setTypeface(typeface);
        backStepButton.setTypeface(typeface);
        goBackButton.setTypeface(typeface);
        ApplicationInfo appInfo = getApplicationInfo();
        pictureView.setApplicationInfo(appInfo);
    }

    public void finishGame() {
        this.setReturnType(1);
        this.finish();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        FullScreenUtils.fullScreen(this);
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && loaded == false) {
            //获得宽度
            int width = pictureView.getMeasuredWidth();
            //获得高度
            int height = pictureView.getMeasuredHeight();
            ViewGroup.LayoutParams lp = pictureView.getLayoutParams();
            if(width * 1.25 < height)
            {
                lp.width = width;
                lp.height = (int)(1.25 * width);
                pictureView.setLayoutParams(lp);
            }
            else
            if(width * 1.25 > height)
            {
                lp.width = (int)(height * 0.8);
                lp.height = height;
                pictureView.setLayoutParams(lp);
            }
            pictureView.setGameActivity(this);
            pictureView.setWidth(pictureView.getLayoutParams().width);
            pictureView.setHeight(pictureView.getLayoutParams().height);
            int id;
            if(!game.equals("game00")) {
                Log.d(TAG, "NAME " + game);
                id = getResources().getIdentifier(game, "string", this.getPackageName());
                String[] sourceStrArray = getResources().getString(id).split("-");
                Log.d(TAG, "ID " + id);
                Log.d(TAG, getResources().getString(id));
                String gameName = sourceStrArray[0];
                setName(gameName);
                pictureView.initFigure(sourceStrArray[1]);
            }
            else {
                SharedPreferences sp = getSharedPreferences("saveState", Context.MODE_PRIVATE);
                gameId = sp.getString("gameId", "00");
                Log.d(TAG, "NAME " + game);
                id = getResources().getIdentifier("game" + gameId, "string", this.getPackageName());
                String[] sourceStrArray = getResources().getString(id).split("-");
                String state = sp.getString("state", sourceStrArray[1]);
                int saveStep = sp.getInt("step", 0);
                this.step = saveStep;
                this.setStep();
                pictureView.initFigure(state);
                String gameName = sourceStrArray[0];
                setName(gameName);
                clearState();
            }
            Log.d(TAG, "onWindowFocusChange");
            loaded = true;
        }

    }
}
