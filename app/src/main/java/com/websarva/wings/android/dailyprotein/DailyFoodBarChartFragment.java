package com.websarva.wings.android.dailyprotein;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class DailyFoodBarChartFragment extends Fragment {

    private BarChart chart;
    private String research_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // このフラグメントが所属するアクティビティオブジェクトを取得
        Activity parentActivity = getActivity();
        // フラグメントで表示する画面をlayoutのXMLファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_daily_food_bar_chart, container, false);
        // インテントオブジェクトを取得
        Intent intent = parentActivity.getIntent();
        // 検索する日付を取得
        research_date = intent.getStringExtra("date");

        chart = (BarChart) view.findViewById(R.id.bar_chart);

        // 表示データ取得
        BarData data = new BarData(getBarData());

        chart.setData(data);

        //Y軸(左)
        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setAxisMaximum(80);
        left.setLabelCount(4);
        left.setDrawTopYLabelEntry(true);
        // 単位の設定
        left.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value+"g";
            }
        });

        //Y軸(右)
        YAxis right = chart.getAxisRight();
        right.setDrawLabels(false);
        right.setDrawGridLines(false);
        right.setDrawZeroLine(false);
        right.setDrawTopYLabelEntry(true);

        //X軸
        XAxis xAxis = chart.getXAxis();
        //X軸に表示するLabelのリスト(最初の""は原点の位置)
        final String[] labels = {"","0:00~", "3:00~", "6:00~", "9:00~", "12:00~", "15:00~", "18:00~", "21:00~"};

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        XAxis bottomAxis = chart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setDrawLabels(true);
        bottomAxis.setDrawGridLines(false);
        bottomAxis.setDrawAxisLine(true);

        //グラフ上の表示
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setClickable(false);

        //凡例
        chart.getLegend().setEnabled(false);

        chart.setScaleEnabled(false);

        // アニメーション
        chart.animateY(2000);

        return view;
    }

    //棒グラフのデータを取得
    private List<IBarDataSet> getBarData(){
        // SQLから記録を取得
        DailyProteinDatabaseHelper _dailyProteinHelper = new DailyProteinDatabaseHelper(getActivity());
        SQLiteDatabase db = _dailyProteinHelper.getReadableDatabase();
        String sql = "SELECT * FROM daily_proteins WHERE take_date = '" + research_date + "'";
        Cursor cursor = db.rawQuery(sql, null);

        double time0_2 = 0;
        double time3_5 = 0;
        double time6_8 = 0;
        double time9_11 = 0;
        double time12_14 = 0;
        double time15_17 = 0;
        double time18_20 = 0;
        double time21_23 = 0;

        // 取得した記録から時間ごとのデータ生成
        Map<String, String> food;
        int idxWeight;
        double weight;
        int idxTime;
        String time;
        BigDecimal sum_weight = new BigDecimal("0");
        while(cursor.moveToNext()){
            idxWeight = cursor.getColumnIndex("weight");
            weight = cursor.getDouble(idxWeight);
            idxTime = cursor.getColumnIndex("take_time");
            time = cursor.getString(idxTime);

            Pattern p = Pattern.compile("[:]+");
            String[] split_time = p.split(time);
            if (split_time[0].equals("0") || split_time[0].equals("1") || split_time[0].equals("2")){
                time0_2 += weight;
            } else if (split_time[0].equals("3") || split_time[0].equals("4") || split_time[0].equals("5")){
                time3_5 += weight;
            } else if (split_time[0].equals("6") || split_time[0].equals("7") || split_time[0].equals("8")){
                time6_8 += weight;
            } else if (split_time[0].equals("9") || split_time[0].equals("10") || split_time[0].equals("11")){
                time9_11 += weight;
            } else if (split_time[0].equals("12") || split_time[0].equals("13") || split_time[0].equals("14")){
                time12_14 += weight;
            } else if (split_time[0].equals("15") || split_time[0].equals("16") || split_time[0].equals("17")){
                time15_17 += weight;
            } else if (split_time[0].equals("18") || split_time[0].equals("19") || split_time[0].equals("20")){
                time18_20 += weight;
            } else if (split_time[0].equals("21") || split_time[0].equals("22") || split_time[0].equals("23")){
                time21_23 += weight;
            }
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, (float)time0_2));
        entries.add(new BarEntry(2, (float)time3_5));
        entries.add(new BarEntry(3, (float)time6_8));
        entries.add(new BarEntry(4, (float)time9_11));
        entries.add(new BarEntry(5, (float)time12_14));
        entries.add(new BarEntry(6, (float)time15_17));
        entries.add(new BarEntry(7, (float)time18_20));
        entries.add(new BarEntry(8, (float)time21_23));
        List<IBarDataSet> bars = new ArrayList<>();
        BarDataSet dataSet = new BarDataSet(entries, "bar");

        //ハイライトさせない
        dataSet.setHighlightEnabled(false);


        bars.add(dataSet);

        return bars;
    }


}