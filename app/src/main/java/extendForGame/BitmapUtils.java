package extendForGame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.InputStream;

public class BitmapUtils {
    public static DrawView drawView;

    public static Bitmap getBitMap(String name) {
        int id = drawView.getResources().getIdentifier(name, "drawable", drawView.getApplicationInfo().packageName);
        InputStream in = drawView.getResources().openRawResource(id);
        return BitmapFactory.decodeStream(in);
    }

    public static Bitmap changeBitmap(Bitmap bitmap, int type, int minWidth) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int targetHeight = 0;
        int targetWidth = 0;
        minWidth -= 15;
        if(type == 0)//小正方形
        {
            targetHeight = minWidth;
            targetWidth = minWidth;
        }
        else if(type == 1)//横
        {
            targetHeight = minWidth;
            targetWidth = 2 * minWidth;
        }
        else if(type == 2)//竖
        {
            targetHeight = 2 * minWidth;
            targetWidth = minWidth;
        }
        else if(type == 3)//大正方形
        {
            targetHeight = 2 * minWidth;
            targetWidth = 2 * minWidth;
        }
        float scaleWidth = ((float) targetWidth) / width;
        float scaleHeight = ((float) targetHeight) / height;
        // 取得想要缩放的matrix参数Matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bm;
    }
}
