package com.websarva.wings.android.dailyprotein;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteFoodDatabaseHelper extends SQLiteOpenHelper {
    // データベースファイル名の定数フィールド
    private static final String DATABASE_NAME = "favorite_foods.db";
    // バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    // コンストラクタ
    public FavoriteFoodDatabaseHelper(Context context){
        // 親コンストラクタの呼び出し
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // テーブル作成用SQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE favorite_foods (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("name TEXT,"); // 食品名
        sb.append("weight REAL"); // タンパク質量
        sb.append(");");
        String sql = sb.toString();

        // SQLの実行
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
