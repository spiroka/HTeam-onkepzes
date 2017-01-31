package hu.tgergo.hteam.dal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by takacsgergo on 2017. 01. 28..
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hteam";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "create table " + DatabaseSchema.TABLE_NAME + "(" +
                    DatabaseSchema.ID_COLUMN_NAME + " integer primary key autoincrement," +
                    DatabaseSchema.TITLE_COLUMN_NAME + " text not null," +
                    "unique(" +
                            DatabaseSchema.TITLE_COLUMN_NAME +
                    ")" +
            ");";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DatabaseSchema.TABLE_NAME);
        onCreate(db);
    }
}
