package com.websarva.wings.android.dailyprotein;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListFragment extends Fragment {

    private DailyProteinDatabaseHelper _dailyProteinHelper;
    // SimpleAdapterの第4引数fromに使用する定数フィールド
    private static final String[] FROM = {"name", "weight", "date", "time"};
    // SimpleAdapterの第5引数toに使用する定数フィールド
    private static final int[] TO = {R.id.tvFoodName, R.id.tvFoodWeight, R.id.tvFoodDate, R.id.tvTime};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // このフラグメントが所属するアクティビティオブジェクトを取得
        Activity parentActivity = getActivity();
        // フラグメントで表示する画面をXMLファイルからインフレートする
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
        int idxName;
        String name;
        int idxWeight;
        String weight;
        int idxDate;
        String date;
        int idxTime;
        String time;
        while(cursor.moveToNext()){
            idxName = cursor.getColumnIndex("name");
            name = cursor.getString(idxName);
            idxWeight = cursor.getColumnIndex("weight");
            weight = String.valueOf(cursor.getDouble(idxWeight));
            idxDate = cursor.getColumnIndex("take_date");
            date = cursor.getString(idxDate);
            idxTime = cursor.getColumnIndex("take_time");
            time = cursor.getString(idxTime);
            food = new HashMap<>();
            food.put("name",name);
            food.put("weight",weight);
            food.put("date",date);
            food.put("time",time);
            foodList.add(food);
        }
        // SimpleAdapterを生成
        SimpleAdapter adapter = new SimpleAdapter(parentActivity, foodList, R.layout.food_row, FROM, TO);
        // アダプタの登録
        lvFoodList.setAdapter(adapter);
        // インフレートされた画面を戻り値として返す。
        return view;
    }
}