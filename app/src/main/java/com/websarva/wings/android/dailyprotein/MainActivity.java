package com.websarva.wings.android.dailyprotein;

import android.app.DatePickerDialog.OnDateSetListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity // AppCompatActivityをFragmentActivityにするとタイトルバーが非表示になる
        implements OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TextView textViewDate;
    private TextView textViewTime;
    // データベースヘルパーオブジェクト
    private DailyProteinDatabaseHelper _dailyProteinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 日付・時刻取得のため
        textViewDate = findViewById(R.id.tvDate);
        textViewTime = findViewById(R.id.tvTime);

        // DBヘルパーオブジェクトを生成
        _dailyProteinHelper = new DailyProteinDatabaseHelper(MainActivity.this);
    }

    public void onFavoriteSet(String name, String weight){
        EditText etFoodName = findViewById(R.id.food_name);
        EditText etProteinWeight = findViewById(R.id.protein_weight);
        etFoodName.setText(name);
        etProteinWeight.setText(weight);
    }

    // DatePickerフラグメントを使い日付のセット
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // フォーマットを日本語にしてセット
        String str = String.format(Locale.JAPAN, "%d年%d月%d日", year, month+1, day);
        textViewDate.setText(str);
    }

    public void showDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String str = String.format(Locale.JAPAN, "%d:%02d", hour, minute);
        textViewTime.setText( str );
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    // オプションメニューの作成
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // メニューインフレーターの取得
        MenuInflater inflater = getMenuInflater();
        // オプションメニュー用.xmlファイルをインフレート
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        return true;
    }

    // オプションメニューのアイテムが選択されたときに呼び出されるメソッド
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.RegistrationHistory: // これまでの記録へ
                intent = new Intent(getApplication(), RegistrationHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.TodayRegistration: // 今日の記録へ
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String date = String.format(Locale.JAPAN, "%d年%d月%d日", year, month+1, day);
                intent = new Intent(getApplication(), DailyHistoryActivity.class);
                intent.putExtra("date",date);
                // 日間データ画面から戻る時の分岐のため
                intent.putExtra("from","MainActivity");
                startActivity(intent);
                break;
            case R.id.Favorite:
                intent = new Intent(getApplication(), FavoriteFoodActivity.class);
                startActivity(intent);
                break;
            case R.id.SetDailyGoal:
                intent = new Intent(getApplication(), DailyGoalActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // DBヘルパーオブジェクトの解放
        _dailyProteinHelper.close();
        super.onDestroy();
    }

    public void onRegisterButtonClick(View view){
        // 入力された食品名を取得
        EditText etFoodName = findViewById(R.id.food_name);
        String foodName = etFoodName.getText().toString();
        // 入力されたタンパク質量を取得
        EditText etProteinWeight = findViewById(R.id.protein_weight);
        String proteinWeight = etProteinWeight.getText().toString();
        // 入力された日付
        TextView tvDate = findViewById(R.id.tvDate);
        String date = tvDate.getText().toString();
        // 入力された時刻
        TextView tvTime = findViewById(R.id.tvTime);
        String time = tvTime.getText().toString();

        if (foodName.isEmpty() || proteinWeight.isEmpty() || date.isEmpty() || time.isEmpty()){
            Toast.makeText(MainActivity.this, "項目を全て入力してください", Toast.LENGTH_LONG).show();
        } else {
            // データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得。
            SQLiteDatabase db = _dailyProteinHelper.getWritableDatabase();
            // インサート用SQLの文字列の用意
            String sqlInsert = "INSERT INTO daily_proteins (name, weight, take_date, take_time) VALUES (?, ?, ?, ?)";
            // SQL文字列を元にプリペアドステートメントを取得
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            // 変数のバインド
            stmt.bindString(1, foodName);
            double d_proteinWeight = Double.parseDouble(proteinWeight);
            stmt.bindDouble(2, d_proteinWeight);
            stmt.bindString(3, date);
            stmt.bindString(4, time);
            // インサートSQLの実行
            stmt.executeInsert();

            // 入力をクリア
            etFoodName.setText("");
            etProteinWeight.setText("");
            tvDate.setText("");
            tvTime.setText("");
            Toast.makeText(MainActivity.this, "タンパク質摂取量を記録しました！", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickDeleteFavoriteFoodButton(View view){
        FavoriteListDialogFragment favoriteListDialogFragment = new FavoriteListDialogFragment();
        favoriteListDialogFragment.show(getSupportFragmentManager(), "FavoriteListDialogFragment");
    }
}