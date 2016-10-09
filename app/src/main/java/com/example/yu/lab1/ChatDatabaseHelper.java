package com.example.yu.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ChatDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Chats.bd";
    public static final int VERSION_NUM = 2;

    public static final String TABLE_NAME = "chat_table";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "_message";


    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + " ( "
            + KEY_ID + " integer primary key autoincrement, "
            + KEY_MESSAGE + " text not null);";

    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ChatDatabaseHelper.class.getName(),"Calling onCreate()");
        // .execSQL-->executes a string SQL statement.
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(ChatDatabaseHelper.class.getName(),"Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
