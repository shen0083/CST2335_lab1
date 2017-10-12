package com.example.cynthiasheng.cst2335_lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public final static String name = "MyTable";
    public final static String KEY_ID = "ID";
    public final static String KEY_MESSAGE = "MESSAGE";
    public final static String DATABASE_NAME = "Message.db";
    public final static int VERSION_NUM = 1;
    protected static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MSG = "CREATE TABLE " + name + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_MESSAGE + " TEXT )";
            db.execSQL(CREATE_TABLE_MSG);
        //db.execSQL("CREATE TABLE " + name + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MESSAGE + "text" + ");");
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + name); //delete what was there previously
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);

    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        Log.i("Database ", "onOpen was called");
    }
}



