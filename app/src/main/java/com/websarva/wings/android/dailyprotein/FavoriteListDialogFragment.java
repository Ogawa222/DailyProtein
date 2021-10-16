package com.websarva.wings.android.dailyprotein;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteListDialogFragment extends DialogFragment {

    private FavoriteFoodDatabaseHelper _favoriteFoodHelper;
    private AlertDialog dialog;

    // SimpleAdapterの第4引数fromに使用する定数フィールド
    private static final String[] FROM = {"id", "name", "weight"};
    // SimpleAdapterの第5引数toに使用する定数フィールド
    private static final int[] TO = {R.id.tvFavoriteFoodId, R.id.tvFavoriteFoodName, R.id.tvFavoriteFoodWeight};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // このフラグメントが所属するアクティビティオブジェクトを取得
        Activity parentActivity = getActivity();

        // カスタムレイアウトの用意
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View customFavoriteListView = inflater.inflate(R.layout.favorite_food_list_dialog, null);
        // 画面部品ListViewを取得
        ListView lvFavoriteFoodList = customFavoriteListView.findViewById(R.id.lvFavoriteFoodListDialog);
        // Listオブジェクトを用意
        List<Map<String, String>> favoriteFoodList = new ArrayList<>();

        // SQLから記録を取得
        _favoriteFoodHelper = new FavoriteFoodDatabaseHelper(getActivity());
        SQLiteDatabase db = _favoriteFoodHelper.getReadableDatabase();
        String sql = "SELECT * FROM favorite_foods";
        Cursor cursor = db.rawQuery(sql, null);

        // 取得した記録からfoodListデータ生成
        Map<String, String> food;
        int idxId;
        String id;
        int idxName;
        String name;
        int idxWeight;
        String weight;
        while (cursor.moveToNext()) {
            idxId = cursor.getColumnIndex("_id");
            id = String.valueOf(cursor.getInt(idxId));
            idxName = cursor.getColumnIndex("name");
            name = cursor.getString(idxName);
            idxWeight = cursor.getColumnIndex("weight");
            weight = String.valueOf(cursor.getDouble(idxWeight));
            food = new HashMap<>();
            food.put("id", id);
            food.put("name", name);
            food.put("weight", weight);
            favoriteFoodList.add(food);
        }
        // リストの要素の順序を逆にする
        Collections.reverse(favoriteFoodList);
        // SimpleAdapterを生成
        SimpleAdapter adapter = new SimpleAdapter(parentActivity, favoriteFoodList, R.layout.favorite_food_row, FROM, TO);
        // アダプタの登録
        lvFavoriteFoodList.setAdapter(adapter);
        // リスナーの登録
        lvFavoriteFoodList.setOnItemClickListener(new FavoriteListDialogFragment.ListItemClickListener());

        // タイトルの変更
        TextView title = customFavoriteListView.findViewById(R.id.favorite_food_list_title);
        title.setText("お気に入り一覧");

        // ダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customFavoriteListView);

        builder.setNeutralButton(R.string.dialog_btn_cancel, new FavoriteListDialogFragment.DialogButtonClickListener());
        dialog = builder.create();
        return dialog;
    }

    // リストがタップされたときの処理が記述されたメンバクラス
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // このフラグメントが所属するアクティビティオブジェクトを取得
            MainActivity parentActivity = (MainActivity) getActivity();

            // タップされた行のデータを取得。SimpleAdapterでは1行分のデータはMap型
            Map<String, String> item = (Map<String, String>) parent.getItemAtPosition(position);
            // タップされた行の各要素の値を取得
            String name = item.get("name");
            String weight = item.get("weight");

            parentActivity.onFavoriteSet(name, weight);

            dialog.dismiss();
        }
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEUTRAL:
                    break;
            }
        }
    }
}
