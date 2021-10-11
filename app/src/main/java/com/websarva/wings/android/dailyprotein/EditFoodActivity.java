package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class EditFoodActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private String database_id;
    private String research_date;
    private String from;
    private TextView tvEditDate;
    private TextView tvEditTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        // タップされた行の各要素の値を取得
        database_id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String weight = intent.getStringExtra("weight");
        research_date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        // 編集画面が終了した時の分岐のため(全データ画面に戻るか、日間データに戻るか）
        from = intent.getStringExtra("from");

        EditText edit_food_name = findViewById(R.id.edit_food_name);
        EditText edit_protein_weight = findViewById(R.id.edit_protein_weight);
        tvEditDate = findViewById(R.id.tvEditDate);
        tvEditTime = findViewById(R.id.tvEditTime);

        edit_food_name.setText(name);
        edit_protein_weight.setText(weight);
        tvEditDate.setText(research_date);
        tvEditTime.setText(time);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 戻り値用の変数を初期値trueで用意
        boolean returnVal = true;
        // 選択されたメニューのIDを取得
        int itemId = item.getItemId();
        // 選択されたメニューが「戻る」の場合、アクティビティを終了
        if(itemId == android.R.id.home){
            // 戻るボタンを押した際、元の画面に戻る(全データ画面に戻るか、日間データに戻るか）
            Intent intent;
            if (from.equals("DailyFoodListFragment")) {
                // 日間データ画面に
                intent = new Intent(EditFoodActivity.this, DailyHistoryActivity.class);
                // 選択されていた日付を再度セット
                intent.putExtra("date", research_date);
            } else {
                // 全データ画面に
                intent = new Intent(EditFoodActivity.this, RegistrationHistoryActivity.class);
            }
            startActivity(intent);
            finish();
        }
        // それ以外・・・
        else {
            // 親クラスの同名メソッドを呼び出し、その戻り値をreturnValとする
            returnVal = super.onOptionsItemSelected(item);
        }
        return returnVal;
    }

    public void onClickDeleteFoodButton(View view){
        AlertDeleteDialogFragment alertFragment = new AlertDeleteDialogFragment();
        alertFragment.show(getSupportFragmentManager(), "AlertDeleteDialogFragment");
    }

    public void onClickEditFoodButton(View view){
        Toast.makeText(EditFoodActivity.this, "編集ボタンは只今開発中です😄", Toast.LENGTH_LONG).show();
    }

    // DatePickerフラグメントを使い日付のセット
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // フォーマットを日本語にしてセット
        String str = String.format(Locale.JAPAN, "%d年%d月%d日", year, month+1, day);
        tvEditDate.setText(str);
    }

    public void showEditDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String str = String.format(Locale.JAPAN, "%d:%02d", hour, minute);
        tvEditTime.setText( str );
    }

    public void showEditTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void deleteFood(){
        DailyProteinDatabaseHelper _dailyProteinHelper = new DailyProteinDatabaseHelper(EditFoodActivity.this);
        // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
        SQLiteDatabase db = _dailyProteinHelper.getWritableDatabase();
        // インサート用SQLの文字列の用意
        String sqlDelete = "DELETE FROM daily_proteins WHERE _id = " + database_id;
        // SQL文字列を元にプリペアドステートメントを取得
        SQLiteStatement stmt = db.compileStatement(sqlDelete);
        // インサートSQLの実行
        stmt.executeUpdateDelete();

        // DBヘルパーオブジェクトを閉じる
        _dailyProteinHelper.close();

        // 編集画面が終了した後、データ削除を反映した上で元の画面に戻る(全データ画面に戻るか、日間データに戻るか）
        Intent intent;
        if (from.equals("DailyFoodListFragment")) {
            // データを更新した上で日間データ画面に
            intent = new Intent(EditFoodActivity.this, DailyHistoryActivity.class);
            // 選択されていた日付を再度セット
            intent.putExtra("date", research_date);
        } else {
            // データを更新した上での全データ画面に
            intent = new Intent(EditFoodActivity.this, RegistrationHistoryActivity.class);
        }
        startActivity(intent);
        finish();
    }
}