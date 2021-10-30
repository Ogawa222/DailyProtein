package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DailyHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_history);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // タイトルの変更
        setTitle(R.string.daily_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 戻り値用の変数を初期値trueで用意
        boolean returnVal = true;
        // 選択されたメニューのIDを取得
        int itemId = item.getItemId();
        // 選択されたメニューが「戻る」の場合、アクティビティを終了
        if(itemId == android.R.id.home){
            Intent intent = getIntent();
            String from = intent.getStringExtra("from");
            if (from != null) {
                if (from.equals("MainActivity")) {
                } else {
                    intent = new Intent(DailyHistoryActivity.this, RegistrationHistoryActivity.class);
                    startActivity(intent);
                }
            }
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