package com.example.markg.automobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by markg on 2017-12-23.
 */

public class AutoDatabaseHelper extends SQLiteOpenHelper {
    protected static final String TABLE_MESSAGES = "messages";
    protected static final String KEY_ID = "id";
    protected static final String KEY_MESSAGE = "message";

    private static final String DATABASE_NAME = "Auto.db";
    private static final int VERSION_NUM = 3;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MESSAGES + "( " + KEY_ID
            + " integer primary key autoincrement, " + KEY_MESSAGE
            + " text not null);";

    public AutoDatabaseHelper(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        Log.i(AutoDatabaseHelper.class.getName(), "Calling onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(AutoDatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);

    }
}
