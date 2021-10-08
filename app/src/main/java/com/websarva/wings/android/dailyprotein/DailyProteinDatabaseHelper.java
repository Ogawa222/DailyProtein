package com.websarva.wings.android.dailyprotein;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DailyProteinDatabaseHelper extends SQLiteOpenHelper {
    // データベースファイル名の定数フィールド
    private static final String DATABASE_NAME = "daily_proteins.db";
    // バージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 1;

    // コンストラクタ
    public DailyProteinDatabaseHelper(Context context){
        // 親コンストラクタの呼び出し
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // テーブル作成用SQL文字列の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE daily_proteins (");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("name TEXT,"); // 食品名
        sb.append("weight REAL,"); // タンパク質量
        sb.append("take_date TEXT,"); // 摂取した日付
        sb.append("take_time TEXT"); // 摂取した時刻（検索のため、日付と時刻のカラムを分ける）
        sb.append(");");
        String sql = sb.toString();

        // SQLの実行
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
