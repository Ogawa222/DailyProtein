package com.websarva.wings.android.dailyprotein;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Locale;

public class DailyProteinActivity extends AppCompatActivity
        implements OnDateSetListener {

    private TextView textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_protein);

        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーの[戻る]メニューを有効に設定
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 日付・時刻取得のため
        textViewDate = findViewById(R.id.tvFoodDate);

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
}