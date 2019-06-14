package extendForGame;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.huarong.GameActivity;
import com.example.huarong.R;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import database.Steps;

public class DrawView extends View {

    private int curX = -1;
    private int curY = -1;

    private int figureIndex = -1;
    private float width = 0;
    private float height = 0;
    private Figure cao;
    private ApplicationInfo applicationInfo;
    private GameActivity gameActivity;
    Paint paint = new Paint();
    private static final String TAG = "DrawView";
    private List<Figure> figureList;
    private Stack<Step> steps;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        BitmapUtils.drawView = this;
        //设置画笔的颜色
        paint.setColor(Color.GREEN);
        figureList = new ArrayList<Figure>();
        steps = new Stack<Step>();
    }

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void initFigure(String str){
        char[] figures = str.toCharArray();
        int[] figureStatus = new int[8];
        for(int i=0;i<8;i++) {
            figureStatus[i] = 0;
        }
        for(int i=0;i<20;i++){
            if(figures[i] == '1') {
                Bitmap bitmap;
                bitmap = BitmapUtils.getBitMap("bing");
                figureList.add(new Figure(bitmap, i % 4, i / 4, 0, "bing"));
            }
            if(figures[i] == '2' && figureStatus[2]==0) {
                Bitmap bitmap;
                bitmap = BitmapUtils.getBitMap("cao");
                cao = new Figure(bitmap, i % 4, i / 4, 3, "cao");
                figureList.add(cao);
                figureStatus[2] += 1;
            }
            if(Integer.parseInt("" + figures[i]) > 2
                    && figureStatus[Integer.parseInt("" + figures[i])] == 0) {
                figureStatus[Integer.parseInt("" + figures[i])] += 1;
                String name = "";
                switch (figures[i]) {
                    case '3':
                    {
                        name = "zhang";
                        break;
                    }
                    case '4':
                    {
                        name = "zhao";
                        break;
                    }
                    case '5':
                    {
                        name = "ma";
                        break;
                    }
                    case '6':
                    {
                        name = "guan";
                        break;
                    }
                    case '7':
                    {
                        name = "huang";
                        break;
                    }
                }
                int type;
                if(figures[i] == figures[i + 1]) {
                    name += 1;
                    type = 1;
                }
                else {
                    name += 2;
                    type = 2;
                }
                Bitmap bitmap;
                bitmap = BitmapUtils.getBitMap(name);
                figureList.add(new Figure(bitmap, i % 4, i / 4, type, name));
            }
        }
    }

    public String saveFigure(){
        String state = "";
        for(int j = 0; j < 5; j++) {
            for(int i = 0; i < 4; i++) {
                int index = Activate(i, j);
                if(index == -1) {
                    state += "0";
                }
                else {
                    state += figureList.get(index).getMark();
                }
            }
        }
        return state;
    }

    private int Activate(int x, int y) {
        if(x < 0 || x > 3 || y < 0 || y > 4) {
            return -2;
        }
        for(int i=0; i<figureList.size(); i++) {
            if(figureList.get(i).ifIn(x, y)) {
                return i;
            }
        }
        return -1;
    }

    private void drawSelected(Canvas canvas) //绘制选中人物的边框和选中的空格的边框
    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorSelected));
        paint.setStrokeWidth(10);
        if(figureIndex != -1)
            figureList.get(figureIndex).drawBackground(canvas, width/4, paint);
        if(curY != -1 && curX != -1) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.colorEmpty));
            RectF oval = new RectF(curX * width/4 + 5, curY * width/4 + 5,
                    (curX + 1) * width/4 - 5, (curY + 1) * width/4 - 5);
            canvas.drawRoundRect(oval,20, 20, paint);
            curX = -1;
            curY = -1;
        }
    }

    private void drawFigures(Canvas canvas) {
        for(int i = 0; i < figureList.size(); i++)
        {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.colorFigureBackground));
            figureList.get(i).drawBackground(canvas, width/4, paint);
            Figure figure = figureList.get(i);
            Bitmap bitmap = BitmapUtils.changeBitmap(figure.getPicture(), figure.getType(), (int)(this.width/4));
            canvas.drawBitmap(bitmap, (this.width/4) * figure.getLocationX() + 10, (this.width/4) * figure.getLocationY() + 10, paint);
        }
    }

    private void ifWin() {
        if(cao.getLocationX() == 1 && cao.getLocationY() == 3) {
            new QMUIDialog.MessageDialogBuilder(gameActivity)
                    .setTitle("胜利")
                    .setMessage("曹操：溜了溜了")
                    .addAction("确定",
                        new QMUIDialogAction.ActionListener() {
                            @Override public void onClick(QMUIDialog dialog, int index) {
                                Steps.saveRecord(gameActivity.gameId, gameActivity.getStep(), gameActivity);
                                gameActivity.finishGame();
                                dialog.dismiss();
                                Toast.makeText(gameActivity, "成功逃脱啦~", Toast.LENGTH_SHORT).show();
                            }
                    })
                    .show();
        }
    }

    private void addStep(Step step) {
        steps.push(step);
        gameActivity.addStep();
    }

    public void backStep() {
        if(steps.size() > 0) {
            gameActivity.subtractStep();
            Step step = steps.pop();
            Figure figure = figureList.get(step.getIndex());
            figure.moveTo(step.getX(), step.getY());
            this.invalidate();
        }
    }

    private boolean tryMove(int index, int x, int y){
        Figure selected = figureList.get(index);
        int locationX = selected.getLocationX();
        int locationY = selected.getLocationY();
        Step step = new Step(index, selected.getLocationX(), selected.getLocationY());
        switch (selected.getType()) {
            case 0: {
                if((Math.abs(locationX - x) == 1 && Math.abs(locationY - y) == 0)
                    || (Math.abs(locationX - x) == 0 && Math.abs(locationY - y) == 1)) { //相邻一格
                    selected.moveTo(x, y);
                    addStep(step);
                    return true;
                }else if(Math.abs(locationX - x) == 1 && Math.abs(locationY - y) == 1) { //拐弯两格
                    if(Activate(locationX , y) == -1 || Activate(x, locationY) == -1)
                    {
                        selected.moveTo(x, y);
                        addStep(step);
                        return true;
                    }
                }else if(Math.abs(locationX - x) == 2 && Math.abs(locationY - y) == 0) // 一条线上移了两格 X
                {
                    if(Activate((int)((x + locationX)/2), locationY) == -1) {
                        selected.moveTo(x, y);
                        addStep(step);
                        return true;
                    }
                }else if(Math.abs(locationX - x) == 0 && Math.abs(locationY - y) == 2) // 一条线上移了两格 Y
                {
                    if(Activate(locationX, (int)((y + locationY)/2)) == -1) {
                        selected.moveTo(x, y);
                        addStep(step);
                        return true;
                    }
                }
                break;
            }
            case 1: {
                if(locationX - x == 1 && locationY == y) {
                    selected.moveTo(locationX - 1, locationY);
                    addStep(step);
                    return true;
                } else if(locationX - x == -2 && locationY == y) {
                    selected.moveTo(locationX + 1, locationY);
                    addStep(step);
                    return true;
                } else if((Activate(locationX, locationY - 1) == -1) &&
                    (Activate(locationX + 1, locationY - 1) == -1)) {
                    selected.moveTo(locationX, locationY - 1);
                    addStep(step);
                    return true;
                } else if((Activate(locationX, locationY + 1) == -1) &&
                        (Activate(locationX + 1, locationY + 1) == -1)) {
                    selected.moveTo(locationX, locationY + 1);
                    addStep(step);
                    return true;
                }else if(locationX - x == 2 && locationY - y == 0) // 一条线上移了两格 Y
                {
                    if(Activate(locationX - 1, locationY) == -1) {
                        selected.moveTo(x, y);
                        addStep(step);
                        return true;
                    }
                }else if(x - locationX == 3 && locationY - y == 0) // 一条线上移了两格 Y
                {
                    if(Activate(locationX + 2, locationY) == -1) {
                        selected.moveTo(locationX + 2, y);
                        addStep(step);
                        return true;
                    }
                }
                break;
            }
            case 2: {
                if(locationX == x && locationY -y == 1) {
                    selected.moveTo(locationX, locationY - 1);
                    addStep(step);
                    return true;
                } else if(locationX == x && locationY - y == -2) {
                    selected.moveTo(locationX, locationY + 1);
                    addStep(step);
                    return true;
                } else if((Activate(locationX - 1, locationY) == -1) &&
                        (Activate(locationX - 1, locationY + 1) == -1)) {
                    selected.moveTo(locationX - 1, locationY);
                    addStep(step);
                    return true;
                } else if((Activate(locationX + 1, locationY) == -1) &&
                        (Activate(locationX + 1, locationY + 1) == -1)) {
                    selected.moveTo(locationX + 1, locationY);
                    addStep(step);
                    return true;
                }else if(locationX - x == 0 && locationY - y == 2) // 一条线上移了两格 Y
                {
                    if(Activate(locationX, locationY - 1) == -1) {
                        selected.moveTo(x, y);
                        addStep(step);
                        return true;
                    }
                }else if(x - locationX == 0 && y - locationY == 3) // 一条线上移了两格 Y
                {
                    if(Activate(locationX, locationY + 2) == -1) {
                        selected.moveTo(x, locationY + 2);
                        addStep(step);
                        return true;
                    }
                }
                break;
            }
            case 3: {
                if((Activate(locationX - 1, locationY) == -1) &&
                        (Activate(locationX - 1, locationY + 1) == -1)) {
                    selected.moveTo(locationX - 1, locationY);
                    addStep(step);
                    return true;
                } else if((Activate(locationX + 2, locationY) == -1) &&
                        (Activate(locationX + 2, locationY + 1) == -1)) {
                    selected.moveTo(locationX + 1, locationY);
                    addStep(step);
                    return true;
                } else if((Activate(locationX, locationY - 1) == -1) &&
                        (Activate(locationX + 1, locationY - 1) == -1)) {
                    selected.moveTo(locationX, locationY - 1);
                    addStep(step);
                    return true;
                } else if((Activate(locationX, locationY + 2) == -1) &&
                        (Activate(locationX + 1, locationY + 2) == -1)) {
                    selected.moveTo(locationX, locationY + 1);
                    addStep(step);
                    return true;
                }
            }
        }
        return false;
    }

    private void tryActivate(int preFigure, int x, int y) {
        Figure figure = figureList.get(preFigure);
        int type = figure.getType();
        int minX, minY, maxX, maxY;
        if(x >= figure.getLocationX()) {
            minX = figure.getLocationX();
            maxX = x;
        }else {
            minX = x;
            maxX = figure.getLocationX();
        }
        if(y >= figure.getLocationY()) {
            minY = figure.getLocationY();
            maxY = y;
        }else {
            minY = y;
            maxY = figure.getLocationY();
        }
        if  (x >= figure.getLocationX()) {
            for(int i = x; i >= figure.getLocationX(); i--) {
                if(y >= figure.getLocationY()) {
                    for(int j = y; j >= figure.getLocationY(); j--) {
                        if(Activate(i, j) == -1)
                            if(tryMove(preFigure, i, j) == true)
                                return;
                    }
                }
                if(y < figure.getLocationY()) {
                    for(int j = y; j <= figure.getLocationY(); j++) {
                        if(Activate(i, j) == -1)
                            if(tryMove(preFigure, i, j) == true)
                                return;
                    }
                }
            }
        }
        else if(x < figure.getLocationX()) {
            for(int i = x; i <= figure.getLocationX(); i++) {
                if(y >= figure.getLocationY()) {
                    for(int j = y; j >= figure.getLocationY(); j--) {
                        if(Activate(i, j) == -1)
                            if(tryMove(preFigure, i, j) == true)
                                return;
                    }
                }
                if(y < figure.getLocationY()) {
                    for(int j = y; j <= figure.getLocationY(); j++) {
                        if(Activate(i, j) == -1)
                            if(tryMove(preFigure, i, j) == true)
                                return;
                    }
                }
            }
        }
        Log.d("tryMove", "X:"+x+ " Y:"+y+ " minX:"+minX+ " minY:"+minY+ " maxX:"+maxX+ " maxY:"+maxY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.width != 0 && this.height != 0)
        {
            drawFigures(canvas);
            drawSelected(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
            {
                int x = (int)(event.getX()/(width / 4));
                int y = (int)(event.getY()/(width / 4));
                if(x >= 0 && x < 4 && y >= 0 && y < 5)
                {
                    figureIndex = Activate(x, y);
                }
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP://抬起
            {
                int x = (int)(event.getX()/(width / 4));
                int y = (int)(event.getY()/(width / 4));
//                if(x >= 0 && x < 4 && y >= 0 && y < 5)
//                {
                    int preFigure = figureIndex; //之前被选中的figure
                    figureIndex = Activate(x, y);
                    if(preFigure != -1 && figureIndex == -1) {
                        tryMove(preFigure, x, y);
                        ifWin();
                    }else if((preFigure != -1 && figureIndex != -1)) {
                        Log.d("滑动", "X:" + x + " Y:" + y);
                        tryActivate(preFigure, x, y);
                        ifWin();
                    }
//                }
                figureIndex = -1;
                curY = -1;
                curX = -1;
                this.invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE://移动
            {
                int x = (int)(event.getX()/(width / 4));
                int y = (int)(event.getY()/(width / 4));
                if(x >= 0 && x < 4 && y >= 0 && y < 5)
                {
                    int preFigure = figureIndex; //之前被选中的figure
                    if(preFigure != -1 && Activate(x, y) == -1) {
                        curX = x;
                        curY = y;
                    }
                }
                this.invalidate();
                break;
            }
        }
        return true;
    }
}
