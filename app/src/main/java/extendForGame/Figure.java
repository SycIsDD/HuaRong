package extendForGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Figure {

    private String name;

    public int getMark() {
        switch (this.name) {
            case "bing" :{
                return 1;
            }
            case "cao" :{
                return 2;
            }
            case "zhang" :{
                return 3;
            }
            case "zhao" :{
                return 4;
            }
            case "ma" :{
                return 5;
            }
            case "guan" :{
                return 6;
            }
            case "huang" :{
                return 7;
            }
        }
        return 0;
    }

    Figure(Bitmap picture, int locationX, int locationY, int type, String name) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.picture = picture;
        this.type = type;
        this.name = name;
        if(this.type == 1 || this.type == 2) {
            this.name = this.name.substring(0, this.name.length() - 1);
        }
    }

    public void drawBackground(Canvas canvas, float width, Paint paint) {
        float left = locationX * width + 5;
        float top = locationY * width + 5;
        float right;
        float bottom;
        if(type == 1 || type == 3) {
            right = (locationX + 2) * width - 5;
        } else {
            right = (locationX + 1) * width - 5;
        }
        if(type == 2 || type == 3) {
            bottom = (locationY + 2) * width - 5;
        } else {
            bottom = (locationY + 1) * width - 5;
        }
        RectF oval = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(oval, 40, 40, paint);
    }

    private Bitmap picture;

    private int locationX;

    private int locationY;

    private int type;

    public Bitmap getPicture() {
        return picture;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void moveTo(int x, int y) {
        this.locationX = x;
        this.locationY = y;
    }

    public boolean ifIn(int x, int y) {
        switch (type) {
            case 0: {
                if(x == locationX && y == locationY)
                    return true;
                else
                {
                    break;
                }
            }
            case 1: {
                if((x == locationX || x == (locationX + 1)) && (y == locationY))
                    return true;
                else
                {
                    break;
                }
            }
            case 2: {
                if((x == locationX) && (y == locationY || y == (locationY + 1)))
                    return true;
                else
                {
                    break;
                }
            }
            case 3: {
                if((x == locationX || x == (locationX + 1)) && (y == locationY || y == (locationY + 1)))
                    return true;
                else
                {
                    break;
                }
            }
        }
        return false;
    }
}
