package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class RegistrationHistoryActivity extends AppCompatActivity
        implements OnDateSetListener {

    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_history);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 日付・時刻取得のため
        textViewDate = findViewById(R.id.tvSearchFoodDate);

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

    // DatePickerフラグメントを使い日付のセット
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // フォーマットを日本語にしてセット
        String str = String.format(Locale.JAPAN, "%d年%d月%d日", year, month+1, day);
        textViewDate.setText(str);
    }

    public void showFoodDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDailyProteinSearchButtonClick(View view){
        // 入力された日付を取得
        TextView tvDate = findViewById(R.id.tvSearchFoodDate);
        String date = tvDate.getText().toString();

        if(date.isEmpty()){
            Toast.makeText(RegistrationHistoryActivity.this, "日付を入力してください", Toast.LENGTH_LONG).show();
        }else{
            // インテントオブジェクトを生成
            Intent intent = new Intent(getApplication(), DailyHistoryActivity.class);
            // 日間データ画面に送るデータに日付を格納
            intent.putExtra("date",date);
            intent.putExtra("from","RegistrationHistoryActivity");
            startActivity(intent);
            finish();
        }
    }

}