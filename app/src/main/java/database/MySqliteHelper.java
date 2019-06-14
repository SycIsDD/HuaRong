package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {

    /*表名*/
    private final String TABLE_NAME_STEP = "steps";
    private final String VALUE_NAME = "name";
    private final String VALUE_TRY = "tryTime";
    private final String VALUE_STEP = "step";
    private final String VALUE_BEST = "best";
    private final String VALUE_SUM = "sum";
    private final String VALUE_RANK = "rank";

    private final String CREATE_STEP = "create table " + TABLE_NAME_STEP + "(" +
            VALUE_NAME + " text primary key," +
            VALUE_TRY + " integer," +
            VALUE_STEP + " integer," +
            VALUE_BEST + " integer," +
            VALUE_SUM + " integer," +
            VALUE_RANK + " integer" +
            ")";

    public MySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STEP);
        ContentValues contentValues=new ContentValues();
        contentValues.put("name", "02");
        for(int i=1; i <= 2; i++) {
            contentValues.put("name", "0" + i);
            contentValues.put("tryTime", 0);
            contentValues.put("step", 0);
            contentValues.put("best", 0);
            contentValues.put("sum", 0);
            contentValues.put("rank", 0);
            db.insert("steps",null,contentValues);
        }
        for(int j=1; j <= 3; j++) {
            for(int i=1; i <= 6; i++) {
                contentValues.put("name", j + "" + i);
                contentValues.put("tryTime", 0);
                contentValues.put("step", 0);
                contentValues.put("best", 0);
                contentValues.put("sum", 0);
                contentValues.put("rank", 0);
                db.insert("steps",null,contentValues);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
