package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FavoriteFoodActivity extends AppCompatActivity {

    // データベースヘルパーオブジェクト
    private FavoriteFoodDatabaseHelper _favoriteFoodHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_food);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // タイトルの変更
        setTitle(R.string.register_favorite);

        // DBヘルパーオブジェクトを生成
        _favoriteFoodHelper = new FavoriteFoodDatabaseHelper(FavoriteFoodActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 戻り値用の変数を初期値trueで用意z
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

    public void onFavoriteRegisterButtonClick(View view){
        // 入力された食品名を取得
        EditText etFavoriteFoodName = findViewById(R.id.favorite_food_name);
        String favoriteFoodName = etFavoriteFoodName.getText().toString();
        // 入力されたタンパク質量を取得
        EditText etFavoriteProteinWeight = findViewById(R.id.favorite_protein_weight);
        String favoriteProteinWeight = etFavoriteProteinWeight.getText().toString();

        if (favoriteFoodName.isEmpty() || favoriteProteinWeight.isEmpty() ){
            Toast.makeText(FavoriteFoodActivity.this, "項目を全て入力してください", Toast.LENGTH_LONG).show();
        } else {
            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            SQLiteDatabase db = _favoriteFoodHelper.getWritableDatabase();
            // インサート用SQLの文字列の用意
            String sqlInsert = "INSERT INTO favorite_foods (name, weight) VALUES (?, ?)";
            // SQL文字列を元にプリペアドステートメントを取得
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            // 変数のバインド
            stmt.bindString(1, favoriteFoodName);
            double d_proteinWeight = Double.parseDouble(favoriteProteinWeight);
            stmt.bindDouble(2, d_proteinWeight);
            // インサートSQLの実行
            stmt.executeInsert();

            // 入力をクリア
            etFavoriteFoodName.setText("");
            etFavoriteProteinWeight.setText("");
            Toast.makeText(FavoriteFoodActivity.this, "お気に入りに登録しました！", Toast.LENGTH_LONG).show();
        }
    }

    public void onFavoriteRegisterListButtonClick(View view){
        // インテントオブジェクトを生成
        Intent intent = new Intent(getApplication(), FavoriteFoodListActivity.class);
        // 編集画面が終了した時の分岐のため(全データ画面に戻るか、お気に入り登録に戻るか）
        intent.putExtra("from","FavoriteFoodActivity");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // DBヘルパーオブジェクトの解放
        _favoriteFoodHelper.close();
        super.onDestroy();
    }
}