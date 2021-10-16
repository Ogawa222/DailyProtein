package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class FavoriteEditFoodActivity extends AppCompatActivity {

    private String database_id;
    private String from;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_edit_food);
        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        // タップされた行の各要素の値を取得
        database_id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String weight = intent.getStringExtra("weight");
        // 編集画面が終了した時の分岐のため(全データ画面に戻るか、日間データに戻るか）
        from = intent.getStringExtra("from");

        EditText edit_favorite_food_name = findViewById(R.id.edit_favorite_food_name);
        EditText edit_favorite_protein_weight = findViewById(R.id.edit_favorite_protein_weight);

        edit_favorite_food_name.setText(name);
        edit_favorite_protein_weight.setText(weight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 戻り値用の変数を初期値trueで用意
        boolean returnVal = true;
        // 選択されたメニューのIDを取得
        int itemId = item.getItemId();
        // 選択されたメニューが「戻る」の場合、アクティビティを終了
        if(itemId == android.R.id.home){
            // 戻るボタンを押した際、元の画面に戻る(全データ画面に戻るか、お気に入り一覧画面に戻るか）
            if (from != null){
                if (from.equals("FavoriteFoodListFragment")) {
                    // お気に入り一覧画面に
                    intent = new Intent(FavoriteEditFoodActivity.this, FavoriteFoodListActivity.class);
                    startActivity(intent);
                } else {
                    // 全データ画面に
//                intent = new Intent(FavoriteEditFoodActivity.this, RegistrationHistoryActivity.class);
//                startActivity(intent);
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

    public void onClickDeleteFavoriteFoodButton(View view){
        FavoriteAlertDeleteDialogFragment alertFragment = new FavoriteAlertDeleteDialogFragment();
        alertFragment.show(getSupportFragmentManager(), "FavoriteAlertDeleteDialogFragment");
    }

    public void deleteFavoriteFood(){
        FavoriteFoodDatabaseHelper _favoriteFoodHelper = new FavoriteFoodDatabaseHelper(FavoriteEditFoodActivity.this);
        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = _favoriteFoodHelper.getWritableDatabase();
        // インサート用SQLの文字列の用意
        String sqlDelete = "DELETE FROM favorite_foods WHERE _id = " + database_id;
        // SQL文字列を元にプリペアドステートメントを取得
        SQLiteStatement stmt = db.compileStatement(sqlDelete);
        // インサートSQLの実行
        stmt.executeUpdateDelete();

        // DBヘルパーオブジェクトを閉じる
        _favoriteFoodHelper.close();

        // 編集画面が終了した後、データ削除を反映した上で元の画面に戻る(main画面に戻るか、お気に入り一覧画面に戻るか）
        Intent intent;
        if (from.equals("FavoriteFoodListFragment")) {
            // データを更新した上でお気に入り画面に
            intent = new Intent(FavoriteEditFoodActivity.this, FavoriteFoodListActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void onClickEditFavoriteFoodButton(View view) {
        Toast.makeText(FavoriteEditFoodActivity.this, "編集ボタンは只今開発中です😄", Toast.LENGTH_LONG).show();
    }
}