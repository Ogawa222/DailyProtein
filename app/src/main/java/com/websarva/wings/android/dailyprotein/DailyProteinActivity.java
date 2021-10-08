package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class DailyProteinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_protein);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 戻り値用の変数を初期値trueで用意
        boolean returnVal = true;
        // 選択されたメニューのIDを取得
        int itemId = item.getItemId();
        // 選択されたメニューが「戻る」の場合、アクティビティを終了
        if(itemId == android.R.id.home){
            finish();
        }
        // それ以外・・・
        else {
            // 親クラスの同名メソッドを呼び出し、その戻り値をreturnValとする
            returnVal = super.onOptionsItemSelected(item);
        }
        return returnVal;
    }
}