package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class FavoriteFoodListActivity extends AppCompatActivity {

    private String from;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_food_list);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // タイトルの変更
        setTitle(R.string.all_favorite_food);

        intent = getIntent();
        // 編集画面が終了した時の分岐のため(全データ画面に戻るか、日間データに戻るか）
        from = intent.getStringExtra("from");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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