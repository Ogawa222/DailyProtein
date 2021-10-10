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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListFragment extends Fragment {

    private DailyProteinDatabaseHelper _dailyProteinHelper;
    // SimpleAdapterの第4引数fromに使用する定数フィールド
    private static final String[] FROM = {"id", "name", "weight", "date", "time"};
    // SimpleAdapterの第5引数toに使用する定数フィールド
    private static final int[] TO = {R.id.tvFoodId, R.id.tvFoodName, R.id.tvFoodWeight, R.id.tvFoodDate, R.id.tvFoodTime};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // このフラグメントが所属するアクティビティオブジェクトを取得
        Activity parentActivity = getActivity();
        // フラグメントで表示する画面をlayoutのXMLファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        // 画面部品ListViewを取得
        ListView lvFoodList = view.findViewById(R.id.lvFoodList);
        // Listオブジェクトを用意
        List<Map<String, String>> foodList = new ArrayList<>();

        // SQLから記録を取得
        DailyProteinDatabaseHelper _dailyProteinHelper = new DailyProteinDatabaseHelper(getActivity());
        SQLiteDatabase db = _dailyProteinHelper.getReadableDatabase();
        String sql = "SELECT * FROM daily_proteins";
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
            food.put("id", id);
            food.put("name",name);
            food.put("weight",weight);
            food.put("date",date);
            food.put("time",time);
            foodList.add(food);
        }
        // リストの要素の順序を逆にする
        Collections.reverse(foodList);
        // SimpleAdapterを生成
        SimpleAdapter adapter = new SimpleAdapter(parentActivity, foodList, R.layout.food_row, FROM, TO);
        // アダプタの登録
        lvFoodList.setAdapter(adapter);
        // リスナーの登録
        lvFoodList.setOnItemClickListener(new FoodListFragment.ListItemClickListener());

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
            intent.putExtra("from","FoodListFragment");
            // 編集画面の起動
            startActivity(intent);
            parentActivity.finish();
        }
    }
}