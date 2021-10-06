package com.websarva.wings.android.dailyprotein;

import androidx.fragment.app.FragmentActivity;
import android.app.DatePickerDialog.OnDateSetListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Locale;


public class MainActivity extends AppCompatActivity // AppCompatActivityをFragmentActivityにするとタイトルバーが非表示になる
        implements OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private TextView textViewDate;
    private TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDate = findViewById(R.id.tvDate);
        textViewTime = findViewById(R.id.tvTime);
    }

    // DatePickerフラグメントを使い日付のセット
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // フォーマットを日本語にしてセット
        String str = String.format(Locale.JAPAN, "%d年%d月%d日", year, month+1, day+1);
        textViewDate.setText(str);
    }

    public void showDatePickerDialog(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        String str = String.format(Locale.US, "%d:%d", hour, minute);
        textViewTime.setText( str );

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // メニューインフレーターの取得
        MenuInflater inflater = getMenuInflater();
        // オプションメニュー用.xmlファイルをインフレート
        inflater.inflate(R.menu.menu_options_menu_list, menu);
        return true;
    }
}