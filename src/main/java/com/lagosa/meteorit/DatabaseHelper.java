package com.lagosa.meteorit;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "score.db";

    private static final String TABLE_NAME = "score_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "value";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (ID INTEGER PRIMARY KEY, "+COL2+" INTEGER DEFAULT 0);";
        db.execSQL(createTable);
        db.execSQL("INSERT INTO "+TABLE_NAME+"(ID,"+COL2+") VALUES (1,0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void updateData(int value){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE "+TABLE_NAME+" SET "+COL2+" = "+value + " WHERE " + COL1 + " = 1";
        db.execSQL(query);
    }

    public int getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ COL2 +" FROM "+TABLE_NAME + " WHERE "+ COL1 + " = 1";
        Cursor data = db.rawQuery(query,null);
        data.moveToFirst();
        int value = data.getInt(0);
        data.close();
        return value;
    }
}
