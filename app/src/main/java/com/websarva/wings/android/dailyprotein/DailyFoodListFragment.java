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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DailyFoodListFragment extends Fragment {

    // SimpleAdapterの第4引数fromに使用する定数フィールド
    private static final String[] FROM = {"id", "name", "weight", "date", "time"};
    // SimpleAdapterの第5引数toに使用する定数フィールド
    private static final int[] TO = {R.id.tvDailyFoodId, R.id.tvDailyFoodName, R.id.tvDailyFoodWeight, R.id.tvDailyFoodDate, R.id.tvDailyFoodTime};

    private TextView daily_protein_sum;
    private TextView daily_research_date;
    SimpleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // このフラグメントが所属するアクティビティオブジェクトを取得
        Activity parentActivity = getActivity();

        // インテントオブジェクトを取得
        Intent intent = parentActivity.getIntent();
        // 検索する日付を取得
        String research_date = intent.getStringExtra("date");

        // フラグメントで表示する画面をlayoutのXMLファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_daily_food_list, container, false);
        // 画面部品ListViewを取得
        ListView lvDailyFoodList = view.findViewById(R.id.lvDailyFoodList);

        // Listオブジェクトを用意
        List<Map<String, String>> foodList = new ArrayList<>();

        // SQLから記録を取得
        DailyProteinDatabaseHelper _dailyProteinHelper = new DailyProteinDatabaseHelper(getActivity());
        SQLiteDatabase db = _dailyProteinHelper.getReadableDatabase();
        String sql = "SELECT * FROM daily_proteins WHERE take_date = '" + research_date + "'";
        Cursor cursor = db.rawQuery(sql, null);

        // 取得した記録からfoodListデータ生成
        Map<String, String> food;
        int idxId;
        String id;
        int idxName;
        String name;
        int idxWeight;
        String weight;
        int idxDate;
        String date;
        int idxTime;
        String time;
        BigDecimal sum_weight = new BigDecimal("0");
        while(cursor.moveToNext()){
            idxId = cursor.getColumnIndex("_id");
            id = String.valueOf(cursor.getInt(idxId));
            idxName = cursor.getColumnIndex("name");
            name = cursor.getString(idxName);
            idxWeight = cursor.getColumnIndex("weight");
            weight = String.valueOf(cursor.getDouble(idxWeight));
            idxDate = cursor.getColumnIndex("take_date");
            date = cursor.getString(idxDate);
            idxTime = cursor.getColumnIndex("take_time");
            time = cursor.getString(idxTime);
            food = new HashMap<>();
            food.put("id",id);
            food.put("name",name);
            food.put("weight",weight);
            food.put("date",date);
            food.put("time",time);
            foodList.add(food);
            // 合計タンパク質摂取量の記録
            BigDecimal protein_weight = new BigDecimal(weight);
            sum_weight = sum_weight.add(protein_weight);
        }
        // リストの要素の順序を逆にする
        Collections.reverse(foodList);
        // SimpleAdapterを生成
        adapter = new SimpleAdapter(parentActivity, foodList, R.layout.daily_food_row, FROM, TO);
        // アダプタの登録
        lvDailyFoodList.setAdapter(adapter);
        // リスナーの登録
        lvDailyFoodList.setOnItemClickListener(new ListItemClickListener());
        // 合計タンパク質摂取量の表示
        daily_protein_sum = (TextView) view.findViewById(R.id.tvDailyProteinSum);
        daily_protein_sum.setText(String.valueOf(sum_weight));
        // 日付の表示
        daily_research_date = (TextView) view.findViewById(R.id.tvDailyResearchDate);
        daily_research_date.setText(research_date);

        // DBヘルパーオブジェクトを閉じる
        _dailyProteinHelper.close();

        // インフレートされた画面を戻り値として返す。
        return view;
    }

    // リストがタップされたときの処理が記述されたメンバクラス
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            // このフラグメントが所属するアクティビティオブジェクトを取得
            Activity parentActivity = getActivity();
            // インテントオブジェクトを取得
            Intent intent = new Intent(parentActivity, EditFoodActivity.class);

            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMap型
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            // タップされた行の各要素の値を取得
            String database_id = item.get("id");
            String name = item.get("name");
            String weight = item.get("weight");
            String date = item.get("date");
            String time = item.get("time");

            intent.putExtra("id",database_id);
            intent.putExtra("name", name);
            intent.putExtra("weight",weight);
            intent.putExtra("date",date);
            intent.putExtra("time",time);
            // 編集画面が終了した時の分岐のため(全データ画面に戻るか、日間データに戻るか）
            intent.putExtra("from","DailyFoodListFragment");
            // 編集画面の起動
            startActivity(intent);
            parentActivity.finish();
        }
    }
}