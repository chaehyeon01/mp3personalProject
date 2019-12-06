package com.example.user.musicmp3test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="musicListDB";
    private static final int VERSION=1;

    public MyDBHelper(Context context) {
        super(context, DB_NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  str="CREATE TABLE musicList(musicName CHAR(20), musicSinger CHAR(20),musicPath CHAR(30), voiteMusic INT(10),musicDuration INTEGER(11),musicAlbumID CHAR(30) ); ";
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS musicList");
        onCreate(db);
    }
}
