package database;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Steps {

    private static MySqliteHelper mySqliteHelper;
    private static SQLiteDatabase db;        // 申明一个数据库对象
    public static void saveRecord(String name, int step, Activity activity) {
        Log.d("dbSave", name + " " + step);
        mySqliteHelper=new MySqliteHelper(activity,"record",null,1);
        db = mySqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("step", step);
        String whereClause = "name=?";
        String[] whereArgs={name};
        db.update("steps",values,whereClause,whereArgs);
    }

    public static void saveOnlineRecord(
            String name, int best, int sum, int rank, Activity activity) {
        mySqliteHelper=new MySqliteHelper(activity,"record",null,1);
        db = mySqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("best", best);
        values.put("sum", sum);
        values.put("rank", rank);
        String whereClause = "name=?";
        String[] whereArgs={name};
        db.update("steps",values,whereClause,whereArgs);
    }

    public static int getOneRecord(String name, Activity activity) {
        mySqliteHelper=new MySqliteHelper(activity,"record",null,1);
        db = mySqliteHelper.getWritableDatabase();
        String selection = "name=?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query("steps", null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("step"));
    }

    public static OnlineRecord getOneOnlineRecord(String name, Activity activity) {
        Log.d("dbGet", name);
        mySqliteHelper=new MySqliteHelper(activity,"record",null,1);
        db = mySqliteHelper.getWritableDatabase();
        String selection = "name=?";
        String[] selectionArgs = {name};
        Cursor cursor = db.query("steps", null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        int best = cursor.getInt(cursor.getColumnIndex("best"));
        if(best != 0) {
            OnlineRecord record = new OnlineRecord(
                cursor.getInt(cursor.getColumnIndex("best")),
                cursor.getInt(cursor.getColumnIndex("sum")),
                cursor.getInt(cursor.getColumnIndex("rank"))
            );
            return record;
        }
        else
            return null;
    }
}
