package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DailyGoalActivity extends AppCompatActivity {

    // データベースヘルパーオブジェクト
    private DailyGoalDatabaseHelper _dailyGoalProteinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_goal);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // タイトルの変更
        setTitle(R.string.daily_goal);

        // SQLから最新の記録を取得
        _dailyGoalProteinHelper = new DailyGoalDatabaseHelper(DailyGoalActivity.this);
        SQLiteDatabase db = _dailyGoalProteinHelper.getReadableDatabase();
        String sql = "SELECT * FROM daily_goals ORDER BY _id DESC LIMIT 1";
        Cursor cursor = db.rawQuery(sql, null);
        // 取得した記録から最新のタンパク質摂取目標を生成
        while(cursor.moveToNext()) {
            int idxWeight = cursor.getColumnIndex("weight");
            String weight = String.valueOf(cursor.getDouble(idxWeight));
            EditText etDailyGoalProteinWeight = findViewById(R.id.daily_goal_protein_weight);
            etDailyGoalProteinWeight.setText(weight);
        }

        _dailyGoalProteinHelper.close();
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

    public void onSetGoalButtonClick(View view){
        // 入力されたタンパク質摂取目標を取得
        EditText etProteinWeight = findViewById(R.id.daily_goal_protein_weight);
        String proteinWeight = etProteinWeight.getText().toString();

        if (proteinWeight.isEmpty()){
            Toast.makeText(DailyGoalActivity.this, "目標タンパク質摂取量を入力してください", Toast.LENGTH_LONG).show();
        } else {
            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            SQLiteDatabase db = _dailyGoalProteinHelper.getWritableDatabase();
            // インサート用SQLの文字列の用意
            String sqlInsert = "INSERT INTO daily_goals (weight) VALUES (?)";
            // SQL文字列を元にプリペアドステートメントを取得
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            // 変数のバインド
            double d_proteinWeight = Double.parseDouble(proteinWeight);
            stmt.bindDouble(1, d_proteinWeight);
            // インサートSQLの実行
            stmt.executeInsert();

            // 入力をクリア
            etProteinWeight.setText("");
            Toast.makeText(DailyGoalActivity.this, "目標タンパク質摂取量を設定しました！", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}